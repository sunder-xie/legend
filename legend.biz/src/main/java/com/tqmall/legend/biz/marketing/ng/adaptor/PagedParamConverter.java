package com.tqmall.legend.biz.marketing.ng.adaptor;

import com.tqmall.wheel.support.data.DefaultSort;
import com.tqmall.wheel.support.rpc.param.PagedParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by xin on 2017/3/25.
 */
public class PagedParamConverter {
    public static PagedParam convert(Pageable pageable, PagedParam pagedParam) {
        if (pageable == null || pagedParam == null) {
            return null;
        }
        pagedParam.setPageNum(pageable.getPageNumber());
        pagedParam.setPageSize(pageable.getPageSize());
        Sort sort = pageable.getSort();
        if (sort != null) {
            for (Sort.Order order : sort) {
                String property = order.getProperty();
                Sort.Direction direction = order.getDirection();
                if (direction == Sort.Direction.ASC) {
                    pagedParam.addSort(new DefaultSort(property, com.tqmall.wheel.support.data.Sort.Direction.ASC));
                } else if (direction == Sort.Direction.DESC) {
                    pagedParam.addSort(new DefaultSort(property, com.tqmall.wheel.support.data.Sort.Direction.DESC));
                }
            }
        }
        return pagedParam;
    }
}
