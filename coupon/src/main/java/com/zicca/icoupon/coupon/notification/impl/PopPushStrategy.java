package com.zicca.icoupon.coupon.notification.impl;

import com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum;
import com.zicca.icoupon.coupon.notification.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum.POPUP;

/**
 * 弹框推送策略
 * @author zicca
 */
@Slf4j
@Component
public class PopPushStrategy implements NotificationStrategy {
    @Override
    public void push(Long userId, String message, Map<String, Object> params) {
        log.info("[策略] 弹框推送策略 - 弹框推送消息，消息体：{}", message);
    }

    @Override
    public NotifyTypeEnum getSupportedType() {
        return POPUP;
    }
}
