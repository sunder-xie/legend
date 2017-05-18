package com.tqmall.legend.entity.feedback;

import java.util.ArrayList;
import java.util.List;

/**
 * 档口版模块枚举
 * Created by lilige on 16/7/18.
 */
public enum TqmallFeedbackModuleEnum {
    SHOUYE("首页"),
    JCWX("接车维修"),
    CANGKU("仓库"),
    CAIWU("财务"),
    KHYX("客户管理"),
    OTHER("其他");

    private final String name;

    TqmallFeedbackModuleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static List<String> getModuleNames() {
        TqmallFeedbackModuleEnum[] modules = values();
        List<String> names = new ArrayList<>();
        for (TqmallFeedbackModuleEnum module : modules){
            names.add(module.getName());
        }
        return names;
    }
}
