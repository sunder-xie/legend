package com.tqmall.legend.entity.course;

/**
 * Created by zsy on 15-5-7.
 */
public enum  CourseStatusEnum {
    COMPLETEDRAFT(Integer.valueOf(0),"草稿"),
    PUBLISH(Integer.valueOf(1),"发布");

    private Integer code;
    private String message;

    private CourseStatusEnum(Integer code,String message){
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
        CourseStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            CourseStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static CourseStatusEnum[] getMessages(){
        CourseStatusEnum[] arr = values();
        return arr;
    }
}
