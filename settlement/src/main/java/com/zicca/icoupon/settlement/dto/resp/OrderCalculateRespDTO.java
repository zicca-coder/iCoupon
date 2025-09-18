package com.zicca.icoupon.settlement.dto.resp;

import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单计算返回实体
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCalculateRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6859430684287475808L;

    /**
     * 订单总金额 | 各订单项小计金额之和
     */
    private BigDecimal totalAmount;

    /**
     * 订单总支付金额 | 各订单项支付金额之和
     */
    private BigDecimal payAmount;

    /**
     * 订单总优惠金额 | 各订单项优惠金额之和
     */
    private BigDecimal discountAmount;

    /**
     * 订单项价格信息
     */
    private Map<Long, PricePair> orderItemPriceInfo;

    /**
     * 订单项使用优惠券信息
     */
    private Map<Long, List<Long>> orderItemCouponInfo;

}
