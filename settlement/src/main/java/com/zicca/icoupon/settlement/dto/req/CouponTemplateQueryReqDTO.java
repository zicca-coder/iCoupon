package com.zicca.icoupon.settlement.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优惠券模板查询请求参数
 *
 * @author zicca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "优惠券模板查询请求参数")
public class CouponTemplateQueryReqDTO {

    @Schema(description = "优惠券模板ID", example = "1L")
    private Long id;

    @Schema(description = "优惠券所属店铺ID", example = "1L")
    private Long shopId;

}
