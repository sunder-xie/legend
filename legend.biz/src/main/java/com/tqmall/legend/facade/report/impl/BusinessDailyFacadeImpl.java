package com.tqmall.legend.facade.report.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcBusinessDailyService;
import com.tqmall.cube.shop.result.*;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.BusinessDailyFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

/**
 * Created by xin on 16/8/25.
 */
@Slf4j
@Service
public class BusinessDailyFacadeImpl implements BusinessDailyFacade {

    @Autowired
    private RpcBusinessDailyService rpcBusinessDailyService;

    /**
     * 查询门店营收信息
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public RevenueDTO getRevenueInfo(Long shopId, String dateStr) {
        Result<RevenueDTO> result = null;
        try {
            result = rpcBusinessDailyService.getRevenueInfo(shopId, dateStr);
        } catch (Exception e) {
            log.error("查询门店营收信息失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            throw new BizException("查询门店营收信息失败", e);
        }
        if (result == null) {
            throw new BizException("查询门店营收信息失败");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 查询门店采销信息
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public PurchaseSaleDTO getPurchaseSaleInfo(Long shopId, String dateStr) {
        Result<PurchaseSaleDTO> result = null;
        try {
            result = rpcBusinessDailyService.getPurchaseSaleInfo(shopId, dateStr);
        } catch (Exception e) {
            log.error("查询门店采销信息失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            throw new BizException("查询门店采销信息失败", e);
        }
        if (result == null) {
            throw new BizException("查询门店采销信息失败");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 查询门店卡券充值信息
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public AccountCardCouponDTO getAccountCardCouponInfo(Long shopId, String dateStr) {
        Result<AccountCardCouponDTO> result = null;
        try {
            result = rpcBusinessDailyService.getAccountCardCouponInfo(shopId, dateStr);
        } catch (Exception e) {
            log.error("查询门店卡券充值信息失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            throw new BizException("查询门店卡券充值信息失败", e);
        }
        if (result == null) {
            throw new BizException("查询门店卡券充值信息失败");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 查询门店访客信息
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public CustomerStatisticsDTO getCustomerStatistics(Long shopId, String dateStr) {
        Result<CustomerStatisticsDTO> result = null;
        try {
            result = rpcBusinessDailyService.getCustomerStatistics(shopId, dateStr);
        } catch (Exception e) {
            log.error("查询门店访客信息失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            throw new BizException("查询门店访客信息失败", e);
        }
        if (result == null) {
            throw new BizException("查询门店访客信息失败");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 查询门店业务收款
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public BusinessStatisticsDTO getBusinessStatistics(Long shopId, String dateStr) {
        Result<BusinessStatisticsDTO> result = null;
        try {
            result = rpcBusinessDailyService.getBusinessStatistics(shopId, dateStr);
        } catch (Exception e) {
            log.error("查询门店业务收款失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            throw new BizException("查询门店业务收款失败", e);
        }
        if (result == null) {
            throw new BizException("查询门店业务收款失败");
        }
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 查询门店工单
     *
     * @param shopId
     * @param dateStr
     * @return
     */
    @Override
    public DefaultPage<OrderInfoDTO> getOrderInfoList(Long shopId, String dateStr, Pageable pageable) {
        // 页码
        int pageNumber = pageable.getPageNumber();
        pageNumber = pageNumber < 1 ? 1 : pageNumber;
        // 每页显示数量
        int pageSize = pageable.getPageSize();
        pageSize = pageSize < 1 ? 10 : pageSize;

        PageRequest pageRequest = new PageRequest(pageNumber, pageSize);

        Result<DefaultResult<OrderInfoDTO>> result = null;
        try {
            result = rpcBusinessDailyService.getOrderInfoList(shopId, dateStr, pageNumber, pageSize);
        } catch (Exception e) {
            log.error("查询门店工单失败, 参数:shopId={}, dateStr={}, 异常信息:", shopId, dateStr, e);
            return new DefaultPage<>(Collections.<OrderInfoDTO>emptyList(), pageRequest, 0);
        }
        if (result == null || !result.isSuccess()) {
            return new DefaultPage<>(Collections.<OrderInfoDTO>emptyList(), pageRequest, 0);
        }

        DefaultResult<OrderInfoDTO> orderResult = result.getData();

        if (orderResult == null || orderResult.getTotal() == 0 || CollectionUtils.isEmpty(orderResult.getContent())) {
            return new DefaultPage<>(Collections.<OrderInfoDTO>emptyList(), pageRequest, 0);
        }
        return new DefaultPage<>(orderResult.getContent(), pageRequest, orderResult.getTotal());
    }
}
