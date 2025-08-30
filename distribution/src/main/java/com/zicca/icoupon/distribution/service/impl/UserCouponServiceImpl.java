package com.zicca.icoupon.distribution.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.distribution.dao.entity.UserCoupon;
import com.zicca.icoupon.distribution.dao.mapper.CouponTemplateMapper;
import com.zicca.icoupon.distribution.dao.mapper.DistributionRecordMapper;
import com.zicca.icoupon.distribution.dao.mapper.UserCouponMapper;
import com.zicca.icoupon.distribution.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final DistributionRecordMapper distributionRecordMapper;




}
