package com.tqmall.legend.biz.marketing.ng;

import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 客户类型分析服务类
 * Created by wanghui on 2/25/16.
 */
public interface CustomerTypeAnalysisService {

    /**
     * 获取活跃列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getActiveCustomer(Long shopId,Pageable pageable);

    /**
     * 获取休眠客户列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getSleepCustomer(Long shopId,Pageable pageable);

    /**
     * 获取流失客户列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getLostCustomer(Long shopId,Pageable pageable);


    /**
     * 获取新客户列表
     * @return
     */
    Page<CustomerInfo> getNewCustomer(Map<String,Object> params,Pageable pageable);

    /**
     * 获取老客户列表
     * @return
     */
    Page<CustomerInfo> getOldCustomer(Map<String,Object> params,Pageable pageable);

}
