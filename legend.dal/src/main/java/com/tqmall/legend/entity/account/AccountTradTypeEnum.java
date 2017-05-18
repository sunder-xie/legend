package com.tqmall.legend.entity.account;

/**
 * Created by twg on 16/6/7.
 */
public enum AccountTradTypeEnum {
    COUPON(1,"优惠券"),
    COMBO(2,"计次卡"),
    MEMBER_CARD(3,"会员卡"),
    COMPOUND_TYPE(4,"多种类型");

    private final int code;
    private final String alias;

    private AccountTradTypeEnum(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return this.code;
    }

    public String getAlias() {
        return this.alias;
    }
}
