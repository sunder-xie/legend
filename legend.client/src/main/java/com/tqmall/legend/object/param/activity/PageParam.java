package com.tqmall.legend.object.param.activity;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by tanghao on 16/11/28.
 */
public class PageParam implements Serializable{
    private Integer page;
    private Integer size;
    private Set sorts;

    public Integer getPage() {
        if (page == null || page < 1) {
            return 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Set getSorts() {
        return sorts;
    }

    public void setSorts(Set sorts) {
        this.sorts = sorts;
    }

    public Integer getOffset() {
        return (getPage() - 1) * getLimit();
    }

    public Integer getLimit() {
        if (size == null || size < 0) {
            return 10;
        }
        return size;
    }
}
