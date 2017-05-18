package com.tqmall.legend.enums.setting;

/**
 * Created by zsy on 16/11/7.
 */
public enum OrderTypeEnum {
    BJ("钣金"),
    BY("保养"),
    BX("保险"),
    WX("维修"),
    SP("索赔");
    
    String name;
    
    OrderTypeEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
