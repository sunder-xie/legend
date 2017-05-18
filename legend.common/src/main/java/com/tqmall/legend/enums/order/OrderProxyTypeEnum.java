package com.tqmall.legend.enums.order;

/**
 * 工单优惠流水金额类型
 */
public enum OrderProxyTypeEnum {
    WT(Integer.valueOf(1), "委托"),
    ST(Integer.valueOf(2), "受托");

    private final Integer code;
    private final String message;

    private OrderProxyTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMessageByCode(int code) {
        for (OrderProxyTypeEnum e : OrderProxyTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getMessage();
            }
        }
        return null;
    }

    public static OrderProxyTypeEnum[] getMessages() {
        OrderProxyTypeEnum[] arr = values();
        return arr;
    }
}