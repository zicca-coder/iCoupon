package com.zicca.icoupon.admin.merchant.controller;

import com.zicca.icoupon.admin.merchant.dto.req.DistributionTaskCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.DistributionTaskQueryRespDTO;
import com.zicca.icoupon.admin.merchant.service.DistributionTaskService;
import com.zicca.icoupon.framework.result.Result;
import com.zicca.icoupon.framework.web.Results;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分发任务管理
 *
 * @author zicca
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/merchant/distribution-tasks")
@Tag(name = "分发任务管理", description = "分发任务接口管理")
public class DistributionTaskController {

    private final DistributionTaskService distributionTaskService;

    @PostMapping
    @Operation(summary = "创建分发任务", description = "创建分发任务")
    @ApiResponse(
            responseCode = "200", description = "创建分发任务成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> createDistributionTask(@RequestBody DistributionTaskCreateReqDTO requestParam) {
        distributionTaskService.createDistributionTask(requestParam);
        return Results.success();
    }

    @GetMapping
    @Operation(summary = "查询所有分发任务", description = "查询所有分发任务")
    @ApiResponse(
            responseCode = "200", description = "查询所有分发任务成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DistributionTaskQueryRespDTO.class))
    )
    public Result<List<DistributionTaskQueryRespDTO>> getAllTasks() {
        return Results.success(distributionTaskService.getAllDistributionTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取任务详情")
    @ApiResponse(
            responseCode = "200", description = "查询分发任务详情成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DistributionTaskQueryRespDTO.class))
    )
    public Result<DistributionTaskQueryRespDTO> getTaskById(@PathVariable("id") Long id) {
        return Results.success(distributionTaskService.getDistributionTaskById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务")
    @ApiResponse(
            responseCode = "200", description = "删除分发任务成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))
    )
    public Result<Void> deleteTask(@PathVariable Long id) {
        distributionTaskService.deleteDistributionTaskById(id);
        return Results.success();
    }

}
