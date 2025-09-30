package com.zicca.icoupon.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dto.req.UserCouponBathLockReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponListReqDTO;
import com.zicca.icoupon.coupon.dto.req.UserCouponReceiveReqDTO;
import com.zicca.icoupon.coupon.dto.resp.UserCouponQueryRespDTO;

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
     * 批量查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponList(UserCouponListReqDTO requestParam);

    /**
     * 根据状态查询用户优惠券列表
     *
     * @param requestParam 查询参数
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponListByStatus(Long userId, UserCouponStatusEnum status);

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


    /**
     * 批量锁定用户优惠券
     *
     * @param requestParam 批量锁定用户优惠券请求参数
     */
    void batchLockUserCoupon(UserCouponBathLockReqDTO requestParam);


    /**
     * 获取用户一周内领取的优惠券
     *
     * @param userId 用户ID
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponListWithInWeek(Long userId);


    /**
     * 获取用户三天内领取的优惠券
     *
     * @param userId 用户ID
     * @return 优惠券列表
     */
    List<UserCouponQueryRespDTO> getUserCouponListWithInThreeDays(Long userId);
}
