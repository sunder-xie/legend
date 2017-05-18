package com.tqmall.legend.enums.shop;

/**
 * Created by zsy on 2016/06/21.
 *
 * '加入委托体系状态：0：无，1：加入 默认为0
 */
public enum ShopJoinStatusEnum {
    NO(0,"未加入"),
    YES(1,"加入体系");

    private Integer code;
    private String message;

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    private ShopJoinStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        ShopJoinStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ShopJoinStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static ShopJoinStatusEnum[] getMessages(){
        ShopJoinStatusEnum[] arr = values();
        return arr;
    }

}
