package com.tqmall.legend.entity.settlement;

/**
 * 结算方式类型
 * <p/>
 * Created by dongc on 15/8/24.
 */
public enum ConsumeTypeEnum {
    COUPON(1, "会员优惠券消费"),
    RECOUPON(2, "优惠券重新结算"),
    BALANCE(3, "会员余额消费"),
    REBALANCE(4, "余额重新结算");

    private final int code;
    private final String alias;

    private ConsumeTypeEnum(int code, String alias) {
        this.code = code;
        this.alias = alias;
    }

    public int getCode() {
        return this.code;
    }

    public String getAlias() {
        return this.alias;
    }

    public static String getAliasByCode(int code) {
        for (ConsumeTypeEnum e : ConsumeTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getAlias();
            }
        }
        return null;
    }


}
