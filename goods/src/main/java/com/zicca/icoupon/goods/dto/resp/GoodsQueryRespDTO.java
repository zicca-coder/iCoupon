package com.zicca.icoupon.goods.dto.resp;

import com.zicca.icoupon.goods.common.enums.CouponSupportTypeEnum;
import com.zicca.icoupon.goods.common.enums.GoodsStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品查询响应参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品查询响应参数")
public class GoodsQueryRespDTO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "店铺ID")
    private Long shopId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "商品原价")
    private BigDecimal originalPrice;

    @Schema(description = "商品库存")
    private Integer stock;

    @Schema(description = "商品状态")
    private GoodsStatusEnum status;

    @Schema(description = "商品优惠券支持类型")
    private CouponSupportTypeEnum couponSupportType;

}
