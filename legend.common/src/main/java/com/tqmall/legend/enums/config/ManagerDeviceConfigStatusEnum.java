package com.tqmall.legend.enums.config;

/**
 * Created by twg on 16/9/6.
 */
public enum ManagerDeviceConfigStatusEnum {
    //0未授权，1已授权, 2授权失败,3未申请
    AUTHOR_DOING(0,"申请中"),
    AUTHOR_AGREE(1,"已同意"),
    AUTHOR_REFUSE(2,"已拒绝"),
    AUTHOR_UNBUND(3,"已解绑");

    private final int code;
    private final String name;

    private ManagerDeviceConfigStatusEnum(int code,String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(int code) {
        for (ManagerDeviceConfigStatusEnum statusEnum : ManagerDeviceConfigStatusEnum.values()) {
            if(statusEnum.getCode() == code){
                return statusEnum.getName();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
