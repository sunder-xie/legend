package com.tqmall.legend.biz.common;


import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Created by yi.yang on 2014/8/9.
 */
@SuppressWarnings("serial")
public class DefaultPage<T> extends PageImpl<T> {
    public DefaultPage(List<T> content) {
        super(content);
    }

    public DefaultPage(List<T> content, Pageable pageable,
                       long total) {
        super(content, pageable, total);
    }

    /**
     * 页面请求地址
     */
    @Getter
    @Setter
    private String pageUri;

    /**
     * 查询条件
     */
    @Getter
    @Setter
    private String searchParam;

    @Getter
    @Setter
    Map<String, Object> otherData;
}
