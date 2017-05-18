package com.tqmall.legend.pojo.sell;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xiangdong.qu on 17/2/23 14:08.
 */
@Data
public class SellOrderSaveVO {
    private Integer shopLevel;
    private String mobile;
    private BigDecimal sellAmount;
}
