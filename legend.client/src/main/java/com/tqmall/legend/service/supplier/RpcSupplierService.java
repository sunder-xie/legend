package com.tqmall.legend.service.supplier;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.supplier.SupplierDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 供应商
 */
public interface RpcSupplierService {
    /**
     * 根据门店id获取供应商列表
     *
     * @param shopId
     * @param nameLike 供应商名称模糊查询，可不传
     * @return
     */
    Result<List<SupplierDTO>> getSupplierList(Long shopId, String nameLike);
}
