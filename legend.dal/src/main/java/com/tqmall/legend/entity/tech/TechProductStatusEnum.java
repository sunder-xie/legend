package com.tqmall.legend.entity.tech;

/**
 * Created by zsy on 2015/4/30.
 */
public enum TechProductStatusEnum {
    CANCEL(Integer.valueOf(0),"取消"),
    SHOW(Integer.valueOf(1),"显示");

    private Integer code;
    private String message;

    private TechProductStatusEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    public static String getMesByCode(Integer code){
        TechProductStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TechProductStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TechProductStatusEnum[] getMessages(){
        TechProductStatusEnum[] arr = values();
        return arr;
    }
}
