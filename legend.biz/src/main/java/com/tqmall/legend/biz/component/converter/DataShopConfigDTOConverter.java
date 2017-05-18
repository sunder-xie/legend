package com.tqmall.legend.biz.component.converter;

import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.object.result.config.ShopConfigDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by twg on 16/10/26.
 */
public class DataShopConfigDTOConverter<T> implements DataConverter<T> {
    @Override
    public List<T> encode(T shopConfig) {
        ShopConfigDTO shopConfigDTO = (ShopConfigDTO)shopConfig;
        if(shopConfigDTO.getShopId() == null){
            throw new BizException("门店id为空");
        }
        if(shopConfigDTO.getConfType() == null){
            throw new BizException("配置类型为空");
        }
        if(StringUtils.isBlank(shopConfigDTO.getConfValue())){
            throw new BizException("配置内容为空");
        }
        ShopConfigure shopConfigure = new ShopConfigure();
        BeanUtils.copyProperties(shopConfigDTO,shopConfigure);
        List configures = Lists.newArrayList();
        configures.add(shopConfigure);
        return configures;
    }

    @Override
    public T decode(List list) {
        return (T) list;
    }
}
