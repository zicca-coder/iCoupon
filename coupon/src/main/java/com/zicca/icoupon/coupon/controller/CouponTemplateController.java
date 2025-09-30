package com.zicca.icoupon.coupon.controller;

import com.zicca.icoupon.coupon.dto.req.*;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.coupon.service.CouponTemplateService;
import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券模板控制层
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "优惠券模板管理", description = "优惠券模板接口管理")
public class CouponTemplateController {

    private final CouponTemplateService couponTemplateService;

    @PostMapping("/api/v1/coupon/coupon-templates")
    @Operation(summary = "创建优惠券模板", description = "创建优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "创建优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Long> createCouponTemplate(@RequestBody CouponTemplateCreateReqDTO requestParam) {
        return Results.success(couponTemplateService.createCouponTemplate(requestParam));
    }

    @PutMapping("/api/v1/coupon/coupon-templates/increase-number")
    @Operation(summary = "增加优惠券模板数量", description = "增加优惠券模板数量")
    @ApiResponse(
            responseCode = "200", description = "增加优惠券模板数量成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> increaseNumberCouponTemplate(@RequestBody CouponTemplateNumberReqDTO requestParam) {
        couponTemplateService.increaseNumberCouponTemplate(requestParam);
        return Results.success();
    }

    @DeleteMapping("/api/v1/coupon/coupon-templates/{id}/{shopId}")
    @Operation(summary = "删除优惠券模板", description = "删除优惠券模板")
    @ApiResponse(
            responseCode = "200",
            description = "删除优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> deleteCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId) {
        couponTemplateService.deleteCouponTemplate(id, shopId);
        return Results.success();
    }

    @GetMapping("/api/v1/coupon/coupon-templates/{shopId}/{id}")
    @Operation(summary = "查询优惠券模板", description = "查询优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "查询优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<CouponTemplateQueryRespDTO> getCouponTemplateById(@PathVariable("id") Long id,
                                                                    @PathVariable("shopId") Long shopId) {
        return Results.success(couponTemplateService.getCouponTemplate(id, shopId));
    }


    @PostMapping("/api/v1/coupon/coupon-templates/support-goods")
    @Operation(summary = "查询优惠券是否支持该商品", description = "查询优惠券是否支持该商品")
    @ApiResponse(
            responseCode = "200",
            description = "查询优惠券是否支持该商品",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))
    )
    public Result<Boolean> isSupportGoods(@RequestBody SupportedGoodsReqDTO requestParam) {
        return Results.success(couponTemplateService.isSupportGoods(requestParam));
    }

    @GetMapping("/api/v1/coupon/coupon-templates/{shopId}")
    @Operation(summary = "查询店铺优惠券模板列表", description = "根据店铺ID查询优惠券模板列表")
    @ApiResponse(
            responseCode = "200",
            description = "查询店铺优惠券模板列表",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByShopId(@PathVariable("shopId") Long shopId) {
        return Results.success(couponTemplateService.listCouponTemplateByShopId(shopId));
    }


    @PostMapping("/api/v1/coupon/coupon-templates/status")
    @Operation(summary = "查询特定状态优惠券模板列表", description = "根据优惠券状态查询优惠券模板列表")
    @ApiResponse(
            responseCode = "200",
            description = "查询特定状态优惠券模板列表",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByStatus(@RequestBody CouponTemplateStatusQueryReqDTO requestParam) {
        return Results.success(couponTemplateService.listCouponTemplateByStatus(requestParam));
    }

    @PostMapping("/api/v1/coupon/coupon-templates/type")
    @Operation(summary = "查询特定类型优惠券模板列表", description = "根据优惠券类型查询优惠券模板列表")
    @ApiResponse(
            responseCode = "200",
            description = "查询特定类型优惠券模板列表",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByType(@RequestBody CouponTemplateTypeQueryReqDTO requestParam) {
        return Results.success(couponTemplateService.listCouponTemplateByType(requestParam));
    }

    @GetMapping("/api/v1/coupon/coupon-templates/not-start/{shopId}")
    @Operation(summary = "查询未开始优惠券模板列表", description = "根据店铺ID查询未开始优惠券模板列表")
    @ApiResponse(
            responseCode = "200",
            description = "查询未开始优惠券模板列表",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<List<CouponTemplateQueryRespDTO>> getNotStartCouponTemplate(@PathVariable("shopId") Long shopId) {
        return Results.success(couponTemplateService.listNotStartCouponTemplate(shopId));
    }


    @PutMapping("/api/v1/coupon/coupon-templates/activate/{id}/{shopId}")
    @Operation(summary = "激活优惠券模板", description = "激活优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "激活优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> activateCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId) {
        couponTemplateService.activeCouponTemplate(id, shopId);
        return Results.success();
    }


    @PutMapping("/api/v1/coupon/coupon-templates/terminate/{id}/{shopId}")
    @Operation(summary = "终止优惠券模板", description = "终止优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "终止优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> terminateCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId) {
        couponTemplateService.terminateCouponTemplate(id, shopId);
        return Results.success();
    }


    @PutMapping("/api/v1/coupon/coupon-templates/cancel/{id}/{shopId}")
    @Operation(summary = "取消优惠券模板", description = "取消优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "取消优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> cancelCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId) {
        couponTemplateService.cancelCouponTemplate(id, shopId);
        return Results.success();
    }

}
