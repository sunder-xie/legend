package com.tqmall.legend.biz.support.elasticsearch;

import lombok.Data;

/**
 * Created by wanghui on 12/8/15.
 * 由于搜索引擎返回的结果格式并没有完全统一,所以需要建立多个result解析
 */
@Data
public class ELResult2<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;
    /**
     * 总记录条数
     */
    private long total;
}
