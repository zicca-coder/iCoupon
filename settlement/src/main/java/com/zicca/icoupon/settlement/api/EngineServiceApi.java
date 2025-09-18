package com.zicca.icoupon.settlement.api;

import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.settlement.dto.req.SupportedGoodsReqDTO;
import com.zicca.icoupon.settlement.dto.resp.UserCouponQueryRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 优惠券引擎服务
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-engine", path = "/api/v1/engine")
public interface EngineServiceApi {

    @PostMapping("/coupon-templates/support-goods")
    Result<Boolean> isSupportGoods(SupportedGoodsReqDTO requestParam);


    @GetMapping("/user-coupons/{id}/{userId}")
    Result<UserCouponQueryRespDTO> getUserCouponById(@PathVariable("id") Long id, @PathVariable("userId") Long userId);


}
