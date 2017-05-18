package com.tqmall.legend.service.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.goods.GoodsInsertParam;
import com.tqmall.legend.object.param.goods.GoodsSearchParam;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.goods.GoodsDTO;
import com.tqmall.legend.object.result.goods.SearchGoodsDTO;
import com.tqmall.legend.object.result.warehouse.WarehouseGoodsSearchDTO;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 配件相关的接口
 */
public interface RpcGoodsService {

    /**
     * 添加自定义配件
     *
     * @param goodsInsertParam
     * @return
     */
    Result<GoodsDTO> insertGoods(GoodsInsertParam goodsInsertParam);

    /**
     * 配件搜索分页查询
     *
     * @param goodsSearchParam
     * @return
     */
    Result<PageEntityDTO<SearchGoodsDTO>> searchGoodsPage(GoodsSearchParam goodsSearchParam);

    /**
     * 获取库存查询筛选条件
     *
     * @param shopId
     * @return
     */
    Result<WarehouseGoodsSearchDTO> getWarehouseGoodsSearchParams(Long shopId);

    /**
     * 根据门店id和配件id得到配件详情
     *
     * @param shopId
     * @param goodsId
     * @return
     */
    Result<GoodsDTO> goodsDetail(Long shopId, Long goodsId);

    /**
     * 获取热门配件列表（20项）
     *
     * @param shopId
     * @return
     */
    Result<List<GoodsDTO>> getHotGoods(Long shopId);
}
