package com.tqmall.legend.facade.report.bo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by tanghao on 16/10/14.
 */
@Excel
public class SalePrefByOrderBO {
    @ExcelCol(value = 0, title = "日期")
    private String dateStr;
    @ExcelCol(value = 1, title = "工单号")
    private String orderSn;
    @ExcelCol(value = 2, title = "车牌")
    private String license;
    @ExcelCol(value = 3, title = "配件名称")
    private String goodsName;
    @ExcelCol(value = 4, title = "配件数量")
    public String getNumStr() {
        return goodsNum + measureUnit;
    }
    private Integer goodsNum;
    private String measureUnit;//单位
    @ExcelCol(value = 5, title = "配件单价")
    private BigDecimal goodsPrice;
    @ExcelCol(value = 6, title = "优惠")
    private BigDecimal discountAmount;
    @ExcelCol(value = 7, title = "配件金额", profile = "turnover")
    private BigDecimal saleAmount;
    @ExcelCol(value = 7, title = "配件毛利", profile = "profit")
    private BigDecimal benefitAmount;
    @ExcelCol(value = 8, title = "提成规则")
    String getRuleStr() {
        return percentageRule + percentageNum;
    }
    private String percentageRule;
    private String percentageNum;//金额:百分比或数字

    @ExcelCol(value = 9, title = "提成金额")
    private BigDecimal percentageAmount;
    @ExcelCol(value = 10, title = "销售员")
    private String empName;

    public BigDecimal getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(BigDecimal benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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

    public BigDecimal getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(BigDecimal percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
