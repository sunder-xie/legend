package com.tqmall.legend.facade.goods.impl;

import com.tqmall.itemcenter.object.result.goods.GoodsAttrRelDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsAttrRelService;
import com.tqmall.legend.facade.goods.GoodsAttrRelFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsAttrRelFacadeImpl implements GoodsAttrRelFacade {

    @Autowired
    RpcGoodsAttrRelService rpcGoodsAttrRelService;

    @Override
    public List<GoodsAttrRelDTO> selectByGoodsIdAndShopId(Long goodsId, Long shopId) {
        return rpcGoodsAttrRelService.selectByGoodsIdAndShopId(goodsId, shopId);
    }

    @Override
    public Integer deleteByGoodsIdAndShopId(Long goodsId, Long shopId) {
        return rpcGoodsAttrRelService.deleteByGoodsIdAndShopId(goodsId, shopId);
    }

    @Override
    public Integer save(GoodsAttrRelDTO goodsAttrRelDTO) {
        return rpcGoodsAttrRelService.save(goodsAttrRelDTO);
    }
}
