package com.tqmall.legend.object.param.order.speedily;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by macx on 2017/2/9.
 */
@Getter
@Setter
public class ServiceParam implements Serializable {

    private static final long serialVersionUID = -7080214613409424656L;

    private Long serviceId;
    private Integer type;
    private BigDecimal discount;
    private String serviceCatName;
    private Long serviceCatId;
    private BigDecimal serviceHour;
    private BigDecimal serviceAmount;
    private BigDecimal servicePrice;
    private BigDecimal soldPrice;
    private BigDecimal soldAmount;
    private String serviceName;
    private Long workerId;
    private String workerIds; //维修工人列表，以逗号，隔开
    private String serviceSn;
}
