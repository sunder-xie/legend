package com.tqmall.legend.entity.account;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by majian on 17/1/5.
 */
public class MemberExportEntity {
    private Long customerId;
    private String customerName;
    private String mobile;
    private String cardInfo;
    private String cardNum;
    private BigDecimal balance;
    private BigDecimal cumulativeDeposit;//累计充值
    private Date expireDate;
    private Date createDate;
    private String licenses;

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCumulativeDeposit() {
        return cumulativeDeposit;
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
