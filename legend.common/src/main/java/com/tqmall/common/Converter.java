package com.tqmall.common;

/**
 * Created by yuchengdu on 16/7/11.
 * POJO adaptor模板
 */
public interface Converter<T,S> {
    void apply(T source,S destination);
}
