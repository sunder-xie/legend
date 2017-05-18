package com.tqmall.legend.service.activity;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.activity.PurchaseActivityConfigParam;
import com.tqmall.legend.object.result.activity.PurchaseActivityConfigDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
public interface RpcPurchaseActivityConfigService {

    /**
     * 插入活动配置
     * @param param
     * @return
     */
    Result<Boolean> insert(PurchaseActivityConfigParam param);

    /**
     * 修改活动配置
     * @param param
     * @return
     */
    Result<Boolean> update(PurchaseActivityConfigParam param);

    /**
     * 删除活动配置
     * @param id
     * @return
     */
    Result<Boolean> delete(Long id);

    /**
     * 查询活动配置信息
     * @param param
     * @return
     */
    Result<PageEntityDTO<PurchaseActivityConfigDTO>> query(PurchaseActivityConfigParam param);

    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    Result<PurchaseActivityConfigDTO> queryById(Long id);

    /**
     * 查询所有活动list
     * @return
     */
    Result<List<PurchaseActivityConfigDTO>> queryAllList();

}
