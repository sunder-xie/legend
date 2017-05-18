package com.tqmall.legend.object.result.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dingbao on 16/3/16.
 */
@Data
@EqualsAndHashCode
public class OrderServicesDTO implements Serializable {

    private static final long serialVersionUID = 4479419005776503612L;
    private Long id;
    private Long shopId;
    private Long orderId;
    private Long serviceId;
    private BigDecimal soldPrice;
    private BigDecimal serviceHour;
    private String orderSn;
    private BigDecimal servicePrice;
    private BigDecimal serviceAmount;
    private BigDecimal discount;
    private Integer type;
    private BigDecimal soldAmount;
    private String serviceName;
    private String serviceSn;
    private Long serviceCatId;
    private String serviceCatName;
    private String flags;
    private String serviceNote;
    private String workerIds;  //维修工列表
    // 服务父ID，用于子服务指向服务套餐
    private Long parentServiceId;
    private String flagsLike;
    //管理费比率
    private BigDecimal manageRate;
}
