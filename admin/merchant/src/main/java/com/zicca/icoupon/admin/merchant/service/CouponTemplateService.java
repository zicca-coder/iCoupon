package com.zicca.icoupon.admin.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.admin.merchant.dao.entity.CouponTemplate;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateCreateReqDTO;
import com.zicca.icoupon.admin.merchant.dto.req.CouponTemplateNumberReqDTO;
import com.zicca.icoupon.admin.merchant.dto.resp.CouponTemplateQueryRespDTO;

import java.util.List;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService extends IService<CouponTemplate> {

    /**
     * 创建优惠券模版
     *
     * @param requestParam 请求参数
     */
    void createCouponTemplate(CouponTemplateCreateReqDTO requestParam);

    /**
     * 查询优惠券模板详情
     * Note: 后管接口不存在高并发情况，因此直接查询数据库即可，不需要做缓存
     *
     * @param couponTemplateId 优惠券模板id
     */
    CouponTemplateQueryRespDTO findCouponTemplateById(Long couponTemplateId);

    /**
     * 获取所有优惠券模板
     *
     * @return 优惠券模板列表
     */
    List<CouponTemplateQueryRespDTO> getAllCouponTemplates();


    /**
     * 删除优惠券模板
     *
     * @param couponTemplateId 优惠券模板id
     */
    void deleteCouponTemplateById(Long couponTemplateId);


    /**
     * 激活优惠券模板
     *
     * @param couponTemplateId 优惠券模板id
     */
    void activeCouponTemplate(Long couponTemplateId);

    /**
     * 终止优惠券模板
     *
     * @param couponTemplateId 优惠券模板id
     */
    void terminateCouponTemplate(Long couponTemplateId);


    /**
     * 取消优惠券模板
     *
     * @param couponTemplateId 优惠券模板id
     */
    void cancelCouponTemplate(Long couponTemplateId);

    /**
     * 增加优惠券模板发行量
     *
     * @param requestParam 请求参数
     */
    void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam);


}
