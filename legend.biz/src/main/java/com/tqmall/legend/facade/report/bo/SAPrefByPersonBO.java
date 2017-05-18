package com.tqmall.legend.facade.report.bo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by tanghao on 16/10/25.
 */
@Excel
public class SAPrefByPersonBO {
    @ExcelCol(value = 0, title = "服务顾问")
    private String empName;
    @ExcelCol(value = 1, title = "单数")
    private Integer orderNum;
    @ExcelCol(value = 2, title = "台次")
    private Integer carNum;
    @ExcelCol(value = 3, title = "营业额", profile = "turnover")
    private BigDecimal orderAmount;//工单金额
    @ExcelCol(value = 3, title = "毛利", profile = "profit")
    private BigDecimal grossProfit;//毛利
    @ExcelCol(value = 4, title = "业绩提成规则")
    private String percentageNum;//提成规则
    @ExcelCol(value = 5, title = "提成总额")
    private BigDecimal percentageAmount;//提成金额

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
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

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }
}
