package com.tqmall.legend.entity.account;

import java.util.Date;

/**
 * Created by majian on 17/1/5.
 */
public class ComboExportEntity {
    private Long customerId;
    private String customerName;
    private String mobile;
    private String comboInfo;
    private String comboService;
    private Integer remainNumber;//剩余次数
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

    public String getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(String comboInfo) {
        this.comboInfo = comboInfo;
    }

    public String getComboService() {
        return comboService;
    }

    public void setComboService(String comboService) {
        this.comboService = comboService;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
