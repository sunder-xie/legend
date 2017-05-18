package com.tqmall.legend.object.param.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 2017/2/13.
 */
@Getter
@Setter
public class ServicePriceParam implements Serializable{
    private static final long serialVersionUID = -4493335122801630349L;

    private Long id;//服务id
    private BigDecimal servicePrice;//服务价格
    private BigDecimal serviceAmount;

}
