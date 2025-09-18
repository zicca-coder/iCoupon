package com.zicca.icoupon.order.controller;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import com.zicca.icoupon.order.dto.req.OrderGenerateReqDTO;
import com.zicca.icoupon.order.dto.resp.OrderQueryRespDTO;
import com.zicca.icoupon.order.service.OrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单信息控制器
 *
 * @author zicca
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order/order-Info")
@Tag(name = "订单信息管理", description = "订单信息接口管理")
public class OrderInfoController {

    private final OrderInfoService orderInfoService;


    @GetMapping("/{id}")
    @Operation(summary = "查询订单", description = "查询订单")
    @ApiResponse(
            responseCode = "200", description = "查询订单成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderQueryRespDTO.class))
    )
    public Result<OrderQueryRespDTO> getOrderById(@PathVariable Long id) {
        return Results.success(orderInfoService.getOrderById(id));
    }

    @GetMapping
    @Operation(summary = "查询订单列表", description = "查询订单列表")
    @ApiResponse(
            responseCode = "200", description = "查询订单列表成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
    )
    public Result<List<OrderQueryRespDTO>> getOrderList() {
        return Results.success(orderInfoService.getOrderList());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单", description = "删除订单")
    @ApiResponse(
            responseCode = "200", description = "删除订单成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> deleteOrderById(@PathVariable Long id) {
        orderInfoService.deleteOrderById(id);
        return Results.success();
    }

    @PostMapping
    @Operation(summary = "生成订单", description = "生成订单")
    @ApiResponse(
            responseCode = "200", description = "生成订单成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> generateOrder(@RequestBody OrderGenerateReqDTO requestParam) {
        orderInfoService.generateOrder(requestParam);
        return Results.success();
    }

}
