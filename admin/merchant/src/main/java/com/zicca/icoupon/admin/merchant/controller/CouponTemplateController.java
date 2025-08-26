package com.zicca.icoupon.admin.merchant.controller;

import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateNumberReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.admin.merchant.service.CouponTemplateService;
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
@RequestMapping("/api/v1/admin/merchant/coupon-templates")
@Tag(name = "优惠券模板管理", description = "优惠券模板接口管理")
public class CouponTemplateController {

    private final CouponTemplateService couponTemplateService;

    @PostMapping
    @Operation(summary = "创建优惠券模板", description = "创建优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "创建优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> createCouponTemplate(@RequestBody CouponTemplateCreateReqDTO requestParam) {
        couponTemplateService.createCouponTemplate(requestParam);
        return Results.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询优惠券模板", description = "查询优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "查询优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<CouponTemplateQueryRespDTO> getCouponTemplateById(@PathVariable("id") Long id) {
        return Results.success(couponTemplateService.findCouponTemplateById(id));
    }

    @GetMapping
    @Operation(summary = "查询所有优惠券模板", description = "查询所有优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "查询所有优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<List<CouponTemplateQueryRespDTO>> getAllCouponTemplates() {
        return Results.success(couponTemplateService.getAllCouponTemplates());
    }

    @PutMapping
    @Operation(summary = "增加优惠券模板库存", description = "增加优惠券模板库存")
    @ApiResponse(
            responseCode = "200", description = "更新优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> updateCouponTemplate(@RequestBody CouponTemplateNumberReqDTO requestParam) {
        couponTemplateService.increaseNumberCouponTemplate(requestParam);
        return Results.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除优惠券模板", description = "删除优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "删除优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> deleteCouponTemplate(@PathVariable("id") Long id) {
        couponTemplateService.deleteCouponTemplateById(id);
        return Results.success();
    }

}
