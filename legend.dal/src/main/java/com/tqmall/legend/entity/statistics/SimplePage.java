package com.tqmall.legend.entity.statistics;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by tanghao on 16/9/6.
 */
@Data
public class SimplePage<T> {
    private int totalElements;
    private List<T> content;
    private int page;
    private int size = 10;
    private int totalPages;

    public SimplePage() {
        totalElements = 0;
        content = Collections.emptyList();
    }

    public SimplePage(Integer totalElements, List<T> content) {
        this.totalElements = totalElements;
        this.content = content;
    }

    public SimplePage(Integer totalElements, List<T> content, int page, int size) {
        this.totalElements = totalElements;
        this.content = content;
        this.page = page;
        this.size = size;
        setTotalPage();
    }

    public void setTotalPage() {
        this.totalPages = totalElements / size + (totalElements % size == 0 ? 0 : 1);
    }

    public void setSize(int size) {
        this.size = size;
        setTotalPage();
    }

    public void setEmptyPage() {
        this.totalPages = 0;
        this.page = 0;
        this.totalElements = 0;
        this.content = Lists.newArrayList();
    }


}