package com.tqmall.legend.facade.warehouse.vo;

import com.tqmall.search.dubbo.client.legend.warehousein.result.LegendWarehouseInDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by sven on 16/8/12.
 */
@Data
public class LegendWarehouseInDTOVo extends LegendWarehouseInDTO {
    private List<WarehouseInDetailVo> detailVoList;
    private String operatorName;//开单人姓名

}
