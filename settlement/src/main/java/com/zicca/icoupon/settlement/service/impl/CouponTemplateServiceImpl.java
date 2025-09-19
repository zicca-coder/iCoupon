package com.zicca.icoupon.settlement.service.impl;

import com.zicca.icoupon.framework.exception.RemoteException;
import com.zicca.icoupon.framework.result.Result;
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
        try {
            SupportedGoodsReqDTO requestParam = new SupportedGoodsReqDTO(couponTemplateId, shopId, goodsId);
            Result<Boolean> result = engineServiceApi.isSupportGoods(requestParam);

            if (result == null) {
                log.error("[结算服务] | 远程调用引擎服务判断商品是否支持优惠券返回空结果，" + "couponTemplateId: {}, shopId: {}, goodsId: {}",
                        couponTemplateId, shopId, goodsId);
                throw new RemoteException("引擎服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[结算服务] | 远程调用引擎服务判断商品是否支持优惠券失败，" + "couponTemplateId: {}, shopId: {}, goodsId: {}, code: {}, message: {}",
                    couponTemplateId, shopId, goodsId, result.getCode(), result.getMessage());
            throw new RemoteException("引擎服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[结算服务] | 远程调用引擎服务判断商品是否支持优惠券异常，" + "couponTemplateId: {}, shopId: {}, goodsId: {}", couponTemplateId, shopId, goodsId, e);
            throw new RemoteException("引擎服务调用异常");
        }
    }

    @Override
    public Boolean isSupportGoods(Long couponTemplateId, List<Long> goodsIds) {
        return false;
    }
}
