package com.tqmall.legend.biz.support.elasticsearch;

import lombok.Data;

import java.util.List;

/**
 * Created by Mokala on 8/27/15.
 * 搜索引擎返回数据结果
 */
@Data
public class ELResponse<T> {
    /**
     * 搜索时间
     */
    private long qtime;
    /**
     * 总记录条数
     */
    private Long total;
    /**
     * 当前页码
     */
    private Integer page;
    /**
     * 分页记录数
     */
    private Integer size;
    /**
     * 搜索记录
     */
    private List<T> list;
}
