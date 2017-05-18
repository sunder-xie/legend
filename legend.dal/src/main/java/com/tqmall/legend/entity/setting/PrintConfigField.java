package com.tqmall.legend.entity.setting;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lilige on 16/11/2.
 * 打印设置里的自定义选项对象
 */
@Setter
@Getter
public class PrintConfigField {
    private String field;
    private String fieldName;//选项名称
    private Boolean isChecked;//true-展示 false-不展示
    private String extValue;//扩展属性
    private String maxWidth;//最大宽度
    private Object fieldValue;//选项的数据值

}
