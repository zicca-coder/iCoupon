package com.zicca.icoupon.admin.merchant.dto.req;

import com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskTypeEnum;
import com.zicca.icoupon.admin.merchant.common.enums.NotifyTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 分发任务创建请求参数
 *
 * @author zicca
 */
@Data
@Schema(description = "分发任务创建请求参数")
public class DistributionTaskCreateReqDTO {

    @Schema(description = "券模板 ID", example = "1960313844890882050")
    private Long couponTemplateId;

    @Schema(description = "分发任务名称", example = "测试任务")
    private String taskName;

    @Schema(description = "分发名单地址", example = "E:\\workspace\\iCoupon\\admin\\tmp\\推送名单.xlsx")
    private String fileAddress;

    @Schema(description = "通知类型：0-弹窗推送 1-邮件 2-短信 3-站内信 4-微信", example = "1")
    private NotifyTypeEnum notifyType;

    @Schema(description = "分发任务类型：0-立即发送 1-定时发送", example = "0")
    private DistributionTaskTypeEnum type;

    @Schema(description = "发送时间", example = "2024-08-31T23:59:59")
    private Date sendTime;
}
