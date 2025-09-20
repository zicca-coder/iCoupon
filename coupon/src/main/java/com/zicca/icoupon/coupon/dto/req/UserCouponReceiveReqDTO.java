package com.zicca.icoupon.coupon.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户领取优惠券请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户领取优惠券请求参数")
public class UserCouponReceiveReqDTO {

    @Schema(description = "优惠券模板id", example = "1960313844890882050")
    private Long couponTemplateId;

    @Schema(description = "用户id", example = "1961811779001393153")
    private Long userId;

    @Schema(description = "店铺id", example = "1810714735922956666")
    private Long shopId;

    /**
     * 领取数量 默认为 1
     */
    @Schema(description = "领取数量", example = "1")
    private Integer count = 1;

}
