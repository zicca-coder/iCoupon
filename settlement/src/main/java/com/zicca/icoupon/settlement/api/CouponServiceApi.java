package com.zicca.icoupon.settlement.api;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.settlement.dto.req.SupportedGoodsReqDTO;
import com.zicca.icoupon.settlement.dto.req.UserCouponListReqDTO;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 优惠券引擎服务
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-coupon", path = "/api/v1/coupon")
public interface CouponServiceApi {

    @PostMapping("/coupon-templates/support-goods")
    @Operation(summary = "查询优惠券是否支持该商品", description = "查询优惠券是否支持该商品")
    Result<Boolean> isSupportGoods(SupportedGoodsReqDTO requestParam);

    @GetMapping("/user-coupons/{id}/{userId}")
    @Operation(summary = "查询用户优惠券", description = "查询用户优惠券")
    Result<UserCouponQueryRespDTO> getUserCouponById(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @PostMapping("/user-coupons/batch")
    @Operation(summary = "批量查询用户优惠券", description = "根据用户优惠券ID集合批量查询用户优惠券")
    Result<List<UserCouponQueryRespDTO>> batchGetUserCouponList(@RequestBody UserCouponListReqDTO requestParam);

    @GetMapping("/user-coupons/available/{userId}")
    @Operation(summary = "查询用户可用优惠券列表", description = "查询用户可用优惠券列表")
    Result<List<UserCouponQueryRespDTO>> getAvailableUserCouponList(@PathVariable("userId") Long userId);


}
