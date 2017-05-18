package com.tqmall.legend.object.result.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macx on 2016/12/7.
 */
@Getter
@Setter
public class ShopWashCarServiceDTO implements Serializable{
    private List<ShopServiceInfoDTO> shopServiceInfoDTOS;//门店洗车标准服务
    private boolean isAddPrice = false;//是否需要初始化价格
}
