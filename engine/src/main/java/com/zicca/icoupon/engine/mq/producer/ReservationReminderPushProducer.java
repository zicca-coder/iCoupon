package com.zicca.icoupon.engine.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.zicca.icoupon.engine.mq.base.BaseExtendDTO;
import com.zicca.icoupon.engine.mq.base.MessageWrapper;
import com.zicca.icoupon.engine.mq.event.ReservationReminderPushEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zicca.icoupon.engine.common.constants.MQConstants.RESERVATION_REMINDER_PUSH_TOPIC;

/**
 * 预约提醒推送消息生产者
 *
 * @author zicca
 */
@Slf4j
@Component
public class ReservationReminderPushProducer extends AbstractCommonProducerTemplate<ReservationReminderPushEvent> {


    public ReservationReminderPushProducer(@Autowired RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected BaseExtendDTO buildBaseExtendDTO(ReservationReminderPushEvent event) {
        return BaseExtendDTO.builder()
                .eventName("预约提醒推送事件")
                .keys(event.getUserId() + ":" + event.getCouponTemplateId())
                .topic(RESERVATION_REMINDER_PUSH_TOPIC)
                .sentTimeout(5000L)
                .delayTime(event.getDelayTime())
                .build();
    }

    @Override
    protected Message<?> buildMessage(ReservationReminderPushEvent event, BaseExtendDTO baseExtendDTO) {
        String keys = StrUtil.isEmpty(baseExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseExtendDTO.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(keys, event))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseExtendDTO.getTag())
                .build();
    }
}
