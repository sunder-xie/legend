package com.tqmall.legend.entity.marketing;

/**
 * Created by twg on 15/8/10.
 */
public enum MarketingStatusEnum {
    MARKETING_STATUS_UP(1,"上架"),MARKETING_STATUS_OFF(0,"下架"),COLUMN_CONFIG_UP(1,"配置"),COLUMN_CONFIG_OFF(0,"未配置"),CASE_STATUS_NOT_ACT(0,"未激活"),CASE_STATUS_ACT(1,"已激活"),CASE_STATUS_ISSUE(2,"已发布");

    MarketingStatusEnum(int key,String value){
        this.key = key;
        this.value = value;
    }

    private int key;
    private String value;

    public int getKey() {
        return key;
    }
}
