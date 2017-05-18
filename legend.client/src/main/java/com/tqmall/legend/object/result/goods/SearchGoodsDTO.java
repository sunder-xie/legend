package com.tqmall.legend.object.result.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by zsy on 16/11/24.
 * 配件搜索查询接口
 * 注：依赖搜索返回的对象，由于字段类型转换，搜索返回的数据和目前goods表的字段不统一，
 * 所以字段属性的取值也沿用了搜索的对象属性
 */
@Getter
@Setter
public class SearchGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1381752661114342128L;
    private String catName;//    分类名称
    private String id;// “商品ID”,
    private String goodsSn;// “商品编号",
    private String tqmallGoodsSn;// “淘汽档口商品编号”,
    private String tqmallGoodsId;// “淘汽商品ID”,
    private String name;// “商品名称",
    private String measureUnit;// “最小单位",
    private String origin;// “产地”,
    private String format;// “型号",
    private String catId;// “分类ID”,
    private String brandId;// “品牌ID”,
    private Double price;// “销售价格”,
    private Double stock;// “库存”,
    private Integer tqmallStatus;// “商品资料来源”,
    private String shopId;// “店铺ID”,
    private String goodsStatus;// “商品状态”,
    private Double inventoryPrice;// “结存价格”,
    private String imgUrl;// “商品图片地址”,
    private Integer shortageNumber;// “缺货临界数量”,
    private String depot;// “仓位信息",
    private String carInfo;// “适用车型",
    private Double lastInPrice;// “最后一次入库价格”,
    private String lastInTime;// “最后一次入库时间”,
    private String partUsedTo;// “适用部位”,
    private String relGoodsFormatList;// “关联零件号”,
    private Integer goodsType;// “物料类型”
    private Integer onsaleStatus;// “上下架状态”
    private String goodsBrand;//物料品牌
    private String goodsCat;//物料分类
    private String brandName;
    private String carInfoStr;
    private String lastInTimeStr;

    public String getLastInTimeStr() {
        if (getLastInTime() == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(getLastInTime());
    }
}
