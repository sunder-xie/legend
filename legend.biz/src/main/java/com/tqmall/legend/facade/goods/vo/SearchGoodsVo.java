package com.tqmall.legend.facade.goods.vo;

import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.search.dubbo.client.legend.goods.result.LegendGoodsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsy on 16/4/26.
 * 搜索接口返回的物料对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SearchGoodsVo extends LegendGoodsDTO {
    /**
     * 外加字段
     */
    private List<GoodsCar> carInfoList;
    private String carInfoStr;
    private String lastInTimeStr; //保留日期(yyyy-MM-dd)
    public String getCarInfoStr() {
        return GoodsUtils.carInfoTranslate(super.getCarInfo());
    }
    public String getLastInTimeStr(){
        if(StringUtils.isBlank(super.getLastInTime()) ){
            return super.getLastInTime();
        }
        return super.getLastInTime().substring(0,10);
    }
    // 配件品牌
    private String brandName;
    // 云修采购价
    private String tqmallPrice;
    // 前3月建议均销量
    private BigDecimal averageNumber;
    // 建议库存
    private BigDecimal suggestGoodsNumber;

    // 用于首页-库存预警展示 [[
    private String stockForShow;
    private String shortageNumberForShow;
    private String carModels;
    private String cityPrice;
    // ]]

}

