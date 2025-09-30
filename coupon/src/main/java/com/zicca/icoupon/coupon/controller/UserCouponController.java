package com.zicca.icoupon.coupon.controller;

import com.zicca.icoupon.coupon.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponListReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.coupon.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.coupon.service.UserCouponService;
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
 * 用户优惠券控制层
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/user-coupons")
@Tag(name = "用户优惠券管理", description = "用户优惠券接口管理")
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping("/api/v1/coupon/user-coupons")
    @Operation(summary = "领取优惠券", description = "领取优惠券")
    @ApiResponse(
            responseCode = "200", description = "领取优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> receiveCoupon(@RequestBody UserCouponReceiveReqDTO requestParam){
        userCouponService.receiveCoupon(requestParam);
        return Results.success();
    }



    @GetMapping("/api/v1/coupon/user-coupons/{id}/{userId}")
    @Operation(summary = "查询用户优惠券", description = "查询用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "查询用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<UserCouponQueryRespDTO> getUserCouponById(@PathVariable("id") Long id, @PathVariable("userId") Long userId){
        return Results.success(userCouponService.getUserCouponById(id, userId));
    }


    @PutMapping("/api/v1/coupon/user-coupons/batch/{id}/{userId}")
    @Operation(summary = "锁定用户优惠券", description = "锁定用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "锁定用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> lockUserCoupon(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        userCouponService.lockUserCoupon(id, userId);
        return Results.success();
    }


    @PutMapping("/api/v1/coupon/user-coupons/batch-lock")
    @Operation(summary = "批量锁定用户优惠券", description = "批量锁定用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "批量锁定用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> batchLockUserCoupon(@RequestBody UserCouponBathLockReqDTO requestParam) {
        userCouponService.batchLockUserCoupon(requestParam);
        return Results.success();
    }


    @PostMapping("/api/v1/coupon/user-coupons/batch")
    @Operation(summary = "批量查询用户优惠券", description = "批量查询用户优惠券")
    @ApiResponse(
            responseCode = "200", description = "批量查询用户优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<List<UserCouponQueryRespDTO>> batchGetUserCouponList(@RequestBody UserCouponListReqDTO requestParam) {
        return Results.success(userCouponService.getUserCouponList(requestParam));
    }


    @GetMapping("/api/v1/coupon/user-coupons/available/{userId}")
    @Operation(summary = "查询用户可用优惠券", description = "查询用户可用优惠券")
    @ApiResponse(
            responseCode = "200", description = "查询用户可用优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<List<UserCouponQueryRespDTO>> getAvailableUserCouponList(@PathVariable("userId") Long userId) {
        return Results.success(userCouponService.getAvailableUserCouponList(userId));
    }


    @GetMapping("/api/v1/coupon/user-coupons/within-one-week/{userId}")
    @Operation(summary = "查询用户一周内领取优惠券", description = "查询用户一周内领取的优惠券")
    @ApiResponse(
            responseCode = "200", description = "查询用户一周内领取优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<List<UserCouponQueryRespDTO>> getUserCouponListWithinOneWeek(@PathVariable("userId") Long userId) {
        return Results.success(userCouponService.getUserCouponListWithInWeek(userId));
    }


    @GetMapping("/api/v1/coupon/user-coupons/within-three-days/{userId}")
    @Operation(summary = "查询用户三天内领取优惠券", description = "查询用户三天内领取的优惠券")
    @ApiResponse(
            responseCode = "200", description = "查询用户三天内领取优惠券成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserCouponQueryRespDTO.class))
    )
    public Result<List<UserCouponQueryRespDTO>> getUserCouponListWithinThreeDays(@PathVariable("userId") Long userId) {
        return Results.success(userCouponService.getUserCouponListWithInThreeDays(userId));
    }


}
