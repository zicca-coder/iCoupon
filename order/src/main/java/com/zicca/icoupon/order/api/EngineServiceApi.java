package com.zicca.icoupon.order.api;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.order.dto.req.UserCouponBathLockReqDTO;
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

    /**
     * 锁定用户优惠券
     *
     * @param id     主键 ID
     * @param userId 用户 ID
     * @return 锁定结果
     */
    @PutMapping("/user-coupons/lock/{id}/{userId}")
    Result<Void> lockUserCoupon(@PathVariable("id") Long id, @PathVariable("userId") Long userId);


    @PutMapping("/user-coupons/batch-lock")
    Result<Void> batchLockUserCoupon(@RequestBody UserCouponBathLockReqDTO requestParam);

}
