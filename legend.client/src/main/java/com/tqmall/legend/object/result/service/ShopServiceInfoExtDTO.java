package com.tqmall.legend.object.result.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by macx on 2017/2/10.
 */
@Getter
@Setter
public class ShopServiceInfoExtDTO extends ShopServiceInfoDTO implements Serializable{

    private static final long serialVersionUID = 7620644124437996695L;
    // 支持大套餐
    private Long serviceNum;
    // 额外属性
    private String serviceCatName;
}
