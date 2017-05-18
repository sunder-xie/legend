package com.tqmall.legend.entity.order;

/**
 * OrderServiceType Enum
 *
 * Created by dongc on 15/7/20.
 */
public enum OrderServiceTypeEnum {

    BASIC(1, "基本服务"),
    ANCILLARY(2, "附属服务");

    private final int code;
    private final String sName;

    private OrderServiceTypeEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public int getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for(OrderServiceTypeEnum e :OrderServiceTypeEnum.values()){
            if(e.getCode() ==code){
                return e.getsName();
            }
        }
        return null;
    }
}
