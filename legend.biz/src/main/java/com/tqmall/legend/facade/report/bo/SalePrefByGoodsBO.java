package com.tqmall.legend.facade.report.bo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by tanghao on 16/10/14.
 */
@Excel
public class SalePrefByGoodsBO {
    @ExcelCol(value = 0, title = "销售员")
    private String empName;
    @ExcelCol(value = 1, title = "配件名称")
    private String goodsName;

    private String percentageRule;
    private String percentageNum;
    @ExcelCol(value = 2, title = "提成规则")
    String getRuleStr() {
        return percentageRule + percentageNum;
    }
    private Integer goodsNum;
    private String measureUnit;//单位
    @ExcelCol(value = 3, title = "数量")
    String getNumStr() {
        if (percentageNum.contains("元")) {
            return goodsNum+measureUnit;
        }
        return "-";
    }
    @ExcelCol(value = 4, title = "配件金额", profile = "turnover")
    private BigDecimal saleAmount;
    @ExcelCol(value = 4, title = "配件毛利", profile = "profit")
    private BigDecimal benefitAmount;
    @ExcelCol(value = 5, title = "提成金额")
    private BigDecimal percentageAmount;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPercentageRule() {
        return percentageRule;
    }

    public void setPercentageRule(String percentageRule) {
        this.percentageRule = percentageRule;
    }

    public String getPercentageNum() {
        return percentageNum;
    }

    public void setPercentageNum(String percentageNum) {
        this.percentageNum = percentageNum;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(BigDecimal benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public BigDecimal getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(BigDecimal percentageAmount) {
        this.percentageAmount = percentageAmount;
    }
}
