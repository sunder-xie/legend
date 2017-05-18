package com.tqmall.legend.enums.customer;

/**
 * Created by zsy on 2015/4/15.
 * 自定义客户标签
 */
public enum CustomCarTagEnum {
    XCKH(0,"洗车客户"),
    KXKB(1,"保养客户");

    private Integer code;
    private String message;

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    private CustomCarTagEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        CustomCarTagEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            CustomCarTagEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static Integer getCodeByMes(String message){
        CustomCarTagEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            CustomCarTagEnum iEnum = arr[i];
            if(iEnum.getMessage().equals(message)){
                return iEnum.getCode();
            }
        }
        return null;
    }

    public static CustomCarTagEnum[] getMessages(){
        CustomCarTagEnum[] arr = values();
        return arr;
    }
}
