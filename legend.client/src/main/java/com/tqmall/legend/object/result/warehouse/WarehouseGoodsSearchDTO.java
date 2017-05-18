package com.tqmall.legend.object.result.warehouse;

import com.tqmall.legend.object.enums.warehouse.OnSaleStatusEnum;
import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;
import com.tqmall.legend.object.result.goods.GoodsBrandDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/11/25.
 * app库存查询对象
 */
@Getter
@Setter
public class WarehouseGoodsSearchDTO implements Serializable {

    private static final long serialVersionUID = -140799297134031976L;

    private List<GoodsBrandDTO> goodsBrandDTOList;//配件品牌
    private OnSaleStatusEnum[] onSaleStatusEnums;//配件上下架状态
    private ZeroStockRangeEnum[] zeroStockRangeEnums;//配件非零库存查询

}
