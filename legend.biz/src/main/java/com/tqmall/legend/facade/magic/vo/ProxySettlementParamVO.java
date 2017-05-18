package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

/**
 * Created by TonyStarkSir on 16/7/12.
 */
@Data
public class ProxySettlementParamVO {
    /**
     * 委托方门店id
     */
    private Long shopId;
    /**
     * 受托方门店id
     */
    private Long proxyShopId;
    /**
     * 账期
     */
    private String paymentMonth;
    /**
     * 账期开始时间
     */
    private String paymentMonthStart;
    /**
     * 账期结束时间
     */
    private String paymentMonthEnd;

    /**
     * 委托开始时间
     */
    private String proxyStartTime;
    /**
     * 委托结束时间
     */
    private String proxyEndTime;
    /**
     * 交车开始时间
     */
    private String completeStartTime;
    /**
     * 交车结束时间
     */
    private String completeEndTime;

}
