package com.tqmall.legend.object.result.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by macx on 2017/2/10.
 */
@Getter
@Setter
public class SearchPackageGoodsDTO implements Serializable{
    private static final long serialVersionUID = -6086755692217519038L;

    private String catName;//物料类型名称
    private String goodsSn;//商品编号
    private Long id;
    private String tqmallGoodsSn;//淘汽档口商品编号
    private Long tqmallGoodsId;//淘汽商品ID
    private String name;//商品名称
    private String measureUnit;//最小单位
    private String origin;//产地
    private String format;//型号
    private Long catId;//分类ID
    private Long brandId;//品牌ID
    private BigDecimal price;//销售价格
    private BigDecimal stock;//库存
    private Integer tqmallStatus;//商品资料来源
    private Long shopId;//店铺ID
    private Integer goodsStatus;//商品状态
    private BigDecimal inventoryPrice;//结存价格
    private String imgUrl;//商品图片地址
    private Long shortageNumber;//缺货临界数量
    private String depot;//仓位信息
    private String carInfo;//适用车型
    private BigDecimal lastInPrice;//最后一次入库价格
    private Date lastInTime;//最后一次入库时间
    private String partUsedTo;//适用部位
    private String relGoodsFormatList;//关联零件号
    private Integer goodsType;//物料类型
    private String priceName;//上下架状态
    private String brandName;//
    private String goodsCat;
    private String carInfoStr;
    private String lastInTimeStr;
    private Long goodsNum;

    public String getLastInTimeStr() {
        if (getLastInTime() == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(getLastInTime());
    }
}
