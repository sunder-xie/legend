package com.tqmall.legend.enums.feedback;

/**
 * Created by zsy on 2015/4/15.
 */
public enum AnswerTypeEnum {
    ANSWER(0,"回答"),
    ASK(1,"追问");

    private Integer code;
    private String message;

    private AnswerTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        AnswerTypeEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            AnswerTypeEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static AnswerTypeEnum[] getMessages(){
        AnswerTypeEnum[] arr = values();
        return arr;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
