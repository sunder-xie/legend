package com.tqmall.legend.biz.support.elasticsearch;

import lombok.Data;

import java.util.List;

/**
 * Created by lilige on 16/4/27.
 */
@Data
public class ELListResult<T>  {
    private String environment;
    private String params;
    private List<T> response;
    private Long qtime;
}
