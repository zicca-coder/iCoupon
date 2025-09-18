package com.zicca.icoupon.settlement.service.basic.calculation;

import java.util.List;

/**
 * 优惠券计算参数 | 多个商品项 + 单张优惠券
 *
 * @author zicca
 */
public class CouponCalculatePairV2 {


    public List<Long> goodsIds;

    private Long userCouponId;

    private Long userId;

    private Integer quantity;



    public static class ItemPari {
        private Long goodsId;

    }

}
