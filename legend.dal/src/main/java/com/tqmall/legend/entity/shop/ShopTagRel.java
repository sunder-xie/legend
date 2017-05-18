package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/14.
 */
@Getter
@Setter
public class ShopTagRel extends BaseEntity {

    private Long shopId;//门店id
    private Long tagId;//标签id

}

