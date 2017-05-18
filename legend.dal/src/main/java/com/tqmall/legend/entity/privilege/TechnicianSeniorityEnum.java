package com.tqmall.legend.entity.privilege;

/**
 * Created by lilige on 16/1/11.
 */
public enum TechnicianSeniorityEnum {
//    维修工龄: 1: 1年以下  2:2-3年中工 3:3-5年师傅  4: 5-8年 5: 8年以上 0-未填写
    UNDER_ONE(Integer.valueOf(1),"1年以下"),
    TWO_THREE(Integer.valueOf(2),"2-3年中工"),
    THREE_FIVE(Integer.valueOf(3),"3-5年师傅"),
    FIVE_EIGHT(Integer.valueOf(4),"5-8年"),
    OVER_EIGHT(Integer.valueOf(5),"8年以上");

    private Integer code;
    private String message;

    private TechnicianSeniorityEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        TechnicianSeniorityEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TechnicianSeniorityEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TechnicianSeniorityEnum[] getMessages(){
        TechnicianSeniorityEnum[] arr = values();
        return arr;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
