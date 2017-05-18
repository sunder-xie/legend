package com.tqmall.legend.enums.setting;

/**
 * Created by zsy on 16/11/7.
 */
public enum OrderTypeShowStatusEnum {
    SHOW(1,"展示"),
    HIDE(0,"隐藏");

    Integer code;
    String name;

    OrderTypeShowStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
