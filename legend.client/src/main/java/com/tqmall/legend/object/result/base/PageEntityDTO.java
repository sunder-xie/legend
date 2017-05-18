package com.tqmall.legend.object.result.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/11/25.
 * 分页
 */
@Getter
@Setter
public class PageEntityDTO<T> implements Serializable {
    private static final long serialVersionUID = 8106669749486243340L;

    private Long totalNum;//记录总数
    private Integer pageNum;//当前页数
    private List<T> content;//当前页记录数

}
