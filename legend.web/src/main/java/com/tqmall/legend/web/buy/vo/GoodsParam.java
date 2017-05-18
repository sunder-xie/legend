package com.tqmall.legend.web.buy.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by mokala on 10/28/15.
 */
@Data
public class GoodsParam {
    private Integer goodsId;
    private Integer goodsNumber;
    private BigDecimal price;
    private Integer actId;
    private Integer groupId;
    private Integer step;
    private Integer minBuyNum;
}
