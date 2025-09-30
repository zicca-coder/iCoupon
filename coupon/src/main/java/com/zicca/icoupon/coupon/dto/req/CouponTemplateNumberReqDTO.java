package com.zicca.icoupon.coupon.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 优惠券模板增加/减少发行量请求参数
 *
 * @author zicca
 */
@Data
public class CouponTemplateNumberReqDTO {

    @Schema(description = "优惠券模板id", example = "1L", required = true)
    private Long couponTemplateId;

    @Schema(description = "店铺id", example = "1L", required = true)
    private Long shopId;

    @Schema(description = "增加/减少发行数量", example = "100", required = true)
    private Integer number;

}
