package com.tqmall.legend.entity.statistics.param;

import lombok.Data;

/**
 * Created by tanghao on 16/9/8.
 */
@Data
public class PageParam {
    private Integer offset;
    private Integer page;
    private Integer size = 10;
    private String sort;
    private String sortMethod = "desc";
}