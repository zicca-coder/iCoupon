package com.zicca.icoupon.order.api;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.order.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderCalculateRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 结算服务
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-settlement", path = "/api/v1/settlement")
public interface SettlementServiceApi {

    @PostMapping("/order-calculate")
    @Operation(summary = "计算订单金额", description = "计算订单金额")
    Result<OrderCalculateRespDTO> calculateOrder(OrderCalculateReqDTO requestParam);

}
