package com.tqmall.legend.facade.onlinepay.enums;

/**
 * Created by sven on 2017/2/21.
 */
public enum OnlinePaySourceEnum {
    SELL(1, "淘汽云修系统服务销售", 3);
    private Integer code;
    private String message;
    private Integer payType;  //支付类型,与finance统一

    OnlinePaySourceEnum(Integer code, String message, Integer payType) {
        this.code = code;
        this.message = message;
        this.payType = payType;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPayType() {
        return payType;
    }

    public Integer getCode() {
        return code;
    }

    public static boolean isSell(Integer source) {
        if (SELL.getCode().equals(source)) {
            return true;
        }
        return false;
    }

}
