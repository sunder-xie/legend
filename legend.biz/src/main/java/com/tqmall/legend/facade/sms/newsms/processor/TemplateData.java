package com.tqmall.legend.facade.sms.newsms.processor;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class TemplateData {
    private String mobile;
    private Map<String, String> placeHolderMap;
    private Integer needNumber;
    private String customerName;
    private String licenses;

    public TemplateData() {
        placeHolderMap = Maps.newHashMap();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getNeedNumber() {
        return needNumber;
    }

    public void setNeedNumber(Integer needNumber) {
        this.needNumber = needNumber;
    }

    public Map<String, String> getPlaceHolderMap() {
        return placeHolderMap;
    }
    public void put(String key, String value) {
        placeHolderMap.put(key, value);
    }

    public int getContentLength() {
        int length = 0;
        Collection<String> values = placeHolderMap.values();
        for (String value : values) {
            if (value == null) {
                continue;
            }
            length += value.length();
        }
        return length;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }
}