package com.tqmall.legend.service.warehouseshare;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.warehouseshare.WarehouseShareSearchParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.warehouseshare.WarehouseShareDTO;

/**
 * Created by xin on 2016/11/23.
 */
public interface RpcWarehouseShareService {

    /**
     * 查询库存共享配件列表
     * @param param
     * @return
     */
    Result<PageEntityDTO<WarehouseShareDTO>> searchWarehouseSharePage(WarehouseShareSearchParam param);

    /**
     * 审核通过
     * @param id
     * @return
     */
    Result<Boolean> checkPass(Long id, Long userId);

    /**
     * 审核不通过
     * @param id
     * @param remark
     * @return
     */
    Result<Boolean> checkNotPass(Long id, String remark, Long userId);
}
