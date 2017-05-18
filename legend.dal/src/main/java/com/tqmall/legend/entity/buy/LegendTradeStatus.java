package com.tqmall.legend.entity.buy;

import lombok.Data;

/**
 * Created by lixiao on 14-12-1.
 */
@Data
public class LegendTradeStatus {

    private String key;
    private String name;
    private Integer count;

    public LegendTradeStatus(String key ,String name, Integer count) {
        this.key = key;
        this.name = name;
        this.count = count;
    }

    public LegendTradeStatus() {
    }

}
