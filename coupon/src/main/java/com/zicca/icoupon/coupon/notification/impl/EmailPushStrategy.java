package com.zicca.icoupon.coupon.notification.impl;

import com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum;
import com.zicca.icoupon.coupon.notification.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum.EMAIL;

/**
 * 邮件推送策略
 * @author zicca
 */
@Slf4j
@Component
public class EmailPushStrategy implements NotificationStrategy {
    @Override
    public void push(Long userId, String message, Map<String, Object> params) {
        log.info("[邮件推送] 用户：{}，消息：{}", userId, message);
    }

    @Override
    public NotifyTypeEnum getSupportedType() {
        return EMAIL;
    }
}
