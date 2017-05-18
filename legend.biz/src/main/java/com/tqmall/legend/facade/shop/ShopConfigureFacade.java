package com.tqmall.legend.facade.shop;

import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;

/**
 * Created by zsy on 17/4/6.
 */
public interface ShopConfigureFacade {

    /**
     * 根据contType和shopId获取门店的配置value值
     *
     * @param shopConfigureTypeEnum
     * @param shopId
     * @return
     */
    String getConfValue(ShopConfigureTypeEnum shopConfigureTypeEnum, Long shopId);
}
