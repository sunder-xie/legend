package com.tqmall.legend.object.param.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/4/9.
 */
@Data
@EqualsAndHashCode (callSuper = false)
public class PerformanceParam implements Serializable {
    private static final long serialVersionUID = -4864659287829346632L;
    private Long worker;
    private Long shopId;
    private String workerName;
    private Integer orderCount;
    private Integer carTimes;
    private String workerType;
    private BigDecimal orderAmount;
    private Date startTime;
    private Date endTime;
}
