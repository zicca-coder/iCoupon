package com.zicca.icoupon.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.coupon.dao.entity.CouponTemplate;
import com.zicca.icoupon.coupon.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.coupon.dto.req.SupportedGoodsReqDTO;
import com.zicca.icoupon.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.coupon.service.CouponTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 优惠券模板服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements CouponTemplateService {

    private final CouponTemplateMapper couponTemplateMapper;

    @Override
    public CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId) {
        if (id == null || shopId == null) {
            log.warn("查询优惠券模板参数不完整: id={}, shopId={}", id, shopId);
            return null;
        }

        return Optional.ofNullable(couponTemplateMapper.selectCouponTemplateById(id, shopId))
                .map(template -> {
                    try {
                        return BeanUtil.copyProperties(template, CouponTemplateQueryRespDTO.class);
                    } catch (Exception e) {
                        log.error("优惠券模板数据转换失败: templateId={}, shopId={}", id, shopId, e);
                        return null;
                    }
                })
                .orElse(null);
    }

    @Override
    public List<CouponTemplate> listCouponTemplateByIds(List<Long> couponTemplateIds, List<Long> shopIds) {
        if (couponTemplateIds == null || couponTemplateIds.isEmpty() || shopIds == null || shopIds.isEmpty()) {
            log.warn("查询优惠券模板参数不完整: couponTemplateIds={}, shopIds={}", couponTemplateIds, shopIds);
            return List.of();
        }
        return couponTemplateMapper.selectCouponTemplateByIds(couponTemplateIds, shopIds);
    }

    @Override
    public Boolean isSupportGoods(SupportedGoodsReqDTO requestParam) {
        // 默认实现
        return Boolean.TRUE;
    }
}
