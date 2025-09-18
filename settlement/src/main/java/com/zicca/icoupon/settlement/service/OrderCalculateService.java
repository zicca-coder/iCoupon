package com.zicca.icoupon.settlement.service;

import com.zicca.icoupon.settlement.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.settlement.dto.resp.OrderCalculateRespDTO;

/**
 * 订单计算服务
 *
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
