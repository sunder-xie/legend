package com.tqmall.legend.server.activity.converter;

import com.tqmall.legend.entity.activity.PurchaseActivityConfig;
import com.tqmall.legend.object.param.activity.PurchaseActivityConfigParam;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/11/24.
 */
public class PurchaseActivityConfigConverter implements Converter<PurchaseActivityConfigParam,PurchaseActivityConfig> {
    @Override
    public PurchaseActivityConfig convert(PurchaseActivityConfigParam purchaseActivityConfigParam) {
        if(null == purchaseActivityConfigParam){
            return null;
        }
        PurchaseActivityConfig purchaseActivityConfig = new PurchaseActivityConfig();
        purchaseActivityConfig.setId(purchaseActivityConfigParam.getId());
        purchaseActivityConfig.setActivityName(purchaseActivityConfigParam.getActivityName());
        purchaseActivityConfig.setActivityType(purchaseActivityConfigParam.getActivityType());
        purchaseActivityConfig.setOptType(purchaseActivityConfigParam.getOptType());
        purchaseActivityConfig.setCustomRedirectUrl(purchaseActivityConfigParam.getCustomRedirectUrl());
        purchaseActivityConfig.setTqmallBannerId(purchaseActivityConfigParam.getTqmallBannerId());
        purchaseActivityConfig.setCreator(purchaseActivityConfigParam.getCreator());
        purchaseActivityConfig.setModifier(purchaseActivityConfigParam.getModifier());
        purchaseActivityConfig.setOffset(purchaseActivityConfigParam.getOffset());
        purchaseActivityConfig.setLimit(purchaseActivityConfigParam.getLimit());
        return purchaseActivityConfig;
    }
}
