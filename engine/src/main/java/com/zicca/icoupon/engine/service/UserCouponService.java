package com.zicca.icoupon.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.engine.dao.entity.UserCoupon;
import com.zicca.icoupon.engine.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.engine.dto.req.UserCouponQueryReqDTO;
import com.zicca.icoupon.engine.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.engine.dto.resp.UserCouponQueryRespDTO;

import java.util.List;

/**
 * 用户优惠券服务
 *
 * @author zicca
 */
public interface UserCouponService extends IService<UserCoupon> {


    /**
     * 用户领取优惠券
     *
     * @param requestParam 用户领取优惠券请求参数
     */
    void receiveCoupon(UserCouponReceiveReqDTO requestParam);


    /**
     * 根据ID查询用户优惠券
     *
     * @param id     用户优惠券 ID
     * @param userId 用户 ID
     * @return 用户优惠券
     */
    UserCouponQueryRespDTO getUserCouponById(Long id, Long userId);

    /**
     * 查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponList(UserCouponQueryReqDTO requestParam);

    /**
     * 根据状态查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponListByStatus(UserCouponQueryReqDTO requestParam);

    /**
     * 获取用户可用优惠券
     *
     * @param userId 用户ID
     * @return 可用优惠券列表
     */
    List<UserCouponQueryRespDTO> getAvailableUserCouponList(Long userId);

    /**
     * 锁定用户优惠券
     *
     * @param id     用户优惠券 ID
     * @param userId 用户 ID
     */
    void lockUserCoupon(Long id, Long userId);


    void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam);


}
