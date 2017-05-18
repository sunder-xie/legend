package com.tqmall.legend.enums.warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * 付款方式
 * Created by sven on 16/8/5.
 */
public enum PayMethodEnum {
    DAILY(0, "日结"),
    MONTH(1, "月结"),
    QUARTER(2, "季结");
    private Integer code;
    private String message;

    PayMethodEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMessageByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayMethodEnum payMethodEnum : values()) {
            if (code.equals(payMethodEnum.getCode())) {
                return payMethodEnum.getMessage();
            }
        }
        return null;
    }
    public static Integer getCodeByMessage(String message){
        if (message == null || message.trim() == "") {
            return null;
        }
        for (PayMethodEnum payMethodEnum : values()) {
            if (message.equals(payMethodEnum.getMessage())) {
                return payMethodEnum.getCode();
            }
        }
        return null;
    }
    public static List<PayMethodEnum> getPaymethod(){
        List<PayMethodEnum> payMethodEnumList = new ArrayList<>();
        for(PayMethodEnum payMethodEnum : values()){
            payMethodEnumList.add(payMethodEnum);
        }
        return payMethodEnumList;
    }

}
