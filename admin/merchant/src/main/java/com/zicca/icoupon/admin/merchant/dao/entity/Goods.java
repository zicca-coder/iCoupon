package com.zicca.icoupon.admin.merchant.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zicca.icoupon.admin.merchant.common.database.BaseDO;
import com.zicca.icoupon.admin.merchant.common.enums.CouponSupportTypeEnum;
import com.zicca.icoupon.admin.merchant.common.enums.GoodsStatusEnum;
import lombok.*;

import java.math.BigDecimal;

/**
 * 商品实体
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_goods")
public class Goods extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 店铺 ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 商品名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 商品原价
     */
    @TableField(value = "original_price")
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    @TableField(value = "stock")
    private Integer stock;

    /**
     * 商品状态
     */
    @TableField(value = "status")
    private GoodsStatusEnum status;

    /**
     * 优惠券支持类型：0-不可使用优惠券 1-可使用单张优惠券 2-可叠加使用优惠券
     */
    @TableField(value = "coupon_support_type")
    private CouponSupportTypeEnum couponSupportType;

}
