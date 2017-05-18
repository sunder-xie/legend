package com.tqmall.legend.biz.support.elasticsearch;


import lombok.Data;

import java.util.List;

/**
 * Created by zhoukai on 16/3/29.
 */
@Data
public class ELResponse2 <T> {

    private String vin;

    private List<String> lidList;
    /**
     * 搜索记录
     */
    private List<T> list;
}
