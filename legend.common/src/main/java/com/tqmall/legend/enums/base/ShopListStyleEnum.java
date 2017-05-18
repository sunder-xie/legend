package com.tqmall.legend.enums.base;

/**
 * Created by wushuai on 16/4/6.
 */
public enum ShopListStyleEnum {

    CARD("card", "卡片风格"),
    TABLE("table", "列表风格");

    private final String style;
    private final String styleName;


    private ShopListStyleEnum(String style, String styleName) {
        this.style = style;
        this.styleName = styleName;
    }


    public String getStyle() {
        return style;
    }

    public String getStyleName() {
        return styleName;
    }
}
