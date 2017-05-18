package com.tqmall.legend.entity.warehousein;


import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseInDetail extends BaseEntity {

    private Long shopId;//店铺id
    private String warehouseInSn;//入库单sn
    private Long warehouseInId;//入库单id
    private Long goodsId;//物料id
    private String goodsSn;//物料编号
    private String goodsName;//物料名称
    private String goodsFormat;//物料规格型号
    private BigDecimal goodsCount;//入库数量
    private BigDecimal goodsDamage;//损坏数量
    private BigDecimal purchasePrice;//入库单价
    private BigDecimal purchaseAmount;//物料入库总价
    private BigDecimal goodsRealCount;//实际入库数量(入库数量-退库数量)\n
    private String tqmallGoodsSn;//淘汽档口goods_sn
    private Long tqmallGoodsId;//淘汽档口goodsid
    private String measureUnit;//单位
    private String depot;//仓位信息
    private String relSn;//关联sn,淘汽入库,填淘汽采购单号,其它入库不填,退库,作废填相关的入库单号
    private String status;//入库状态:lzrk:蓝字入库;hzrk:红字入库;lzzf:蓝字作废;hzzf:红字作废;
    private String carInfo;//物料车型适配信息
    private BigDecimal stockCount;
    private Long catId;

}



