package com.tqmall.legend.entity.statistics.param;

import lombok.Data;

/**
 * Created by tanghao on 16/9/9.
 */
@Data
public class WarehouseInReportParam extends PageParam{
    private String startTime;//开始日期
    private String endTime;//结束日期
    private String goodsName;//配件名称
    private String goodsFormat;//零件号
    private Long supplierId;//供货商
    private Long purchaseAgent;//领料人
    private String status;//单据类型
    private Long shopId;//店铺id
    private String warehouseInSn;
}
