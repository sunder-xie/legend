package com.tqmall.legend.web.warehouse.export.vo;

import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.search.dubbo.client.legend.goods.result.LegendGoodsDTO;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tanghao on 17/2/9.
 */
@Getter
@Setter
@com.tqmall.wheel.component.excel.annotation.Excel
public class SearchGoodsCommission {
    /**
     * 外加字段
     */
    private List<GoodsCar> carInfoList;
    private String carInfoStr;
    private String lastInTimeStr; //保留日期(yyyy-MM-dd)
    @ExcelCol(value = 8, title = "适配车型" , width = 35)
    public String getCarInfoStr() {
        String tmpCarInfoStr = GoodsUtils.carInfoTranslate(getCarInfo());
        tmpCarInfoStr = tmpCarInfoStr.replaceAll("\t|\n|\r","");
        tmpCarInfoStr = tmpCarInfoStr.intern().trim();
        tmpCarInfoStr = tmpCarInfoStr.replaceAll(" ","");
        return tmpCarInfoStr;
    }
    public String getLastInTimeStr(){
        if(StringUtils.isBlank(getLastInTime()) ){
            return getLastInTime();
        }
        return getLastInTime().substring(0,10);
    }
    // 配件品牌
    private String brandName;
    // 云修采购价
    private String tqmallPrice;
    // 前3月建议均销量
    @ExcelCol(value = 5, title = "前三月均销量")
    private BigDecimal averageNumber;
    // 建议库存
    private BigDecimal suggestGoodsNumber;
    // 配件ID
    @ExcelCol(value = 0, title = "配件ID")
    private String id;
    // 配件名称
    @ExcelCol(value = 1, title = "配件名称")
    private String name;
    // 库存数量
    @ExcelCol(value = 2, title = "库存数量")
    private Double stock;
    // 预警库存
    @ExcelCol(value = 3, title = "预警库存")
    private Integer shortageNumber;
    // 库存单位
    @ExcelCol(value = 4, title = "库存单位")
    private String measureUnit;
    // 最后入库时间
    private String lastInTime;
    // 车辆信息
    private String carInfo;

    @ExcelCol(value = 6, title = "建议库存")
    public String getSuggestGoodsNumber(){
        if (suggestGoodsNumber.intValue()> 0){
            return suggestGoodsNumber.toString();
        } else {
          return "不建议采购";
        }
    }

    @ExcelCol(value = 7, title = "云修采购价")
    public String getTqmallPrice(){
        if (StringUtils.isEmpty(tqmallPrice)){
            return "--";
        } else {
            return tqmallPrice;
        }
    }

}
