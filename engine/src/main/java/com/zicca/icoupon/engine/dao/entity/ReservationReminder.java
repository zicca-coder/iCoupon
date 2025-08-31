package com.zicca.icoupon.engine.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.engine.common.database.BaseDO;
import lombok.*;

import java.util.Date;

/**
 * 预约提醒实体类
 *
 * @author zicca
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_reservation_reminder")
public class ReservationReminder extends BaseDO {

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 优惠券模板 ID
     */
    @TableField(value = "coupon_template_id")
    private Long couponTemplateId;

    /**
     * 预约信息
     */
    @TableField(value = "information")
    private Long information;

    /**
     * 店铺 ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

}
