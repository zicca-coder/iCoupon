package com.zicca.icoupon.coupon.service.basics.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.coupon.common.enums.RedisStockDecrementErrorEnum;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.coupon.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.coupon.toolkit.StockDecrementReturnCombinedUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.zicca.icoupon.coupon.common.constants.RedisConstant.*;

/**
 * 用户优惠券缓存服务
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH = "lua/stock_decrement_and_save_user_receive.lua";
    private final static String STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_ROLLBACK_LUA_PATH = "lua/stock_decrement_and_save_user_receive_rollback.lua";

    private final DefaultRedisScript<Long> redisScript = Singleton.get(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH, () -> {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH)));
        redisScript.setResultType(Long.class);
        return redisScript;
    });
    private final DefaultRedisScript<Long> redisScriptRollback = Singleton.get(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_ROLLBACK_LUA_PATH, () -> {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_ROLLBACK_LUA_PATH)));
        redisScript.setResultType(Long.class);
        return redisScript;
    });


    /**
     * 扣减库存并保存用户领取记录数量
     *
     * @param userId   用户id
     * @param template 优惠券模板
     * @return 领取结果
     */
    public Integer decrementAndSaveUserReceive(Long userId, CouponTemplateQueryRespDTO template) {
        Integer maxReceiveCount = template.getMaxReceivePerUser();
        String couponTemplateCacheKey = COUPON_TEMPLATE_KEY + template.getId();
        String userCouponLimitCacheKey = USER_COUPON_LIMIT_KEY + userId + ":" + template.getId();
        String expireTime = String.valueOf(template.getValidEndTime().getTime());
        List<String> keys = List.of(couponTemplateCacheKey, userCouponLimitCacheKey);
        List<String> args = List.of(expireTime, maxReceiveCount.toString());
        long stockDecrementResult = stringRedisTemplate.execute(redisScript, keys, args.toArray());
        long firstField = StockDecrementReturnCombinedUtil.extractFirstField(stockDecrementResult);
        if (RedisStockDecrementErrorEnum.isFail(firstField)) {
            log.warn("[用户优惠券服务] 领取优惠券 - 分布式缓存扣减库存失败, templateId: {}, shopId: {}, errorCode: {}, errorMsg: {}", template.getId(), template.getShopId(), firstField, RedisStockDecrementErrorEnum.fromType(firstField));
            return -1;
        }
        int receiveCount = (int) StockDecrementReturnCombinedUtil.extractSecondField(stockDecrementResult);
        log.debug("[用户优惠券服务] 领取优惠券 - 分布式缓存扣减库存成功, templateId: {}, shopId: {}, receiveCount: {}", template.getId(), template.getShopId(), receiveCount);
        return receiveCount;
    }

    public void decrementAndSaveUserReceiveRollback(Long userId, CouponTemplateQueryRespDTO template) {
        String couponTemplateCacheKey = COUPON_TEMPLATE_KEY + template.getId();
        String userCouponLimitCacheKey = USER_COUPON_LIMIT_KEY + userId + ":" + template.getId();
        List<String> keys = List.of(couponTemplateCacheKey, userCouponLimitCacheKey);
        stringRedisTemplate.execute(redisScriptRollback, keys);
    }


    /**
     * 添加用户优惠券记录至用户领取列表
     *
     * @param userId           用户id
     * @param userCouponId     用户优惠券id
     * @param couponTemplateId 优惠券模板id
     */
    public void addToUserCouponList(Long userId, Long userCouponId, Long couponTemplateId) {
        if (userId == null || userCouponId == null || couponTemplateId == null) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录参数异常: userId={}, userCouponId={}, couponTemplateId={}", userId, userCouponId, couponTemplateId);
            return;
        }
        // 用户主键ID
        String key = USER_COUPON_LIST_KEY + userId;
        // 优惠券模板主键ID-用户优惠券关联主键ID
        String valueItem = StrUtil.builder()
                .append(couponTemplateId)
                .append("-")
                .append(userCouponId)
                .toString();
        long score = new Date().getTime();
        stringRedisTemplate.opsForZSet().add(key, valueItem, score);
        // 写后查
        Double scored;
        try {
            scored = stringRedisTemplate.opsForZSet().score(key, valueItem);
            if (scored == null) {
                log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录，写后查未命中: userId={}, userCouponId={}, couponTemplateId={}", userId, userCouponId, couponTemplateId);
                stringRedisTemplate.opsForZSet().add(key, valueItem, score);
            }
        } catch (Throwable ex) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录，写后查过程发生异常，检查Redis服务是否宕机: userId={}, userCouponId={}, couponTemplateId={}, ex={}", userId, userCouponId, couponTemplateId, ex);
            // 延时队列重新缓存，为了简单直接新增
            stringRedisTemplate.opsForZSet().add(key, valueItem, score);
        }
        log.debug("[用户优惠券分布式缓存服务] 缓存用户领取记录成功: userId={}, userCouponId={}, couponTemplateId={}", userId, userCouponId, couponTemplateId);
    }


    /**
     * 添加用户优惠券记录至用户领取列表
     *
     * @param userCoupon 用户优惠券
     */
    public void addToUserCouponList(UserCoupon userCoupon) {
        if (userCoupon == null) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录参数异常: userCoupon={}", userCoupon);
            return;
        }
        addToUserCouponList(userCoupon.getUserId(), userCoupon.getId(), userCoupon.getCouponTemplateId());
    }

    public void addUserCoupon(UserCoupon userCoupon) {
        if (ObjectUtil.isNull(userCoupon)) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录参数异常: userCoupon={}", userCoupon);
            return;
        }
        UserCouponQueryRespDTO cacheValue = BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class);
        addUserCoupon(cacheValue);
    }

    @SneakyThrows
    public void addUserCoupon(UserCouponQueryRespDTO userCoupon) {
        if (ObjectUtil.isNull(userCoupon)) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录参数异常: userCoupon={}", userCoupon);
            return;
        }
        String key = USER_COUPON_LIST_KEY + userCoupon.getUserId();
        String value = JSON.toJSONString(userCoupon);
        long score = new Date().getTime();
        stringRedisTemplate.opsForZSet().add(key, value, score);
        // 写后查
        Double scored;
        try {
            scored = stringRedisTemplate.opsForZSet().score(key, value);
            if (scored == null) {
                log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录，写后查未命中: userCoupon={}", userCoupon);
                stringRedisTemplate.opsForZSet().add(key, value, score);
            }
        }
        catch (Throwable ex) {
            log.warn("[用户优惠券分布式缓存服务] 缓存用户领取记录，写后查过程发生异常，检查Redis服务是否宕机: userCoupon={}, ex={}", userCoupon, ex);
            // 延时队列重新缓存，为了简单直接新增
            stringRedisTemplate.opsForZSet().add(key, value, score);
        }
        log.debug("[用户优惠券分布式缓存服务] 缓存用户领取记录成功: userCoupon={}", userCoupon);
    }

    @SneakyThrows
    public List<UserCouponQueryRespDTO> getUserCoupons(Long userId) {
        if (userId == null) {
            log.warn("[用户优惠券分布式缓存服务] 获取用户领取记录参数异常: userId={}", userId);
            return Collections.emptyList();
        }
        String key = USER_COUPON_LIST_KEY + userId;
        Set<String> values = stringRedisTemplate.opsForZSet().range(key, 0, -1);
        if (CollUtil.isEmpty(values)) {
            log.warn("[用户优惠券分布式缓存服务] 获取用户领取记录为空: userId={}", userId);
            return Collections.emptyList();
        }
        List<UserCouponQueryRespDTO> list = values.stream().map(value -> JSON.parseObject(value, UserCouponQueryRespDTO.class)).toList();
        log.debug("[用户优惠券分布式缓存服务] 获取用户领取记录成功: userId={}, list={}", userId, list);
        return list;
    }

    public void deleteUserCoupon(UserCoupon userCoupon) {
        if (userCoupon == null) {
            log.warn("[用户优惠券分布式缓存服务] 删除用户领取记录参数异常: userCoupon={}", userCoupon);
            return;
        }
        String key = USER_COUPON_LIST_KEY + userCoupon.getUserId();
        String value = JSON.toJSONString(userCoupon);
        Long removed = stringRedisTemplate.opsForZSet().remove(key, value);
        if (removed == null || removed <= 0) {
            log.warn("[用户优惠券分布式缓存服务] 删除用户领取记录失败: userCoupon={}", userCoupon);
            return;
        }
        log.debug("[用户优惠券分布式缓存服务] 删除用户领取记录成功: userCoupon={}", userCoupon);
    }


}
