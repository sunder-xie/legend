package com.tqmall.legend.entity.customer;

import lombok.Data;

/**
 * Created by lilige on 16/4/27.
 * 搜索回参：接车首页 根据输入的关键字 匹配车主，车主电话，车牌
 */
@Data
public class SearchVo {
    private Long id;
    private String searchValue;
}
