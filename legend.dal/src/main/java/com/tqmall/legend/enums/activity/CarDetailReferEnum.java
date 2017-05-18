package com.tqmall.legend.enums.activity;

/**
 * Created by zsy on 16/4/7.
 */
public enum CarDetailReferEnum {
    //客户模块
    CUSTOMER(1, "customer"),
    //维修工单
    ORDER(2, "order"),
    //维修配件
    GOODS(3, "goods"),
    //预约信息
    APPOINT(4, "appoint"),
    //回访信息
    FEEDBACK(5, "feedback"),
    //车况信息
    PRECHECK(6, "precheck");

    private final Integer code;
    private final String message;

    private CarDetailReferEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (CarDetailReferEnum e : CarDetailReferEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getMessage();
            }
        }
        return null;
    }

    public static Integer getCodeByMessage(String message) {
        for (CarDetailReferEnum e : CarDetailReferEnum.values()) {
            if (e.getMessage().equals(message)) {
                return e.getCode();
            }
        }
        return null;
    }

    /**
     * 获取枚举数组
     *
     * @return
     */
    public static CarDetailReferEnum[] getMessages() {
        CarDetailReferEnum[] carDetailFlagsEnums = CarDetailReferEnum.values();
        return carDetailFlagsEnums;
    }
}
