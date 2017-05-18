package com.tqmall.legend.enums.payment;

/**
 * 在线支付方式枚举
 */
public enum PaymentEnum {
    WECHAT(10000l, "微信预付定金"),
    ALIPAY(10001l, "支付宝预付定金");

    private final Long id;
    private final String name;

    PaymentEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static String getNameById(Long id) {
        for (PaymentEnum e : PaymentEnum.values()) {
            if (e.getId().equals(id)) {
                return e.getName();
            }
        }
        return "";
    }

    public static PaymentEnum[] getNames() {
        PaymentEnum[] arr = values();
        return arr;
    }
}