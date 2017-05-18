package com.tqmall.legend.server.activity.converter;

import com.tqmall.legend.entity.activity.PurchaseBannerConfig;
import com.tqmall.legend.object.param.activity.PurchaseBannerConfigParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/11/24.
 */
public class PurchaseBannerConfigConverter implements Converter<PurchaseBannerConfigParam,PurchaseBannerConfig> {
    @Override
    public PurchaseBannerConfig convert(PurchaseBannerConfigParam purchaseBannerConfigParam) {
        if(null == purchaseBannerConfigParam){
            return null;
        }
        PurchaseBannerConfig purchaseBannerConfig = new PurchaseBannerConfig();
        BeanUtils.copyProperties(purchaseBannerConfigParam,purchaseBannerConfig);
        purchaseBannerConfig.setOffset(purchaseBannerConfigParam.getPage()<1?0:purchaseBannerConfigParam.getPage()-1);
        purchaseBannerConfig.setLimit(purchaseBannerConfigParam.getSize());
        return purchaseBannerConfig;
    }
}
