package com.zicca.icoupon.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zicca.icoupon.framework.exception.ServiceException;
import com.zicca.icoupon.settlement.dto.req.MultiItemReqDTO;
import com.zicca.icoupon.settlement.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.settlement.service.CouponCalculateService;
import com.zicca.icoupon.settlement.service.OrderItemCalculateService;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateOverlayParam;
import com.zicca.icoupon.settlement.service.basic.calculation.CalculateSingleParam;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.zicca.icoupon.settlement.common.enums.CouponSupportTypeEnum.OVERLAY_AVAILABLE;
import static com.zicca.icoupon.settlement.common.enums.CouponSupportTypeEnum.SINGLE_AVAILABLE;

/**
 * 订单项计算服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemCalculateServiceImpl implements OrderItemCalculateService {

    private final CouponCalculateService couponCalculateService;


    @Override
    public PricePair calculateItemWithSingleCoupon(OrderItemReqDTO requestParam) {
        if (requestParam == null || requestParam.getCouponSupportType() != SINGLE_AVAILABLE) {
            log.error("[订单商品项计算服务] 单张优惠券计算 - 参数错误, requestParam: {}", requestParam);
            throw new ServiceException("参数错误");
        }
        log.info("[订单商品项计算服务] 单张优惠券计算 - 开始执行, requestParam: {}", requestParam);
        List<Long> userCouponIds = requestParam.getUserCouponIds();
        if (CollUtil.isEmpty(userCouponIds)) {
            log.warn("[订单商品项计算服务] 单张优惠券计算 - 优惠券为空, requestParam: {}", requestParam);
            return PricePair.builder()
                    .subtotalAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .discountAmount(BigDecimal.ZERO)
                    .payAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .build();
        }
        CalculateSingleParam calculateSingleParam = CalculateSingleParam.builder()
                .goodsId(requestParam.getGoodsId())
                .userId(requestParam.getUserId())
                .userCouponId(userCouponIds.get(0))
                .price(requestParam.getPrice())
                .quantity(requestParam.getQuantity())
                .build();
        return couponCalculateService.calculateSingleAmount(calculateSingleParam);
    }


    @Override
    public PricePair calculateItemWithOverlayCoupon(OrderItemReqDTO requestParam) {
        if (requestParam == null || requestParam.getCouponSupportType() != OVERLAY_AVAILABLE) {
            log.error("[订单商品项计算服务] 优惠券叠加计算 - 优惠券参数错误, requestParam: {}", requestParam);
            throw new ServiceException("参数不能为空");
        }
        List<Long> userCouponIds = requestParam.getUserCouponIds();
        if (CollUtil.isEmpty(userCouponIds)) {
            log.info("[订单商品项计算服务] 优惠券叠加计算 - 优惠券为空, requestParam: {}", requestParam);
            return PricePair.builder()
                    .subtotalAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .discountAmount(BigDecimal.ZERO)
                    .payAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .build();
        }
        // 设定规则：多张券叠加依次使用立减券、满减券、折扣券、随机券
        CalculateOverlayParam calculateOverlayParam = CalculateOverlayParam.builder()
                .goodsId(requestParam.getGoodsId())
                .userId(requestParam.getUserId())
                .userCouponIds(userCouponIds)
                .price(requestParam.getPrice())
                .quantity(requestParam.getQuantity())
                .build();

        return couponCalculateService.calculateOverlayAmount(calculateOverlayParam);
    }

    @Override
    public PricePair calculateItemsWithSingleCoupon(MultiItemReqDTO requestParam) {
        return PricePair.ZERO;
    }

    @Override
    public PricePair calculateItemsWithOverlayCoupon(MultiItemReqDTO requestParam) {
        return PricePair.ZERO;
    }
}
