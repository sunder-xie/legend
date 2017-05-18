package com.tqmall.legend.facade.report;

import com.tqmall.cube.shop.result.*;
import com.tqmall.legend.biz.common.DefaultPage;
import org.springframework.data.domain.Pageable;

/**
 * Created by xin on 16/8/25.
 */
public interface BusinessDailyFacade {

    /**
     * 查询门店营收信息
     * @param shopId
     * @param dateStr
     * @return
     */
    RevenueDTO getRevenueInfo(Long shopId, String dateStr);

    /**
     * 查询门店采销信息
     * @param shopId
     * @param dateStr
     * @return
     */
    PurchaseSaleDTO getPurchaseSaleInfo(Long shopId, String dateStr);

    /**
     * 查询门店卡券充值信息
     * @param shopId
     * @param dateStr
     * @return
     */
    AccountCardCouponDTO getAccountCardCouponInfo(Long shopId, String dateStr);

    /**
     * 查询门店访客信息
     * @param shopId
     * @param dateStr
     * @return
     */
    CustomerStatisticsDTO getCustomerStatistics(Long shopId, String dateStr);

    /**
     * 查询门店业务收款
     * @param shopId
     * @param dateStr
     * @return
     */
    BusinessStatisticsDTO getBusinessStatistics(Long shopId, String dateStr);

    /**
     * 查询门店工单
     * @param shopId
     * @param dateStr
     * @return
     */
    DefaultPage<OrderInfoDTO> getOrderInfoList(Long shopId, String dateStr, Pageable pageable);
}
