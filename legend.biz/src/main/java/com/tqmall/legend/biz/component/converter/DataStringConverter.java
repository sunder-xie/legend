package com.tqmall.legend.biz.component.converter;

import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by twg on 16/4/6.
 */
public class DataStringConverter<T> implements DataConverter<T> {

    @Override
    public List<T> encode(T t) {
        return null;
    }

    @Override
    public T decode(List list) {
        if(!CollectionUtils.isEmpty(list)){
            ShopConfigure shopConfigure = (ShopConfigure)list.get(0);
            return (T) shopConfigure.getConfValue();
        }
        return null;
    }
}
