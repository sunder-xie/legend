package com.tqmall.legend.enums.marketing.gather;

/**
 * Created by wushuai on 16/12/21.
 */
public enum ReceiveCouponResultEnum {
    SUCCESS(0, "领取成功"),
    GET_OVER(1, "券领完了"),
    OVER_ACCOUNT_LIMIT(2, "超过当前账户领取上限");

    private int value;
    private String msg;

    ReceiveCouponResultEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }
}
