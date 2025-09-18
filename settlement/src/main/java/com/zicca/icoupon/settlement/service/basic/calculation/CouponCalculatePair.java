package com.zicca.icoupon.settlement.service.basic.calculation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 优惠券计算参数 | 单个商品项 + 单张优惠券
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCalculatePair {

    /**
     * 商品 ID
     */
    private Long goodsId;

    /**
     * 用户优惠券 ID
     */
    private Long userCouponId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品价格
     */
    private BigDecimal price;

}
