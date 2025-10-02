package com.zicca.icoupon.coupon.mq.event;

import com.zicca.icoupon.coupon.common.enums.StockUpdateTypeEnum;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存更新事件
 *
 * @author zicca
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateEvent {

    /*
     * 库存更新类型：1：用户端领取优惠券，2：商家端/后台修改优惠券库存
     */
    private StockUpdateTypeEnum type;

    /*
     * 优惠券模板ID
     */
    private Long couponTemplateId;

    private CouponTemplateStockUpdateEvent couponTemplateStockUpdateEvent;

    private UserCouponReceiveEvent userCouponReceiveEvent;


    /**
     * 优惠券模板库存更新事件
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponTemplateStockUpdateEvent {

        /**
         * 优惠券模板ID
         */
        private Long couponTemplateId;

        /**
         * 店铺ID
         */
        private Long shopId;

        /**
         * 优惠券模板更新数量：增加库存为整数，减少库存为负数
         */
        private Integer number;

        /**
         * 是否增加库存
         */
        @Builder.Default
        private boolean isAdd = true;
    }

    /**
     * 用户优惠券领取事件
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCouponReceiveEvent {

        /**
         * 用户ID
         */
        private Long userId;

        /**
         * 优惠券模板ID
         */
        private Long couponTemplateId;

        /**
         * 店铺ID
         */
        private Long shopId;

        /**
         * 领取数量 默认为 1
         */
        @Builder.Default
        private Integer count = 1;

        /**
         * 优惠券模板信息
         */
        private CouponTemplateQueryRespDTO couponTemplate;

        /**
         * 用户优惠券领取数量
         */
        private Integer receiveCount;

    }


}
