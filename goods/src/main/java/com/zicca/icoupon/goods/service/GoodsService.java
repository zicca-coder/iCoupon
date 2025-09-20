package com.zicca.icoupon.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zicca.icoupon.goods.dao.entity.Goods;
import com.zicca.icoupon.goods.dto.req.GoodsCreateReqDTO;
import com.zicca.icoupon.goods.dto.resp.GoodsQueryRespDTO;

import java.util.List;

/**
 * 商品服务
 *
 * @author zicca
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 创建商品
     *
     * @param requestParam 请求参数
     */
    Long createGoods(GoodsCreateReqDTO requestParam);

    /**
     * 根据ID查询商品
     *
     * @param id 商品ID
     */
    GoodsQueryRespDTO getGoodsById(Long id);

    /**
     * 根据店铺ID查询商品列表
     *
     * @param shopId 店铺ID
     */
    List<GoodsQueryRespDTO> listGoodsByShopId(Long shopId);

    /**
     * 获取优惠商品
     *
     * @param shopId 店铺ID
     * @return 优惠商品
     */
    List<GoodsQueryRespDTO> listDiscountedGoods(Long shopId);

    /**
     * 增加商品库存
     *
     * @param id 商品ID
     */
    void increaseGoodsStock(Long shopId, Long id);

    /**
     * 减少商品库存
     *
     * @param id 商品ID
     */
    void decreaseGoodsStock(Long shopId, Long id);

}
