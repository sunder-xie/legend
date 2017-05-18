package com.tqmall.legend.object.result.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/4/9.
 */
@EqualsAndHashCode (callSuper = false)
@Data
public class PerformanceDTO  implements Serializable {
    private static final long serialVersionUID = -8513499606980879430L;
    private Long worker;
    private Long shopId;
    private String workerName;
    private Integer orderCount;
    private Integer carTimes;
    private String workerType;
    @Deprecated
    private BigDecimal orderAmount;
    private Date startTime;
    private Date endTime;
}
