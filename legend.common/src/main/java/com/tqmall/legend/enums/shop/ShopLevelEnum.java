package com.tqmall.legend.enums.shop;

/**
 * Created by wushuai on 2016/08/10.
 * <p>门店版本,对应数据库字段:legend_shop.level </p>
 * <p>ShopSellEnum中也有售卖版本的枚举信息,修改BASE、STANDARD、PROFESSION同步修改ShopSellEnum! </p>
 */
public enum ShopLevelEnum {
    TQMALL(6,"档口版"),
    YUNXIU(9,"云修版"),
    BASE(10,"基础版"),
    STANDARD(11,"标准版"),
    PROFESSION(12,"专业版");

    private Integer value;
    private String name;

    private ShopLevelEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    //获值名称
    public static ShopLevelEnum getLeveEnum(Integer value) {
        if (value == null) {
            return null;
        }
        ShopLevelEnum[] shopLevelEna = ShopLevelEnum.values();
        for (ShopLevelEnum shopLevelEnum : shopLevelEna) {
            if (shopLevelEnum.getValue().equals(value)) {
                return shopLevelEnum;
            }
        }
        return null;
    }

}
