package com.tqmall.legend.entity.shop;

/**
 * Created by zsy on 2016/03/22.
 * 用与服务列表的查询、展示
 */
public enum ServiceFlagsEnum {
    TQFW("TQFW","淘汽服务"),
    CZFW("CZFW","车主服务"),
    BZFW("BZFW","标准服务"),
    PABQ("PABQ","平安补漆"),
    PABY("PABY","平安保养"),
    BPFW("BPFW","钣喷服务"),
    MDFW("MDFW","门店服务");

    private final String flags;
    private final String message;

    private ServiceFlagsEnum(String flags, String message){
        this.flags = flags;
        this.message = message;
    }

    public static String getMesByCode(String flags){
        ServiceFlagsEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ServiceFlagsEnum iEnum = arr[i];
            if(iEnum.getFlags().equals(flags)){
                return iEnum.getMessage();
            }
        }
        return null;
    }

    public String getFlags(){
        return this.flags;
    }

    public String getMessage(){
        return this.message;
    }

    public static ServiceFlagsEnum[] getMessages(){
        ServiceFlagsEnum[] arr = values();
        return arr;
    }
}
