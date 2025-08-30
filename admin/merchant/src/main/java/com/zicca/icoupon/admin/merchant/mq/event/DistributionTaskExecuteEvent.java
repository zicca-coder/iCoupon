package com.zicca.icoupon.admin.merchant.mq.event;

import com.zicca.icoupon.admin.merchant.common.enums.DistributionTaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 分发任务执行事件
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionTaskExecuteEvent {

    /**
     * 分发任务ID
     */
    private Long distributionTaskId;

    /**
     * 分发任务类型：0-立即发送 1-定时发送
     */
    private DistributionTaskTypeEnum type;

    /**
     * 分发时间
     */
    private Long sendTime;
}
