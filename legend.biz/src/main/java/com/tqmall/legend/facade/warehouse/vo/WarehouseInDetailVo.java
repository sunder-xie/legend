package com.tqmall.legend.facade.warehouse.vo;


import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.common.GoodsUtils;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sven on 16/8/4.
 */
@Data
public class WarehouseInDetailVo extends WarehouseInDetail {
    private BigDecimal stock;//库存
    private String carInfoStr;//适用车型
    private String catName;//分类名称

    public String getCarInfoStr() {
        String info = super.getCarInfo();
        return GoodsUtils.carInfoTranslate(info);
    }
}