package com.tqmall.legend.enums.magic;

/**
 * Created by zsy on 16/05/17.
 */
public enum ShopShareEnum {
    BPZX(1, "钣喷中心");

    private Integer code;
    private String message;

    private ShopShareEnum(Integer code, String message) {
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
        ShopShareEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ShopShareEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static Integer getCodeByMes(String message) {
        ShopShareEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ShopShareEnum iEnum = arr[i];
            if (iEnum.getMessage().equals(message)) {
                return iEnum.getCode();
            }
        }
        return null;
    }
}
