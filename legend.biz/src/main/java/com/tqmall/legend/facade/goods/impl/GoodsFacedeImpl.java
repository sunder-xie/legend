package com.tqmall.legend.facade.goods.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.warehouse.IWarehouseOutDetailService;
import com.tqmall.legend.biz.warehousein.WarehouseInDetailService;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.facade.magic.BPGoodsFacade;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.common.result.Result;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendShortageGoodsRequest;
import com.tqmall.search.dubbo.client.legend.goods.result.LegendGoodsDTO;
import com.tqmall.search.dubbo.client.legend.goods.service.LegendGoodsService;
import com.tqmall.tqmallstall.domain.param.GoodsSearchParam;
import com.tqmall.tqmallstall.domain.result.Legend.LegendGoodsInfoDTO;
import com.tqmall.tqmallstall.service.OrderRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 配件服务service
 */
@Service
@Slf4j
public class GoodsFacedeImpl implements GoodsFacade {

    @Resource
    private LegendGoodsService legendGoodsService;
    @Resource
    private GoodsService goodsService;
    @Autowired
    BPGoodsFacade bpGoodsFacade;
    @Autowired
    private WarehouseInDetailService warehouseInDetailService;
    @Autowired
    private IWarehouseOutDetailService warehouseOutDetailService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderRemoteService orderRemoteService;


    @Override
    public DefaultPage<SearchGoodsVo> goodsPageSearch(LegendGoodsRequest goodsRequest, PageableRequest pageable) {

        log.info("[搜索平台]获取配件,入参:{},{}", ObjectUtils.objectToJSON(goodsRequest),ObjectUtils.objectToJSON(pageable));
        Result<PageableResponseExtend<LegendGoodsDTO>> result = null;
        try {
            result = legendGoodsService.queryLegendGoodsWithAggs(goodsRequest, pageable);
        } catch (Exception e) {
            log.error("[搜索平台]获取配件异常,异常信息:{}", e);
            return new DefaultPage(new ArrayList<SearchGoodsVo>());
        }

        if (!result.isSuccess()) {
            log.error("[搜索平台]获取配件失败,失败原因:{}", result.getMessage());
            return new DefaultPage(new ArrayList<SearchGoodsVo>());
        }
        Page<LegendGoodsDTO> page = result.getData();

        // 包装后集合对象
        List<SearchGoodsVo> searchGoodsVos = wrapperSearchGoods(page.getContent());
        DefaultPage<SearchGoodsVo> defaultPage = new DefaultPage<>(searchGoodsVos, pageable, page.getTotalElements());
        defaultPage.setOtherData(result.getData().getExtend());

        return defaultPage;
    }

    @Override
    public DefaultPage<SearchGoodsVo> goodsPageSearchInStockWarning(LegendShortageGoodsRequest goodsRequest, PageableRequest pageable) {

        log.info("[搜索平台]获取配件,入参:{}", ObjectUtils.objectToJSON(goodsRequest));
        Result<Page<LegendGoodsDTO>> result = null;
        try {
            result = legendGoodsService.queryShortageLegendGoods(goodsRequest, pageable);
        } catch (Exception e) {
            log.error("[搜索平台]获取配件异常,异常信息:{}", e);
            return new DefaultPage(new ArrayList<SearchGoodsVo>());
        }

        if (!result.isSuccess()) {
            log.error("[搜索平台]获取配件失败,失败原因:{}", result.getMessage());
            return new DefaultPage(new ArrayList<SearchGoodsVo>());
        }
        Page<LegendGoodsDTO> page = result.getData();

        // 包装后集合对象
        List<SearchGoodsVo> searchGoodsVos = wrapperSearchGoods(page.getContent());

        return new DefaultPage<SearchGoodsVo>(searchGoodsVos, pageable, page.getTotalElements());
    }

