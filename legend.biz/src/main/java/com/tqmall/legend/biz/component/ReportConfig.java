package com.tqmall.legend.biz.component;

import lombok.Data;

/**
 * Created by twg on 16/4/11.
 */
@Data
public class ReportConfig {
    private String field;// 字段
    private String name;// 字段名
    private boolean display = true;//是否显示，默认true
}
