package com.tqmall.legend.service.activity;


import com.tqmall.core.common.entity.Result;

/**
 * Created by sven on 16/5/10.
 */
public interface RpcConsignmentService {
    /**
     * 根据门店Id获取协议
     *
     * @param ucShopId 门店Id
     * @return
     */
    Result<String> getAgreements(Long ucShopId, String source);

    /**
     * @param ucShopId 门店ID
     * @param quota    共建基金金额
     * @return
     */
    Result setFundQuota(Long ucShopId, Integer quota,String source);

}
