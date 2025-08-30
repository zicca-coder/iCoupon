package com.zicca.icoupon.distribution.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.zicca.icoupon.distribution.common.database.BaseDO;
import com.zicca.icoupon.distribution.common.enums.DistributionTaskStatusEnum;
import com.zicca.icoupon.distribution.common.enums.DistributionTaskTypeEnum;
import com.zicca.icoupon.distribution.common.enums.NotifyTypeEnum;
import lombok.*;

import java.util.Date;

/**
 * 分发任务
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_distribution_task")
public class DistributionTask extends BaseDO {

    /**
     * 分发任务 ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 券模板 ID
     */
    @TableField(value = "coupon_template_id")
    private Long couponTemplateId;

    /**
     * 店铺 ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 任务名称
     */
    @TableField(value = "task_name")
    private String taskName;

    /**
     *  文件地址
     */
    @TableField(value = "file_address")
    private String fileAddress;

    /**
     * 失败文件地址
     */
    @TableField(value = "fail_file_address")
    private String failFileAddress;

    /**
     * 发送数量
     */
    @TableField(value = "send_num")
    private Integer sendNum;

    /**
     * 通知方式
     * 0：弹框推送 1: 邮件 2: 短信 3: 站内信 4: 微信
     */
    @TableField(value = "notify_type")
    private NotifyTypeEnum notifyType;

    /**
     * 任务类型
     * 0：立即发送 1：定时发送
     */
    @TableField(value = "type")
    private DistributionTaskTypeEnum type;

    /**
     * 发送时间
     */
    @TableField(value = "send_time")
    private Date sendTime;

    /**
     * 任务状态
     * 0：未开始 1：进行中 2：已完成 3：已取消 4：失败
     */
    @TableField(value = "status")
    private DistributionTaskStatusEnum status;

    /**
     * 完成时间
     */
    @TableField(value = "completion_time")
    private Date completionTime;

}
