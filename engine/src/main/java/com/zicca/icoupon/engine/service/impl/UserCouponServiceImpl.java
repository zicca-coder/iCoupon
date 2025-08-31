package com.zicca.icoupon.engine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.engine.dao.entity.CouponTemplate;
import com.zicca.icoupon.engine.dao.entity.UserCoupon;
import com.zicca.icoupon.engine.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.engine.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.engine.dto.req.UserCouponQueryReqDTO;
import com.zicca.icoupon.engine.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.engine.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.engine.service.UserCouponService;
import com.zicca.icoupon.framework.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.zicca.icoupon.engine.common.enums.CouponTemplateStatusEnum.IN_PROGRESS;
import static com.zicca.icoupon.engine.common.enums.UserCouponStatusEnum.NOT_USED;

/**
 * 用户优惠券服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    private final CouponTemplateMapper couponTemplateMapper;
    private final UserCouponMapper userCouponMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveCoupon(UserCouponReceiveReqDTO requestParam) {
        // 参数校验
        if (requestParam == null || requestParam.getUserId() == null ||
                requestParam.getCouponTemplateId() == null) {
            log.warn("[用户优惠券服务] 领取优惠券 - 请求参数不完整");
            throw new ServiceException("请求参数不完整");
        }

        // 查询优惠券模板
        CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(
                requestParam.getCouponTemplateId(), requestParam.getShopId());

        if (couponTemplate == null) {
            log.warn("[用户优惠券服务] 领取优惠券 - 优惠券模板不存在, templateId: {}, shopId: {}",
                    requestParam.getCouponTemplateId(), requestParam.getShopId());
            throw new ServiceException("优惠券模板不存在");
        }

        if (IN_PROGRESS != couponTemplate.getStatus()) {
            log.warn("[用户优惠券服务] 领取优惠券 - 优惠券模板状态异常, status: {}", couponTemplate.getStatus());
            throw new ServiceException("优惠券模板状态异常");
        }

        // 扣减库存
        int decreased = couponTemplateMapper.decreaseNumberCouponTemplate(
                requestParam.getCouponTemplateId(), requestParam.getShopId(), 1);

        if (decreased <= 0) {
            log.warn("[用户优惠券服务] 领取优惠券 - 优惠券库存不足, templateId: {}, shopId: {}",
                    requestParam.getCouponTemplateId(), requestParam.getShopId());
            throw new ServiceException("优惠券库存不足");
        }

        // 插入领取记录
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(requestParam.getUserId())
                .couponTemplateId(requestParam.getCouponTemplateId())
                .shopId(requestParam.getShopId())
                .target(couponTemplate.getTarget())
                .type(couponTemplate.getType())
                .faceValue(couponTemplate.getFaceValue())
                .minAmount(couponTemplate.getMinAmount())
                .receiveTime(new Date())
                .validStartTime(couponTemplate.getValidStartTime())
                .validEndTime(couponTemplate.getValidEndTime())
                .status(NOT_USED)
                .build();

        int result = userCouponMapper.insertUserCoupon(userCoupon);
        if (result <= 0) {
            log.warn("[用户优惠券服务] 领取优惠券 - 领取失败, userId: {}", requestParam.getUserId());
            throw new ServiceException("优惠券领取失败");
        }

        log.info("[用户优惠券服务] 领取优惠券成功, userId: {}, templateId: {}",
                requestParam.getUserId(), requestParam.getCouponTemplateId());
    }

    @Override
    public UserCouponQueryRespDTO getUserCouponById(Long id, Long userId) {
        // 参数校验
        if (id == null || userId == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券 - 请求参数不完整, id: {}, userId: {}", id, userId);
            throw new ServiceException("请求参数不完整");
        }

        // 查询用户优惠券
        // todo：此处的userId不应该传参数，应该从用户上下文中获取
        UserCoupon userCoupon = userCouponMapper.selectUserCouponById(id, userId);
        if (userCoupon == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券 - 优惠券不存在, id: {}, userId: {}", id, userId);
            throw new ServiceException("优惠券不存在");
        }

        // 转换为响应DTO
        UserCouponQueryRespDTO result = BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class);

        log.info("[用户优惠券服务] 查询用户优惠券成功, id: {}, userId: {}", id, userId);
        return result;
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponList(UserCouponQueryReqDTO requestParam) {
        // 参数校验
        if (requestParam == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券列表 - 请求参数为空");
            return List.of();
        }

        // 查询用户优惠券列表
        List<UserCoupon> userCoupons = userCouponMapper.selectUserCouponListByCondition(requestParam);

        // 如果查询结果为空，返回空列表
        if (CollectionUtil.isEmpty(userCoupons)) {
            return List.of();
        }

        // 转换为响应DTO列表
        return userCoupons.stream()
                .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                .toList();
    }

    @Override
    public List<UserCouponQueryRespDTO> getUserCouponListByStatus(UserCouponQueryReqDTO requestParam) {
        // 参数校验
        if (requestParam == null) {
            log.warn("[用户优惠券服务] 查询用户优惠券列表 - 请求参数为空");
            return List.of();
        }

        // 查询用户优惠券列表
        List<UserCoupon> userCoupons = userCouponMapper.selectUserCouponListByCondition(requestParam);

        // 如果查询结果为空，返回空列表
        if (CollectionUtil.isEmpty(userCoupons)) {
            return List.of();
        }

        // 转换为响应DTO列表
        return userCoupons.stream()
                .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                .toList();
    }

    @Override
    public List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId) {
        // 参数校验
        if (userId == null) {
            log.warn("[用户优惠券服务] 查询可用用户优惠券列表 - 用户ID不能为空");
            return List.of();
        }

        // 查询可用用户优惠券列表
        List<UserCoupon> userCoupons = userCouponMapper.selectAvailableUserCouponList(userId);

        // 如果查询结果为空，返回空列表
        if (CollectionUtil.isEmpty(userCoupons)) {
            return List.of();
        }

        // 转换为响应DTO列表
        return userCoupons.stream()
                .map(userCoupon -> BeanUtil.copyProperties(userCoupon, UserCouponQueryRespDTO.class))
                .toList();
    }
}
