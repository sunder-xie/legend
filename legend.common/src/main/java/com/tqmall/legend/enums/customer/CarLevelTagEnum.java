package com.tqmall.legend.enums.customer;

/**
 * Created by zsy on 2015/4/15.
 */
public enum CarLevelTagEnum {
    LEVEL0(0,"无级别"),
    LEVEL1(1,"低端客户"),
    LEVEL2(2,"中端客户"),
    LEVEL3(3,"高端客户");

    private Integer code;
    private String message;

    private CarLevelTagEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        CarLevelTagEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            CarLevelTagEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static CarLevelTagEnum[] getMessages(){
        CarLevelTagEnum[] arr = values();
        return arr;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
