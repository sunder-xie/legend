package com.tqmall.legend.biz.component.converter;

import com.tqmall.legend.biz.component.DataConverter;
import com.tqmall.legend.entity.shop.ShopConfigure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by twg on 16/4/6.
 */
@Slf4j
public class DataBigDecimalConverter<T> implements DataConverter<T> {

    @Override
    public List<T> encode(T t) {
        return null;
    }

    @Override
    public T decode(List list) {
        if(!CollectionUtils.isEmpty(list)){
            ShopConfigure shopConfigure = (ShopConfigure) list.get(0);
            try {
                return (T) new BigDecimal(shopConfigure.getConfValue());
            } catch (NumberFormatException e) {
                log.error("值：{}，类型转换异常：{}", shopConfigure.getConfValue(), e);
                throw new RuntimeException("值：" + shopConfigure.getConfValue() + "，类型转换异常");
            }
        }
        return null;
    }
}
