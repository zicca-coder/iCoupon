package com.zicca.icoupon.engine.mq.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 预约提醒到时推送事件
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationReminderPushEvent {

    /**
     * 优惠券模板ID
     */
    private Long couponTemplateId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 提醒方式
     */
    private Integer type;

    /**
     * 提醒时间
     */
    private Integer remindTime;

    /**
     * 开抢时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 延迟时间
     */
    private Long delayTime;
}
