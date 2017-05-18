package com.tqmall.legend.entity.base;

import lombok.Data;

import java.util.List;

/**
 * Created by zsy on 2015/7/17.
 */
@Data
public class CountOrderEntity {
    private Long shopId;
    private String orderStatus;   //工单状态
    private Integer[] payStatus;    //支付状态
    private Integer symbol;       //派工=>工单计数：1.待报价车辆 2.已报价车辆 3.修理中车辆 4.已完工车辆 5.已挂账车辆 6.已结算车辆 7.全部工单
                                  //结算=>工单计数：1.带结算工单 2.已挂账工单 3.已结算工单 4.全部工单
    private String startTime;//工单创建时间查询起始时间
    private String endTime;//工单创建时间查询结束时间
    private String payStartTime;//结算时间查询起始时间
    private String payEndTime;//结算时间查询结束时间
}
