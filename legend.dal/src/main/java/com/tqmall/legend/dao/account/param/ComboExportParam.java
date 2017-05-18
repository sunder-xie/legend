package com.tqmall.legend.dao.account.param;

import java.util.Date;

/**
 * Created by majian on 17/1/5.
 */
public class ComboExportParam {
    private Long shopId;
    private Long comboInfoId;
    private Long serviceId;
    private String mobile;
    private String customerName;
    private Date now = new Date();

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public Long getComboInfoId() {
        return comboInfoId;
    }

    public void setComboInfoId(Long comboInfoId) {
        this.comboInfoId = comboInfoId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
