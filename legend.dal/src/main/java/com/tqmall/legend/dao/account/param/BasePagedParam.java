package com.tqmall.legend.dao.account.param;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by majian on 17/1/5.
 */
public class BasePagedParam {
    private int offset;
    private int limit;
    private List<String> sorts;

    public BasePagedParam() {
    }

    public BasePagedParam(Pageable pageable) {
        this.offset = pageable.getOffset()-pageable.getPageSize();
        this.limit = pageable.getPageSize();
        this.sorts = translateSort(pageable.getSort());
    }

    private List<String> translateSort(Sort sort) {
        if (sort == null) {
            return null;
        }
        Iterator<Sort.Order> iterator = sort.iterator();
        ArrayList<String> sorts = new ArrayList<String>();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            sorts.add(order.getProperty() + " " + order.getDirection().name());
        }
        return sorts;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getSorts() {
        return sorts;
    }

    public void setSorts(List<String> sorts) {
        this.sorts = sorts;
    }
}
