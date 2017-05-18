package com.tqmall.legend.entity.warehouseout;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseOutDetail extends BaseEntity {

    private Long shopId;//店铺id
    private Long warehouseOutId;//出库单编码
    private Long goodsId;//物料编码
    private Long supplierId;//供应商id
    private BigDecimal goodsCount;//物料实际出库数量
    private Long handByIn;//领料经手人
    private Long handByOn;//出库经手人
    private Long orderId;//工单id
    private String warehouseOutSn;//出库单编号
    private BigDecimal inventoryPrice;//结存价格
    private BigDecimal salePrice;//蓝字出库时售卖价格
    private BigDecimal goodsRealCount;//可退数量
    private String goodsSn;//商品编号
    private String goodsName;//商品名称
    private String goodsFormat;//商品型号
    private String carInfo;//适用车型，0为通用件，或者goods_car对象列表json字符串
    private String status;//状态
    private String relSn;//所关联的出库单sn
    private Long relId;//所关联的工单id
    private Long relDetailId;//所关联的出库单详情id
    private Long orderGoodsId;//所关联的工单物料id

    private String measureUnit;
    private BigDecimal stock;

    private String gmtCreateStr;
    private Integer goodsOrderCount;

    private Long goodsReceiver;//领料人
    private BigDecimal goodsAmount;//材料费
    private BigDecimal inventoryAmount;//材料成本

    public String getGmtCreateStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        if (gmtCreate != null) {
            return df.format(gmtCreate);
        } else {
            return null;
        }
    }

    private BigDecimal totalSalePriceAmount;//总销售金额
    private String depot;//货位

}


