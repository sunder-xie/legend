package com.tqmall.legend.service.activity;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.activity.PurchaseBannerConfigParam;
import com.tqmall.legend.object.result.activity.PurchaseBannerConfigDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public interface RpcPurchaseBannerConfigService {

    /**
     * 插入banner配置
     * @param param
     * @return
     */
    Result<Boolean> insert(PurchaseBannerConfigParam param);

    /**
     * 修改banner配置
     * @param param
     * @return
     */
    Result<Boolean> update(PurchaseBannerConfigParam param);

    /**
     * 删除banner配置
     * @param id
     * @return
     */
    Result<Boolean> delete(Long id);

    /**
     * 查询banner配置信息
     * @param param
     * @return
     */
    Result<PageEntityDTO<PurchaseBannerConfigDTO>> query(PurchaseBannerConfigParam param);

    /**
     * 根据id获取数据接口
     * @param id
     * @return
     */
    Result<PurchaseBannerConfigDTO> queryById(Long id);

}
