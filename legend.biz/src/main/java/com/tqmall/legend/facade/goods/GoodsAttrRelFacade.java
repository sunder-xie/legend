package com.tqmall.legend.facade.goods;

import com.tqmall.itemcenter.object.result.goods.GoodsAttrRelDTO;

import java.util.List;

/**
 * 配件属性关联
 */
public interface GoodsAttrRelFacade {

    /**
     * 通过商品ID和shopId获取列表
     *
     * @param goodsId
     * @param shopId
     * @return
     */
    List<GoodsAttrRelDTO> selectByGoodsIdAndShopId(Long goodsId, Long shopId);

    /**
     * 删除关联属性
     *
     * @param goodsId
     * @param shopId
     * @return
     */
    Integer deleteByGoodsIdAndShopId(Long goodsId, Long shopId);

    /**
     * 保存关联属性
     *
     * @param goodsAttrRelDTO
     * @return
     */
    Integer save(GoodsAttrRelDTO goodsAttrRelDTO);

}
