package com.zicca.icoupon.engine.service.basics.reminder;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 预约提醒数据传输对象
 * @author zicca
 */
@Data
@Schema(description = "预约提醒数据传输对象")
public class ReservationReminderDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id", example = "1961811779001393153")
    private Long userId;

    /**
     * 优惠券模板id
     */
    @Schema(description = "优惠券模板id", example = "1960313844890882050")
    private Long couponTemplateId;

    /**
     * 店铺id
     */
    @Schema(description = "店铺id", example = "1810714735922956666")
    private Long shop_id;

    /**
     * 预约提醒方式
     */
    @Schema(description = "预约提醒方式", example = "1")
    private Integer type;

    /**
     * 预约提醒时间，比如五分钟，十分钟、十五分钟
     */
    @Schema(description = "预约提醒时间，比如五分钟，十分钟、十五分钟", example = "5")
    private Integer remindTime;

    /**
     * 预约开始时间
     */
    @Schema(description = "预约开始时间", example = "2023-05-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;


}
