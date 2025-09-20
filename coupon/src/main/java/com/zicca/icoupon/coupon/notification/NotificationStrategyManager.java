package com.zicca.icoupon.coupon.notification;

import com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略模式管理
 * @author zicca
 */
@Component
public class NotificationStrategyManager {

    private final Map<NotifyTypeEnum, NotificationStrategy> strategyMap = new ConcurrentHashMap<>();

    public NotificationStrategyManager(List<NotificationStrategy> strategies) {
        strategies.stream().forEach(strategy -> strategyMap.put(strategy.getSupportedType(), strategy));
    }

    /**
     * 根据通知类型获取对应的策略
     * @param type 通知类型
     * @return 推送策略
     */
    public NotificationStrategy getStrategy(NotifyTypeEnum type) {
        return strategyMap.get(type);
    }

    /**
     * 执行推送
     * @param type 通知类型
     * @param userId 用户ID
     * @param message 消息内容
     * @param params 额外参数
     */
    public void executePush(NotifyTypeEnum type, Long userId, String message, Map<String, Object> params) {
        NotificationStrategy strategy = getStrategy(type);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的通知类型: " + type);
        }
        strategy.push(userId, message, params);
    }

}
