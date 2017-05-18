package com.tqmall.legend.web.report.export.vo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 16/12/23.
 */
@Excel
public class AchievementSummary {
    @ExcelCol(value = 0, title = "员工姓名")
    private String empName;
    @ExcelCol(value = 1, title = "维修业绩")
    private BigDecimal repairAmount = BigDecimal.ZERO;
    @ExcelCol(value = 2, title = "维修提成" )
    private BigDecimal repairPercentage = BigDecimal.ZERO;
    @ExcelCol(value = 3, title = "配件金额", profile = "sale_turnover")
    private BigDecimal saleAmount = BigDecimal.ZERO;//销售业绩
    @ExcelCol(value = 3, title = "配件毛利", profile = "sale_profit")
    private BigDecimal saleOrderOrGrossAmount = BigDecimal.ZERO;//销售金额或毛利
    @ExcelCol(value = 4, title = "销售提成")
    private BigDecimal salePercentage = BigDecimal.ZERO;
    @ExcelCol(value = 5, title = "营业额", profile = "advisor_turnover")
    private BigDecimal saAmount = BigDecimal.ZERO;//服务顾问业绩
    @ExcelCol(value = 5, title = "毛利", profile = "advisor_profit")
    private BigDecimal saOrderOrGrossAmount = BigDecimal.ZERO;//服务顾问业绩或毛利
    @ExcelCol(value = 6, title = "服务顾问提成")
    private BigDecimal saPercentage = BigDecimal.ZERO;
    @ExcelCol(value = 7, title = "加点提成")
    private BigDecimal addPointPercentage = BigDecimal.ZERO;
    @ExcelCol(value = 8, title = "总提成")
    public BigDecimal getTotalPercentageStr() {
        return repairPercentage.add(salePercentage).add(saPercentage).add(addPointPercentage);
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public BigDecimal getRepairAmount() {
        return repairAmount;
    }

    public void setRepairAmount(BigDecimal repairAmount) {
        this.repairAmount = repairAmount;
    }

    public BigDecimal getRepairPercentage() {
        return repairPercentage;
    }

    public void setRepairPercentage(BigDecimal repairPercentage) {
        this.repairPercentage = repairPercentage;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getSaleOrderOrGrossAmount() {
        return saleOrderOrGrossAmount;
    }

    public void setSaleOrderOrGrossAmount(BigDecimal saleOrderOrGrossAmount) {
        this.saleOrderOrGrossAmount = saleOrderOrGrossAmount;
    }

    public BigDecimal getSalePercentage() {
        return salePercentage;
    }

    public void setSalePercentage(BigDecimal salePercentage) {
        this.salePercentage = salePercentage;
    }

    public BigDecimal getSaAmount() {
        return saAmount;
    }

    public void setSaAmount(BigDecimal saAmount) {
        this.saAmount = saAmount;
    }

    public BigDecimal getSaOrderOrGrossAmount() {
        return saOrderOrGrossAmount;
    }

    public void setSaOrderOrGrossAmount(BigDecimal saOrderOrGrossAmount) {
        this.saOrderOrGrossAmount = saOrderOrGrossAmount;
    }

    public BigDecimal getSaPercentage() {
        return saPercentage;
    }

    public void setSaPercentage(BigDecimal saPercentage) {
        this.saPercentage = saPercentage;
    }

    public BigDecimal getAddPointPercentage() {
        return addPointPercentage;
    }

    public void setAddPointPercentage(BigDecimal addPointPercentage) {
        this.addPointPercentage = addPointPercentage;
    }
}
