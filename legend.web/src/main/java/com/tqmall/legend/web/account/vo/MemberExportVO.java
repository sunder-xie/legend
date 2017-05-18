package com.tqmall.legend.web.account.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;

/**
 * Created by majian on 17/1/5.
 */
@Excel
public class MemberExportVO {
    private Long customerId;
    @ExcelCol(value = 0, title = "车主")
    private String customerName;
    @ExcelCol(value = 1, title = "车主电话", width = 12)
    private String mobile;
    @ExcelCol(value = 2, title = "车牌号", width = 12)
    private String licenses;
    @ExcelCol(value = 3, title = "会员卡类型", width = 10)
    private String cardInfo;
    @ExcelCol(value = 4, title = "会员卡号", width = 12)
    private String cardNum;
    @ExcelCol(value = 5, title = "会员卡余额", width = 10)
    private BigDecimal balance;
    @ExcelCol(value = 6, title = "累计充值金额", width = 12)
    private BigDecimal cumulativeDeposit;//累计充值
    @ExcelCol(value = 7, title = "过期时间", width = 12)
    private Date expireDate;
    @ExcelCol(value = 8, title = "办卡时间", width = 12)
    private Date createDate;

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_UP);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCumulativeDeposit() {
        return cumulativeDeposit.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCumulativeDeposit(BigDecimal cumulativeDeposit) {
        this.cumulativeDeposit = cumulativeDeposit;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
