package com.tqmall.legend.enums.account;

/**
 * @Author 辉辉大侠
 * @Date:1:49 PM 03/03/2017
 */
public enum CardGoodsDiscountTypeEnum {
    NO(0, "无折扣"), ALL(1, "全部配件享受折扣"), PART(2, "部分配件享受折扣");
    private int type;
    private String desc;

    CardGoodsDiscountTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static final CardGoodsDiscountTypeEnum type(int type) {
        for (CardGoodsDiscountTypeEnum v : values()) {
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
