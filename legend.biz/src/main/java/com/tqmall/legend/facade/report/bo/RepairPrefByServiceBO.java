package com.tqmall.legend.facade.report.bo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/14.
 */
@com.tqmall.wheel.component.excel.annotation.Excel
public class RepairPrefByServiceBO {
    @ExcelCol(value = 0 , title = "员工姓名")
    private String empName;
    @ExcelCol(value = 1, title = "服务项目名")
    private String serviceName;
    @ExcelCol(value = 2, title = "提成规则")
    private String percentageRule;
    @ExcelCol(value = 3, title = "项目数")
    private BigDecimal serviceCount;
    @ExcelCol(value = 4, title = "金额")
    private BigDecimal repairAmount;
    @ExcelCol(value = 5, title = "提成")
    private BigDecimal repairPercentage;

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPercentageRule() {
        return percentageRule;
    }

    public void setPercentageRule(String percentageRule) {
        this.percentageRule = percentageRule;
    }

    public BigDecimal getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(BigDecimal serviceCount) {
        this.serviceCount = serviceCount;
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
