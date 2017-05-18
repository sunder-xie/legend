package com.tqmall.legend.web.report.export.vo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 16/12/23.
 */
@Excel
public class NewCustomerCommission {
    @ExcelCol(value = 0, title = "员工")
    private String userName;
    @ExcelCol(value = 1, title = "车牌")
    private String license;
    @ExcelCol(value = 2, title = "到店消费时间")
    private String confirmTime;
    @ExcelCol(value = 3, title = "营业额")
    private BigDecimal orderAmount;
    @ExcelCol(value = 4, title = "新客户到店奖励")
    private BigDecimal newCustomerReward;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getNewCustomerReward() {
        return newCustomerReward;
    }

    public void setNewCustomerReward(BigDecimal newCustomerReward) {
        this.newCustomerReward = newCustomerReward;
    }
}
