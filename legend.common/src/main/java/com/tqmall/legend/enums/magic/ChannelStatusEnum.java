package com.tqmall.legend.enums.magic;

/**
 * Created by macx on 16/5/11.
 */
public enum ChannelStatusEnum {

    VALID(1,"有效"),
    INVALID(2,"无效");

    private Integer code;
    private String name;

    ChannelStatusEnum(Integer code,String name){
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
