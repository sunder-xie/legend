package com.tqmall.legend.facade.goods.impl;

import com.google.common.collect.Lists;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.goods.GoodsUnitSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsUnitDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsUnitService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.goods.GoodsUnitFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配件单位service
 */
@Slf4j
@Service
public class GoodsUnitFacadeImpl implements GoodsUnitFacade {

    @Autowired
    RpcGoodsUnitService rpcGoodsUnitService;

    @Override
    public List<GoodsUnitDTO> selectByNameLike(String unitNameLike, Long shopId) {
        return rpcGoodsUnitService.selectByNameLike(unitNameLike, shopId);
    }

    @Override
    public Result addWithoutRepeat(GoodsUnitDTO goodsUnitDTO) {
        return rpcGoodsUnitService.addWithoutRepeat(goodsUnitDTO);
    }

    @Override
    public boolean checkGoodsUnitIsExist(String measureUnitName, Long shopId) {
        return rpcGoodsUnitService.checkGoodsUnitIsExist(measureUnitName, shopId);
    }

    @Override
    public boolean deleteGoodsUnit(final Long shopId) {
        Result<Boolean> result = rpcGoodsUnitService.deleteGoodsUnit(shopId);
        return result.getData();
    }

    @Override
    public boolean deleteGoodsUnitByIds(List<Long> ids) {
        Result<Boolean> result = rpcGoodsUnitService.deleteGoodsUnitByIds(ids);
        return result.getData();
    }

    @Override
    public Result<DefaultPage<GoodsUnitDTO>> getPage(GoodsUnitSearchParam goodsUnitSearchParam, Pageable pageable) {
        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        PagingResult<GoodsUnitDTO> pagingResult = rpcGoodsUnitService.getPage(goodsUnitSearchParam);
        int total = 0;
        List<GoodsUnitDTO> goodsUnitDTOList = Lists.newArrayList();
        if (pagingResult.isSuccess()) {
            total = pagingResult.getTotal();
            goodsUnitDTOList = pagingResult.getList();
        }
        DefaultPage<GoodsUnitDTO> defaultPage = new DefaultPage<GoodsUnitDTO>(goodsUnitDTOList, pageRequest, total);
        return Result.wrapSuccessfulResult(defaultPage);
    }
}