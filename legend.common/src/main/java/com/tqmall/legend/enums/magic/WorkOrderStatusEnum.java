package com.tqmall.legend.enums.magic;

/**
 * 施工单状态枚举
 * Created by shulin on 16/7/8.
 */
public enum WorkOrderStatusEnum {
    DSG("DSG", "待施工"),
    SGZ("SGZ", "施工中"),
    SGZD("SGZD", "施工中断"),
    YWG("YWG", "已完工");

    private final String code;
    private final String name;

    WorkOrderStatusEnum(String code, String name) {
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
        for (WorkOrderStatusEnum e : WorkOrderStatusEnum.values()) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return null;
    }
}
