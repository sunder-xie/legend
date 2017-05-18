package com.tqmall.common.search;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuchengdu on 16/9/9.
 */
public class PagedQuery extends BasicQuery implements Serializable {

    private static final long serialVersionUID = -8588266422690602177L;

    // 查询开始记录
    protected static final String KEY_OFFSET = "_offset";
    // 获取记录数量
    protected static final String KEY_LIMIT = "_limit";
    // 排序
    protected static final String ORDER_BY = "_orderby";

    public PagedQuery(Class<? extends Enum<?>> paramType) {
        super(paramType);
    }

    public PagedQuery() {
    }

    public void setPaging(int pagenum, int pagesize) {
        if (pagenum < 1) {
            throw new IllegalArgumentException("page num should >= 1");
        }
        if (pagesize < 1) {
            throw new IllegalArgumentException("page size should >= 1 ");
        }

        long offset =(long) ((pagenum - 1) * pagesize);
        long limit = pagesize;
        putNoCheck(KEY_OFFSET, offset);
        putNoCheck(KEY_LIMIT, limit);
    }

    public void setOrderBy(List<String> conditions) {
        if (!conditions.isEmpty()) {
            putNoCheck(ORDER_BY, conditions);
        }
    }
}
