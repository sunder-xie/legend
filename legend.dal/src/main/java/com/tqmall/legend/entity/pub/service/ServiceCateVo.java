package com.tqmall.legend.entity.pub.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jason on 15-07-09.
 * 车主APP服务类别
 * 2C-APP 接口对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceCateVo {

    //服务名称
    private String name;
    //id
    private Long id;

    private Integer cateSort;//类别排序,默认降序

}
