package com.tqmall.legend.entity.shop;

/**
 * 门店配置表 配置类型枚举
 */
public enum ShopServiceTypeEnum {
    REGULAR(1, "常规服务"),
    OTHER(2, "其它费用服务");

    private final int code;
    private final String sName;

    ShopServiceTypeEnum(int code, String sName) {
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
        for (ShopServiceTypeEnum e : ShopServiceTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

}
