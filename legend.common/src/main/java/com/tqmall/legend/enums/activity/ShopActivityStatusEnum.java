package com.tqmall.legend.enums.activity;

/**
 * Created by wushuai on 16/8/10.
 * 对应legend.legend_shop_activity.act_status字段
 */
public enum ShopActivityStatusEnum {
    NOJOIN(-1, "未参加"),//这样的值不存在数据库中
    COLOSED(0,"关闭"),
    OUTLINE(1,"草稿"),
    PUBLISH(2,"发布");

    private final Integer value;
    private final String name;

    private ShopActivityStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
