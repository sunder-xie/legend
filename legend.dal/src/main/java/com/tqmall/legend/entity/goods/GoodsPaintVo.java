package com.tqmall.legend.entity.goods;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by dingbao on 16/11/9.
 */
@Data
public class GoodsPaintVo {

    private Long id;

    private String goodsSn;//配件编号

    private String name;//商品名称

    private String relGoodsFormatList;//关联零件号

    private String depot;//仓库位

    private String carInfoStr;//适配车型

    private BigDecimal stock;//库存

    private BigDecimal inventoryPrice;//成本

    private BigDecimal noBucketWeight;//非整桶总重量(含桶和搅拌头)

    private BigDecimal noBucketNum;//非整桶数量

    private BigDecimal stirNum;//搅拌头数量

    private BigDecimal totalStockAmount;//库存总成本

    private BigDecimal netWeight;//净重

    private BigDecimal bucketWeight;//带桶重量=净重+桶重（包含桶盖重量）

    private BigDecimal stirWeight;//'带桶和搅拌头的重量

    private Long shopId;//门店id

    private String format;//零件号

    private String measureUnit;//配件单位

    private Integer onsaleStatus; //上下架状态

    private String onsaleStatusStr;

}
