package com.tqmall.legend.facade.report.bo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/14.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class SalePrefByPersonBO {
    @ExcelCol(value = 0, title = "销售员")
    private String workerName;
    @ExcelCol(value = 1, title = "销售数量")
    private Integer goodsNum;
    @ExcelCol(value = 2, title = "配件销售总额")
    private BigDecimal saleAmount;
    @ExcelCol(value = 3,title = "配件毛利")
    private BigDecimal grossBenifitAmount;
    @ExcelCol(value = 4,title = "毛利率")
    private String grossBenifitAmountRate;
    @ExcelCol(value= 5,title = "提成总额")
    private BigDecimal repairPercentage;

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getGrossBenifitAmount() {
        return grossBenifitAmount;
    }

    public void setGrossBenifitAmount(BigDecimal grossBenifitAmount) {
        this.grossBenifitAmount = grossBenifitAmount;
    }

    public String getGrossBenifitAmountRate() {
        return grossBenifitAmountRate;
    }

    public void setGrossBenifitAmountRate(String grossBenifitAmountRate) {
        this.grossBenifitAmountRate = grossBenifitAmountRate;
    }

    public BigDecimal getRepairPercentage() {
        return repairPercentage;
    }

    public void setRepairPercentage(BigDecimal repairPercentage) {
        this.repairPercentage = repairPercentage;
    }
}
