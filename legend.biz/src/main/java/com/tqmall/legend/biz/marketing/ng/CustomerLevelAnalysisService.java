package com.tqmall.legend.biz.marketing.ng;

import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 客户级别分析服务类
 * Created by wanghui on 2/25/16.
 */
public interface CustomerLevelAnalysisService {

    /**
     * 获取客户列表
     * @return
     */
    Page<CustomerInfo> getCustomerWithTime(Map<String,Object> params,Pageable pageable);

}
