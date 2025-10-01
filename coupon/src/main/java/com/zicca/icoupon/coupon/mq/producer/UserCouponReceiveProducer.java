package com.zicca.icoupon.coupon.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.zicca.icoupon.coupon.mq.base.BaseExtendDTO;
import com.zicca.icoupon.coupon.mq.base.MessageWrapper;
import com.zicca.icoupon.coupon.mq.event.UserCouponReceiveEvent;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.USER_COUPON_RECEIVE_TOPIC;

/**
 * 用户优惠券领取消息生产者
 *
 * @author zicca
 */
@Component
public class UserCouponReceiveProducer extends AbstractCommonProducerTemplate<UserCouponReceiveEvent> {

    public UserCouponReceiveProducer(@Autowired RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected BaseExtendDTO buildBaseExtendDTO(UserCouponReceiveEvent event) {
        // 用户领取优惠券是否需要是顺序消息？？？
        // 如果先领取的先成功，后者失败则需要顺序消息
        return BaseExtendDTO.builder()
                .eventName("用户领取优惠券")
                .keys(event.getUserId().toString())
                .topic(USER_COUPON_RECEIVE_TOPIC)
                .sentTimeout(5000L)
                .isOrdered(true)
                .build();
    }

    @Override
    protected Message<?> buildMessage(UserCouponReceiveEvent event, BaseExtendDTO baseExtendDTO) {
        String keys = StrUtil.isEmpty(baseExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseExtendDTO.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(keys, event))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseExtendDTO.getTag())
                .build();
    }
}
