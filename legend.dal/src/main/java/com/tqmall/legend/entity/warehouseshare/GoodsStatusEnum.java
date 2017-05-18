package com.tqmall.legend.entity.warehouseshare;

/**
 * Created by tanghao on 16/11/12.
 */
public enum GoodsStatusEnum {
    WAITFORSALE(0,"待出售"),
    SALE(1,"出售中"),
    UNPASS(9,"审核未通过");

    private final int code;
    private final String message;

    private GoodsStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
