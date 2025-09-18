package com.zicca.icoupon.order.dto.req;

import com.zicca.icoupon.order.common.enums.CouponSupportTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单项请求参数
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单项请求参数")
public class OrderItemReqDTO {

    /**
     * 商品 ID
     */
    @Schema(description = "商品 ID", example = "1L")
    private Long goodsId;

    /**
     * 用户优惠券 ID
     */
    @Schema(description = "用户优惠券 ID", example = "1L")
    private List<Long> userCouponIds;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID", example = "1L")
    private Long userId;

    /**
     * 商品数量
     */
    @Schema(description = "商品数量", example = "1")
    private Integer quantity;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "商品")
    private String goodsName;

    /**
     * 商品价格
     */
    @Schema(description = "商品价格", example = "10.00")
    private BigDecimal price;

    /**
     * 优惠券支持类型
     */
    @Schema(description = "优惠券支持类型", example = "1")
    private CouponSupportTypeEnum couponSupportType;

}
