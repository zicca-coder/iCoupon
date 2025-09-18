package com.zicca.icoupon.engine.controller;

import com.zicca.icoupon.engine.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.engine.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.engine.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.engine.service.UserCouponService;
import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户优惠券控制层
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/engine/user-coupons")
@Tag(name = "用户优惠券管理", description = "用户优惠券接口管理")
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping
    @Operation(summary = "领取优惠券", description = "领取优惠券")
    @ApiResponse(
            responseCode = "200", description = "领取优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> receiveCoupon(@RequestBody UserCouponReceiveReqDTO requestParam){
        userCouponService.receiveCoupon(requestParam);
        return Results.success();
    }



    @GetMapping("/{id}/{userId}")
    @Operation(summary = "查询用户优惠券", description = "查询用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "查询用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<UserCouponQueryRespDTO> getUserCouponById(@PathVariable("id") Long id, @PathVariable("userId") Long userId){
        return Results.success(userCouponService.getUserCouponById(id, userId));
    }


    @PutMapping("/batch/{id}/{userId}")
    @Operation(summary = "锁定用户优惠券", description = "锁定用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "锁定用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> lockUserCoupon(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        userCouponService.lockUserCoupon(id, userId);
        return Results.success();
    }


    @PutMapping("/batch-lock")
    public Result<Void> batchLockUserCoupon(@RequestBody UserCouponBathLockReqDTO requestParam) {
        userCouponService.batchLockUserCoupon(requestParam);
        return Results.success();
    }


}
