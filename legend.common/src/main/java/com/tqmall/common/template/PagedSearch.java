package com.tqmall.common.template;

import java.io.Serializable;

/**
 * Created by yuchengdu on 16/9/9.
 */
public class PagedSearch implements Serializable {
    private Integer pageIndex;
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
