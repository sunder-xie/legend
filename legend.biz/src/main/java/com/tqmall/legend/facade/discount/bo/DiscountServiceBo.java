package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 辉辉大侠
 * @Date:11:28 AM 02/03/2017
 */
@Data
public class DiscountServiceBo {
    /**
     * 服务id
     */
    private Long serviceId;
    /**
     * 服务在工单服务表中的实例id
     */
    private Long orderServiceId;
    /**
     * 服务工时
     */
    private BigDecimal serviceHour;
    /**
     * 服务总金额(减去优惠后)
     */
    private BigDecimal amount;
    /**
     * 服务分类id
     */
    private Long serviceCatId;
}
