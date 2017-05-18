package com.tqmall.legend.server.goods;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.provider.popularsort.RpcPopularSortService;
import com.tqmall.cube.shop.result.popularsort.PopularDataDTO;
import com.tqmall.legend.biz.bo.goods.GoodsDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.common.GoodsUtils;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.object.enums.warehouse.OnSaleStatusEnum;
import com.tqmall.legend.object.enums.warehouse.ZeroStockRangeEnum;
import com.tqmall.legend.object.param.goods.GoodsInsertParam;
import com.tqmall.legend.object.param.goods.GoodsSearchParam;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.goods.GoodsBrandDTO;
import com.tqmall.legend.object.result.goods.SearchGoodsDTO;
import com.tqmall.legend.object.result.warehouse.WarehouseGoodsSearchDTO;
import com.tqmall.legend.service.goods.RpcGoodsBrandService;
import com.tqmall.legend.service.goods.RpcGoodsService;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/23.
 */
@Service("rpcGoodsService")
public class RpcGoodsServiceImpl implements RpcGoodsService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsFacade goodsFacade;
    @Autowired
    private RpcGoodsBrandService rpcGoodsBrandService;
    @Autowired
    private RpcPopularSortService rpcPopularSortService;

    /**
     * 添加自定义配件
     *
     * @param goodsInsertParam
     * @return
     */
    @Override
    public Result<com.tqmall.legend.object.result.goods.GoodsDTO> insertGoods(final GoodsInsertParam goodsInsertParam) {
        return new ApiTemplate<com.tqmall.legend.object.result.goods.GoodsDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Long shopId = goodsInsertParam.getShopId();
                Long creator = goodsInsertParam.getCreator();
                String goodsSn = goodsInsertParam.getGoodsSn();
                String name = goodsInsertParam.getName();
                String measureUnit = goodsInsertParam.getMeasureUnit();
                String format = goodsInsertParam.getFormat();
                Long catId = goodsInsertParam.getCatId();
                Long stdCatId = goodsInsertParam.getStdCatId();
                String goodsCat = goodsInsertParam.getGoodsCat();
                BigDecimal price = goodsInsertParam.getPrice();
                Long shortageNumber = goodsInsertParam.getShortageNumber();
                Assert.notNull(shopId, "门店id不能为空");
                Assert.notNull(creator, "用户id不能为空");
                Assert.notNull(goodsSn, "商品编号不能为空");
                Assert.notNull(name, "商品名称不能为空");
                Assert.notNull(measureUnit, "最小单位不能为空");
                Assert.notNull(format, "零件号不能为空");
                if(catId == null && stdCatId == null){
                    throw new IllegalArgumentException("配件类别id不能为空");
                }
                Assert.notNull(goodsCat, "配件类别不能为空");
                Assert.notNull(price, "零售单价不能为空");
                Assert.notNull(shortageNumber, "预警库存不能为空");
            }

            @Override
            protected com.tqmall.legend.object.result.goods.GoodsDTO process() throws BizException {
                GoodsDTO goods = new GoodsDTO();
                BeanUtils.copyProperties(goodsInsertParam, goods);
                goods.setGoodsType(0);//实开物料
                goods.setTqmallStatus(4);//自定义配件
                Long stdCatId = goodsInsertParam.getStdCatId();
                if(stdCatId != null){
                    goods.setStdCatId(stdCatId.intValue());
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(goodsInsertParam.getCreator());
                userInfo.setShopId(goodsInsertParam.getShopId());
                GoodsDTO resultGoods = goodsService.addBasicGoods(goods, userInfo);
                com.tqmall.legend.object.result.goods.GoodsDTO goodsDTO = new com.tqmall.legend.object.result.goods.GoodsDTO();
                BeanUtils.copyProperties(resultGoods, goodsDTO);
                return goodsDTO;
            }
        }.execute();
    }

    /**
     * 配件搜索
     *
     * @param goodsSearchParam
     * @return
     */
    @Override
    public Result<PageEntityDTO<SearchGoodsDTO>> searchGoodsPage(final GoodsSearchParam goodsSearchParam) {
        return new ApiTemplate<PageEntityDTO<SearchGoodsDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(goodsSearchParam, "参数不能为空");
                Long shopId = goodsSearchParam.getShopId();
                Assert.notNull(shopId, "门店id不能为空");
                List<String> sortList = goodsSearchParam.getSortList();
                if (CollectionUtils.isNotEmpty(sortList)) {
                    for (String sortStr : sortList) {
                        String[] split = sortStr.split(":");
                        if (split.length != 2 || (!split[1].equalsIgnoreCase(Sort.Direction.ASC.toString()) && !split[1].equalsIgnoreCase(Sort.Direction.DESC.toString()))) {
                            throw new IllegalArgumentException("排序参数有误");
                        }
                    }
                }
            }

            @Override
            protected PageEntityDTO<SearchGoodsDTO> process() throws BizException {
                Long shopId = goodsSearchParam.getShopId();
                String goodsSn = goodsSearchParam.getGoodsSn();
                String goodsName = goodsSearchParam.getGoodsName();
                String goodsFormat = goodsSearchParam.getGoodsFormat();
                String goodsType = goodsSearchParam.getGoodsType();
                String carInfoLike = goodsSearchParam.getCarInfoLike();
                String goodsCatLike = goodsSearchParam.getGoodsCatLike();
                Integer brandId = goodsSearchParam.getBrandId();
                Integer zeroStockRange = goodsSearchParam.getZeroStockRange();
                Integer onsaleStatus = goodsSearchParam.getOnsaleStatus();
                String keyword = goodsSearchParam.getKeyword();
                Integer size = goodsSearchParam.getSize();
                LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
                goodsRequest.setShopId(shopId.toString());
                if (StringUtils.isNotBlank(goodsName)) {
                    goodsRequest.setGoodsName(goodsName);
                }
                if (StringUtils.isNotBlank(goodsSn)) {
                    goodsRequest.setGoodsSn(goodsSn);
                }
                if (StringUtils.isNotBlank(goodsType)) {
                    goodsRequest.setGoodsType(goodsType);
                }
                if (StringUtils.isNotBlank(goodsFormat)) {
                    goodsRequest.setGoodsFormat(goodsFormat);
                }
                if (StringUtils.isNotBlank(carInfoLike)) {
                    goodsRequest.setCarInfoLike(carInfoLike);
                }
                if (StringUtils.isNotBlank(goodsCatLike)) {
                    goodsRequest.setGoodsCat(goodsCatLike);
                }
                if (brandId != null) {
                    goodsRequest.setBrandId(brandId);
                }
                if (zeroStockRange != null) {
                    goodsRequest.setZeroStockRange(zeroStockRange);
                }
                if (onsaleStatus != null) {
                    goodsRequest.setOnsaleStatus(onsaleStatus);
                }
                if (StringUtils.isNotBlank(keyword)) {
                    goodsRequest.setLikeKeyWords(keyword);//goods_sn_like,name_like,format_like
                }
                Integer page = goodsSearchParam.getPage() - 1;
                List<String> sortList = goodsSearchParam.getSortList();
                PageableRequest pageableRequest;
                if (CollectionUtils.isEmpty(sortList)) {
                    pageableRequest = new PageableRequest(page, size);
                } else {
                    List<Sort.Order> orders = Lists.newArrayList();
                    for (String sortStr : sortList) {
                        String[] split = sortStr.split(":");
                        String direction = split[1];
                        String property = split[0];
                        Sort.Order order;
                        if(direction.equalsIgnoreCase(Sort.Direction.ASC.toString())){
                            order = new Sort.Order(Sort.Direction.ASC,property);
                        } else {
                            order = new Sort.Order(Sort.Direction.DESC,property);
                        }
                        orders.add(order);
                    }
                    Sort sort = new Sort(orders);
                    FieldsSort fieldsSort = new FieldsSort(sort);
                    pageableRequest = new PageableRequest(page, size, fieldsSort);
                }
                DefaultPage<SearchGoodsVo> searchGoodsVoDefaultPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
                List<SearchGoodsVo> searchGoodsVoList = searchGoodsVoDefaultPage.getContent();
                List<SearchGoodsDTO> searchGoodsDTOList = Lists.newArrayList();
                for (SearchGoodsVo searchGoodsVo : searchGoodsVoList) {
                    SearchGoodsDTO searchGoodsDTO = new SearchGoodsDTO();
                    BeanUtils.copyProperties(searchGoodsVo, searchGoodsDTO);
                    searchGoodsDTOList.add(searchGoodsDTO);
                }
                PageEntityDTO<SearchGoodsDTO> pageEntityDTO = new PageEntityDTO();
                pageEntityDTO.setContent(searchGoodsDTOList);
                pageEntityDTO.setPageNum(searchGoodsVoDefaultPage.getNumber());
                pageEntityDTO.setTotalNum(searchGoodsVoDefaultPage.getTotalElements());
                return pageEntityDTO;
            }
        }.execute();
    }

    /**
     * 获取库存查询筛选条件
     *
     * @param shopId
     * @return
     */
    @Override
    public Result<WarehouseGoodsSearchDTO> getWarehouseGoodsSearchParams(final Long shopId) {
        return new ApiTemplate<WarehouseGoodsSearchDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected WarehouseGoodsSearchDTO process() throws BizException {
                WarehouseGoodsSearchDTO warehouseGoodsSearchDTO = new WarehouseGoodsSearchDTO();
                Result<List<GoodsBrandDTO>> goodsBrandResult = rpcGoodsBrandService.getGoodsBrand(shopId);
                if (goodsBrandResult.isSuccess()) {
                    List<GoodsBrandDTO> goodsBrandDTOList = goodsBrandResult.getData();
                    warehouseGoodsSearchDTO.setGoodsBrandDTOList(goodsBrandDTOList);
                    OnSaleStatusEnum[] onSaleStatusEnums = OnSaleStatusEnum.getNames();
                    ZeroStockRangeEnum[] zeroStockRangeEnums = ZeroStockRangeEnum.getNames();
                    warehouseGoodsSearchDTO.setOnSaleStatusEnums(onSaleStatusEnums);
                    warehouseGoodsSearchDTO.setZeroStockRangeEnums(zeroStockRangeEnums);
                } else {
                    throw new BizException(goodsBrandResult.getMessage());
                }
                return warehouseGoodsSearchDTO;
            }
        }.execute();
    }

    /**
     * 配件详情
     *
     * @param shopId
     * @param goodsId
     * @return
     */
    @Override
    public Result<com.tqmall.legend.object.result.goods.GoodsDTO> goodsDetail(final Long shopId, final Long goodsId) {
        return new ApiTemplate<com.tqmall.legend.object.result.goods.GoodsDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
                Assert.notNull(goodsId, "配件id不能为空");
            }

            @Override
            protected com.tqmall.legend.object.result.goods.GoodsDTO process() throws BizException {
                Optional<Goods> goodsOpt = goodsService.selectById(goodsId, shopId);
                if (!goodsOpt.isPresent()) {
                    throw new BizException("配件不存在");
                }
                Goods goods = goodsOpt.get();
                com.tqmall.legend.object.result.goods.GoodsDTO goodsDTO = new com.tqmall.legend.object.result.goods.GoodsDTO();
                BeanUtils.copyProperties(goods, goodsDTO);
                String carInfoStr = GoodsUtils.carInfoTranslate(goods.getCarInfo());
                if (StringUtils.isNotBlank(carInfoStr)) {
                    carInfoStr = carInfoStr.replace("\n", "");
                    carInfoStr = carInfoStr.replace("\t", "");
                }
                goodsDTO.setCarInfoStr(carInfoStr);
                return goodsDTO;
            }
        }.execute();
    }

    /**
     * 获取热门配件列表（20项）
     *
     * @param shopId
     * @return
     */
    @Override
    public Result<List<com.tqmall.legend.object.result.goods.GoodsDTO>> getHotGoods(final Long shopId) {
        return new ApiTemplate<List<com.tqmall.legend.object.result.goods.GoodsDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<com.tqmall.legend.object.result.goods.GoodsDTO> process() throws BizException {
                List<com.tqmall.legend.object.result.goods.GoodsDTO> goodsDTOList = Lists.newArrayList();
                // 调用cube获取热门配件列表（最多20项）
                Result<List<PopularDataDTO>> orderGoodsListResult = null;
                try {
                    orderGoodsListResult = rpcPopularSortService.getOrderGoodsList(shopId);
                } catch (Exception e) {
                    logger.error("调用cube获取常用配件列表异常", e);
                }
                if (!orderGoodsListResult.isSuccess() || CollectionUtils.isEmpty(orderGoodsListResult.getData())) {
                    return goodsDTOList;
                }
                List<PopularDataDTO> popularOrderGoodsDTOList = orderGoodsListResult.getData();
                List<Long> goodsIdsList = Lists.newArrayList();
                for(PopularDataDTO popularDataDTO : popularOrderGoodsDTOList){
                    Long id = popularDataDTO.getId();
                    goodsIdsList.add(id);
                }
                List<Goods> goodsList = goodsService.selectByIds(goodsIdsList.toArray(new Long[goodsIdsList.size()]));
                Map<Long,Goods> goodsMap = Maps.newHashMap();
                for (Goods goods : goodsList) {
                    Long id = goods.getId();
                    goodsMap.put(id, goods);
                }
                for(PopularDataDTO popularDataDTO : popularOrderGoodsDTOList){
                    Long id = popularDataDTO.getId();
                    if(!goodsMap.containsKey(id)){
                        continue;
                    }
                    Goods goods = goodsMap.get(id);
                    com.tqmall.legend.object.result.goods.GoodsDTO goodsDTO = new com.tqmall.legend.object.result.goods.GoodsDTO();
                    goodsDTO.setId(goods.getId());
                    goodsDTO.setName(goods.getName());
                    goodsDTO.setPrice(goods.getPrice());
                    goodsDTO.setFormat(goods.getFormat());
                    goodsDTO.setStock(goods.getStock());
                    goodsDTO.setGoodsType(goods.getGoodsType());
                    goodsDTO.setMeasureUnit(goods.getMeasureUnit());
                    goodsDTO.setGoodsSn(goods.getGoodsSn());
                    goodsDTOList.add(goodsDTO);
                }
                return goodsDTOList;
            }
        }.execute();
    }
}
