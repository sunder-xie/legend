package com.tqmall.legend.enums.coupon;

/**
 * Created by wushuai on 16/07/08.
 * 对应数据库字段legend_account_coupon.coupon_source
 */
public enum AccountCouponSourceEnum {
    RECHARGE(0,"充值"),
    PRESENTED(1,"赠送"),
    SHOP_WECHAT(2,"门店微信公众号发放"),
    GATHER_NEW_CUSTOMER(3,"集客老客户带新"),
    GATHER_PANHUO_PHONE(4, "集客方案盘活客户电话回访"),
    GATHER_PANHUO_SMS(5, "集客方案盘活客户发送短信");

    private final Integer value;
    private final String name;

    private AccountCouponSourceEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    //获值名称
    public static String getNameByValue(Integer value) {
        if (value == null) {
            return "";
        }
        AccountCouponSourceEnum[] statusEnums = AccountCouponSourceEnum.values();
        for (AccountCouponSourceEnum statusEnum : statusEnums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.getName();
            }
        }
        return "";
    }
}
