package com.zicca.icoupon.admin.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.zicca.icoupon.admin.merchant.common.context.UserContext;
import com.zicca.icoupon.admin.merchant.dao.entity.CouponTemplate;
import com.zicca.icoupon.admin.merchant.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateNumberReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.admin.merchant.service.CouponTemplateGoodsService;
import com.zicca.icoupon.admin.merchant.service.CouponTemplateService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.zicca.icoupon.admin.merchant.common.enums.CouponTemplateStatusEnum.*;
import static com.zicca.icoupon.admin.merchant.common.enums.DiscountTargetEnum.PARTIAL;

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
    private final CouponTemplateGoodsService couponTemplateGoodsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createCouponTemplate(CouponTemplateCreateReqDTO requestParam) {
        CouponTemplate couponTemplate = BeanUtil.copyProperties(requestParam, CouponTemplate.class);
        save(couponTemplate);
        // 如果不是全店通用券，则需要保存关联商品
        if (PARTIAL == couponTemplate.getTarget()) {
            couponTemplateGoodsService.batchAssociateGoods(couponTemplate.getId(), requestParam.getGoodIds());
        }
    }

    @Override
    public CouponTemplateQueryRespDTO findCouponTemplateById(Long couponTemplateId) {
        if (couponTemplateId == null) {
            return null;
        }
        CouponTemplate template = lambdaQuery()
                .eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .one();
        return template == null ? null : BeanUtil.copyProperties(template, CouponTemplateQueryRespDTO.class);
    }

    @Override
    public List<CouponTemplateQueryRespDTO> getAllCouponTemplates() {
        List<CouponTemplate> templates = lambdaQuery().eq(CouponTemplate::getShopId, UserContext.getShopId()).list();
        if (templates == null || templates.isEmpty()) {
            return List.of();
        }
        return templates.stream()
                .map(template -> BeanUtil.copyProperties(template, CouponTemplateQueryRespDTO.class)).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCouponTemplateById(Long couponTemplateId) {
        boolean deleted = remove(new LambdaQueryWrapper<CouponTemplate>()
                .eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId()));
        couponTemplateGoodsService.batchDissociateGoods(couponTemplateId);
        if (!deleted) {
            throw new ServiceException("优惠券模板不存在或无权限删除");
        }
    }

    @Override
    public void activeCouponTemplate(Long couponTemplateId) {
        CouponTemplate template = lambdaQuery().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .eq(CouponTemplate::getStatus, NOT_START) // 未开始的优惠券才可以激活
                .one();
        if (Objects.isNull(template)) {
            return;
        }
        lambdaUpdate().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .set(CouponTemplate::getStatus, IN_PROGRESS)
                .update();
    }

    @Override
    public void terminateCouponTemplate(Long couponTemplateId) {
        CouponTemplate template = lambdaQuery().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .eq(CouponTemplate::getStatus, IN_PROGRESS) // 进行中的优惠券才可以到期结束
                .one();
        if (Objects.isNull(template)) {
            return;
        }
        lambdaUpdate().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .set(CouponTemplate::getStatus, END)
                .update();
    }

    @Override
    public void cancelCouponTemplate(Long couponTemplateId) {
        CouponTemplate template = lambdaQuery().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .eq(CouponTemplate::getStatus, NOT_START) // 未开始的优惠券才可以取消
                .one();
        if (Objects.isNull(template)) {
            return;
        }
        lambdaUpdate().eq(CouponTemplate::getId, couponTemplateId)
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .set(CouponTemplate::getStatus, CANCELED)
                .update();
    }

    @Override
    public void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam) {
        CouponTemplate template = lambdaQuery().eq(CouponTemplate::getId, requestParam.getCouponTemplateId())
                .eq(CouponTemplate::getShopId, UserContext.getShopId())
                .in(CouponTemplate::getStatus, NOT_START, IN_PROGRESS)
                .one();
        if (Objects.isNull(template)) {
            throw new ServiceException("优惠券模板状态异常，暂时无法增加库存....");
        }
        int increased = couponTemplateMapper.increaseNumberCouponTemplate(requestParam.getCouponTemplateId(), UserContext.getShopId(), requestParam.getNumber());
        if (!SqlHelper.retBool(increased)) {
            throw new ServiceException("优惠券模板库存增加异常....");
        }
    }


}
