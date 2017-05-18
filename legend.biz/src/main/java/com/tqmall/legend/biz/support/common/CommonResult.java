package com.tqmall.legend.biz.support.common;

import lombok.Data;

import java.util.Map;

/**
 * Created by mokala on 10/26/15.
 */
@Data
public class CommonResult {
    private String message;
    private String code;
    private boolean success;
    private Map data;
}
