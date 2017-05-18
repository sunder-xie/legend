package com.tqmall.legend.entity.privilege;

/**
 * Created by lilige on 16/1/11.
 */
public enum TechnicianLevelEnum {
    //技师等级：1-初级 2-中级 3-高级
    PRIMARY(Integer.valueOf(1),"初级"),
    MEDIUM(Integer.valueOf(2),"中级"),
    SENIOR(Integer.valueOf(3),"高级");

    private Integer code;
    private String message;

    private TechnicianLevelEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        TechnicianLevelEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TechnicianLevelEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TechnicianLevelEnum[] getMessages(){
        TechnicianLevelEnum[] arr = values();
        return arr;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
