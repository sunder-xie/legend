package com.tqmall.legend.biz.account.param;


/**
 * Created by majian on 17/1/5.
 */
public class ComboExportArgs {
    private Long shopId;
    private Long comboInfoId;
    private Long serviceId;
    private String mobile;
    private String customerName;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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
