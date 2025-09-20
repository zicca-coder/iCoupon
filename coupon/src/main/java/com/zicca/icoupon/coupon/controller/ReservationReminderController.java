package com.zicca.icoupon.coupon.controller;

import com.zicca.icoupon.coupon.dto.req.ReservationReminderCancelReqDTO;
import com.zicca.icoupon.coupon.dto.req.ReservationReminderCreateReqDTO;
import com.zicca.icoupon.coupon.dto.resp.ReservationReminderQueryRespDTO;
import com.zicca.icoupon.coupon.service.ReservationReminderService;
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
 * 预约提醒控制层
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/reservation-reminders")
@Tag(name = "预约提醒管理", description = "预约提醒接口管理")
public class ReservationReminderController {

    private final ReservationReminderService reservationReminderService;

    @PostMapping
    @Operation(summary = "创建预约提醒", description = "创建预约提醒")
    @ApiResponse(
            responseCode = "200", description = "创建预约提醒成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> createReservationReminder(ReservationReminderCreateReqDTO requestParam) {
        reservationReminderService.createReservationReminder(requestParam);
        return Results.success();
    }

    @GetMapping
    @Operation(summary = "查询预约提醒", description = "查询预约提醒")
    @ApiResponse(
            responseCode = "200", description = "查询预约提醒成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationReminderQueryRespDTO.class))
    )
    public Result<List<ReservationReminderQueryRespDTO>> listReservationReminder() {
        return Results.success(reservationReminderService.listReservationReminder());
    }


    @PutMapping
    @Operation(summary = "取消预约提醒", description = "取消预约提醒")
    @ApiResponse(
            responseCode = "200", description = "取消预约提醒成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> cancelReservationReminder(ReservationReminderCancelReqDTO requestParam) {
        reservationReminderService.cancelReservationReminder(requestParam);
        return Results.success();
    }

}
