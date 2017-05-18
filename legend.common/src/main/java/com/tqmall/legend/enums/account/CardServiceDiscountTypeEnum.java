package com.tqmall.legend.enums.account;

/**
 * @Author 辉辉大侠
 * @Date:1:46 PM 03/03/2017
 */
public enum CardServiceDiscountTypeEnum {
    NO(0, "无折扣"), ALL(1, "全部服务享受折扣"), PART(2, "部分服务享受折扣");
    private int type;
    private String desc;

    CardServiceDiscountTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static final CardServiceDiscountTypeEnum type(int type) {
        for(CardServiceDiscountTypeEnum v : values()) {
            if (v.type == type) {
                return v;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
