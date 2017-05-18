package com.tqmall.legend.enums.sell;

import java.math.BigDecimal;

/**
 * Created by feilong.li on 17/2/23.
 */
public enum ShopSellEnum {
    BASE(10, "基础版", new BigDecimal(1960)),
    STANDARD(11, "标准版", new BigDecimal(5960)),
    PROFESSION(12, "专业版", new BigDecimal(11960));


    private Integer shopLevel;
    private String name;
    private BigDecimal price;

    private ShopSellEnum(Integer shopLevel, String name, BigDecimal price) {
        this.shopLevel = shopLevel;
        this.name = name;
        this.price = price;
    }

    public Integer getShopLevel() {
        return shopLevel;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static BigDecimal getPriceByShopLevel(Integer shopLevel) {
        if (shopLevel == null) {
            return null;
        }
        ShopSellEnum[] ShopSellEnums = ShopSellEnum.values();
        for (ShopSellEnum shopSellEnum : ShopSellEnums) {
            if (shopSellEnum.getShopLevel().equals(shopLevel)) {
                return shopSellEnum.getPrice();
            }
        }
        return null;
    }

    public static String getNameByShopLevel(Integer shopLevel) {
        if (shopLevel == null) {
            return null;
        }
        for (ShopSellEnum shopSellEnum : ShopSellEnum.values()) {
            if (shopSellEnum.getShopLevel().equals(shopLevel)) {
                return shopSellEnum.getName();
            }
        }
        return null;
    }

    public static ShopSellEnum getShopSellEnumByShopLevel(Integer shopLevel) {
        if (shopLevel == null) {
            return null;
        }
        ShopSellEnum[] ShopSellEnums = ShopSellEnum.values();
        for (ShopSellEnum shopSellEnum : ShopSellEnums) {
            if (shopSellEnum.getShopLevel().equals(shopLevel)) {
                return shopSellEnum;
            }
        }
        return null;
    }
}
