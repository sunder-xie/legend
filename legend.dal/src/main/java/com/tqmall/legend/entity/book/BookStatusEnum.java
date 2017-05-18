package com.tqmall.legend.entity.book;

/**
 * Created by Administrator on 2015/4/15.
 */
public enum BookStatusEnum {
    INCOMPLETEDRAFT(Integer.valueOf(-1),"不完整草稿"),
    COMPLETEDRAFT(Integer.valueOf(0),"完整草稿"),
    PUBLISH(Integer.valueOf(1),"发布");

    private Integer code;
    private String message;

    private BookStatusEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        BookStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            BookStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static BookStatusEnum[] getMessages(){
        BookStatusEnum[] arr = values();
        return arr;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
