package com.tqmall.legend.facade.report.bo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/14.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class RepairPrefByOrderBO {
    @ExcelCol(value = 0, title = "日期")
    private String dateStr;
    @ExcelCol(value = 1, title = "工单号")
    private String orderSn;
    @ExcelCol(value = 2, title = "车牌")
    private String license;
    @ExcelCol(value = 3, title = "服务项目")
    private String serviceName;
    @ExcelCol(value = 4, title = "工时费")
    private BigDecimal servicePrice;
    @ExcelCol(value = 5, title = "维修工")
    private String workerName;
    @ExcelCol(value = 6, title = "工时")
    private BigDecimal hours;
    @ExcelCol(value = 7, title = "提成规则")
    private String percentageRule;
    @ExcelCol(value = 8, title = "提成金额")
    private BigDecimal repairPercentage;

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public String getPercentageRule() {
        return percentageRule;
    }

    public void setPercentageRule(String percentageRule) {
        this.percentageRule = percentageRule;
    }

    public BigDecimal getRepairPercentage() {
        return repairPercentage;
    }

    public void setRepairPercentage(BigDecimal repairPercentage) {
        this.repairPercentage = repairPercentage;
    }
}
