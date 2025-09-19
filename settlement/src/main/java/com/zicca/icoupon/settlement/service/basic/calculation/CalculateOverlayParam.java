package com.zicca.icoupon.settlement.service.basic.calculation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券计算参数 | 单个商品项 + 多张优惠券
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateOverlayParam {

    /**
     * 商品 ID
     */
    private Long goodsId;

    /**
     * 用户优惠券 ID
     */
    private List<Long> userCouponIds;

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
