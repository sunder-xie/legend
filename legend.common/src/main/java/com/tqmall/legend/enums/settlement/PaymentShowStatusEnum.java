package com.tqmall.legend.enums.settlement;

/**
 * Created by sven on 16/6/3.
 */
public enum PaymentShowStatusEnum {
    NOTSHOW(Integer.valueOf(0),"不展示"),
    SHOW(Integer.valueOf(1),"展示");

    Integer code;
    String message;
    PaymentShowStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static Integer getCodeByName( String name){
        for(PaymentShowStatusEnum payStatusEnum : PaymentShowStatusEnum.values()){
            if(payStatusEnum.name().equals(name)){
                return payStatusEnum.getCode();
            }
        }
        return null;
    }
}
