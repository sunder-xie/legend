package com.tqmall.legend.web.report.export.vo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 16/12/23.
 */
@Excel
public class AchievementBelongCommission {
    @ExcelCol(value = 0, title = "员工")
    private String userName;
    @ExcelCol(value = 1, title = "车辆")
    private String license;
    @ExcelCol(value = 2, title = "到店消费时间")
    private String confirmTime;
    @ExcelCol(value = 3, title = "营业额", profile = "turnover")
    private BigDecimal orderAmount = BigDecimal.ZERO;//营业额
    @ExcelCol(value = 3, title = "毛利", profile = "profit")
    private BigDecimal grossAmount = BigDecimal.ZERO;//毛利
    @ExcelCol(value = 4, title = "业绩归属奖励")
    private BigDecimal businessBeloneReward;

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

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getBusinessBeloneReward() {
        return businessBeloneReward;
    }

    public void setBusinessBeloneReward(BigDecimal businessBeloneReward) {
        this.businessBeloneReward = businessBeloneReward;
    }
}
