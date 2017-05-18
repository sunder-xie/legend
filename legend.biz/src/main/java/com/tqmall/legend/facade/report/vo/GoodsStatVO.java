package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/8.
 */
@Data
public class GoodsStatVO {
    // 排序
    private int rank;
    // 配件id
    private Long goodsId;
    // 配件名称
    private String goodsName;
    // 单位
    private String measureUnit;
    // 期初数量
    private BigDecimal beginGoodsNumber;
    // 期初金额
    private BigDecimal beginGoodsAmount;
    // 期末数量
    private BigDecimal endGoodsNumber;
    // 期末金额
    private BigDecimal endGoodsAmount;
    // 借进数量
    private BigDecimal borrowGoodsNumber;
    // 借进金额
    private BigDecimal borrowGoodsAmount;
    // 贷出数量
    private BigDecimal lendGoodsNumber;
    // 贷出金额
    private BigDecimal lendGoodsAmount;
    // 前三个月月均销量
    private BigDecimal averageNumber;
    // 损/益数量
    private BigDecimal profitGoodsNumber;
    // 损/益金额
    private BigDecimal profitGoodsAmount;
    // 建议采购量
    private BigDecimal suggestGoodsNumber;
    // 周转率
    private String rotationRate;
}
