package com.tqmall.legend.enums.serviceInfo;

/**
 * Created by twg on 16/8/26.
 */
public enum ServiceInfoStatusEnum {
    VALID(0,"有效"),
    TQFW_INVALID(-1,"淘汽服务下架"),
    CZFW_INVALID(-2,"车主服务下架");

    private final int code;
    private final String name;

    private ServiceInfoStatusEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
