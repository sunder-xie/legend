package com.tqmall.legend.facade.goods.impl;

import com.tqmall.itemcenter.object.result.goods.GoodsCarDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsCarService;
import com.tqmall.legend.facade.goods.GoodsCarFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsCarFacadeImpl implements GoodsCarFacade {

    @Autowired
    RpcGoodsCarService rpcGoodsCarService;

    @Override
    public List<GoodsCarDTO> selectByGoodsIdAndShopId(Long goodsId, Long shopId) {
        return rpcGoodsCarService.selectByGoodsIdAndShopId(goodsId, shopId);
    }

    @Override
    public Integer save(GoodsCarDTO goodsCar) {
        return rpcGoodsCarService.save(goodsCar);
    }

    @Override
    public Integer deleteByGoodsIdAndShopId(Long goodsId, Long shopId) {
        return rpcGoodsCarService.deleteByGoodsIdAndShopId(goodsId, shopId);
    }
}
