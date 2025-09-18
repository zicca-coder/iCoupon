package com.zicca.icoupon.order.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.order.common.database.BaseDO;
import lombok.*;

/**
 * 商品优惠券使用记录 | 某个订单项中的商品使用了哪些优惠券
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_item_coupon_record")
public class OrderItemCouponRecord extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单项 ID
     */
    @TableField(value = "order_item_id")
    private Long orderItemId;

    /**
     * 商品 ID
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 用户优惠券 ID
     */
    @TableField(value = "user_coupon_id")
    private Long userCouponId;


    /**
     * 用户 ID | 冗余存储，用于分片键
     */
    @TableField(value = "user_id")
    private Long userId;

}
