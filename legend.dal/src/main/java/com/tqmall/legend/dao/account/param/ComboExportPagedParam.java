package com.tqmall.legend.dao.account.param;

import java.util.Date;

import org.springframework.data.domain.Pageable;

/**
 * Created by majian on 17/1/5.
 */
public class ComboExportPagedParam extends BasePagedParam{
    private ComboExportParam conditions;

    public ComboExportPagedParam() {
    }

    public ComboExportPagedParam(Pageable pageable, ComboExportParam conditions) {
        super(pageable);
        this.conditions = conditions;
    }

    public Long getShopId() {
        return conditions.getShopId();
    }

    public void setShopId(Long shopId) {
        conditions.setShopId(shopId);
    }

    public Date getNow() {
        return conditions.getNow();
    }

    public void setNow(Date now) {
        conditions.setNow(now);
    }

    public Long getComboInfoId() {
        return conditions.getComboInfoId();
    }

    public Long getServiceId() {
        return conditions.getServiceId();
    }

    public void setServiceId(Long serviceId) {
        conditions.setServiceId(serviceId);
    }

    public void setCustomerName(String customerName) {
        conditions.setCustomerName(customerName);
    }

    public void setMobile(String mobile) {
        conditions.setMobile(mobile);
    }

    public String getMobile() {
        return conditions.getMobile();
    }

    public void setComboInfoId(Long comboInfoId) {
        conditions.setComboInfoId(comboInfoId);
    }

    public String getCustomerName() {
        return conditions.getCustomerName();
    }
}
