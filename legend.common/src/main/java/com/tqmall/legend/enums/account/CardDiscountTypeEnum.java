package com.tqmall.legend.enums.account;

/**
 * @Author 辉辉大侠
 * @Date:1:42 PM 03/03/2017
 */
public enum CardDiscountTypeEnum {
    NO(0, "无折扣"), ALL(1, "全部工单折扣"), SERVICE(2, "全部服务折扣"), GOODS(3, "全部配件折扣"), MULTI(4, "多种类型折扣");
    private int type;
    private String desc;

    CardDiscountTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public final static CardDiscountTypeEnum type(int type) {
        for (CardDiscountTypeEnum v : values()) {
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
