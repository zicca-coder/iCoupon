package com.zicca.icoupon.coupon.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import com.zicca.icoupon.coupon.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.coupon.mq.base.MessageWrapper;
import com.zicca.icoupon.coupon.mq.event.UserCouponExpireEvent;
import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.USER_COUPON_EXPIRE_CG;
import static com.zicca.icoupon.coupon.common.constants.MQConstants.USER_COUPON_EXPIRE_TOPIC;
import static com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum.EXPIRED;

/**
 * 用户优惠券过期消费者
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = USER_COUPON_EXPIRE_TOPIC,
        consumerGroup = USER_COUPON_EXPIRE_CG
)
public class UserCouponExpireConsumer implements RocketMQListener<MessageWrapper<UserCouponExpireEvent>> {

    private final UserCouponMapper userCouponMapper;
    private final UserCouponRedisService redisService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(MessageWrapper<UserCouponExpireEvent> messageWrapper) {
        log.info("[消费者] 用户领取优惠券，用户优惠券过期事件 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));

        UserCouponExpireEvent event = messageWrapper.getMessage();

        // 更新用户优惠券状态为过期
        userCouponMapper.updateStatusById(event.getUserCouponId(), event.getUserId(), EXPIRED);
        // 删除缓存
        redisService.deleteUserCoupon(event.getUserCoupon());
    }
}
