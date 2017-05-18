package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/14.
 * 集客方案：门店标签
 */
@Getter
@Setter
public class ShopTag extends BaseEntity {

    private String tagName;//标签名称
    private String tagCode;//标签code,如样板店：YBD
    private String tagNote;//标签注释

}