package com.tqmall.legend.web.report.export.vo;

import java.math.BigDecimal;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 16/12/23.
 */
@Excel
public class AddPointCommissionSummary {
    @ExcelCol(value = 0, title = "员工")
    private String userName;
    @ExcelCol(value = 1, title = "营业额", profile = "turnover")
    private BigDecimal businessAmount;//营业额
    @ExcelCol(value = 1, title = "毛利", profile = "profit")
    private BigDecimal grossAmount;//毛利
    @ExcelCol(value = 2, title = "客户消费提成")
    private BigDecimal businessBeloneReward;//客户消费提成
    @ExcelCol(value = 3, title = "到店新客户")
    private Integer newCustomerNum;
    @ExcelCol(value = 4, title = "新客户提成")
    private BigDecimal newCustomerReward;
    @ExcelCol(value = 5, title = "销售之星提成")
    private BigDecimal saleStarReward;
    @ExcelCol(value = 6, title = "加点提成")
    private BigDecimal addPointPrefAmount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(BigDecimal businessAmount) {
        this.businessAmount = businessAmount;
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

    public Integer getNewCustomerNum() {
        return newCustomerNum;
    }

    public void setNewCustomerNum(Integer newCustomerNum) {
        this.newCustomerNum = newCustomerNum;
    }

    public BigDecimal getNewCustomerReward() {
        return newCustomerReward;
    }

    public void setNewCustomerReward(BigDecimal newCustomerReward) {
        this.newCustomerReward = newCustomerReward;
    }

    public BigDecimal getSaleStarReward() {
        return saleStarReward;
    }

    public void setSaleStarReward(BigDecimal saleStarReward) {
        this.saleStarReward = saleStarReward;
    }

    public BigDecimal getAddPointPrefAmount() {
        return addPointPrefAmount;
    }

    public void setAddPointPrefAmount(BigDecimal addPointPrefAmount) {
        this.addPointPrefAmount = addPointPrefAmount;
    }
}
