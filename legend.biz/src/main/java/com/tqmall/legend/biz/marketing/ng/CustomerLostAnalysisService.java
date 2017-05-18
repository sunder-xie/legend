package com.tqmall.legend.biz.marketing.ng;

import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * 客户流失分析服务类
 * Created by wanghui on 2/25/16.
 */
public interface CustomerLostAnalysisService {
    /**
     * 高质量客户价值
     */
    BigDecimal CUSTOMER_HIGHT_VALUE = BigDecimal.valueOf(2500);
    /**
     * 中等质量客户价值
     */
    BigDecimal CUSTOMER_MIDDLE_VALUE = BigDecimal.valueOf(1500);
    /**
     * 低等质量客户价值
     */
    BigDecimal CUSTOMER_LOW_VALUE = BigDecimal.valueOf(500);


    /**
     * 获取流失的高质量客户列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getLostHighCustomer(Long shopId,Pageable pageable);

    /**
     * 获取流失的中质量客户列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getLostMiddleCustomer(Long shopId,Pageable pageable);

    /**
     * 获取流失的低质量客户列表
     * @param shopId
     * @return
     */
    Page<CustomerInfo> getLostLowCustomer(Long shopId,Pageable pageable);

}

