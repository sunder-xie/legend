package com.tqmall.legend.entity.shop;


import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/4/18.
 */
@Getter
@Setter
public class ShopVersionConfig extends BaseEntity {
    private String moduleKey; //模块
    private Integer defaultVersion;//默认版本，老版本：0，新版本：1，新老版本切换：2
    private Integer stableStatus;//版本是否稳定，未稳定：0（需要看关系表），已稳定：1

}

