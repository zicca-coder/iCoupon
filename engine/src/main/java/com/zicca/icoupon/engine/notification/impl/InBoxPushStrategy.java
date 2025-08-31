package com.zicca.icoupon.engine.notification.impl;

import com.zicca.icoupon.engine.common.enums.NotifyTypeEnum;
import com.zicca.icoupon.engine.notification.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zicca.icoupon.engine.common.enums.NotifyTypeEnum.INBOX;

/**
 * APP站内推送策略
 *
 * @author zicca
 */
@Slf4j
@Component
public class InBoxPushStrategy implements NotificationStrategy {
    @Override
    public void push(Long userId, String message, Map<String, Object> params) {
        log.info("[APP站内推送] userId: {}, message: {}", userId, message);
    }

    @Override
    public NotifyTypeEnum getSupportedType() {
        return INBOX;
    }
}
