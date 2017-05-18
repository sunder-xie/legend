package com.tqmall.legend.enums.marketing.gather;

/**
 * Created by xin on 2016/12/19.
 */
public enum GatherTypeEnum {
    PANHUO(0, "盘活客户"),
    LAXIN(1, "老客户拉新");

    private int value;
    private String name;

    GatherTypeEnum(int value, String name) {
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
        for (GatherTypeEnum gatherTypeEnum : values()) {
            if (gatherTypeEnum.getValue() == value) {
                return gatherTypeEnum.getName();
            }
        }
        return "";
    }
}
