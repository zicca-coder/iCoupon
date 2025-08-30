package com.zicca.icoupon.admin.merchant.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.admin.merchant.mq.base.BaseExtendDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;

/**
 * RocketMQ生产者抽象模版类
 *
 * @author zicca
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommonProducerTemplate<T> {

    private final RocketMQTemplate rocketMQTemplate;

    /**
     * 构建消息发送事件基础扩充实体
     *
     * @param event 消息发送事件
     * @return 消息发送事件基础扩充实体
     */
    protected abstract BaseExtendDTO buildBaseExtendDTO(T event);

    /**
     * 构建消息基本参数
     *
     * @param event         消息发送事件
     * @param baseExtendDTO 消息发送事件基础扩充实体
     * @return 消息基本参数
     */
    protected abstract Message<?> buildMessage(T event, BaseExtendDTO baseExtendDTO);

    public SendResult sendMessage(T event) {
        BaseExtendDTO baseExtendDTO = buildBaseExtendDTO(event);
        SendResult result;
        try {
            StringBuilder destinationTopic = StrUtil.builder().append(baseExtendDTO.getTopic());
            if (StrUtil.isNotBlank(baseExtendDTO.getTag())) {
                destinationTopic.append(":").append(baseExtendDTO.getTag());
            }

            if (baseExtendDTO.isOrdered()) { // 如果是顺序消息
                result = rocketMQTemplate.syncSendOrderly(
                        destinationTopic.toString(), // topic
                        buildMessage(event, baseExtendDTO), // 消息体
                        baseExtendDTO.getKeys(), // 消息的Key， 哈希值
                        baseExtendDTO.getSentTimeout() // 发送超时
                );
            } else if (baseExtendDTO.getDelayTime() != null) { // 如果是延时消息
                result = rocketMQTemplate.syncSendDeliverTimeMills( // 同步发送定时消息（绝对时间）
                        destinationTopic.toString(), // topic 格式：${topic}:${tag}
                        buildMessage(event, baseExtendDTO), // 消息体
                        baseExtendDTO.getDelayTime() // 定时发送的时间戳
                );
            } else { // 默认是普通消息
                result = rocketMQTemplate.syncSend( // 同步发送
                        destinationTopic.toString(), // topic 格式：${topic}:${tag}
                        buildMessage(event, baseExtendDTO), // 消息体
                        baseExtendDTO.getSentTimeout() // 发送超时时间
                );
            }
            log.info("[生产者] {} - 发送结果：{}，消息ID：{}，消息Keys：{}", baseExtendDTO.getEventName(), result.getSendStatus(), result.getMsgId(), baseExtendDTO.getKeys());
        } catch (Exception e) {
            log.error("[生产者] {} - 消息发送失败，消息体：{}", baseExtendDTO.getEventName(), JSON.toJSONString(event), e);
            throw e;
        }
        return result;
    }

}
