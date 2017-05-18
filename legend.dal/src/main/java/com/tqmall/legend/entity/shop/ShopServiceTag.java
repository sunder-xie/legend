package com.tqmall.legend.entity.shop;

/**
 * Created by jason on 15/8/24.
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopServiceTag extends BaseEntity {

    private String name;
    private Integer type;
    private String imgUrl;
    private Integer sort;

}


