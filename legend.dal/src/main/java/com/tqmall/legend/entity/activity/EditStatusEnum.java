package com.tqmall.legend.entity.activity;

/**
 * Created by lilige on 16/3/22.
 */
public enum EditStatusEnum {
    //0可编辑 1可部分编辑,价格等 2不可编辑';
    EDIT(Integer.valueOf(0), "可编辑"),
    PART_EDIT(Integer.valueOf(1), "可部分编辑"),
    NO_EDIT(Integer.valueOf(2), "不可编辑");

    private Integer code;
    private String message;

    private EditStatusEnum(Integer code, String message) {
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
        EditStatusEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            EditStatusEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static String getExcelByCode(Integer code) {
        EditStatusEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            EditStatusEnum iEnum = arr[i];
            if (iEnum.getCode().equals(code)) {
                return iEnum.getMessage();
            }
        }
        return null;
    }
}
