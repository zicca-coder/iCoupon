package com.zicca.icoupon.settlement.controller;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import com.zicca.icoupon.settlement.dto.req.OrderCalculateReqDTO;
import com.zicca.icoupon.settlement.dto.resp.OrderCalculateRespDTO;
import com.zicca.icoupon.settlement.service.OrderCalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单计算接口
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settlement/order-calculate")
@Tag(name = "订单计算接口", description = "订单计算接口管理")
public class OrderCalculateController {

    private final OrderCalculateService orderCalculateService;

    @PostMapping
    @Operation(summary = "订单计算", description = "订单计算")
    @ApiResponse(
            responseCode = "200", description = "订单计算成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderCalculateRespDTO.class))
    )
    public Result<OrderCalculateRespDTO> calculateOrder(@RequestBody OrderCalculateReqDTO requestParam){
        return Results.success(orderCalculateService.calculateOrder(requestParam));
    }


}
