package com.tqmall.legend.entity.tech;

/**
 * Created by zsy on 2015/4/30.
 */
public enum TechProductPositionEnum {
    INDEX(Integer.valueOf(0),"首页推送"),
    TECH(Integer.valueOf(1),"技术中心推送");

    private Integer code;
    private String message;

    private TechProductPositionEnum(Integer code, String message){
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
        TechProductPositionEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TechProductPositionEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TechProductPositionEnum[] getMessages(){
        TechProductPositionEnum[] arr = values();
        return arr;
    }
}
