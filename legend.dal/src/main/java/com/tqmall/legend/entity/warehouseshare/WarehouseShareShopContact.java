package com.tqmall.legend.entity.warehouseshare;

import com.tqmall.legend.entity.base.BaseEntity;
/**
 * Created by tanghao on 16/11/10.
 */


public class WarehouseShareShopContact extends BaseEntity {

    private Long shopId;//店铺id
    private Long contactId;//联系人id
    private String contactName;//联系人名称
    private String contactMobile;//手机号码


    private String contactAddress;//联系地址

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
}
