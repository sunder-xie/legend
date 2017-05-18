package com.tqmall.legend.biz.goods.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.goods.GoodsCategoryService;
import com.tqmall.legend.dao.goods.GoodsCategoryDao;
import com.tqmall.legend.entity.goods.GoodsCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配件类别service
 */
@Slf4j
@Service
public class GoodsCategoryServiceImpl extends BaseServiceImpl implements GoodsCategoryService {

	@Autowired
	GoodsCategoryDao goodsCategoryDao;

    @Override
    public Optional<GoodsCategory> select(String catName, Long shopId, Long goodsTypeId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(3);
        paramMap.put("shopId", shopId);
        paramMap.put("catName", catName);
        paramMap.put("goodsTypeId", goodsTypeId);
        List<GoodsCategory> goodsCategories = null;
        try {
            goodsCategories = goodsCategoryDao.select(paramMap);
        } catch (Exception e) {
            log.error("[DB]获取配件分类异常 异常信息:{}", e);
            return Optional.absent();
        }
        if (CollectionUtils.isEmpty(goodsCategories)) {
            return Optional.absent();
        }

        return Optional.fromNullable(goodsCategories.get(0));
    }

    @Override
    public int save(GoodsCategory goodsCategory) {
        return goodsCategoryDao.insert(goodsCategory);
    }
}
