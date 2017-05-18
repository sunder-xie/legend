package com.tqmall.legend.facade.goods.impl;

import com.tqmall.itemcenter.object.result.goods.GoodsHotCategoryDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsHotCategoryService;
import com.tqmall.legend.facade.goods.GoodsHotCategoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsHotCategoryFacadeImpl implements GoodsHotCategoryFacade {

    @Autowired
    RpcGoodsHotCategoryService rpcGoodsHotCategoryService;

    @Override
    public List<GoodsHotCategoryDTO> selectByCatNameLike(String categoryNameLike) {
        return rpcGoodsHotCategoryService.selectByCatNameLike(categoryNameLike);
    }
}
