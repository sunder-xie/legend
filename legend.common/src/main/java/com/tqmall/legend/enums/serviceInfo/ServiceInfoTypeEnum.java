package com.tqmall.legend.enums.serviceInfo;

/**
 * Created by twg on 16/8/26.
 * ShopServiceTypeEnum
 */

public enum ServiceInfoTypeEnum {
    GENERAL_SERVICE(1,"常规服务"),
    OTHER_SERVICE(2,"其它费用服务");

    private final int code;
    private final String name;

    private ServiceInfoTypeEnum(int code,String name){
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getNameByCode(int code){
        for (ServiceInfoTypeEnum type : ServiceInfoTypeEnum.values()) {
            if(type.getCode() == code){
                return type.getName();
            }
        }
        return "";
    }
}
