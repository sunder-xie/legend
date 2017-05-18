package com.tqmall.legend.object.result.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zsy on 16/11/23.
 */
@Getter
@Setter
public class GoodsDTO implements Serializable {
    private static final long serialVersionUID = -8477405802215389665L;

    private Long id;//配件id
    private String goodsSn;//商品编号
    private String tqmallGoodsSn;//淘汽档口商品编号
    private Long tqmallGoodsId;//淘汽商品id
    private String name;//商品名称
    private String measureUnit;//最小单位
    private String origin;//产地
    private String format;//型号
    private Long catId;//分类id
    private Long brandId;//品牌id
    private BigDecimal price;//销售价格
    private BigDecimal stock;//库存
    private Integer tqmallStatus;//物料/商品资料(信息)来源:0.缺省;1.来自tqmall;2.新增;3.来自tqmall修改4.自定义配件
    private Long shopId;//店铺id
    private Integer goodsStatus;//'0
    private BigDecimal inventoryPrice;//结存价格
    private String imgUrl;//商品图片地址
    private Long shortageNumber;//缺货临界数量
    private String depot;//仓位信息
    private String carInfo;//适用车型，0为通用件，或者goods_car对象列表json字符串
    private BigDecimal lastInPrice;//最后一次入库价格：上次采购价
    private Date lastInTime;//最后一次入库时间：入库时间
    private String partUsedTo;//适用部位
    private String relGoodsFormatList;//关联零件号
    private Integer goodsType;//物料类型，0为实际物料，1为虚开物料
    private String note;//备注，用于处理excel导入不同门店的特有但却重要的数据
    private String goodsBrand;//物料品牌
    private String goodsCat;//物料分类
    private Long stdCatId;//标准物料分类id
    private Integer onsaleStatus;//上架状态，1为上架，0为下架
    private Integer goodsTag;//配件打标

    //扩展字段：
    private BigDecimal totalAmount;//库存总成本
    private String carInfoStr;//适配车型

    private Long goodsNum;//当前配件数量
    private BigDecimal goodsAmount;//当前配件总金额  goodsAmount = price*goodsNum;

    /**
     * 库存总成本
     * @return
     */
    public BigDecimal getTotalAmount(){
        BigDecimal totalAmount;
        if(stock != null && inventoryPrice != null){
            totalAmount = stock.multiply(inventoryPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            totalAmount = BigDecimal.ZERO;
        }
        return totalAmount;
    }
}

