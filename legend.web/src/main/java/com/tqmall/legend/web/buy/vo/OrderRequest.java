package com.tqmall.legend.web.buy.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by mokala on 10/28/15.
 */
@Data
public class OrderRequest {
    /**
     * 订单备注信息
     */
    private String postScript;
    /**
     * 配送地址
     */
    private Integer addressId;
    /**
     * 配送城市地址
     */
    private Integer cityId;
    /**
     * 提交订单时的商品参数
     */
    private List<GoodsParam> goodsParams;
}
