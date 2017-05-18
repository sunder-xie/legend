package com.tqmall.legend.enums.magic;

/**
 * Created by macx on 16/5/18.
 */
public enum ChannelTypeEnum {

    UNTQMALL(1,"非云修店"),
    INSURANCE(2,"保险公司"),
    PERSONAL(3,"个人(自费)"),
    TQMALL(4,"云修店"),
    PARTNER(5,"股东店");

    private Integer code;
    private String name;

    ChannelTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
