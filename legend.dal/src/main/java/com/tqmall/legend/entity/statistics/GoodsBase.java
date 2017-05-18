package com.tqmall.legend.entity.statistics;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.goods.GoodsCar;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by cloudgu on 15/6/15.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsBase extends BaseEntity {

    private String goodsSn;
    private String tqmallGoodsSn;
    private Long tqmallGoodsId;
    private String name;
    private String measureUnit;
    private String origin;
    private String format;
    private Long catId;
    private Long brandId;
    private BigDecimal price;
    private BigDecimal stock;
    private Integer tqmallStatus;
    private Long shopId;
    private Integer goodsStatus;
    private BigDecimal inventoryPrice;
    private String imgUrl;
    private Long shortageNumber;
    private String depot;
    private String carInfo;
    private BigDecimal lastInPrice;
    private Date lastInTime;
    private String partUsedTo;
    private String relGoodsFormatList;
    private Integer goodsType;

    private String priceName;
    private String cityPrice;

    private String brandName;
    private Long cat1Id;
    private String cat1Name;
    private Long cat2Id;
    private String cat2Name;

    private String lastInTimeStr;
    private String lastInPriceStr;
    private Long goodsNum;
    private BigDecimal goodsAmount;
    private List<GoodsCar> carInfoList;
    private String carInfoStr;
    private Integer days; //积压天数

    public String getLastInTimeStr(){
        return DateUtil.convertDateToYMD(lastInTime);
    }

    public String getLastInPriceStr() {
        return CommonUtils.convertMoney(lastInPrice);
    }
    public String month;
    //
    private BigDecimal inventoryPriceActual;
    private BigDecimal stockActual;
    private BigDecimal amountActual;


}
