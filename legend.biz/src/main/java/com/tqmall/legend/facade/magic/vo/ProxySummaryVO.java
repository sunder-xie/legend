package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by TonyStarkSir on 16/7/21.
 */
@Data
public class ProxySummaryVO{
    /**
     * 时间区间
     */
    private String dateTime;
    /**
     * 应付（收）金额
     */
    private BigDecimal realAmount;
    /**
     * 门店名称
     */
    private String shopName;
    /**
     *交车总数量
     */
    private Integer completeCount;
    /**
     *接单总数量
     */
    private Integer orderTakeCount;
    /**
     *交车已经结算的数量
     */
    private Integer balanceProxyCount;
    /**
     * 交车未结算数量
     */
    private Integer completeNoBalanceCount;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 接单未交车
     */
    private Integer orderTakeNoCompleteCount;
    /**
     * 未接单(拒单)
     */
    private Integer orderRefusedCount;
    /**
     * 取消的委托单数量
     */
    private Integer cancelProxyCount;

}
