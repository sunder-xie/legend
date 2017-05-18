package com.tqmall.legend.service.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.service.ShopServiceCateDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 服务类别
 */
public interface RpcShopServiceCateService {
    /**
     * 根据门店id获取服务类别
     *
     * @param shopId
     * @param nameLike 服务名称模糊查询，可不传
     * @return
     */
    Result<List<ShopServiceCateDTO>> getServiceCateList(Long shopId, String nameLike);
}
