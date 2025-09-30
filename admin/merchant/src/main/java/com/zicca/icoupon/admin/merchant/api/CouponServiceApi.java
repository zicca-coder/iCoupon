package com.zicca.icoupon.admin.merchant.api;

import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateNumberReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券服务接口
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-coupon")
public interface CouponServiceApi {

    @PostMapping("/api/v1/coupon/coupon-templates")
    @Operation(summary = "创建优惠券模板", description = "创建优惠券模板")
    Result<Long> createCouponTemplate(@RequestBody CouponTemplateCreateReqDTO requestParam);

    @PutMapping("/api/v1/coupon/coupon-templates/increase-number")
    @Operation(summary = "增加优惠券模板数量", description = "增加优惠券模板数量")
    Result<Void> increaseNumberCouponTemplate(@RequestBody CouponTemplateNumberReqDTO requestParam);

    @DeleteMapping("/api/v1/coupon/coupon-templates/{id}/{shopId}")
    @Operation(summary = "删除优惠券模板", description = "删除优惠券模板")
    Result<Void> deleteCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);

    @GetMapping("/api/v1/coupon/coupon-templates/{shopId}/{id}")
    @Operation(summary = "查询优惠券模板", description = "查询优惠券模板")
    Result<CouponTemplateQueryRespDTO> getCouponTemplateById(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);

    @GetMapping("/api/v1/coupon/coupon-templates/{shopId}")
    @Operation(summary = "查询店铺优惠券模板列表", description = "根据店铺ID查询优惠券模板列表")
    Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByShopId(@PathVariable("shopId") Long shopId);


    @PutMapping("/api/v1/coupon/coupon-templates/activate/{id}/{shopId}")
    @Operation(summary = "激活优惠券模板", description = "激活优惠券模板")
    Result<Void> activateCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);

    @PutMapping("/api/v1/coupon/coupon-templates/terminate/{id}/{shopId}")
    @Operation(summary = "终止优惠券模板", description = "终止优惠券模板")
    Result<Void> terminateCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);

    @PutMapping("/api/v1/coupon/coupon-templates/cancel/{id}/{shopId}")
    @Operation(summary = "取消优惠券模板", description = "取消优惠券模板")
    Result<Void> cancelCouponTemplate(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);


}
