package com.zicca.icoupon.coupon.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zicca.icoupon.coupon.common.context.UserContext;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.coupon.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.coupon.dto.req.CouponTemplateNumberReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponCreateReqDTO;
import com.zicca.icoupon.coupon.mq.base.MessageWrapper;
import com.zicca.icoupon.coupon.mq.event.UserCouponExpireEvent;
import com.zicca.icoupon.coupon.mq.event.UserCouponReceiveEvent;
import com.zicca.icoupon.coupon.mq.producer.UserCouponExpireProducer;
import com.zicca.icoupon.coupon.service.CouponTemplateService;
import com.zicca.icoupon.coupon.service.UserCouponService;
import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.Objects;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.USER_COUPON_RECEIVE_CG;
import static com.zicca.icoupon.coupon.common.constants.MQConstants.USER_COUPON_RECEIVE_TOPIC;
import static com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum.NOT_USED;

/**
 * 用户领取优惠券消费者
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = USER_COUPON_RECEIVE_TOPIC,
        consumerGroup = USER_COUPON_RECEIVE_CG
)
public class UserCouponReceiveConsumer implements RocketMQListener<MessageWrapper<UserCouponReceiveEvent>> {


    private final CouponTemplateMapper couponTemplateMapper;
    private final UserCouponMapper userCouponMapper;
    private final UserCouponRedisService redisService;
    private final UserCouponExpireProducer userCouponExpireProducer;
    private final TransactionTemplate transactionTemplate;


    @Override
    public void onMessage(MessageWrapper<UserCouponReceiveEvent> messageWrapper) {
        log.info("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));

        UserCouponReceiveEvent event = messageWrapper.getMessage();
        CouponTemplateNumberReqDTO decreaseStockParam = buildCouponTemplateNumberParam(event);
        UserCoupon userCoupon = buildUserCoupon(event);

        transactionTemplate.executeWithoutResult(status -> {
            try {
                // 扣减库存
                int decremented = couponTemplateMapper.decreaseNumberCouponTemplate(event.getCouponTemplate().getId(), event.getCouponTemplate().getShopId(), 1);
                if (!SqlHelper.retBool(decremented)) {
                    log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，扣减优惠券数据库库存失败，消息体：{}", JSON.toJSONString(messageWrapper));
                    throw new ServiceException("用户领取优惠券，数据库扣减库存异常");
                }

                int created = userCouponMapper.insert(userCoupon);
                // 扣减失败应该回滚库存
                if (!SqlHelper.retBool(created)) {
                    log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，创建用户优惠券数据库记录失败，消息体：{}", JSON.toJSONString(messageWrapper));
                    throw new ServiceException("用户领取优惠券，新增用户优惠券记录异常");
                }
                // 缓存用户领券记录
                redisService.addUserCoupon(userCoupon);
                // 发送延时消息，优惠券过期时从缓存中删除领取的优惠券
                UserCouponExpireEvent expireEvent = UserCouponExpireEvent.builder()
                        .userId(userCoupon.getUserId())
                        .userCouponId(userCoupon.getId())
                        .userCoupon(userCoupon)
                        .delayTime(event.getCouponTemplate().getValidEndTime().getTime() - System.currentTimeMillis())
                        .build();
                SendResult sendResult = userCouponExpireProducer.sendMessage(expireEvent);

                if (!Objects.equals(sendResult.getSendStatus(), SendStatus.SEND_OK)) {
                    log.warn("[用户优惠券服务] 用户领取优惠券，用户优惠券过期事件 - 延时消息发送失败, templateId: {}, shopId: {}, errorCode: {}, errorMsg: {}", event.getCouponTemplate().getId(), event.getCouponTemplate().getShopId(), sendResult.getSendStatus(), sendResult.getMsgId());
                    throw new ServiceException("延时消息发送失败");
                }
            } catch (Exception e) {
                status.setRollbackOnly();
                // 回滚redis
                redisService.decrementAndSaveUserReceiveRollback(event.getUserId(), event.getCouponTemplate());
                log.warn("[消费者] 用户领取优惠券，用户领取优惠券事件 - 执行消费逻辑，数据库操作失败，消息体：{}", JSON.toJSONString(messageWrapper));
                // 这里抛出异常暂时注释掉，避免服务重启后消费端一直尝试重复消费
                // 注：这里取消注释后，服务端重复消费，但是redis已经回滚库存和用户领取限制了，需要额外处理
//                throw new ServiceException("优惠券领取异常，请稍后再试");
            }
        });
    }


    private CouponTemplateNumberReqDTO buildCouponTemplateNumberParam(UserCouponReceiveEvent event) {
        return CouponTemplateNumberReqDTO.builder()
                .couponTemplateId(event.getCouponTemplate().getId())
                .shopId(event.getCouponTemplate().getShopId())
                .number(1)
                .build();
    }

    private UserCouponCreateReqDTO buildUserCouponCreateParam(UserCouponReceiveEvent event) {
        return UserCouponCreateReqDTO.builder()
                .userId(event.getUserId())
                .couponTemplateId(event.getCouponTemplate().getId())
                .shopId(event.getCouponTemplate().getShopId())
                .target(event.getCouponTemplate().getTarget())
                .type(event.getCouponTemplate().getType())
                .faceValue(event.getCouponTemplate().getFaceValue())
                .minAmount(event.getCouponTemplate().getMinAmount())
                .receiveTime(new Date()) // 领取时间
                .validStartTime(event.getCouponTemplate().getValidStartTime())
                .validEndTime(event.getCouponTemplate().getValidEndTime())
                .status(NOT_USED) // 优惠券状态：未使用
                .build();
    }

    private UserCoupon buildUserCoupon(UserCouponReceiveEvent event) {
        return UserCoupon.builder()
                .userId(event.getUserId())
                .couponTemplateId(event.getCouponTemplate().getId())
                .shopId(event.getCouponTemplate().getShopId())
                .target(event.getCouponTemplate().getTarget())
                .type(event.getCouponTemplate().getType())
                .faceValue(event.getCouponTemplate().getFaceValue())
                .minAmount(event.getCouponTemplate().getMinAmount())
                .receiveTime(new Date()) // 领取时间
                .validStartTime(event.getCouponTemplate().getValidStartTime())
                .validEndTime(event.getCouponTemplate().getValidEndTime())
                .status(NOT_USED)
                .build();
    }


}
