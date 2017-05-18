package com.tqmall.legend.biz.support.common;

import lombok.Data;

import java.util.Map;

/**
 * Created by mokala on 11/9/15.
 * CRM通用result
 */
@Data
public class CRMResult {
    private String errorMsg;
    private String code;
    private Map data;
    private boolean success;

    public CRMResult(){
        this.success = false;
    }
}
