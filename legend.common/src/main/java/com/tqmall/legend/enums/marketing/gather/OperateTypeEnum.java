package com.tqmall.legend.enums.marketing.gather;

/**
 * Created by xin on 2016/12/19.
 */
public enum OperateTypeEnum {
    PHONE(0, "电话回访"),
    SMS(1, "短信回访"),
    WX_COUPON(2, "微信优惠券");

    private int value;
    private String name;

    OperateTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getNameByValue(int value) {
        for (OperateTypeEnum operateTypeEnum : values()) {
            if (operateTypeEnum.getValue() == value) {
                return operateTypeEnum.getName();
            }
        }
        return "";
    }
}
