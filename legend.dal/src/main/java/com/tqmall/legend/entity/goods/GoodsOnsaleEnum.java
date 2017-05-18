package com.tqmall.legend.entity.goods;

/**
 * 配件上下架状态 ENUM
 */
public enum GoodsOnsaleEnum {

    UPSHELF(Integer.valueOf(1), "上架"),
    DOWNSHELF(Integer.valueOf(0), "下架");

    private final Integer code;
    private final String sName;

    private GoodsOnsaleEnum(int code, String sName) {
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
        for (GoodsOnsaleEnum e : GoodsOnsaleEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

    public static GoodsOnsaleEnum[] getMessages() {
        GoodsOnsaleEnum[] arr = values();
        return arr;
    }
}
