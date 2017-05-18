package com.tqmall.legend.enums.setting;

/**
 * Created by lilige on 17/1/9.
 */
public enum EducationEnum {
    CHUZHONG(1,"初中"),
    GAOZHONG(2,"高中"),
    ZHONGZHUAN(3,"中技"),
    DAZHUAN(4,"大专"),
    BENKE(5,"本科及以上");

    Integer code;
    String message;

    EducationEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static Integer getCodeByName(String name){
        for(EducationEnum educationEnum : EducationEnum.values()){
            if(educationEnum.message.equals(name)){
                return educationEnum.getCode();
            }
        }
        return null;
    }
}
