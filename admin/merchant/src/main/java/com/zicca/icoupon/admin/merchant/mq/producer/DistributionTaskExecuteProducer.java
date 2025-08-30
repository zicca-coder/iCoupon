package com.zicca.icoupon.admin.merchant.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.zicca.icoupon.admin.merchant.mq.base.BaseExtendDTO;
import com.zicca.icoupon.admin.merchant.mq.base.MessageWrapper;
import com.zicca.icoupon.admin.merchant.mq.event.DistributionTaskExecuteEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zicca.icoupon.admin.merchant.common.constants.MQConstants.DISTRIBUTION_TASK_EXECUTE_TOPIC;

/**
 * 分发任务执行消息生产者
 * {@link com.zicca.icoupon.distribution.mq.consumer.DistributionTaskExecuteConsumer}
 *
 * @author zicca
 */
@Slf4j
@Component
public class DistributionTaskExecuteProducer extends AbstractCommonProducerTemplate<DistributionTaskExecuteEvent> {

    public DistributionTaskExecuteProducer(@Autowired RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }

    @Override
    protected BaseExtendDTO buildBaseExtendDTO(DistributionTaskExecuteEvent event) {
        switch (event.getType()) {
            case IMMEDIATELY -> { // 立即执行
                return BaseExtendDTO.builder()
                        .eventName("优惠券分发任务执行事件")
                        .keys(event.getDistributionTaskId().toString())
                        .topic(DISTRIBUTION_TASK_EXECUTE_TOPIC)
                        .tag("immediately")
                        .sentTimeout(5000L)
                        .build();
            }
            case TIMING -> { // 定时执行
                return BaseExtendDTO.builder()
                        .eventName("优惠券分发任务执行事件")
                        .keys(event.getDistributionTaskId().toString())
                        .topic(DISTRIBUTION_TASK_EXECUTE_TOPIC)
                        .tag("timing")
                        .sentTimeout(5000L)
                        .delayTime(event.getSendTime())
                        .build();
            }
            default -> throw new IllegalArgumentException("不支持的分发任务执行类型");
        }
    }

    @Override
    protected Message<?> buildMessage(DistributionTaskExecuteEvent event, BaseExtendDTO baseExtendDTO) {
        String keys = StrUtil.isEmpty(baseExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseExtendDTO.getKeys();
        return MessageBuilder.withPayload(new MessageWrapper(keys, event))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseExtendDTO.getTag())
                .build();
    }
}
