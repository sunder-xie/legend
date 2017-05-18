package com.tqmall.legend.entity.shop;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by lixiao on 15/11/17.
 */
@Data
public class ShopServiceInfoSimpleVo {

    private Long id ;
    private String name;
    private BigDecimal servicePrice;
}
