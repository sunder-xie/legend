package com.tqmall.legend.entity.goods;

import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 配件查询实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsQueryParam implements Serializable {
    private static final long serialVersionUID = -3426481152795701854L;


    // 门店ID
    private Long shopId;
    // 配件型号
    private String format;
    // 淘汽商品ID
    private Long tqmallGoodsId;
    // 记录状态 {'N':'未删除的' ;'Y':已删除的}
    private DBStatusEnum dbStatusEnum;
    // 配件类别 {'1':'油漆' ;'Y':普通}
    private GoodsTagEnum goodsTagEnum;
    // 配件上下架状态 {'1':上架;'':下架}
    private GoodsOnsaleEnum onsaleEnum;
    // 库存状态 {'1':非0库存;'0':0库存}
    private ZeroStockRangeEnum zeroStockRangeEnum;
    // 排序字段
    List<String> sorts;
    // 配件名称(模糊)
    private String goodsNameLike;
    // 配件型号(模糊)
    private String goodsFormatLike;
    // 库位(模糊)
    private String depotLike;
    // 品牌ID
    private String brandId;
    // 配件编号(模糊)
    private String goodsSnLike;
    // 查询记录起始位置
    private String offset;
    // 查询数据长度
    private String limit;
    // 分类ID
    private Long[] catIds;
    // 车型
    private String carInfoLike;
}
