package com.tqmall.legend.facade.goods;

import com.tqmall.itemcenter.object.result.goods.GoodsHotCategoryDTO;

import java.util.List;

/**
 * 配件热门分类
 */
public interface GoodsHotCategoryFacade {

    /**
     * 按照分类名称 模糊查询
     *
     * @param categoryNameLike 分类名称
     * @return
     */
    List<GoodsHotCategoryDTO> selectByCatNameLike(String categoryNameLike);
}
