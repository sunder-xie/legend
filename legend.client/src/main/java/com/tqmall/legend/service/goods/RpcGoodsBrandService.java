package com.tqmall.legend.service.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.goods.GoodsBrandDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/25.
 * 配件品牌
 */
public interface RpcGoodsBrandService {
    /**
     * 根据门店id获取门店的品牌
     *
     * @param shopId
     * @return
     */
    Result<List<GoodsBrandDTO>> getGoodsBrand(Long shopId);
}
