package com.tqmall.legend.entity.order;

/**
 * OrderGoodType Enum
 */
public enum OrderGoodTypeEnum {

    ACTUAL(0, "实开物料"),
    VIRTUAL(1, "虚开物料");

    private final int code;
    private final String sName;

    private OrderGoodTypeEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public int getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for (OrderGoodTypeEnum e : OrderGoodTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }
}
