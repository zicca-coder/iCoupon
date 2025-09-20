package com.zicca.icoupon.admin.merchant.dto.req;

import com.zicca.icoupon.admin.merchant.common.enums.CouponSupportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 创建商品请求参数
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建商品请求参数")
public class GoodsCreateReqDTO {

    @Schema(description = "店铺ID", example = "1810714735922956666")
    private Long shopId;

    @Schema(description = "商品名称", example = "iPhone 17 Pro Max")
    private String name;

    @Schema(description = "商品价格", example = "9999.99")
    private BigDecimal price;

    @Schema(description = "商品原价", example = "10999.99")
    private BigDecimal originalPrice;

    @Schema(description = "商品库存", example = "1000")
    private Integer stock;

    @Schema(description = "商品状态", example = "1")
    private CouponSupportTypeEnum couponSupportType;

}
