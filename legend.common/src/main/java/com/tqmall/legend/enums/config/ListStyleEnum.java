package com.tqmall.legend.enums.config;

/**
 * Created by wushuai on 16/4/19.
 */
public enum ListStyleEnum {

    CARD("card","卡片风格"),
    TABLE("table","列表风格");

    private final String confValue;
    private final String name;

    public static boolean isExist(String confValue) {
        for (ListStyleEnum e : ListStyleEnum.values()) {
            if(e.getConfValue().equals(confValue)){
                return true;
            }
        }
        return false;
    }

    private ListStyleEnum(String confValue,String name) {
        this.confValue = confValue;
        this.name = name;
    }

    public String getConfValue() {
        return confValue;
    }

    public String getName() {
        return name;
    }
}
