package com.tqmall.legend.biz.api.entity;

import lombok.Data;

/**
 * Created by twg on 16/10/11.
 */
@Data
public class PageRequstParam {
    protected Integer pageSize = 20;
    protected Integer pageNum = 0;
    protected String sortBy = "id";   //单字段 like:'gmtModified'  默认id
    protected String sortType = "desc"; // 排序类型 asc/desc 默认desc
}
