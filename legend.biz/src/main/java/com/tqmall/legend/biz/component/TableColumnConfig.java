package com.tqmall.legend.biz.component;

import lombok.Data;

/**
 * 页面表格列展示设置
 * Created by wushuai on 16/4/18.
 */
@Data
public class TableColumnConfig {
    private String field;// 字段
    private String name;// 字段名
    private boolean display = true;//是否显示，false
}
