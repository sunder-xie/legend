package com.tqmall.legend.object.result.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wanghui on 6/16/16.
 */
public class AccountComboDTO implements Serializable{
    /**
     * 计次卡id
     */
    private Long accountComboId;
    /**
     * 计次卡名称
     */
    private String comboName;
    /**
     * 计次卡创建时间
     */
    private Date createDate;
    /**
     * 计次卡有效期
     */
    private Date expireDate;
    /**
     * 计次卡售价
     */
    private BigDecimal salePrice;
    /**
     * 计次卡关联服务信息
     */
    private List<AccountComboServiceDTO> comboServiceList;

    private List<String> licenseList;

    public Long getAccountComboId() {
        return accountComboId;
    }

    public void setAccountComboId(Long accountComboId) {
        this.accountComboId = accountComboId;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public List<AccountComboServiceDTO> getComboServiceList() {
        return comboServiceList;
    }

    public void setComboServiceList(List<AccountComboServiceDTO> comboServiceList) {
        this.comboServiceList = comboServiceList;
    }

    public List<String> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<String> licenseList) {
        this.licenseList = licenseList;
    }
}
