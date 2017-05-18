package com.tqmall.legend.biz.support.elasticsearch;

import lombok.Data;

import java.util.Map;

/**
 * Created by zhoukai on 16/3/29.
 */
@Data
public class ELResult3<T> {

    private String environment;
    private String params;
    private ELResponse2<T> response;

    private Long qtime;
}
