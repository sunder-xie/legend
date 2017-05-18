package com.tqmall.legend.object.param.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zsy on 16/11/22.
 * 服务保存等对象
 */
@Getter
@Setter
public class ServiceInfoParam implements Serializable {
    private String name;//服务名称
    private Long categoryId;//服务类别
    private String serviceSn;//服务编号
    private String carLevelName;//车辆级别名称
    private BigDecimal servicePrice;//服务价格
    private String categoryName;//服务名称
    private Long creator;//用户id
    private Long shopId;//门店id
}