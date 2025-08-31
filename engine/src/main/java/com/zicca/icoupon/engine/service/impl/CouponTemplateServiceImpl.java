package com.zicca.icoupon.engine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.engine.dao.entity.CouponTemplate;
import com.zicca.icoupon.engine.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.engine.dto.req.CouponTemplateQueryReqDTO;
import com.zicca.icoupon.engine.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.engine.service.CouponTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
