package com.zicca.icoupon.distribution.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分发记录实体
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_distribution_record")
public class DistributionRecord {

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 分发任务 ID
     */
    @TableField(value = "distribution_task_id")
    private Long distributionTaskId;


}
