package com.tqmall.legend.entity.order;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by zsy on 2015/1/20.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderType extends BaseEntity implements Serializable{

    private Long shopId;
    private String name;
    private Long sort;
    private Integer showStatus;//0隐藏，1显示
}