    /**
     * 包装配件搜索结果
     * 2.转换成本价
     *
     * @param legendGoodsDTOList 搜索出配件结果列表
     * @return List<SearchGoodsVo>
     */
    private List<SearchGoodsVo> wrapperSearchGoods(List<LegendGoodsDTO> legendGoodsDTOList) {

        List<SearchGoodsVo> searchGoodsVos = new ArrayList<SearchGoodsVo>();
        SearchGoodsVo searchGoods = null;
        for (LegendGoodsDTO legendGoodsDTO : legendGoodsDTOList) {
            searchGoods = new SearchGoodsVo();
            BeanUtils.copyProperties(legendGoodsDTO, searchGoods);

            Double inventoryPriceDouble = searchGoods.getInventoryPrice();
            if (inventoryPriceDouble == null) {
                searchGoods.setInventoryPrice(0.0);
            }
            BigDecimal inventoryPrice = new BigDecimal(inventoryPriceDouble);
            searchGoods.setInventoryPrice(inventoryPrice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            searchGoodsVos.add(searchGoods);
        }

        return searchGoodsVos;
    }

    @Transactional
    @Override
    public void purchaseTqmallGoodses(List<GoodsBo> goodsBos) {
        for (GoodsBo item : goodsBos) {
            goodsService.addWithAttrCar(item);
        }
    }


    @Override
    public com.tqmall.legend.common.Result getGoodsDetailStatus(Long goodsId, Long shopId) {
        StringBuffer sb = new StringBuffer();
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodesByGoodsId(goodsId, shopId);
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            sb.append("该配件曾开过工单").append(orderGoodsList.size()).append("次<br/>");
        }

        List<WarehouseInDetail> warehouseInDetailList = warehouseInDetailService.queryInDetail(goodsId, shopId);
        if (!CollectionUtils.isEmpty(warehouseInDetailList)) {
            sb.append("该配件曾做过入库").append(warehouseInDetailList.size()).append("次<br/>");
        }
        List<WarehouseOutDetail> warehouseOutDetailList = warehouseOutDetailService.queryOutDetail(goodsId, shopId);
        if (!CollectionUtils.isEmpty(warehouseOutDetailList)) {
            sb.append("该配件曾做过出库").append(warehouseOutDetailList.size()).append("次<br/>");
        }
        if (sb.length() > 0) {
            return com.tqmall.legend.common.Result.wrapErrorResult("10000", sb.toString());
        } else {
            return com.tqmall.legend.common.Result.wrapSuccessfulResult(true);
        }
    }


    public Optional<LegendGoodsInfoDTO> getTqmallGoods(@NotNull Long tqmallGoodsId, Long cityId) {

        // TODO '383'默认仓库系统参数配置,便于维护调整.
        Long city = cityId == null ? Long.getLong("383") : cityId;

        //  查询档口商品价格
        GoodsSearchParam goodsSearchParam = new GoodsSearchParam();
        goodsSearchParam.setCityId(city.intValue());
        if (null == tqmallGoodsId) {
            return Optional.absent();
        }
        goodsSearchParam.setGoodsId(tqmallGoodsId.toString());
        com.tqmall.core.common.entity.Result<LegendGoodsInfoDTO> result = null;
        try {
            result = orderRemoteService.legendGoods(goodsSearchParam);
        } catch (Exception e) {
            log.error("[获取档口配件价格]获取档口配件价格异常 参数:{} 异常:{}", LogUtils.objectToString(goodsSearchParam), e);
            return Optional.absent();
        }

        if (result != null && result.isSuccess() && result.getData() != null) {
            return Optional.fromNullable(result.getData());
        } else {
            return Optional.absent();
        }
    }

    @Override
    public List<BaseEnumBo> getGoodsLocation(Long shopId) {
        List<String> list = goodsService.getGoodsLocation(shopId);
        List<BaseEnumBo> boList = Lists.newArrayList();
        for (String name : list) {
            BaseEnumBo baseEnumBo = new BaseEnumBo();
            baseEnumBo.setName(name);
            boList.add(baseEnumBo);
        }
        return boList;
    }
}
