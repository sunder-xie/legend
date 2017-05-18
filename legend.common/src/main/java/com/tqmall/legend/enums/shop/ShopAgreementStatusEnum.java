package com.tqmall.legend.enums.shop;

/**
 * Created by zsy on 2016/08/11.
 *
 * 同意协议状态：0：未同意，1：同意
 */
public enum ShopAgreementStatusEnum {
    NO(0,"未同意"),
    YES(1,"同意");

    private Integer code;
    private String message;

    public Integer getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    private ShopAgreementStatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(Integer code){
        ShopAgreementStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ShopAgreementStatusEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public static ShopAgreementStatusEnum[] getMessages(){
        ShopAgreementStatusEnum[] arr = values();
        return arr;
    }

}
