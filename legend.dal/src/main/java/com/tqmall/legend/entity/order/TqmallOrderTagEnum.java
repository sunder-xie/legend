package com.tqmall.legend.entity.order;

/**
 * 档口店工单类型
 */
public enum TqmallOrderTagEnum {

    CARWASH(2, "洗车"),
    SPEEDILY(3, "快修快保"),
    SELLGOODS(5, "销售");

    private final int code;
    private final String sName;

    private TqmallOrderTagEnum(int code, String sName) {
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
        for (TqmallOrderTagEnum e : TqmallOrderTagEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

    public static TqmallOrderTagEnum[] getMessages() {
        TqmallOrderTagEnum[] arr = values();
        return arr;
    }
}
