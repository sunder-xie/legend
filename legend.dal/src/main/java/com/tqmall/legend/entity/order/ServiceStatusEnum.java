package com.tqmall.legend.entity.order;

/**
 * 保养、保险等服务状态
 */
public enum ServiceStatusEnum {
    NOT_PROCESS(0, "通知未处理"),
    PROCESSED(1, "通知已处理"),
    EXPIRED(2, "通知已过期"),
    CANCEL(3, "通知已废弃");

    private final int code;
    private final String sName;

    private ServiceStatusEnum(int code, String sName) {
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
        for(ServiceStatusEnum e : ServiceStatusEnum.values()){
            if(e.getCode() ==code){
                return e.getsName();
            }
        }
        return null;
    }
}
