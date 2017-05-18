package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.facade.goods.GoodsBrandFacade;
import com.tqmall.legend.facade.goods.GoodsCategoryFacade;
import com.tqmall.legend.web.fileImport.vo.GoodsImportContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class GoodsImportProcess implements FileImportProcess {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    GoodsBrandFacade goodsBrandFacade;
    @Autowired
    GoodsCategoryFacade goodsCategoryFacade;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()){
            log.debug("This is GoodsImportProcess");
        }

        GoodsImportContext goodsImportContext = (GoodsImportContext) fileImportContext;
        Long shopId = goodsImportContext.getShopId();
        List<Goods> goodses = goodsImportContext.getExcelContents();
        Map<String, GoodsBrandDTO> goodsBrandMap = goodsImportContext.getGoodsBrandMap();
        Map<String, GoodsCategoryDTO> goodsCategoryHashMap = goodsImportContext.getGoodsCategoryHashMap();

        if (!CollectionUtils.isEmpty(goodses)) {
            goodsCategoryHashMap = getGoodsCategoryMap(shopId, goodsCategoryHashMap);
            goodsBrandMap = getGoodsBrandMap(shopId, goodsBrandMap);
            for (Goods good : goodses) {
                String catName = good.getGoodsCat();
                String brandName = good.getBrandName();
                if (good.getCatId() == null && goodsCategoryHashMap.containsKey(catName)) {
                    good.setCatId(goodsCategoryHashMap.get(catName).getId());
                }
                if (good.getBrandId() == null && goodsBrandMap.containsKey(brandName)) {
                    good.setBrandId(goodsBrandMap.get(brandName).getId());
                }
                good.setCat2Name(catName);
            }
            goodsService.batchSave(goodses);
            goodsImportContext.setSuccessNum(goodses.size());
        }
    }

    private Map<String, GoodsCategoryDTO> getGoodsCategoryMap(Long shopId, Map<String, GoodsCategoryDTO> goodsCategoryHashMap) {
        if (!goodsCategoryHashMap.isEmpty()) {
            List<GoodsCategoryDTO> goodsCategories = Lists.newArrayList();
            List<String> catNames = Lists.newArrayList();
            for (Map.Entry<String, GoodsCategoryDTO> goodsCategoryEntry : goodsCategoryHashMap.entrySet()) {
                goodsCategories.add(goodsCategoryEntry.getValue());
                catNames.add(goodsCategoryEntry.getKey());
            }
            goodsCategoryFacade.batchSave(goodsCategories);
            List<GoodsCategoryDTO> goodsCategoryList = goodsCategoryFacade.select(shopId, 2, catNames.toArray(new String[]{}));
            goodsCategoryHashMap = Maps.uniqueIndex(goodsCategoryList, new Function<GoodsCategoryDTO, String>() {
                @Override
                public String apply(GoodsCategoryDTO input) {
                    return input.getCatName();
                }
            });
        }
        return goodsCategoryHashMap;
    }

    private Map<String, GoodsBrandDTO> getGoodsBrandMap(Long shopId, Map<String, GoodsBrandDTO> goodsBrandMap) {
        if (!goodsBrandMap.isEmpty()) {
            List<GoodsBrandDTO> goodsBrands = Lists.newArrayList();
            List<String> goodsBrandNames = Lists.newArrayList();
            for (Map.Entry<String, GoodsBrandDTO> brandEntry : goodsBrandMap.entrySet()) {
                goodsBrands.add(brandEntry.getValue());
                goodsBrandNames.add(brandEntry.getKey());
            }
            goodsBrandFacade.batchSave(goodsBrands);
            List<GoodsBrandDTO> goodsBrandList = goodsBrandFacade.selectByBrandNames(shopId, goodsBrandNames.toArray(new String[]{}));

            goodsBrandMap = Maps.uniqueIndex(goodsBrandList, new Function<GoodsBrandDTO, String>() {
                @Override
                public String apply(GoodsBrandDTO input) {
                    return input.getBrandName();
                }
            });
        }
        return goodsBrandMap;
    }
}
