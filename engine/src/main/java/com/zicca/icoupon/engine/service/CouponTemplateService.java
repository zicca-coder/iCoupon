package com.zicca.icoupon.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.engine.dao.entity.CouponTemplate;
import com.zicca.icoupon.engine.dto.req.CouponTemplateQueryReqDTO;
import com.zicca.icoupon.engine.dto.resp.CouponTemplateQueryRespDTO;

/**
 * 优惠券模板服务
 *
 * @author zicca
 */
public interface CouponTemplateService extends IService<CouponTemplate> {


    /**
     * 获取优惠券模板
     *
     * @param requestParam 查询参数
     * @return 优惠券模板
     */
    CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId);


}
