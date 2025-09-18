package com.zicca.icoupon.settlement.service.basic.calculation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 价格对
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricePair {

    /**
     * 小计金额（商品单价*商品数量）
     */
    BigDecimal subtotalAmount;

    /**
     * 实际支付金额（商品单价*商品数量-优惠金额）
     */
    BigDecimal payAmount;

    /**
     * 优惠金额
     */
    BigDecimal discountAmount;

    /**
     * 零金额
     */
    public static PricePair ZERO;

    static {
        ZERO = PricePair.builder()
                .subtotalAmount(BigDecimal.ZERO)
                .payAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .build();
    }

    /**
     * 构造
     *
     * @param subtotalAmount 订单金额
     * @param payAmount      支付金额
     */
    public PricePair(BigDecimal subtotalAmount, BigDecimal payAmount) {
        this.subtotalAmount = subtotalAmount;
        this.payAmount = payAmount;
        this.discountAmount = subtotalAmount.subtract(payAmount);
    }


    /**
     * 构建无折扣金额
     *
     * @param subtotalAmount 小计金额
     * @return 优惠金额
     */
    public static PricePair noDiscount(BigDecimal subtotalAmount) {
        return PricePair.builder()
                .subtotalAmount(subtotalAmount)
                .payAmount(subtotalAmount)
                .discountAmount(BigDecimal.ZERO)
                .build();
    }

    /**
     * 是否有优惠 | 是否使用了优惠券
     * @return true:有优惠 | false:无优惠
     */
    public boolean isDiscount() {
        return this.discountAmount.compareTo(BigDecimal.ZERO) > 0;
    }

}
