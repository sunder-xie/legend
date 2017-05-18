package com.tqmall.legend.facade.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.result.goods.GoodsAttributeDTO;

import java.util.List;

/**
 * 配件属性
 */
public interface GoodsAttrbuteFacade {

    /**
     * 通过商品类型Id获取列表
     *
     * @param goodsTypeId 商品类型ID
     * @param shopId      门店ID
     * @return
     */
    List<GoodsAttributeDTO> selectByGoodsTypeId(Integer goodsTypeId, Long shopId);


    /**
     * 新增配件属性
     *
     * @param goodsAttributeDTO
     * @return
     */
    Result add(GoodsAttributeDTO goodsAttributeDTO);

    /**
     * 查找配件属性
     *
     * @param id 属性ID
     * @return
     */
    GoodsAttributeDTO selectById(Long id);


    /**
     * 更新配件属性
     *
     * @param goodsAttributeDTO
     * @return
     */
    Result updateById(GoodsAttributeDTO goodsAttributeDTO);

}
