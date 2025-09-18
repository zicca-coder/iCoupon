package com.zicca.icoupon.settlement.service;

import com.zicca.icoupon.settlement.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;

/**
 * 优惠券计算服务
 *
 * @author zicca
 */
public interface OrderItemCalculateService {


    /**
     * 单张优惠券计算
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateWithSingleCoupon(OrderItemReqDTO requestParam);


    /**
     * 多张优惠券计算
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateWithOverlayCoupon(OrderItemReqDTO requestParam);


}
