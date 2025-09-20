package com.zicca.icoupon.goods.controller;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import com.zicca.icoupon.goods.dto.req.GoodsCreateReqDTO;
import com.zicca.icoupon.goods.dto.resp.GoodsQueryRespDTO;
import com.zicca.icoupon.goods.service.GoodsService;
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
 * 商品控制器
 *
 * @author zicca
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/goods/goods")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品接口管理")
public class GoodsController {

    private final GoodsService goodsService;

    @PostMapping
    @Operation(summary = "创建商品", description = "创建商品")
    @ApiResponse(
            responseCode = "200", description = "创建商品成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Long> createGoods(@RequestBody GoodsCreateReqDTO requestParam) {
        return Results.success(goodsService.createGoods(requestParam));
    }


    @GetMapping("/{id}")
    @Operation(summary = "查询商品", description = "查询商品")
    @ApiResponse(
            responseCode = "200", description = "查询商品成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoodsQueryRespDTO.class))
    )
    public Result<GoodsQueryRespDTO> getGoodsById(@PathVariable("id") Long id) {
        return Results.success(goodsService.getGoodsById(id));
    }


    @GetMapping("/shop/{shopId}")
    @Operation(summary = "查询商品", description = "查询商品")
    @ApiResponse(
            responseCode = "200", description = "查询商品成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoodsQueryRespDTO.class))
    )
    public Result<List<GoodsQueryRespDTO>> listGoodsByShopId(@PathVariable("shopId") Long shopId) {
        return Results.success(goodsService.listGoodsByShopId(shopId));
    }

    @GetMapping("/discount/{shopId}")
    @Operation(summary = "查询优惠券商品", description = "查询优惠券商品")
    @ApiResponse(
            responseCode = "200", description = "查询商品成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoodsQueryRespDTO.class))
    )
    public Result<List<GoodsQueryRespDTO>> listDiscountedGoods(@PathVariable("shopId") Long shopId) {
        return Results.success(goodsService.listDiscountedGoods(shopId));
    }


    @PutMapping("/increase/{shopId}/{id}")
    @Operation(summary = "增加商品库存", description = "增加商品库存")
    @ApiResponse(
            responseCode = "200", description = "增加商品库存成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> increaseGoodsStock(@PathVariable("shopId") Long shopId, @PathVariable("id") Long id) {
        goodsService.increaseGoodsStock(shopId, id);
        return Results.success();
    }

    @PutMapping("/decrease/{shopId}/{id}")
    @Operation(summary = "减少商品库存", description = "减少商品库存")
    @ApiResponse(
            responseCode = "200", description = "减少商品库存成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> decreaseGoodsStock(@PathVariable("shopId") Long shopId, @PathVariable("id") Long id) {
        goodsService.decreaseGoodsStock(shopId, id);
        return Results.success();
    }

}
