package com.tqmall.legend.enums.order;

/**
 * 工单优惠流水金额类型
 * 优惠类型：1折扣、2优惠、3代金券、4折扣券、5淘汽优惠、6第三方优惠、7会员卡优惠、8现金券、9通用券、10计次卡
 */
public enum OrderDiscountTypeEnum {
    DISCOUNT(1, "折扣"),
    BENEFIT(2, "优惠"),
    COUPON(3, "代金券"),
    DISCOUNTCOUPON(4, "折扣券"),
    TAOQICOUPON(5, "淘汽优惠"),
    OTHERCOUPON(6, "第三方优惠"),
    MEMBERCOUPON(7, "会员卡优惠"),
    CASHCOUPON(8, "现金券"),
    UNIVERSALCOUPON(9, "通用券"),
    METERCARD(10, "计次卡"),
    GIFTCOUPON(11, "转赠券");

    private final int code;
    private final String message;

    private OrderDiscountTypeEnum(int code, String message) {
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
        for (OrderDiscountTypeEnum e : OrderDiscountTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getMessage();
            }
        }
        return null;
    }

    public static OrderDiscountTypeEnum[] getMessages() {
        OrderDiscountTypeEnum[] arr = values();
        return arr;
    }
}