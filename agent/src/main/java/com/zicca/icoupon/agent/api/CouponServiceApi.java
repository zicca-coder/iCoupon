package com.zicca.icoupon.agent.api;

import com.zicca.icoupon.agent.dto.req.*;
import com.zicca.icoupon.agent.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.agent.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.agent.dto.resp.UserCouponQueryRespDTO;
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

    @GetMapping("/api/v1/coupon/coupon-templates/{id}/{shopId}")
    @Operation(summary = "查询优惠券模板", description = "查询优惠券模板")
    Result<CouponTemplateQueryRespDTO> getCouponTemplateById(@PathVariable("id") Long id, @PathVariable("shopId") Long shopId);


    @GetMapping("/api/v1/coupon/coupon-templates/{shopId}")
    @Operation(summary = "查询店铺优惠券模板列表", description = "根据店铺ID查询优惠券模板列表")
    Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByShopId(@PathVariable("shopId") Long shopId);


    @PostMapping("/api/v1/coupon/coupon-templates/status")
    @Operation(summary = "查询特定状态优惠券模板列表", description = "根据优惠券状态查询优惠券模板列表")
    Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByStatus(@RequestBody CouponTemplateStatusQueryReqDTO requestParam);


    @PostMapping("/api/v1/coupon/coupon-templates/type")
    @Operation(summary = "查询特定类型优惠券模板列表", description = "根据优惠券类型查询优惠券模板列表")
    Result<List<CouponTemplateQueryRespDTO>> getCouponTemplateByType(@RequestBody CouponTemplateTypeQueryReqDTO requestParam);


    @GetMapping("/api/v1/coupon/coupon-templates/not-start/{shopId}")
    @Operation(summary = "查询未开始优惠券模板列表", description = "根据店铺ID查询未开始优惠券模板列表")
    Result<List<CouponTemplateQueryRespDTO>> getNotStartCouponTemplate(@PathVariable("shopId") Long shopId);


    @PostMapping("/api/v1/coupon/user-coupons")
    @Operation(summary = "领取优惠券", description = "领取优惠券")
    Result<Void> receiveCoupon(@RequestBody UserCouponReceiveReqDTO requestParam);


    @GetMapping("/api/v1/coupon/user-coupons/{id}/{userId}")
    Result<UserCouponQueryRespDTO> getUserCouponById(@PathVariable("id") Long id, @PathVariable("userId") Long userId);


    @GetMapping("/api/v1/coupon/user-coupons/available/{userId}")
    @Operation(summary = "查询用户可用优惠券", description = "查询用户可用优惠券")
    Result<List<UserCouponQueryRespDTO>> getAvailableUserCouponList(@PathVariable("userId") Long userId);


    @GetMapping("/api/v1/coupon/user-coupons/within-one-week/{userId}")
    @Operation(summary = "查询用户一周内领取优惠券", description = "查询用户一周内领取的优惠券")
    Result<List<UserCouponQueryRespDTO>> getUserCouponListWithinOneWeek(@PathVariable("userId") Long userId);


    @GetMapping("/api/v1/coupon/user-coupons/within-three-days/{userId}")
    @Operation(summary = "查询用户三天内领取优惠券", description = "查询用户三天内领取的优惠券")
    Result<List<UserCouponQueryRespDTO>> getUserCouponListWithinThreeDays(@PathVariable("userId") Long userId);


    @PostMapping("/api/v1/coupon/reservation-reminders")
    @Operation(summary = "创建预约提醒", description = "创建预约提醒")
    Result<Void> createReservationReminder(ReservationReminderCreateReqDTO requestParam);


    @GetMapping("/api/v1/coupon/reservation-reminders")
    @Operation(summary = "查询预约提醒", description = "查询预约提醒")
    Result<List<ReservationReminderQueryRespDTO>> listReservationReminder();


    @PutMapping("/api/v1/coupon/reservation-reminders")
    @Operation(summary = "取消预约提醒", description = "取消预约提醒")
    Result<Void> cancelReservationReminder(ReservationReminderCancelReqDTO requestParam);

}
