package com.tqmall.legend.enums.order;

/**
 * Created by lilige on 16/12/27.
 */
public enum OrderTradeTypeEnum {
    BENEFIT(1, "优惠"),
    METERCARD(2, "计次卡"),
    MEMBERCOUPON(3, "会员卡优惠"),
    OTHER(4,"组合优惠");
    private final int code;
    private final String message;

    private OrderTradeTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMessageByCode(int code) {
        for (OrderTradeTypeEnum e : OrderTradeTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getMessage();
            }
        }
        return null;
    }
}
