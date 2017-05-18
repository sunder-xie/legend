package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.enums.warehouse.WarehouseOutTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sven on 16/8/23.
 */
@Data
public class WarehouseOutVo extends WarehouseOut {
    //领料人名称
    private String receiverName;
    //出库人名称
    private String operatorName;
    private String orderSn;
    private String outTypeName;//出库类型名称
    private BigDecimal saleAmount;//销售总价
    private BigDecimal costAmount;//成本总价
    private List<WarehouseOutDetailVo> detailVoList;

    public String getOutTypeName() {
        return WarehouseOutTypeEnum.getMessageByName(super.getOutType());
    }
}
