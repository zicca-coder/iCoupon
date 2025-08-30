package com.zicca.icoupon.distribution.mq.event;

import com.zicca.icoupon.distribution.common.enums.DistributionTaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
