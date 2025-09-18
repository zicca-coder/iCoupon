package com.zicca.icoupon.order.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.order.dao.entity.OrderItemCouponRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项优惠券记录 Mapper 接口
 *
 * @author zicca
 */
public interface OrderItemCouponRecordMapper extends BaseMapper<OrderItemCouponRecord> {

    OrderItemCouponRecord selectRecordById(@Param("id") Long id);

    OrderItemCouponRecord selectRecordByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    List<OrderItemCouponRecord> selectRecordListByOrderItemId(@Param("orderItemId") Long orderItemId);

    List<OrderItemCouponRecord> selectRecordListByOrderItemIdAndUserId(@Param("userId") Long userId);

    List<OrderItemCouponRecord> selectRecordListByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    int insertRecord(@Param("record") OrderItemCouponRecord record);

    int batchInsertRecord(@Param("recordList") List<OrderItemCouponRecord> list);

    int deleteRecordById(@Param("id") Long id);

    int deleteRecordByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int batchDeleteRecordByIds(@Param("ids") List<Long> ids);

    int batchDeleteRecordByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    int batchDeleteRecordByOrderItemId(@Param("orderItemId") Long orderItemId);

    int batchDeleteRecordByOrderItemIdAndUserId(@Param("orderItemId") Long orderItemId, @Param("userId") Long userId);

}
