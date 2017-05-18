package com.tqmall.legend.object.result.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/11/22.
 */
@Getter
@Setter
public class CarLevelDTO implements Serializable {
    private static final long serialVersionUID = -1432628534818094591L;

    private Long id;//主键id
    private String name;//车辆级别名称
    private Long shopId;//门店ID
    private Long sort;//排序ID，升序
}
