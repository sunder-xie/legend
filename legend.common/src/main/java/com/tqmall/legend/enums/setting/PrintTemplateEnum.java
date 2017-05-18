package com.tqmall.legend.enums.setting;

/**
 * Created by lilige on 16/11/7.
 */
public enum PrintTemplateEnum {
    //1-派工单 2-结算单 3-报价单 4-试车单
    ORDER(1,"派工单打印"),
    SETTLE(2,"结算单打印"),
    QUOTE(3,"报价单打印"),
    TRAILRUN(4,"试车单打印"),
    TICKET(5,"小票打印");

    private final Integer code;
    private final String name;

    PrintTemplateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode(){
        return code;
    }
    public String  getName(){
        return name;
    }

    public static String getNameByCode(int code){
        for (PrintTemplateEnum printTemplateEnum : PrintTemplateEnum.values()) {
            if(printTemplateEnum.getCode() == code){
                return printTemplateEnum.getName();
            }
        }
        return "";
    }
}
