package com.zicca.icoupon.coupon.service.basics.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zicca.icoupon.coupon.common.enums.CouponTemplateStatusEnum;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zicca.icoupon.coupon.common.constants.RedisConstant.COUPON_TEMPLATE_KEY;

/**
 * 优惠券模板缓存服务
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateRedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final Long NULL_VALUE_MARKER = -1L;

    // lua 脚本
    private static final String LUA_SCRIPT = "redis.call('HMSET', KEYS[1], unpack(ARGV, 1, #ARGV - 1)) " +
            "redis.call('EXPIREAT', KEYS[1], ARGV[#ARGV])";
    // 预编译的Lua脚本对象
    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);

    /**
     * 缓存优惠券模板
     *
     * @param id                         优惠券模板id
     * @param couponTemplateQueryRespDTO 优惠券模板
     */
    public void put(Long id, CouponTemplateQueryRespDTO couponTemplateQueryRespDTO) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(couponTemplateQueryRespDTO)) {
            log.warn("[优惠券模板分布式缓存服务] 缓存数据参数异常: id={}, couponTemplateQueryRespDTO={}", id, couponTemplateQueryRespDTO);
            return;
        }
        String cacheKey = COUPON_TEMPLATE_KEY + id;
        Map<String, String> cacheHashMap = BeanUtil.beanToMap(couponTemplateQueryRespDTO).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() != null ? entry.getValue().toString() : "")
                );
        List<String> keys = Collections.singletonList(cacheKey);
        List<String> args = new ArrayList<>(cacheHashMap.size() * 2 + 1);
        cacheHashMap.forEach((key, value) -> {
            args.add(key);
            args.add(value);
        });
        args.add(String.valueOf(couponTemplateQueryRespDTO.getValidEndTime().getTime() / 1000));
        stringRedisTemplate.execute(redisScript, keys, args.toArray());
        log.info("[优惠券模板分布式缓存服务] 缓存数据成功: id={}, couponTemplateQueryRespDTO={}", id, couponTemplateQueryRespDTO);
    }

    /**
     * 缓存空值
     *
     * @param id 优惠券模板id
     */
    public void putNullToCache(Long id) {
        if (ObjectUtil.isNull(id)) {
            log.warn("[优惠券模板分布式缓存服务] 缓存空值参数异常: id={}", id);
            return;
        }
        String cacheKey = COUPON_TEMPLATE_KEY + id;
        List<String> keys = Collections.singletonList(cacheKey);
        List<String> args = new ArrayList<>(3);
        args.add("id");
        args.add(String.valueOf(NULL_VALUE_MARKER));
        args.add(String.valueOf(System.currentTimeMillis() + 5 * 60 * 1000)); // 5分钟后过期
        stringRedisTemplate.execute(redisScript, keys, args.toArray());
        log.info("[优惠券模板分布式缓存服务] 缓存空值成功: id={}", id);
    }

    public boolean isNullCache(CouponTemplateQueryRespDTO value) {
        if (ObjectUtil.isNull(value)) {
            log.warn("[优惠券模板分布式缓存服务] 判断缓存空值参数异常: value={}", value);
            return false;
        }
        return value.getId().equals(NULL_VALUE_MARKER);
    }


    /**
     * 获取缓存数据
     *
     * @param id 优惠券模板id
     * @return 优惠券模板
     */
    public CouponTemplateQueryRespDTO get(Long id) {
        if (ObjectUtil.isNull(id)) {
            log.warn("[优惠券模板分布式缓存服务] 获取缓存数据参数异常: id={}", id);
            return null;
        }
        String key = COUPON_TEMPLATE_KEY + id;
        CouponTemplateQueryRespDTO value = new CouponTemplateQueryRespDTO();
        try {
            Map<Object, Object> cacheHashMap = stringRedisTemplate.opsForHash().entries(key);
            if (CollUtil.isEmpty(cacheHashMap)) {
                log.info("[优惠券模板分布式缓存服务] 缓存未命中: id={}", id);
                return null;
            }
            value = BeanUtil.fillBeanWithMap(cacheHashMap, value, true);
            log.info("[优惠券模板分布式缓存服务] 获取缓存数据成功: id={}, couponTemplateQueryRespDTO={}", id, value);
            return value;
        } catch (Exception e) {
            log.error("[优惠券模板分布式缓存服务] 获取缓存数据失败: id={}, value={}", id, value);
            throw new ServiceException("获取缓存数据失败");
        }
    }

    public void incrStock(Long id, Integer num) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(num)) {
            log.warn("[优惠券模板分布式缓存服务] 修改缓存库存参数异常: id={}, num={}", id, num);
            return;
        }
        String key = COUPON_TEMPLATE_KEY + id;
        stringRedisTemplate.opsForHash().increment(key, "stock", num);
        log.info("[优惠券模板分布式缓存服务] 修改缓存库存成功: id={}, num={}", id, num);
    }

    public void incrStock(Long id) {
        if (ObjectUtil.isNull(id)) {
            log.warn("[优惠券模板分布式缓存服务] 修改缓存库存参数异常: id={}", id);
            return;
        }
        incrStock(id, 1);
    }

    public void decrStock(Long id, Integer num) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(num)) {
            log.warn("[优惠券模板分布式缓存服务] 修改缓存库存参数异常: id={}, num={}", id, -num);
            return;
        }
        String key = COUPON_TEMPLATE_KEY + id;
        stringRedisTemplate.opsForHash().increment(key, "stock", -num);
        log.info("[优惠券模板分布式缓存服务] 修改缓存库存成功: id={}, num={}", id, -num);
    }

    public void decrStock(Long id) {
        if (ObjectUtil.isNull(id)) {
            log.warn("[优惠券模板分布式缓存服务] 修改缓存库存参数异常: id={}", id);
            return;
        }
        decrStock(id, 1);
    }

    public void delete(Long id) {
        if (ObjectUtil.isNull(id)) {
            log.warn("[优惠券模板分布式缓存服务] 删除数据参数异常: id={}", id);
            return;
        }
        String key = COUPON_TEMPLATE_KEY + id;
        stringRedisTemplate.delete(key);
        log.info("[优惠券模板分布式缓存服务] 删除缓存数据成功: id={}", id);
    }

    /**
     * 修改缓存状态
     *
     * @param id     优惠券模板id
     * @param status 优惠券模板状态
     * @return 优惠券模板
     */
    public CouponTemplateQueryRespDTO updateStatus(Long id, CouponTemplateStatusEnum status) {
        if (ObjectUtil.isNull(id) || ObjectUtil.isNull(status)) {
            log.error("[优惠券模板分布式缓存服务] 修改缓存状态参数异常: id={}, status={}", id, status);
        }
        String key = COUPON_TEMPLATE_KEY + id;
        stringRedisTemplate.opsForHash().put(key, "status", status.getValue().toString());
        CouponTemplateQueryRespDTO value = new CouponTemplateQueryRespDTO();
        Map<Object, Object> cacheHashMap = stringRedisTemplate.opsForHash().entries(key);
        value = BeanUtil.fillBeanWithMap(cacheHashMap, value, true);
        log.info("[优惠券模板分布式缓存服务] 修改缓存状态成功: id={}, status={}, value={}", id, status, value);
        return value;
    }


}
