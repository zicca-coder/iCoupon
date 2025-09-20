package com.zicca.icoupon.coupon.executor;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.alibaba.fastjson2.JSON;
import com.zicca.icoupon.coupon.common.enums.NotifyTypeEnum;
import com.zicca.icoupon.coupon.notification.NotificationStrategyManager;
import com.zicca.icoupon.coupon.service.ReservationReminderService;
import com.zicca.icoupon.coupon.service.basics.reminder.ReservationReminderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 预约提醒推送执行器
 *
 * @author zicca
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationReminderPushExecutor {

    private final ReservationReminderService reservationReminderService;
    private final NotificationStrategyManager notificationStrategyManager;
    // 推送消息执行器
    private final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() << 1,
            Runtime.getRuntime().availableProcessors() << 2,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNamePrefix("reservation-reminder-push-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );


    public void execute(ReservationReminderDTO requestParam) {
        if (!reservationReminderService.isReservationReminderExist(requestParam)) {
            log.info("用户已取消优惠券预约提醒，参数：{}", JSON.toJSONString(requestParam));
            return;
        }
        executorService.execute(() -> {
            // 推送提醒
            String message = String.format("您预约的优惠券【%s】即将在%s开始领取，请及时关注！",
                    requestParam.getCouponTemplateId(), requestParam.getStartTime());
            notificationStrategyManager.executePush(NotifyTypeEnum.getByCode(requestParam.getType()), requestParam.getUserId(), message, null);
        });

    }

}
