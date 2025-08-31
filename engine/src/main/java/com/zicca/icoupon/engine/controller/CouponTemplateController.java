package com.zicca.icoupon.engine.controller;

import com.zicca.icoupon.engine.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.engine.service.CouponTemplateService;
import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券模板控制层
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/engine/coupon-templates")
@Tag(name = "优惠券模板管理", description = "优惠券模板接口管理")
public class CouponTemplateController {

    private final CouponTemplateService couponTemplateService;

    @GetMapping("/{shopId}/{id}")
    @Operation(summary = "查询优惠券模板", description = "查询优惠券模板")
    @ApiResponse(
            responseCode = "200", description = "查询优惠券模板成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponTemplateQueryRespDTO.class))
    )
    public Result<CouponTemplateQueryRespDTO> getCouponTemplateById(@PathVariable("id") Long id,
                                                                    @PathVariable("shopId") Long shopId) {
        return Results.success(couponTemplateService.getCouponTemplate(id, shopId));
    }

}
