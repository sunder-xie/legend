package com.tqmall.legend.entity.goods;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class Goods extends BaseEntity {

    public String catName;//物料类型名称
    private String goodsSn;
    private String tqmallGoodsSn;
    private Long tqmallGoodsId;
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "*配件名称")
    @NotBlank(message = "配件名称不能为空")
    @Length(min = 1,max = 200,message = "配件名称长度不能超过200")
    private String name;
    @ExcelCol(value = 3,defaultValue = "个",title = "单位(默认为个)")
    @Length(max = 50,message = "单位长度不能超过50")
    private String measureUnit;
    private String origin;
    @ExcelCol(value = 1,title = "*零件号")
    @NotBlank(message = "零件号不能为空")
    @Length(min = 1,max = 255,message = "零件号长度不能超过255")
    private String format;
    private Long catId;
    private Long brandId;
    @ExcelCol(value = 5,title = "销售价格")
    @DecimalMin(value = "0.00",message = "销售价格必须大于或等于0.00")
    private BigDecimal price;
    @ExcelCol(value = 6,title = "库存")
    @DecimalMin(value = "0.00",message = "库存必须大于或等于0.00")
    private BigDecimal stock;
    private Integer tqmallStatus;
    private Long shopId;
    private Integer goodsStatus;
    @ExcelCol(value = 7,title = "成本单价")
    @DecimalMin(value = "0.00",message = "成本单价必须大于或等于0.00")
    private BigDecimal inventoryPrice;
    private String imgUrl;
    @ExcelCol(value = 8,title = "缺货临界数量")
    @Min(value = 0,message = "缺货临界数量不能为负数")
    private Long shortageNumber;
    @ExcelCol(value = 9,title = "仓位信息")
    @Length(min = 0,max = 50,message = "仓位信息长度不能超过50")
    private String depot;
    @ExcelCol(value = 10,title = "适用车型")
    private String carInfo;
    private BigDecimal lastInPrice;
    private Date lastInTime;
    @ExcelCol(value = 11,title = "适用部位")
    @Length(min = 0,max = 100,message = "适用部位长度不能超过100")
    private String partUsedTo;
    private String relGoodsFormatList;
    private Integer goodsType;
    private String priceName;
    private String cityPrice;
    @ExcelCol(value = 4,title = "品牌")
    @Length(max = 60,message = "品牌长度不能超过60")
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
    private Integer goodsTag;//配件打标，1：油漆。默认是0
    /**
     * 标准分类id
     */
    @ExcelCol(value = 2,defaultValue = "通用配件",title = "类别(默认为通用配件)")
    @Length(max = 90,message = "类别长度不能超过90")
    private String goodsCat;
    private Integer stdCatId;
    private Integer onsaleStatus; //上下架状态

    private String onsaleStatusStr;

    // 用于首页-库存预警展示 [[
    private String stockForShow;
    private String shortageNumberForShow;
    private String carModels;
    // ]]

    // 云修采购价
    private String tqmallPrice;
    // 前3月建议均销量
    private BigDecimal averageNumber;
    // 建议库存
    private BigDecimal suggestGoodsNumber;

    public boolean isCustomCat(){
        return stdCatId == null;
    }
    public String getCarInfoStr() {
        String  jsonInfo= getCarInfo();
       return GoodsUtils.carInfoTranslate(jsonInfo);
    }
    public Object getFinalCatId(){
        if (stdCatId != null && catId != null) {
            if (stdCatId == 0) {
                return catId;
            } else if (catId == 0) {
                return stdCatId;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
//        return stdCatId == null? catId:stdCatId;
    }
    public String getLastInTimeStr(){
        return DateUtil.convertDateToYMD(lastInTime);
    }

    public String getOnsaleStatusStr(){
        if (onsaleStatus != null){
            if (onsaleStatus.equals(1)){
                return "上架";
            }
            if (onsaleStatus.equals(0)){
                return "下架";
            }
        }
        return "";
    }

    public String setGoodsCat(){
        if(StringUtils.isBlank(this.goodsCat)){
            return "通用配件";
        }
        return goodsCat;
    }

    public String setMeasureUnit(){
        if(StringUtils.isBlank(this.measureUnit)){
            return "个";
        }
        return measureUnit;
    }


}

