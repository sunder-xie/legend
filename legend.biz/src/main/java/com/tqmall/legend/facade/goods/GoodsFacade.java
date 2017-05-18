package com.tqmall.legend.facade.goods;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendShortageGoodsRequest;
import com.tqmall.tqmallstall.domain.result.Legend.LegendGoodsInfoDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配件服务 Facade
 */
public interface GoodsFacade {


    /**
     * 配件分页搜索
     *
     * @param goodsRequest 搜索参数
     * @param pageable     分页对象
     * @return DefaultPage<SearchGoodsVo>
     */
    DefaultPage<SearchGoodsVo> goodsPageSearch(LegendGoodsRequest goodsRequest, PageableRequest pageable);

    /**
     * 库存预警页面 配件分页搜索
     *
     * @param goodsRequest    搜索参数
     * @param pageableRequest 分页对象
     * @return
     */
    DefaultPage<SearchGoodsVo> goodsPageSearchInStockWarning(LegendShortageGoodsRequest goodsRequest, PageableRequest pageableRequest);

    /**
     * 淘汽采购-采购订单-签收入库
     *
     * @param goodsBos 采购商品
     */
    void purchaseTqmallGoodses(List<GoodsBo> goodsBos);


    /**
     * 检查物料详细状态
     *
     * @param goodsId 工单ID
     * @param shopId  门店ID
     * @return
     */
    Result getGoodsDetailStatus(Long goodsId, Long shopId);


    /**
     * TODO batch 获取档口商品采购价
     * <p/>
     * 根据档口商品ID和城市ID 获取档口的商品价格
     *
     * @param tqmallGoodsId 档口商品ID
     * @param cityId        城市ID
     * @return Optional<LegendGoodsInfoDTO>
     */
    Optional<LegendGoodsInfoDTO> getTqmallGoods(Long tqmallGoodsId, Long cityId);

    List<BaseEnumBo> getGoodsLocation(Long shopId);
}
