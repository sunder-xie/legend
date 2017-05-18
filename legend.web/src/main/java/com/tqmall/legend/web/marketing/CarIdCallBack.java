package com.tqmall.legend.web.marketing;

import org.springframework.data.domain.Page;

/**
 * Created by majian on 16/12/2.
 */
public interface CarIdCallBack<T> {
    Page<T> getPagedCustomer(int pageIndex);
    Long getCarId(T t);
}
