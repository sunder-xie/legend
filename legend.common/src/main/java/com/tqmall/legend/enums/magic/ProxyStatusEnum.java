package com.tqmall.legend.enums.magic;

public enum ProxyStatusEnum {
    YWT("YWT", "已委托"),
    YJD("YJD", "已接单"),
    YYC("YYC", "已验车"),
    YPG("FPDD", "已派工"),
    YWG("DDWC", "已完工"),
    YJC("YJC", "已交车"),
    YGZ("YGZ","已挂账"),
    YJQ("YJQ", "已结清"),
    YQX("YQX", "已无效");

    private final String code;
    private final String name;

    ProxyStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static String getNameByCode(String code) {
        for (ProxyStatusEnum e : ProxyStatusEnum.values()) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return null;
    }


    public static String getCodeByName(String name){
        for (ProxyStatusEnum e : ProxyStatusEnum.values()) {
            if (name.equals(e.getName())) {
                return e.getCode();
            }
        }
        return null;
    }
}