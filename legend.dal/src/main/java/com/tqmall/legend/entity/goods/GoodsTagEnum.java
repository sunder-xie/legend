package com.tqmall.legend.entity.goods;

/**
 * 配件类别 ENUM
 */
public enum GoodsTagEnum {

    PAINT(Integer.valueOf(1), "油漆配件"),
    COMMON(Integer.valueOf(0), "普通配件");

    private final Integer code;
    private final String sName;

    private GoodsTagEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for (GoodsTagEnum e : GoodsTagEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

    public static GoodsTagEnum[] getMessages() {
        GoodsTagEnum[] arr = values();
        return arr;
    }
}
