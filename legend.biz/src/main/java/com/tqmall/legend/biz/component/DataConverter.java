package com.tqmall.legend.biz.component;

import java.util.List;

/**
 * Created by twg on 16/4/6.
 */
public interface DataConverter<T> {
    /**
     * 解码
     * @param t
     * @return
     */
    List<T> encode(T t);

    /**
     * 编码
     * @return
     */
    T decode(List list);
}
