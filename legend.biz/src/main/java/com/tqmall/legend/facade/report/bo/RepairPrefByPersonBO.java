package com.tqmall.legend.facade.report.bo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/12.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class RepairPrefByPersonBO {
    @ExcelCol(value = 0, title = "维修工")
    private String workerName;
    @ExcelCol(value = 1, title = "单数")
    private Integer orderNum;
    @ExcelCol(value = 2, title = "台次")
    private Integer repairCarNum;
    @ExcelCol(value = 3,title = "服务项目数")
    private Double serviceNum;
    @ExcelCol(value = 4,title = "服务金额")
    private BigDecimal repairAmount;
    @ExcelCol(value= 5,title = "提成总额")
    private BigDecimal repairPercentage;


    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getRepairCarNum() {
        return repairCarNum;
    }

    public void setRepairCarNum(Integer repairCarNum) {
        this.repairCarNum = repairCarNum;
    }

    public Double getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(Double serviceNum) {
        this.serviceNum = serviceNum;
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
}
