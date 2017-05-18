package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yanxinyin on 16/7/9.
 */
@Setter
@Getter
public class ProductionLineVO {
    private Long id;//流水线id
    private Long shopId;//所属门店id
    private String name;//生产线名称
    private Integer type;//生产线类型：1：快修线2：事故线3：快喷线
    private String remark;
    private String typeName;
}
