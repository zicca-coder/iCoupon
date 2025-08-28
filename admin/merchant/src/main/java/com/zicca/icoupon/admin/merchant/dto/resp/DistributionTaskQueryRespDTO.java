package com.zicca.icoupon.admin.merchant.dto.resp;

import com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskStatusEnum;
import com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskTypeEnum;
import com.zicca.icoupon.admin.merchant.common.enums.NotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 分发任务查询响应参数
 * @author zicca
 */
@Schema(description = "分发任务查询响应参数")
public class DistributionTaskQueryRespDTO {

    @Schema(description = "分发任务 ID", example = "1")
    private Long id;

    @Schema(description = "券模板 ID", example = "1")
    private Long couponTemplateId;

    @Schema(description = "分发任务名称", example = "测试任务")
    private String taskName;

    @Schema(description = "分发数量", example = "100")
    private Integer sendNum;

    @Schema(description = "通知类型", example = "弹出推送")
    private NotifyTypeEnum notifyType;

    @Schema(description = "分发任务类型：0-立即发送 1-定时发送", example = "定时发送")
    private DistributionTaskTypeEnum type;

    @Schema(description = "分发任务状态：0-未开始 1-进行中 2-已完成 3-已取消 4-失败", example = "未开始")
    private DistributionTaskStatusEnum status;

    @Schema(description = "发送时间", example = "2024-08-31T23:59:59")
    private Date sendTime;

    @Schema(description = "完成时间", example = "2024-08-31T23:59:59")
    private Date completionTime;

    @Schema(description = "操作人", example = "商家123")
    private String updateBy;

}
