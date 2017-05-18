package com.tqmall.legend.enums.activity;

/**
 * Created by lilige on 16/3/4.
 */
public enum BankEnum {
//    工商银行、农业银行、中国银行、建设银行、招商银行
    GONGSHANG(1,"工商银行"),
    NONGYE(2,"农业银行"),
    ZHONGGUO(3,"中国银行"),
    JIANSHE(4,"建设银行"),
    ZHAOSHANG(5,"招商银行");

    private final Integer code;
    private final String message;

    private BankEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static String getMessageByCode(Integer code) {
        for (BankEnum e : BankEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getMessage();
            }
        }
        return null;
    }

    public static BankEnum[] getMessages() {
        BankEnum[] arr = values();
        return arr;
    }
}
