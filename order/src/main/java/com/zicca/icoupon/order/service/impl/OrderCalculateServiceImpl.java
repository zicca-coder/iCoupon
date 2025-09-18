package com.zicca.icoupon.order.service.impl;

import com.zicca.icoupon.order.api.SettlementServiceApi;
import com.zicca.icoupon.order.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderCalculateRespDTO;
import com.zicca.icoupon.order.service.OrderCalculateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCalculateServiceImpl implements OrderCalculateService {

    private final SettlementServiceApi settlementServiceApi;

    @Override
    public OrderCalculateRespDTO calculateOrder(OrderCalculateReqDTO requestParam) {
        return settlementServiceApi.calculateOrder(requestParam).getData();
    }
}
