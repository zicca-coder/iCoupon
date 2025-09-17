package com.zicca.icoupon.engine.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 预约提醒查询响应参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预约提醒查询响应参数")
public class ReservationReminderQueryRespDTO {

    /**
     * 优惠券模板id
     */
    @Schema(description = "优惠券模板id", example = "1960313844890882050")
    private Long couponTemplateId;

    @Schema(description = "店铺id", example = "1810714735922956666")
    private Long shopId;

    @Schema(description = "优惠券模板", example = "天天神券")
    private String name;

    @Schema(description = "优惠券模板有效期开始时间", example = "2023-05-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validStartTime;

    @Schema(description = "优惠券模板有效期结束时间", example = "2023-05-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validEndTime;

    @Schema(description = "预约提醒时间集合")
    List<RemindPair> remindPairs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemindPair {
        @Schema(description = "预约提醒时间", example = "2023-05-01 00:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date remindTime;

        @Schema(description = "预约提醒方式", example = "1")
        private String remindType;
    }

}
