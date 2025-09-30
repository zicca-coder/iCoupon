package com.zicca.icoupon.agent.service.impl;

import com.zicca.icoupon.agent.api.CouponServiceApi;
import com.zicca.icoupon.agent.dto.req.CouponTemplateStatusQueryReqDTO;
import com.zicca.icoupon.agent.dto.req.CouponTemplateTypeQueryReqDTO;
import com.zicca.icoupon.agent.dto.resp.CouponTemplateQueryRespDTO;
import com.zicca.icoupon.agent.service.CouponTemplateService;
import com.zicca.icoupon.framework.exception.RemoteException;
import com.zicca.icoupon.framework.result.Result;
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


    private final CouponServiceApi couponServiceApi;

    @Override
    public CouponTemplateQueryRespDTO getCouponTemplate(Long id, Long shopId) {
        try {
            Result<CouponTemplateQueryRespDTO> result = couponServiceApi.getCouponTemplateById(id, shopId);

            if (result == null) {
                log.error("[智能体服务] | 远程调用优惠券服务查询优惠券模板返回空结果，" + "id：{}, shopId：{}", id, shopId);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }

            log.warn("[智能体服务] | 优惠券服务查询优惠券模板失败，" + "id：{}, shopId：{}, code：{}, message：{}",
                    id, shopId, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询优惠券模板异常，" + "id：{}, shopId：{}", id, shopId, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByShopId(Long shopId) {
        try {
            Result<List<CouponTemplateQueryRespDTO>> result = couponServiceApi.getCouponTemplateByShopId(shopId);

            if (result == null) {
                log.error("[智能体服务] | 优惠券服务查询优惠券模板列表返回空结果，" + "shopId：{}", shopId);
                throw new RemoteException("优惠券服务调用返回空结果");
            }

            if (result.isSuccess()) {
                return result.getData();
            }
            log.warn("[智能体服务] | 优惠券服务查询优惠券模板列表失败，" + "shopId：{}, code：{}, message：{}", shopId, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询优惠券模板列表异常，" + "shopId：{}", shopId, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByStatus(CouponTemplateStatusQueryReqDTO requestParam) {
        try {
            Result<List<CouponTemplateQueryRespDTO>> result = couponServiceApi.getCouponTemplateByStatus(requestParam);
            if (result == null) {
                log.error("[智能体服务] | 优惠券服务查询特定状态优惠券模板列表返回空结果，" + "requestParam：{}", requestParam);
                throw new RemoteException("优惠券服务调用返回空结果");
            }
            if (result.isSuccess()) {
                return result.getData();
            }
            log.warn("[智能体服务] | 优惠券服务查询特定状态优惠券模板列表失败，" + "requestParam：{}, code：{}, message：{}", requestParam, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询特定状态优惠券模板列表异常，" + "requestParam：{}", requestParam, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listCouponTemplateByType(CouponTemplateTypeQueryReqDTO requestParam) {
        try {
            Result<List<CouponTemplateQueryRespDTO>> result = couponServiceApi.getCouponTemplateByType(requestParam);
            if (result == null) {
                log.error("[智能体服务] | 优惠券服务查询特定类型优惠券模板列表返回空结果，" + "requestParam：{}", requestParam);
                throw new RemoteException("优惠券服务调用返回空结果");
            }
            if (result.isSuccess()) {
                return result.getData();
            }
            log.warn("[智能体服务] | 优惠券服务查询特定类型优惠券模板列表失败，" + "requestParam：{}, code：{}, message：{}", requestParam, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询特定类型优惠券模板列表异常，" + "requestParam：{}", requestParam, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }

    @Override
    public List<CouponTemplateQueryRespDTO> listNotStartCouponTemplate(Long shopId) {
        try {
            Result<List<CouponTemplateQueryRespDTO>> result = couponServiceApi.getNotStartCouponTemplate(shopId);
            if (result == null) {
                log.error("[智能体服务] | 优惠券服务查询未开始优惠券模板列表返回空结果，" + "shopId：{}", shopId);
                throw new RemoteException("优惠券服务调用返回空结果");
            }
            if (result.isSuccess()) {
                return result.getData();
            }
            log.warn("[智能体服务] | 优惠券服务查询未开始优惠券模板列表失败，" + "shopId：{}, code：{}, message：{}", shopId, result.getCode(), result.getMessage());
            throw new RemoteException("优惠券服务调用失败: " + result.getMessage());
        } catch (Exception e) {
            log.error("[智能体服务] | 优惠券服务查询未开始优惠券模板列表异常，" + "shopId：{}", shopId, e);
            throw new RemoteException("优惠券服务调用异常");
        }
    }


}
