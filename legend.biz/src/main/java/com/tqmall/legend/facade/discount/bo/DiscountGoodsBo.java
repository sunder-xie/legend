package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 辉辉大侠
 * @Date:11:47 AM 02/03/2017
 */
@Data
public class DiscountGoodsBo {
    /**
     * 工单物料表中的物料实例id
     */
    private Long orderGoodsId;
    /**
     * 物料id
     */
    private Long goodsId;
    /**
     * 物料价格（去除折扣）
     */
    private BigDecimal amount;
    private BigDecimal goodsNumber;
    private Long goodsStdCatId;
    private Long goodsCustomCatId;

}
