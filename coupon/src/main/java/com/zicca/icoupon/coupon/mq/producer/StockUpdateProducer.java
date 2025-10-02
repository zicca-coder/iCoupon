package com.zicca.icoupon.coupon.mq.producer;

import cn.hutool.core.util.StrUtil;
import com.zicca.icoupon.coupon.mq.base.BaseExtendDTO;
import com.zicca.icoupon.coupon.mq.base.MessageWrapper;
import com.zicca.icoupon.coupon.mq.event.StockUpdateEvent;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zicca.icoupon.coupon.common.constants.MQConstants.COUPON_TEMPLATE_STOCK_UPDATE_TOPIC;

/**
 * 优惠券库存更新消息生产者
 * @author zicca
 */
@Component
public class StockUpdateProducer extends AbstractCommonProducerTemplate<StockUpdateEvent>{

    public StockUpdateProducer(@Autowired RocketMQTemplate rocketMQTemplate) {
        super(rocketMQTemplate);
    }


    @Override
    protected BaseExtendDTO buildBaseExtendDTO(StockUpdateEvent event) {
        return BaseExtendDTO.builder()
                .eventName(event.getType().getDesc())
                .keys(event.getCouponTemplateId().toString())
                .topic(COUPON_TEMPLATE_STOCK_UPDATE_TOPIC)
                .sentTimeout(5000L)
                .isOrdered(true)
                .build();
    }

    @Override
    protected Message<?> buildMessage(StockUpdateEvent event, BaseExtendDTO baseExtendDTO) {
        String keys = StrUtil.isEmpty(baseExtendDTO.getKeys()) ? UUID.randomUUID().toString() : baseExtendDTO.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(keys, event))
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .setHeader(MessageConst.PROPERTY_TAGS, baseExtendDTO.getTag())
                .build();
    }
}
