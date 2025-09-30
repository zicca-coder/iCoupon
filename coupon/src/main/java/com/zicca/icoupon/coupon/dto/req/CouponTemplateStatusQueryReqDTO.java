package com.zicca.icoupon.coupon.dto.req;

import com.zicca.icoupon.coupon.common.enums.CouponTemplateStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券模板状态查询请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "优惠券模板状态查询请求参数")
public class CouponTemplateStatusQueryReqDTO {

    @Schema(description = "优惠券模板ID", example = "1810714735922956666")
    private Long shopId;

    @Schema(description = "优惠券模板状态", example = "1")
    private CouponTemplateStatusEnum status;

}
