package com.zicca.icoupon.engine.notification;

import com.zicca.icoupon.engine.common.enums.NotifyTypeEnum;

import java.util.Map;

/**
 * 推送通知策略接口
 *
 * @author zicca
 */
public interface NotificationStrategy {

    /**
     * 推送消息
     *
     * @param userId  用户 ID
     * @param message 消息内容
     * @param params  额外参数
     */
    void push(Long userId, String message, Map<String, Object> params);

    /**
     * 获取支持的推送通知类型
     *
     * @return 推送通知类型
     */
    NotifyTypeEnum getSupportedType();


}
