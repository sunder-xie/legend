package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zsy on 2015/8/5.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopServiceInfoRel extends BaseEntity {

    private Long templateId;
    private Long city;
}

