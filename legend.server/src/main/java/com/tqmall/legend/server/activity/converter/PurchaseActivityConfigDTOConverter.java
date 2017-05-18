package com.tqmall.legend.server.activity.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.activity.PurchaseActivityConfig;
import com.tqmall.legend.object.result.activity.PurchaseActivityConfigDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public class PurchaseActivityConfigDTOConverter implements Converter<PurchaseActivityConfig,PurchaseActivityConfigDTO>{
    @Override
    public PurchaseActivityConfigDTO convert(PurchaseActivityConfig config) {
        if(null == config){
            return null;
        }
        PurchaseActivityConfigDTO dto = new PurchaseActivityConfigDTO();
        BeanUtils.copyProperties(config,dto);
        return dto;
    }

    public List<PurchaseActivityConfigDTO> getList(List<PurchaseActivityConfig> purchaseActivityConfigs){
        if(CollectionUtils.isEmpty(purchaseActivityConfigs)){
            return Lists.newArrayList();
        }
        List<PurchaseActivityConfigDTO> resultList = Lists.newArrayListWithCapacity(purchaseActivityConfigs.size());
        for(PurchaseActivityConfig config : purchaseActivityConfigs){
            if(config == null){
                continue;
            }
            resultList.add(convert(config));
        }
        return resultList;
    }
}
