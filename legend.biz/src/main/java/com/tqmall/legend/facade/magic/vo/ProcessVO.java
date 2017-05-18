package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

/**
 * Created by shulin on 16/7/1.
 */
@Data
public class ProcessVO {
    private Long id; //工序id
    private Long shopId;//所属门店
    private String name;//工序名称
    private String barCode;//工序条码
    private String workTime;//施工时间
    private Long processId;
    private Long processSort;
    private Long lineId;//生产线id
    private String processName;//工序名称
    private String lineName;//生产线名称
    private String isDeleted; //默认N
}
