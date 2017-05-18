package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ShopVersionConfigRel;

import java.util.List;

/**
 * Created by sven on 2017/1/10.
 */
public interface ShopVersionConfigRelService {
    /**
     * 根据门店id,配置id查询版本
     *
     * @param shopId
     * @return
     */
    List<ShopVersionConfigRel> selectByShopIdAndIds(Long shopId, List<Long> configIds);
}
