package com.tqmall.legend.facade.report.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xin on 2016/11/8.
 */
@Data
public class GoodsCateStatVO {
    // 排序
    private int rank;
    // 配件类型名称
    private String goodsCateName;
    // 分类方式
    private String goodsCateWay;
    // 期初金额
    private BigDecimal beginGoodsAmount;
    // 期末金额
    private BigDecimal endGoodsAmount;
    // 借进金额
    private BigDecimal borrowGoodsAmount;
    // 贷出金额
    private BigDecimal lendGoodsAmount;
    // 周转率
    private String rotationRate;
}
