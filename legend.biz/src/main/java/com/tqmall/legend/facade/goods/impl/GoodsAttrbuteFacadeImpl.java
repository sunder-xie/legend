package com.tqmall.legend.facade.goods.impl;

import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.result.goods.GoodsAttributeDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsAttributeService;
import com.tqmall.legend.facade.goods.GoodsAttrbuteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配件属性service
 */
@Service
public class GoodsAttrbuteFacadeImpl implements GoodsAttrbuteFacade {

    @Autowired
    RpcGoodsAttributeService rpcGoodsAttributeService;

    @Override
    public List<GoodsAttributeDTO> selectByGoodsTypeId(Integer goodsTypeId, Long shopId) {
        return rpcGoodsAttributeService.selectByGoodsTypeId(goodsTypeId, shopId);
    }

    @Override
    public Result add(GoodsAttributeDTO goodsAttributeDTO) {
        return rpcGoodsAttributeService.add(goodsAttributeDTO);
    }

    @Override
    public GoodsAttributeDTO selectById(Long id) {
        return rpcGoodsAttributeService.selectById(id);
    }

    @Override
    public Result updateById(GoodsAttributeDTO goodsAttributeDTO) {
        return rpcGoodsAttributeService.updateById(goodsAttributeDTO);
    }
}
