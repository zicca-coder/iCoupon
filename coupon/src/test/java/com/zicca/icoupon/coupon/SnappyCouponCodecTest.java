package com.zicca.icoupon.coupon;

import com.zicca.icoupon.coupon.common.enums.DiscountTargetEnum;
import com.zicca.icoupon.coupon.common.enums.DiscountTypeEnum;
import com.zicca.icoupon.coupon.common.enums.UserCouponStatusEnum;
import com.zicca.icoupon.coupon.dao.entity.UserCoupon;
import com.zicca.icoupon.coupon.dto.resp.UserCouponQueryRespDTO;
import com.zicca.icoupon.coupon.service.basics.cache.UserCouponRedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zicca
 */
@SpringBootTest
public class SnappyCouponCodecTest {

    @Test
    public void test(@Autowired UserCouponRedisService redisService) throws IOException {
        UserCoupon userCoupon = UserCoupon.builder()
                .id(1L)
                .userId(1L)
                .couponTemplateId(1L)
                .shopId(1L)
                .target(DiscountTargetEnum.ALL)
                .type(DiscountTypeEnum.DISCOUNT)
                .faceValue(new BigDecimal("10"))
                .minAmount(new BigDecimal("10"))
                .receiveTime(new Date())
                .useTime(new Date())
                .validStartTime(new Date())
                .validEndTime(new Date())
                .status(UserCouponStatusEnum.NOT_USED)
                .build();
        redisService.addUserCoupon(userCoupon);
        List<UserCouponQueryRespDTO> userCoupons = redisService.getUserCoupons(1L);
        System.out.println(userCoupons);
    }

}
