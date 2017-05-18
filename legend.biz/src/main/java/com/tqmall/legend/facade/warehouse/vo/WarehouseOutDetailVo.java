package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.common.GoodsUtils;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sven on 16/8/24.
 */
@Data
public class WarehouseOutDetailVo extends WarehouseOutDetail {
    private BigDecimal stock;//库存数量
    private String carInfoStr;//适用车型

    public String getCarInfoStr() {
        String jsonInfo = super.getCarInfo();
        return GoodsUtils.carInfoTranslate(jsonInfo);
    }
}
