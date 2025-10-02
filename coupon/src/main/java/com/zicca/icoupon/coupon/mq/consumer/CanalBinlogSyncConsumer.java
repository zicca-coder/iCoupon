package com.zicca.icoupon.coupon.mq.consumer;

import com.zicca.icoupon.coupon.mq.event.CanalBinlogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.CANAL_BINLOG_SYNC_CG;
import static com.zicca.icoupon.coupon.common.constants.MQConstants.CANAL_BINLOG_SYNC_TOPIC;

/**
 * canal binlog 同步消费者
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = CANAL_BINLOG_SYNC_TOPIC,
        consumerGroup = CANAL_BINLOG_SYNC_CG
)
public class CanalBinlogSyncConsumer implements RocketMQListener<CanalBinlogEvent> {
    @Override
    public void onMessage(CanalBinlogEvent message) {
        log.info("[消费者] canal binlog 同步事件 - 执行消费逻辑，消息体：{}", message);
    }
}
