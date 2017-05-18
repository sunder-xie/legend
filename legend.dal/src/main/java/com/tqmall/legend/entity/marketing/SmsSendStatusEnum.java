package com.tqmall.legend.entity.marketing;

public enum SmsSendStatusEnum {
    SEND_NO(Integer.valueOf(0), "未发送"),
    SEND_SUCCESS(Integer.valueOf(1), "发送成功"),
    SEND_FAILURE(Integer.valueOf(2), "发送失败");

    private final Integer code;
    private final String message;

    private SmsSendStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMesByCode(int code) {
        SmsSendStatusEnum[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            SmsSendStatusEnum iEnum = arr$[i$];
            if(iEnum.getCode().intValue() == code) {
                return iEnum.getMessage();
            }
        }

        return null;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
