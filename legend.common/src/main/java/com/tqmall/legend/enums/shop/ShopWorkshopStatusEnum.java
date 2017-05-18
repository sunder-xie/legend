package com.tqmall.legend.enums.shop;

/**
 * Created by zsy on 2016/06/21.
 *
 * 使用车间状态 0：不使用车间 1：使用车间
 */
public enum ShopWorkshopStatusEnum {
    NO(0,"不使用车间"),
    YES(1,"使用车间");

    private Integer code;
    private String message;

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    private ShopWorkshopStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        ShopWorkshopStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ShopWorkshopStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static ShopWorkshopStatusEnum[] getMessages(){
        ShopWorkshopStatusEnum[] arr = values();
        return arr;
    }

}
