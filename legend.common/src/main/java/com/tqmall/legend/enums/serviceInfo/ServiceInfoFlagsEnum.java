package com.tqmall.legend.enums.serviceInfo;

/**
 * Created by twg on 16/8/26.
 */
public enum ServiceInfoFlagsEnum {
    TQFW("TQFW","淘汽服务"),
    CZFW("CZFW","车主服务"),
    BZFW("BZFW","标准服务"),
    PABQ("PABQ","平安补漆"),
    PABY("PABY","平安保养"),
    BPFW("BPFW","钣喷服务");

    private String code;
    private String name;
    private ServiceInfoFlagsEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
