package com.tqmall.legend.enums.settlement;

/**
 * Created by sven on 16/6/3.
 */
public enum PayStatusEnum {
    DDZF(1,"等待支付"),
    BFZF(2,"部分支付"),
    ZFWC(3,"支付完成");
    Integer code;
    String message;
    PayStatusEnum(Integer code ,String message){
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
        for(PayStatusEnum payStatusEnum : PayStatusEnum.values()){
            if(payStatusEnum.name().equals(name)){
                return payStatusEnum.getCode();
            }
        }
        return null;
    }
}
