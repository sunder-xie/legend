package com.tqmall.legend.pojo.sell;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xiangdong.qu on 17/2/23 14:17.
 */
@Data
public class SellShopTypeVO {
    private Integer shopLevel;  //版本level
    private String name;        //版本名称 (基础版、标准版、专业版)
    private BigDecimal originalPrice; //原价
    private BigDecimal price;   //折扣后价格
    private String effectiveStr; //有效期

    private Boolean isShowDiscountPrice = Boolean.TRUE; //是否展示折扣价
}
