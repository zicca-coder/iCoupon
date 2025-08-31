package com.zicca.icoupon.engine.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.engine.executor.ReservationReminderPushExecutor;
import com.zicca.icoupon.engine.mq.base.MessageWrapper;
import com.zicca.icoupon.engine.mq.event.ReservationReminderPushEvent;
import com.zicca.icoupon.engine.service.basics.reminder.ReservationReminderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static com.zicca.icoupon.engine.common.constants.MQConstants.RESERVATION_REMINDER_PUSH_CG;
import static com.zicca.icoupon.engine.common.constants.MQConstants.RESERVATION_REMINDER_PUSH_TOPIC;

/**
 * 预约提醒推送消费者
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = RESERVATION_REMINDER_PUSH_TOPIC,
        consumerGroup = RESERVATION_REMINDER_PUSH_CG
)
public class ReservationReminderPushConsumer implements RocketMQListener<MessageWrapper<ReservationReminderPushEvent>> {

    private final ReservationReminderPushExecutor reservationReminderPushExecutor;

    @Override
    public void onMessage(MessageWrapper<ReservationReminderPushEvent> messageWrapper) {
        // 开头打印日志，平常可 Debug 看任务参数，线上可报平安（比如消息是否消费，重新投递时获取参数等）
        log.info("[消费者] 提醒用户抢券 - 执行消费逻辑，消息体：{}", JSON.toJSONString(messageWrapper));
        ReservationReminderPushEvent event = messageWrapper.getMessage();
        ReservationReminderDTO requestParam = BeanUtil.toBean(event, ReservationReminderDTO.class);
        reservationReminderPushExecutor.execute(requestParam);
    }
}
