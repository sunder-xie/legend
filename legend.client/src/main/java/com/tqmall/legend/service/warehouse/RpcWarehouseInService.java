package com.tqmall.legend.service.warehouse;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.warehouse.WarehouseInDetailParam;

import java.util.List;

/**
 * Created by sven on 16/11/22.
 */
public interface RpcWarehouseInService {
    Result<Boolean> batchWarehouseIn(List<WarehouseInDetailParam> warehouseInDetailParamList);
}
