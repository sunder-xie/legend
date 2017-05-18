package com.tqmall.legend.enums.shop;

/**
 * Created by zsy on 2016/09/02.
 */
public enum ShopStatusEnum {
    FORMAL(Integer.valueOf(1), "开通"),
    FROZEN(Integer.valueOf(2), "冻结"),
    TEST(Integer.valueOf(3), "测试"),
    TRY(Integer.valueOf(4), "试用"),
    LORRY(Integer.valueOf(5),"商用车");

    private Integer code;
    private String message;

    private ShopStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMesByCode(Integer code) {
        ShopStatusEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ShopStatusEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static ShopStatusEnum[] getMessages() {
        ShopStatusEnum[] arr = values();
        return arr;
    }
}
