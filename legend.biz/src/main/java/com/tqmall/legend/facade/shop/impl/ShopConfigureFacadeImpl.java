package com.tqmall.legend.facade.shop.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.shop.ShopConfigureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsy on 17/4/6.
 */
@Service
public class ShopConfigureFacadeImpl implements ShopConfigureFacade {
    @Autowired
    ShopConfigureService shopConfigureService;

    /**
     * 根据contType和shopId获取门店的配置value值
     *
     * @param shopConfigureTypeEnum
     * @param shopId
     * @return
     */
    @Override
    public String getConfValue(ShopConfigureTypeEnum shopConfigureTypeEnum, Long shopId) {
        ShopConfigure shopConfigure = getShopConfigure(shopConfigureTypeEnum, shopId);
        if (shopConfigure != null) {
            return shopConfigure.getConfValue();
        }
        return null;
    }

    private ShopConfigure getShopConfigure(ShopConfigureTypeEnum shopConfigureTypeEnum, Long shopId) {
        ShopConfigure shopConfigure = null;
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(shopConfigureTypeEnum, shopId);
        if (shopConfigureOptional.isPresent()) {
            shopConfigure = shopConfigureOptional.get();
        } else {
            //取默认的值
            Optional<ShopConfigure> defaultShopConfigureOptional = shopConfigureService.getShopConfigure(shopConfigureTypeEnum, 0l);
            if (defaultShopConfigureOptional.isPresent()) {
                shopConfigure = defaultShopConfigureOptional.get();
            }
        }
        return shopConfigure;
    }
}
