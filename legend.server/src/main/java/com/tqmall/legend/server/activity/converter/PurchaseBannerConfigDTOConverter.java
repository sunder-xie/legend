package com.tqmall.legend.server.activity.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.activity.PurchaseBannerConfig;
import com.tqmall.legend.object.result.activity.PurchaseBannerConfigDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public class PurchaseBannerConfigDTOConverter implements Converter<PurchaseBannerConfig,PurchaseBannerConfigDTO> {

    @Override
    public PurchaseBannerConfigDTO convert(PurchaseBannerConfig purchaseBannerConfig) {
        PurchaseBannerConfigDTO purchaseBannerConfigDTO = new PurchaseBannerConfigDTO();
        BeanUtils.copyProperties(purchaseBannerConfig,purchaseBannerConfigDTO);
        return purchaseBannerConfigDTO;
    }

    public List<PurchaseBannerConfigDTO> getList(List<PurchaseBannerConfig> purchaseActivityConfigs){
        if(CollectionUtils.isEmpty(purchaseActivityConfigs)){
            return Lists.newArrayList();
        }
        List<PurchaseBannerConfigDTO> resultList = Lists.newArrayListWithCapacity(purchaseActivityConfigs.size());
        for(PurchaseBannerConfig config : purchaseActivityConfigs){
            if(config == null){
                continue;
            }
            resultList.add(convert(config));
        }
        return resultList;
    }
}
