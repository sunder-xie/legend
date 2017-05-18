package com.tqmall.legend.entity.buy;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/7/17.
 * 商品详情
 */
@Data
public class GoodsExtendDetailForWebVO {
    private Integer goodsId;

    private String goodsName;

    private Integer groupId;

    private Integer actId;

    private String actName;

    private String groupName;

    private Integer goodsNumber;

    /**
     * 2015-04-28   是否缺货
     */
    private String goodsNumberTag;

    private Integer salesVolume;

    private Integer warehouseId;

    private String originalUnit;

    private String actUnit;

    private Short transformValue;

    private String hdPriceDescription;

    private Integer actGoodsNumMax;

    private Integer actGoodsNumMin;

    private Byte isOnSale;

    private BigDecimal price;

    private BigDecimal listPrice;

    private String priceName;

    private String listPriceName;

    private Integer catId;

    private String catName;

    private Integer pCatId;

    private String pCatName;

    private Short brandId;

    //用户选定车型与该商品是否匹配
    private boolean isMatch;
    //是否为原厂商品
    private boolean isOriginal;

    //适用于web端显示，在详情页显示的属性
    private List<GoodsSpecificationVO> showAttrs;

    private Integer sellerId;

    private String sellerNick;

    private List<Map<String, String>> supportServiceList;

    private boolean legendCity;

    private boolean legendUser;

    private String limitInfo;

    public String getPrice() {
        return null == price ? "0.00" : String.format("%.2f", price);
    }

    public String getListPrice() {
        return null == listPrice ? "0.00" : String.format("%.2f", listPrice);
    }



}
