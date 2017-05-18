package com.tqmall.legend.enums.magic;

/**
 * 工序状态枚举
 * Created by shulin on 16/7/8.
 */
public enum ProcessStatusEnum {
    DSG("DSG", "待施工"),
    SGZ("SGZ", "施工中"),
    SGZD("SGZD", "施工中断"),
    SGWC("SGWC", "施工完成"),
    WZT("WZT", "无状态");


    private final String code;
    private final String name;

    ProcessStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static String getNameByCode(String code) {
        for (ProcessStatusEnum e : ProcessStatusEnum.values()) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return null;
    }
}
