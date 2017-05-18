package com.tqmall.legend.object.result.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangdong.qu on 17/2/10 11:08.
 */
@Data
public class WashCarOrderStatsDTO implements Serializable {
    private Integer statsCount; //统计数量
    private String statsAmount; //统计金额
}
