package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.search.dubbo.client.legend.warehouseout.result.LegendWarehouseOutDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by sven on 16/8/12.
 */
@Data
public class LegendWarehouseOutDTOVo extends LegendWarehouseOutDTO {
    private List<WarehouseOutDetailVo> detailVoList;
    private BigDecimal saleAmount;//销售总价
    private BigDecimal costAmount;//成本总价
    private DecimalFormat df = new DecimalFormat("0.00");
    public String getSaleAmount(){
        return df.format(saleAmount);
    }
    public String getCostAmount(){
        return df.format(costAmount);
    }
}
