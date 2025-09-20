package com.zicca.icoupon.admin.merchant.api;

import com.zicca.icoupon.admin.merchant.dto.req.GoodsCreateReqDTO;
import com.zicca.icoupon.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 商品服务接口
 *
 * @author zicca
 */
@FeignClient(name = "iCoupon-goods", path = "api/v1/goods")
public interface GoodsServiceApi {


    @PostMapping("/goods")
    @Operation(summary = "创建商品", description = "创建商品")
    Result<Long> createGoods(@RequestBody GoodsCreateReqDTO requestParam);

}
