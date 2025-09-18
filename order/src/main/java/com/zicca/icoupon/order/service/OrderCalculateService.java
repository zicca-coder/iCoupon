package com.zicca.icoupon.order.service;

import com.zicca.icoupon.order.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderCalculateRespDTO;

/**
 * @author zicca
 */
public interface OrderCalculateService {

    /**
     * 订单计算
     *
     * @param requestParam 请求参数
     * @return 订单计算结果
     */
    OrderCalculateRespDTO calculateOrder(OrderCalculateReqDTO requestParam);

}
