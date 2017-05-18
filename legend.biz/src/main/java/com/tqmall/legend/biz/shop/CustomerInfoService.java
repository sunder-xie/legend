package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.marketing.ng.CustomerInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/3/22.
 */
public interface CustomerInfoService {
    /**
     * 客户列表
     * @param param
     * @return
     */
    List<CustomerInfo> getCustomerInfoList(Map param);

    /**
     * 获取客户信息
     */
    CustomerInfo getCustomerInfo(Long shopId,Long customerCarId);

    List<CustomerInfo> selectCustomerInfoIsNote(Map param);

    List<CustomerInfo> listByCarIds(Long shopId, Collection<Long> carIds);
}
