package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/8.
 */
@Data
public class GoodsCateAmountVO {
    // 配件类型名称
    private String goodsCateName;
    // 期初金额
    private BigDecimal beginGoodsAmount;
    // 期末金额
    private BigDecimal endGoodsAmount;
}
