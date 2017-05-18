package com.tqmall.legend.service.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.goods.GoodsCategoryDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 配件类别相关接口
 */
public interface RpcGoodsCategoryService {
    /**
     * 根据门店id获取配件类别
     *
     * @param shopId
     * @param nameLike 配件类别模糊查询  可不填
     * @param customCat 配件类别是否查询自定义 可不填（true：查询自定义，false：不查询），默认查询自定义类别
     * @return
     */
    Result<List<GoodsCategoryDTO>> getGoodsCategoryList(Long shopId, String nameLike, Boolean customCat);

    /**
     * 获取热门分类
     * @param shopId
     * @param nameLike 热门配件类别模糊查询  可不填
     * @return
     */
    Result<List<GoodsCategoryDTO>> getHotGoodsCategoryList(Long shopId, String nameLike);
}
