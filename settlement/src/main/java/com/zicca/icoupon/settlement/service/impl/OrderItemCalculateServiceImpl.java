package com.zicca.icoupon.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zicca.icoupon.framework.exception.ServiceException;
import com.zicca.icoupon.settlement.dto.req.OrderItemReqDTO;
import com.zicca.icoupon.settlement.service.CouponCalculateService;
import com.zicca.icoupon.settlement.service.OrderItemCalculateService;
import com.zicca.icoupon.settlement.service.basic.calculation.CouponCalculatePair;
import com.zicca.icoupon.settlement.service.basic.calculation.PricePair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    public PricePair calculateWithSingleCoupon(OrderItemReqDTO requestParam) {
        log.info("订单项计算服务开始执行，请求参数：{}", requestParam);
        if (Objects.isNull(requestParam)) {
            throw new ServiceException("请求参数为空");
        }
        CouponCalculatePair couponCalculateParam = CouponCalculatePair.builder()
                .goodsId(requestParam.getGoodsId())
                .userId(requestParam.getUserId())
                .userCouponId(requestParam.getUserCouponIds().get(0))
                .price(requestParam.getPrice())
                .quantity(requestParam.getQuantity())
                .build();
        return couponCalculateService.calculateAmount(couponCalculateParam);
    }


    @Override
    public PricePair calculateWithOverlayCoupon(OrderItemReqDTO requestParam) {
        if (requestParam == null) {
            throw new ServiceException("参数不能为空");
        }
        List<Long> userCouponIds = requestParam.getUserCouponIds();
        if (CollUtil.isEmpty(userCouponIds)) {
            return PricePair.builder()
                    .subtotalAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .discountAmount(BigDecimal.ZERO)
                    .payAmount(requestParam.getPrice().multiply(BigDecimal.valueOf(requestParam.getQuantity())))
                    .build();
        }
        // 如果商品只支持使用一张优惠券，那么默认使用第一张优惠券
        if (SINGLE_AVAILABLE == requestParam.getCouponSupportType()) {
            return calculateWithSingleCoupon(requestParam);
        }


        return null;
    }
}
