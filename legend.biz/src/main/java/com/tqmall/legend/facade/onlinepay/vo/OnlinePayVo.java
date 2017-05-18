package com.tqmall.legend.facade.onlinepay.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by sven on 2017/2/27.
 */
@Getter
@Setter
public class OnlinePayVo {
    private String orderSn;
    private Integer ucShopId;
    private Integer payMethod;
    private BigDecimal totalFee;
    private Integer source;
}
