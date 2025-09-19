package com.zicca.icoupon.settlement.service;

import com.zicca.icoupon.settlement.dto.req.MultiItemReqDTO;
import com.zicca.icoupon.settlement.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;

/**
 * 优惠券计算服务
 *
 * @author zicca
 */
public interface OrderItemCalculateService {


    /**
     * 计算仅支持单张优惠券的商品项的价格和优惠
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateItemWithSingleCoupon(OrderItemReqDTO requestParam);


    /**
     * 计算支持叠加优惠券的商品项的价格和优惠
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateItemWithOverlayCoupon(OrderItemReqDTO requestParam);

    /**
     * 计算仅支持单张优惠券的商品项组合价格和优惠
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateItemsWithSingleCoupon(MultiItemReqDTO requestParam);

    /**
     * 计算支持叠加优惠券的商品项组合的价格和优惠
     *
     * @param requestParam 订单项参数
     * @return 优惠金额
     */
    PricePair calculateItemsWithOverlayCoupon(MultiItemReqDTO requestParam);


    // 实现接口，从优惠券列表中选择使用后价格最低的优惠券，返回金额以及使用的优惠券


}
