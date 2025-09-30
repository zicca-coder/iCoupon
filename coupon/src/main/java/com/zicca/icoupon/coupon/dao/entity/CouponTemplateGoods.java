package com.zicca.icoupon.coupon.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.coupon.common.database.BaseDO;
import lombok.*;

/**
 * 优惠券模板商品关联表实体（可选，店铺券 & 平台券都能用）
 *
 * @author zicca
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_coupon_template_goods")
@EqualsAndHashCode(callSuper = true)
public class CouponTemplateGoods extends BaseDO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券模板 ID
     */
    @TableField("coupon_template_id")
    private Long couponTemplateId;

    /**
     * 商品 ID
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 店铺 ID
     */
    @TableField("shop_id")
    private Long shopId;

}