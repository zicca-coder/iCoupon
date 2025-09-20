package com.zicca.icoupon.goods.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zicca.icoupon.framework.exception.ServiceException;
import com.zicca.icoupon.goods.dao.entity.Goods;
import com.zicca.icoupon.goods.dao.mapper.GoodsMapper;
import com.zicca.icoupon.goods.dto.req.GoodsCreateReqDTO;
import com.zicca.icoupon.goods.dto.resp.GoodsQueryRespDTO;
import com.zicca.icoupon.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.zicca.icoupon.goods.common.enums.GoodsStatusEnum.ON_SALE;

/**
 * 商品服务实现类
 *
 * @author zicca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final GoodsMapper goodsMapper;

    @Override
    public Long createGoods(GoodsCreateReqDTO requestParam) {
        if (Objects.isNull(requestParam)) {
            log.error("[商品服务] 创建商品 - 请求参数为空，requestParam = {}", requestParam);
            throw new ServiceException("请求参数不能为空");
        }
        log.info("[商品服务] 创建商品 - 创建商品, requestParam: {}", requestParam);
        try {
            Goods goods = BeanUtil.copyProperties(requestParam, Goods.class);
            goods.setStatus(ON_SALE);
            int result = goodsMapper.insertGoods(goods);
            if (result <= 0) {
                log.error("[商品服务] 创建商品 - 插入数据库失败, goods: {}", goods);
                throw new ServiceException("商品创建失败");
            }
            return goods.getId();
        } catch (Exception e) {
            log.error("[商品服务] 创建商品 - 创建失败, requestParam: {}", requestParam, e);
            throw new ServiceException("商品创建失败");
        }
    }

    @Override
    public GoodsQueryRespDTO getGoodsById(Long id) {
        if (Objects.isNull(id)) {
            log.error("[商品服务] 查询商品 - 商品ID为空，id = {}", id);
            throw new ServiceException("商品ID不能为空");
        }
        log.info("[商品服务] 获取商品信息 - 获取商品信息, id: {}", id);
        Goods goods = goodsMapper.selectGoodsById(id);
        if (Objects.nonNull(goods)) {
            log.info("[商品服务] 获取商品信息 - 商品信息: {}", goods);
            return BeanUtil.copyProperties(goods, GoodsQueryRespDTO.class);
        }
        log.info("[商品服务] 获取商品信息 - 商品不存在，id = {}", id);
        return new GoodsQueryRespDTO();
    }

    @Override
    public List<GoodsQueryRespDTO> listGoodsByShopId(Long shopId) {
        if (Objects.isNull(shopId)) {
            log.error("[商品服务] 查询商品 - 店铺ID为空，shopId = {}", shopId);
            throw new ServiceException("店铺ID不能为空");
        }
        log.info("[商品服务] 获取商品信息 - 获取商品信息, shopId: {}", shopId);
        List<Goods> goods = goodsMapper.selectGoodsListByShopId(shopId);
        if (CollUtil.isNotEmpty(goods)) {
            log.info("[商品服务] 获取优惠商品 - 优惠商品: {}", goods);
            return goods.stream().map(e -> BeanUtil.copyProperties(e, GoodsQueryRespDTO.class)).toList();
        }
        log.info("[商品服务] 获取优惠商品 - 优惠商品不存在，shopId = {}", shopId);
        return List.of();
    }


    @Override
    public List<GoodsQueryRespDTO> listDiscountedGoods(Long shopId) {
        if (Objects.isNull(shopId)) {
            log.error("[商品服务] 获取优惠商品 - 店铺ID为空，shopId = {}", shopId);
            throw new ServiceException("店铺ID不能为空");
        }
        log.info("[商品服务] 获取优惠商品 - 获取优惠商品, shopId: {}", shopId);
        List<Goods> goods = goodsMapper.selectDiscountedGoodsList(shopId);
        if (CollUtil.isNotEmpty(goods)) {
            log.info("[商品服务] 获取优惠商品 - 优惠商品: {}", goods);
            return goods.stream().map(e -> BeanUtil.copyProperties(e, GoodsQueryRespDTO.class)).toList();
        }
        log.info("[商品服务] 获取优惠商品 - 优惠商品不存在，shopId = {}", shopId);
        return List.of();
    }

    @Override
    public void increaseGoodsStock(Long shopId, Long id) {
        if (Objects.isNull(shopId) || Objects.isNull(id)) {
            log.error("[商品服务] 扣减商品库存 - 参数错误，shopId = {}, id = {}", shopId, id);
            throw new ServiceException("参数错误");
        }
        log.info("[商品服务] 扣减商品库存 - 扣减商品库存, shopId: {}, id: {}", shopId, id);
        try {
            int result = goodsMapper.increaseNumberGoods(id, shopId, 1);
            if (result <= 0) {
                log.error("[商品服务] 扣减商品库存 - 扣减失败, shopId: {}, id: {}", shopId, id);
                throw new ServiceException("扣减失败");
            }
            log.info("[商品服务] 扣减商品库存 - 扣减成功, shopId: {}, id: {}", shopId, id);
        } catch (ServiceException e) {
            log.error("[商品服务] 扣减商品库存 - 扣减失败, shopId: {}, id: {}", shopId, id);
            throw new ServiceException("扣减失败");
        }

    }

    @Override
    public void decreaseGoodsStock(Long shopId, Long id) {
        if (Objects.isNull(shopId) || Objects.isNull(id)) {
            log.error("[商品服务] 扣减商品库存 - 参数错误，shopId = {}, id = {}", shopId, id);
            throw new ServiceException("参数错误");
        }
        log.info("[商品服务] 扣减商品库存 - 扣减商品库存, shopId: {}, id: {}", shopId, id);
        try {
            int result = goodsMapper.decreaseNumberGoods(id, shopId, 1);
            if (result <= 0) {
                log.error("[商品服务] 扣减商品库存 - 扣减失败, shopId: {}, id: {}", shopId, id);
                throw new ServiceException("扣减失败");
            }
            log.info("[商品服务] 扣减商品库存 - 扣减成功, shopId: {}, id: {}", shopId, id);
        } catch (ServiceException e) {
            log.error("[商品服务] 扣减商品库存 - 扣减失败, shopId: {}, id: {}", shopId, id);
            throw new ServiceException("扣减失败");
        }
    }

}
