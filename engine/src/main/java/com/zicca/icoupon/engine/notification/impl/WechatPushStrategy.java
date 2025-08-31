package com.zicca.icoupon.engine.notification.impl;

import com.zicca.icoupon.engine.common.enums.NotifyTypeEnum;
import com.zicca.icoupon.engine.notification.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.zicca.icoupon.engine.common.enums.NotifyTypeEnum.WECHAT;

/**
 * 微信推送策略
 * @author zicca
 */
@Slf4j
@Component
public class WechatPushStrategy implements NotificationStrategy {
    @Override
    public void push(Long userId, String message, Map<String, Object> params) {
        log.info("[微信推送] 用户：{}，消息：{}", userId, message);
    }

    @Override
    public NotifyTypeEnum getSupportedType() {
        return WECHAT;
    }
}
