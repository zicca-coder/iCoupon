package com.zicca.icoupon.order.api;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.order.dto.req.UserCouponBathLockReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 优惠券引擎服务
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-engine", path = "/api/v1/engine")
public interface EngineServiceApi {

    @PutMapping("/user-coupons/lock/{id}/{userId}")
    @Operation(summary = "锁定用户优惠券", description = "锁定用户优惠券")
    Result<Void> lockUserCoupon(@PathVariable("id") Long id, @PathVariable("userId") Long userId);


    @PutMapping("/user-coupons/batch-lock")
    @Operation(summary = "批量锁定用户优惠券", description = "根据用户优惠券ID集合批量锁定用户优惠券")
    Result<Void> batchLockUserCoupon(@RequestBody UserCouponBathLockReqDTO requestParam);

}
