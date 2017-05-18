package com.tqmall.legend.entity.statistics.param;

import lombok.Data;

/**
 * Created by tanghao on 16/9/8.
 */
@Data
public class WarehouseOutReportParam extends PageParam {
    private String startTime;//开始日期
    private String endTime;//结束日期
    private String goodsName;//配件名称
    private String goodsFormat;//零件号
    private String warehouseOutSn;//出库单号
    private String orderSn;//工单编号
    private String customerName;//车主
    private Long goodsReceiver;//领料人
    private String status;//单据类型
    private Long shopId;//店铺id
}
