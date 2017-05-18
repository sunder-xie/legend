package com.tqmall.legend.enums.shop;

/**
 * Created by sven on 2017/1/16.
 */
public enum ShopVersionStatusEnum {
    UNSTABLE(0, "不稳定"),
    STABLE(1, "稳定");
    private Integer code;
    private String message;

    ShopVersionStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
