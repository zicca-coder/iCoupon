package com.zicca.icoupon.agent.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 预约提醒创建请求参数
 *
 * @author zicca
 */
@Data
@Schema(description = "预约提醒创建请求参数")
public class ReservationReminderCreateReqDTO {

    /**
     * 优惠券模板id
     */
    @Schema(description = "优惠券模板id", example = "1960313844890882050")
    private Long couponTemplateId;

    /**
     * 店铺id
     */
    @Schema(description = "店铺id", example = "1810714735922956666")
    private Long shopId;

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


}
