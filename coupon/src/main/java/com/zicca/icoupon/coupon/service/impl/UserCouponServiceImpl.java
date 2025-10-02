package com.zicca.icoupon.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.coupon.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponListReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.coupon.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.coupon.mq.event.StockUpdateEvent;
import com.zicca.icoupon.coupon.mq.producer.StockUpdateProducer;
import com.zicca.icoupon.coupon.service.CouponTemplateService;
import com.zicca.icoupon.coupon.service.UserCouponService;
import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import com.zicca.icoupon.framework.exception.ClientException;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.zicca.icoupon.coupon.common.enums.CouponTemplateStatusEnum.IN_PROGRESS;
import static com.zicca.icoupon.coupon.common.enums.StockUpdateTypeEnum.USER_RECEIVE_COUPON;
import static com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum.LOCKED;

/**
 * 用户优惠券服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    private final UserCouponMapper userCouponMapper;
    private final CouponTemplateService couponTemplateService;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserCouponRedisService redisService;
    private final StockUpdateProducer stockUpdateProducer;



    @Override
    public void receiveCouponMQ(UserCouponReceiveReqDTO requestParam) {
        // 参数校验
        if (requestParam == null || requestParam.getUserId() == null || requestParam.getCouponTemplateId() == null) {
            log.warn("[用户优惠券服务] 领取优惠券 - 请求参数不完整");
            throw new ServiceException("请求参数不完整");
        }
        CouponTemplateQueryRespDTO template = couponTemplateService.getCouponTemplate(requestParam.getCouponTemplateId(), requestParam.getShopId());
        // 如果优惠券未开始或优惠券已结束
        if (template == null || template.getStatus() != IN_PROGRESS || template.getValidEndTime().before(new Date()) || template.getValidStartTime().after(new Date())) {
            log.warn("[用户优惠券服务] 领取优惠券 - 优惠券未开始或已结束, templateId: {}, shopId: {}", requestParam.getCouponTemplateId(), requestParam.getShopId());
            return;
        }
        Integer receiveCount = redisService.decrementAndSaveUserReceive(requestParam.getUserId(), template);
        if (receiveCount <= 0) {
            log.warn("[用户优惠券服务] 领取优惠券 - 用户领取优惠券失败, templateId: {}, shopId: {}", requestParam.getCouponTemplateId(), requestParam.getShopId());
            return;
        }
        StockUpdateEvent.UserCouponReceiveEvent receiveEvent = StockUpdateEvent.UserCouponReceiveEvent.builder()
                .couponTemplateId(template.getId())
                .shopId(template.getShopId())
                .userId(requestParam.getUserId())
                .receiveCount(receiveCount)
                .couponTemplate(template)
                .count(1)
                .build();

        StockUpdateEvent event = StockUpdateEvent.builder()
                .type(USER_RECEIVE_COUPON)
                .couponTemplateId(template.getId())
                .userCouponReceiveEvent(receiveEvent)
                .build();

        SendResult sendResult = stockUpdateProducer.sendMessage(event);

        if (!Objects.equals(sendResult.getSendStatus(), SendStatus.SEND_OK)) {
            log.warn("[用户优惠券服务] 领取优惠券，用户领取优惠券事件 - 消息发送失败, templateId: {}, shopId: {}, errorCode: {}, errorMsg: {}", requestParam.getCouponTemplateId(), requestParam.getShopId(), sendResult.getSendStatus(), sendResult.getMsgId());
            return;
        }
        log.info("[用户优惠券服务] 领取优惠券 - 优惠券领取成功, templateId: {}, shopId: {}, userId: {}", requestParam.getCouponTemplateId(), requestParam.getShopId(), requestParam.getUserId());
    }

    @Override
    public void receiveCouponByCanal(UserCouponReceiveReqDTO requestParam) {

    }

    @Override
    public UserCouponQueryRespDTO getUserCouponById(Long id, Long userId) {
        // 参数校验
        if (id == null || userId == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券 - 请求参数不完整, id: {}, userId: {}", id, userId);
            throw new ServiceException("请求参数不完整");
        }

        // 查询用户优惠券
        // todo：此处的userId不应该传参数，应该从用户上下文中获取
        UserCoupon userCoupon = userCouponMapper.selectUserCouponById(id, userId);
        if (userCoupon == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券 - 优惠券不存在, id: {}, userId: {}", id, userId);
            throw new ServiceException("优惠券不存在");
        }

        // 转换为响应DTO
        UserCouponQueryRespDTO result = BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class);

        log.info("[用户优惠券服务] 查询用户优惠券成功, id: {}, userId: {}", id, userId);
        return result;
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponList(UserCouponListReqDTO requestParam) {
        if (requestParam == null || requestParam.getUserId() == null) {
            log.warn("[用户优惠券服务] 获取用户优惠券列表 - 请求参数不完整, userId: {}", requestParam.getUserId());
            return List.of();
        }
        if (CollectionUtil.isEmpty(requestParam.getUserCouponIds())) {
            log.warn("[用户优惠券服务] 获取用户优惠券列表 - 优惠券ID列表为空, userId: {}", requestParam.getUserId());
            return List.of();
        }
        List<UserCoupon> userCoupons = userCouponMapper.selectUserCouponByIdsAndUserId(requestParam.getUserCouponIds(), requestParam.getUserId());
        if (CollectionUtil.isEmpty(userCoupons)) {
            log.warn("[用户优惠券服务] 获取用户优惠券列表 - 优惠券不存在, userId: {}", requestParam.getUserId());
            return List.of();
        }
        return userCoupons.stream()
                .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                .toList();
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponListByStatus(Long userId, UserCouponStatusEnum status) {
        return List.of();
    }


    @Override
    public List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId) {
        // 参数校验
        if (userId == null) {
            log.warn("[用户优惠券服务] 查询可用用户优惠券列表 - 用户ID不能为空");
            return List.of();
        }

        // 查询可用用户优惠券列表
        List<UserCoupon> userCoupons = userCouponMapper.selectAvailableUserCouponList(userId);

        // 如果查询结果为空，返回空列表
        if (CollectionUtil.isEmpty(userCoupons)) {
            return List.of();
        }

        // 转换为响应DTO列表
        return userCoupons.stream()
                .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                .toList();
    }

    @Override
    public void lockUserCoupon(Long id, Long userId) {
        if (id == null || userId == null) {
            log.warn("[用户优惠券服务] 锁定用户优惠券 - 参数错误, id: {}, userId: {}", id, userId);
            throw new ServiceException("参数错误");
        }
        try {
            int result = userCouponMapper.updateStatusById(id, userId, LOCKED);
        } catch (Exception e) {
            log.error("[用户优惠券服务] 锁定用户优惠券 - 锁定失败, id: {}, userId: {}", id, userId);
            throw new ServiceException("锁定失败");
        }

    }

    @Override
    public void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam) {
        if (Objects.isNull(requestParam) || CollectionUtil.isEmpty(requestParam.getUserCouponIds()) || requestParam.getUserId() == null) {
            log.warn("[用户优惠券服务] 批量锁定用户优惠券 - 参数错误, requestParam: {}", requestParam);
            throw new ServiceException("参数错误");
        }
        try {
            int result = userCouponMapper.updateStatusByIdsAndUserId(requestParam.getUserCouponIds(), requestParam.getUserId(), LOCKED);
            log.info("[用户优惠券服务] 批量锁定用户优惠券 - 锁定成功, requestParam: {}", requestParam);
        } catch (Exception e) {
            log.error("[用户优惠券服务] 批量锁定用户优惠券 - 锁定失败, requestParam: {}", requestParam);
            throw new ServiceException("锁定失败");
        }
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponListWithInWeek(Long userId) {
        if (Objects.isNull(userId)) {
            log.warn("[用户优惠券服务] 获取用户一周内领取的优惠券列表 - 用户ID不能为空");
            throw new ClientException("用户ID不能为空");
        }
        try {
            List<UserCoupon> userCoupons = userCouponMapper.selectUserCouponsWithinOneWeek(userId);
            if (CollUtil.isEmpty(userCoupons)) {
                log.warn("[用户优惠券服务] 获取用户一周内领取的优惠券列表 - 优惠券不存在, userId: {}", userId);
                return List.of();
            }
            return userCoupons.stream()
                    .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("[用户优惠券服务] 获取用户一周内领取的优惠券列表 - 获取失败, userId: {}", userId);
            throw new ServiceException("获取失败");
        }
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponListWithInThreeDays(Long userId) {
        if (Objects.isNull(userId)) {
            log.warn("[用户优惠券服务] 获取用户三天内领取的优惠券列表 - 用户ID不能为空");
            throw new ClientException("用户ID不能为空");
        }
        try {
            List<UserCoupon> userCoupons = userCouponMapper.selectUserCouponsWithinThreeDays(userId);
            if (CollUtil.isEmpty(userCoupons)) {
                log.warn("[用户优惠券服务] 获取用户三天内领取的优惠券列表 - 优惠券不存在, userId: {}", userId);
                return List.of();
            }
            return userCoupons.stream()
                    .map(userCoupon -> BeanUtil.copyProperties(userCoupons, UserCouponQueryRespDTO.class))
                    .toList();
        } catch (Exception e) {
            log.error("[用户优惠券服务] 获取用户三天内领取的优惠券列表 - 获取失败, userId: {}", userId);
            throw new ServiceException("获取失败");
        }
    }


}
