package com.tqmall.legend.enums.appoint;

/**
 * Created by zsy on 16/10/22.
 */
public enum AppointPayStatusEnum {

    /**
     * 预约单状态枚举类
     */
    SUCCESS(1,"支付成功"),
    FAIL(0,"支付失败");


    // 成员变量
    private final Integer payStatus;

    private final String payName;


    AppointPayStatusEnum(Integer payStatus,String payName) {
        this.payStatus = payStatus;
        this.payName = payName;
    }

    //获取状态名称
    public static String getPayNameByPayStatus(Integer payStatus) {
        if (payStatus == null) {
            return "";
        }
        AppointPayStatusEnum[] statusEnums = AppointPayStatusEnum.values();
        for (AppointPayStatusEnum statusEnum : statusEnums) {
            if (statusEnum.getPayStatus().equals(payStatus)) {
                return statusEnum.getPayName();
            }
        }
        return "";
    }

    public String getPayName() {
        return payName;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

}
