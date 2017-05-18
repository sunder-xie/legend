package com.tqmall.legend.facade.onlinepay.enums;

/**
 * Created by sven on 2017/2/21.
 */
public enum OnlinePayMethodEnum {
    LIANLIANPAY(1, "/onlinepay/lianlian/verify/", "连连支付"),
    ALIPAY(2, "/onlinepay/alipay/verify/", "支付宝");
    private Integer code;
    private String message;
    private String url;

    OnlinePayMethodEnum(Integer code, String url, String message) {
        this.code = code;
        this.url = url;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public static String getReturnUrl(Integer code) {
        for (OnlinePayMethodEnum onlinePayMethodEnum : OnlinePayMethodEnum.values()) {
            if (onlinePayMethodEnum.getCode().equals(code)) {
                return onlinePayMethodEnum.getUrl();
            }
        }
        return null;
    }

    public static boolean isLianlianPay(Integer code) {
        if (OnlinePayMethodEnum.LIANLIANPAY.getCode().equals(code)) {
            return true;
        }
        return false;
    }

    public static boolean isAliPay(Integer code) {
        if (OnlinePayMethodEnum.ALIPAY.getCode().equals(code)) {
            return true;
        }
        return false;
    }
}
