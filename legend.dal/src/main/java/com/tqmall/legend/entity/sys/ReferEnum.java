package com.tqmall.legend.entity.sys;

/**
 * Created by tan.li on 15-4-21.
 */

public enum ReferEnum {
    WEB(0,"web"),
    ANDROID(1,"安卓"),
    IOS(2,"苹果");

    private int code;
    private String message ;

    public int getCode(){
        return this.code;
    }
    public String getMessage(){
        return this.message;
    }

    private ReferEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}
