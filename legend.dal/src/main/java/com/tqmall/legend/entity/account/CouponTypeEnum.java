package com.tqmall.legend.entity.account;

/**
 * Created by twg on 16/6/7.
 */
public enum CouponTypeEnum {
    DISCOUNT_COUPON(0, "折扣券"),
    CASH_COUPON(1, "现金券"),
    UNIVERSAL_COUPON(2, "通用券");

    private final int code;
    private final String alias;

    private CouponTypeEnum(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return this.code;
    }

    public String getAlias() {
        return this.alias;
    }

    public final static CouponTypeEnum code(int code) {
        for (CouponTypeEnum v : values()) {
            if (code == v.code) {
                return v;
            }
        }
        return null;
    }
}
