package com.tqmall.legend.biz.goods;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.goods.GoodsCategory;

/**
 * 配件类别 service
 * @deprecated
 */
public interface GoodsCategoryService {

    /**
     * 获取商品类别
     *
     * @param catName     配件分类名
     * @param shopId      门店id
     * @param goodsTypeId 配件类型
     * @return
     */
    Optional<GoodsCategory> select(String catName, Long shopId, Long goodsTypeId);

    /**
     * 保存配件
     *
     * @param goodsCategory
     * @return
     */
    int save(GoodsCategory goodsCategory);

}
