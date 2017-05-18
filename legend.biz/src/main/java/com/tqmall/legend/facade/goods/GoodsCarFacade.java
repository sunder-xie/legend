package com.tqmall.legend.facade.goods;

import com.tqmall.itemcenter.object.result.goods.GoodsCarDTO;

import java.util.List;

/**
 * 配件关联车型
 */
public interface GoodsCarFacade {


    /**
     * 根据配件ID 查询关联车型
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return List<GoodsCar>
     */
    List<GoodsCarDTO> selectByGoodsIdAndShopId(Long goodsId, Long shopId);


    /**
     * 保存车辆关联车型
     *
     * @param goodsCar 车辆
     * @return
     */
    Integer save(GoodsCarDTO goodsCar);


    /**
     * 删除配件关联车型
     *
     * @param goodsId 配件ID
     * @param shopId  门店ID
     * @return
     */
    Integer deleteByGoodsIdAndShopId(Long goodsId, Long shopId);
}
