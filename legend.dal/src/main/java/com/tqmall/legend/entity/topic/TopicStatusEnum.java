package com.tqmall.legend.entity.topic;

/**
 * Created by zsy on 2015/4/28.
 */
public enum TopicStatusEnum {
    COMPLETEDRAFT(Integer.valueOf(0),"草稿"),
    PUBLISH(Integer.valueOf(1),"发布");

    private Integer code;
    private String message;

    private TopicStatusEnum(Integer code,String message){
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
        TopicStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TopicStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static TopicStatusEnum[] getMessages(){
        TopicStatusEnum[] arr = values();
        return arr;
    }
}
