package com.tqmall.legend.enums.insurance;

/**
 * Created by lilige on 16/8/1.
 */
public enum TianServiceEnum {
    SERVICE_A(40989l,"A"),
    SERVICE_B(40990l,"B");
    private final Long serviceId;
    private final String serviceName;

    private TianServiceEnum(Long serviceId , String serviceName){
        this.serviceId = serviceId;
        this.serviceName = serviceName;
    }

    public Long getServiceId(){
        return serviceId;
    }

    public String getServiceName(){
        return serviceName;
    }

    public static Long getIdByName(String serviceName) {
        for (TianServiceEnum e : TianServiceEnum.values()) {
            if (e.getServiceName().equals(serviceName)) {
                return e.getServiceId();
            }
        }
        return null;
    }
}
