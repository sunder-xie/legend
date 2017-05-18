package com.tqmall.legend.object.param.note;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by twg on 16/4/11.
 */
@Data
public class ShopConfigParam implements Serializable {
    private Long shopId;// 门店ID
    private Integer confType;// 配置类型
    private String firstValue;// 第一个值
    private String secondValue;// 第二个值
    private String invalidValue;//

}
