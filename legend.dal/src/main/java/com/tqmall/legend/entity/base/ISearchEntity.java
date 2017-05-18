package com.tqmall.legend.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ISearchEntity<T>{

    private Integer total;//总数量
    private Integer page; //页码
    private Integer size; //每页数量
    private List<T> list; //Result List

}

