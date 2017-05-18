package com.tqmall.legend.biz.component.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by twg on 16/4/6.
 */
@Slf4j
public class DataShopConfigConverter<T> implements DataConverter<T> {

    @Override
    public T decode(List list) {
        return (T) list;
    }

    @Override
    public List<T> encode(T noteShopConfig) {
        ShopConfigure note = (ShopConfigure) noteShopConfig;
        Long shopId = note.getShopId();
        Integer confType = note.getConfType().intValue();
        if (shopId == null) {
            throw new RuntimeException("shopId不能为空");
        }
        if (confType == null) {
            throw new RuntimeException("confType不能为空");
        }
        List configures = Lists.newArrayList();
        configures.add(note);
        return configures;
    }
}
