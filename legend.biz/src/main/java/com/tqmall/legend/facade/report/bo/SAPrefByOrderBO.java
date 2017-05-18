package com.tqmall.legend.facade.report.bo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by tanghao on 16/10/25.
 */
@Excel
public class SAPrefByOrderBO {
    @ExcelCol(value = 0, title = "日期")
    private String dateStr;
    @ExcelCol(value = 1, title = "工单号")
    private String orderSn;
    @ExcelCol(value = 2, title = "车牌")
    private String license;
    @ExcelCol(value = 3, title = "营业额", profile = "turnover")
    private BigDecimal orderAmount;
    @ExcelCol(value = 3, title = "毛利", profile = "profit")
    private BigDecimal grossProfit;//毛利
    @ExcelCol(value = 4, title = "提成规则")
    private String percentageNum;
    @ExcelCol(value = 5, title = "提成金额")
    private BigDecimal percentageAmount;

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
}
