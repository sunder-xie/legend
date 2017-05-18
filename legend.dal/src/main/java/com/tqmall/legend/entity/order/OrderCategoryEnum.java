package com.tqmall.legend.entity.order;

/**
 * 工单类别
 * <p/>
 * OrderGoodType Enum
 */
public enum OrderCategoryEnum {

    CARWASH(2, "洗车"),
    SPEEDILY(3, "快修快保"),
    COMMON(1, "综合维修"),
    SELLGOODS(5, "销售"),
    INSURANCE(4, "引流活动");

    private final int code;
    private final String sName;

    private OrderCategoryEnum(int code, String sName) {
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
        for (OrderCategoryEnum e : OrderCategoryEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

    public static OrderCategoryEnum[] getMessages() {
        OrderCategoryEnum[] arr = values();
        return arr;
    }
}
