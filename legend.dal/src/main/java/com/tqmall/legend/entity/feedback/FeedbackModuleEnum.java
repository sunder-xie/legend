package com.tqmall.legend.entity.feedback;

import com.tqmall.legend.entity.settlement.ConsumeTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块枚举
 * Created by lilige on 16/7/18.
 */
public enum FeedbackModuleEnum {
    SHOUYE("首页"),
    JCWX("接车维修"),
    CANGKU("仓库"),
    CAIWU("财务"),
    TQCAIGOU("淘汽采购"),
    KHYX("客户营销"),
    YLHD("引流活动"),
    OTHER("其他");

    private final String name;

    FeedbackModuleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static List<String> getModuleNames() {
        FeedbackModuleEnum[] modules = values();
        List<String> names = new ArrayList<>();
        for (FeedbackModuleEnum module : modules){
            names.add(module.getName());
        }
        return names;
    }
}
