package com.tqmall.legend.enums.customer;

/**
 * Created by xin on 16/9/26.
 */
public enum  CustomerFeedbackNoteTypeEnum {
    APPOINT(0, "预约单提醒"),
    FIRST_MAINTAIN(1, "保养提醒"),
    INSURANCE(2, "保险提醒"),
    AUDITING(3, "年检提醒"),
    VISIT(4, "回访提醒"),
    BIRTHDAY(5, "生日提醒"),
    LOST_CUSTOMER(6, "流失客户提醒"),
    NEXT_VISIT(7, "回访提醒"),
    SECOND_MAINTAIN(8, "保养提醒");

    private Integer code;
    private String message;

    CustomerFeedbackNoteTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getMesByCode(Integer code){
        CustomerFeedbackNoteTypeEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            CustomerFeedbackNoteTypeEnum iEnum = arr[i];
            if(iEnum.getCode().equals(code)){
                return iEnum.getMessage();
            }
        }
        return null;
    }
}
