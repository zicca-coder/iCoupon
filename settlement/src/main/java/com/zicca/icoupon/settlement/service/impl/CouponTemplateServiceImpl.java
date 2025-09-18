package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.settlement.api.EngineServiceApi;
import com.zicca.icoupon.settlement.dto.req.SupportedGoodsReqDTO;
import com.zicca.icoupon.settlement.service.CouponTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 优惠券模板服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private final EngineServiceApi engineServiceApi;

    @Override
    public Boolean isSupportGoods(Long couponTemplateId, Long shopId, Long goodsId) {
        SupportedGoodsReqDTO requestParam = new SupportedGoodsReqDTO(couponTemplateId, shopId, goodsId);
        return engineServiceApi.isSupportGoods(requestParam).getData();
    }

    @Override
    public Boolean isSupportGoods(Long couponTemplateId, List<Long> goodsIds) {
        return false;
    }
}
