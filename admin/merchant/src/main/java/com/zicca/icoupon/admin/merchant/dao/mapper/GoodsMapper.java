package com.zicca.icoupon.admin.merchant.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zicca.icoupon.admin.merchant.dao.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品 Mapper 接口
 *
 * @author zicca
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 插入商品
     *
     * @param goods 商品
     * @return 插入数量
     */
    int insertGoods(@Param("goods") Goods goods);

    /**
     * 批量插入商品
     *
     * @param goodsList 商品列表
     * @return 插入数量
     */
    int batchInsertGoods(@Param("goodsList") List<Goods> goodsList);

    /**
     * 根据id查询商品
     *
     * @param id 商品id
     * @return 商品
     */
    Goods selectGoodsById(@Param("id") Long id);

    /**
     * 根据id和shopId查询商品
     *
     * @param id     商品id
     * @param shopId 商家id
     * @return 商品
     */
    Goods selectGoodsByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    /**
     * 根据shopId查询商品
     *
     * @param shopId 商家id
     * @return 商品列表
     */
    List<Goods> selectGoodsListByShopId(@Param("shopId") Long shopId);

    /**
     * 根据ids查询商品
     *
     * @param ids 商品id列表
     * @return 商品列表
     */
    List<Goods> selectGoodsListByIds(@Param("ids") List<Long> ids);

    /**
     * 根据ids和userId查询商品
     *
     * @param ids    商品id列表
     * @param userId 用户id
     * @return 列表
     */
    List<Goods> selectGoodsListByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * 根据id删除商品
     *
     * @param id 商品id
     * @return 删除行数
     */
    int deleteGoodsById(@Param("id") Long id);


    /**
     * 根据id和shopId删除商品
     *
     * @param id     商品id
     * @param shopId 商家id
     * @return 删除行数
     */
    int deleteGoodsByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    /**
     * 批量删除商品
     *
     * @param ids 商品id列表
     * @return 删除行数
     */
    int batchDeleteGoodsByIds(@Param("ids") List<Long> ids);

    /**
     * 根据ids和shopId批量删除商品
     *
     * @param ids    商品id列表
     * @param shopId 商家id
     * @return 删除行数
     */
    int batchDeleteGoodsByIdsAndShopId(@Param("ids") List<Long> ids, @Param("shopId") Long shopId);

    /**
     * 增加商品数量
     *
     * @param id     商品id
     * @param shopId 商家id
     * @param number 增加数量
     * @return 更新行数
     */
    int increaseNumberGoods(@Param("id") Long id, @Param("shopId") Long shopId, @Param("number") Integer number);

    /**
     * 减少商品数量
     *
     * @param id     商品id
     * @param shopId 商家id
     * @param number 减少数量
     * @return 更新行数
     */
    int decreaseNumberGoods(@Param("id") Long id, @Param("shopId") Long shopId, @Param("number") Integer number);


}
