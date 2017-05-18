package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.legend.entity.warehousein.WarehouseIn;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仅用于数据输出到页面(不适用更新操作)
 * Created by sven on 16/7/31.
 */
@Data
public class WarehouseInVo extends WarehouseIn {
    private  String relSn; //红字入库关联蓝字入库对应的sn
    private String operatorName;//开单人
    private List<WarehouseInDetailVo> detailList;
    public BigDecimal getTax(){
        if(super.getTax() == null){
            return BigDecimal.ZERO;
        }
        return super.getTax();
    }
    public BigDecimal getFreight(){
        if(super.getFreight() == null){
            return BigDecimal.ZERO;
        }
        return super.getFreight();
    }
}
