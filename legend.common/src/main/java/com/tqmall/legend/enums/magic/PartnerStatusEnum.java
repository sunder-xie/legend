package com.tqmall.legend.enums.magic;

/**
 * Created by macx on 16/5/13.
 */
public enum PartnerStatusEnum {
    UNJOIN(0,"未加入"),
    JOIN(1,"已加入"),
    QUIT(2,"已退出");

    private Integer code;
    private String name;

    PartnerStatusEnum(Integer code, String name){
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
