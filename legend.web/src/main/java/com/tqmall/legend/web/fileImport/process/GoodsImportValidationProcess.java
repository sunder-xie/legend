package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO;
import com.tqmall.legend.biz.goods.GoodsCategoryService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsCategory;
import com.tqmall.legend.facade.goods.GoodsBrandFacade;
import com.tqmall.legend.facade.goods.GoodsCategoryFacade;
import com.tqmall.legend.web.fileImport.vo.GoodsImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/12/10.
 */
@Slf4j
@Component
public class GoodsImportValidationProcess implements FileImportProcess {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    GoodsBrandFacade goodsBrandFacade;
    @Autowired
    GoodsCategoryFacade goodsCategoryFacade;
    @Autowired
    private GoodsCategoryService goodsCategoryService;

    static final String DEFAULT_CAT_NAME = "自定义配件类别";

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is GoodsImportValidationProcess");
        }
        GoodsImportContext goodsImportContext = (GoodsImportContext) fileImportContext;
        Long shopId = goodsImportContext.getShopId();
        Long userId = goodsImportContext.getUserId();
        List<Goods> goodses = goodsImportContext.getExcelContents();
        List<String> faildMessages = goodsImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = goodsImportContext.getRowFaildMessages();

        List<String> getGoodFormats = getGoodFormats(goodses, shopId);
        Map<String, GoodsCategoryDTO> goodsCategoryMap = getGoodsCategoryMap(goodses, shopId);

        Map<String, GoodsBrandDTO> goodBrandsMap = getGoodsBrandMap(goodses, shopId);

        int goodNum = goodsService.selectByShopId(shopId).size();

        List<Goods> goods = Lists.newArrayList();
        /*需要批量新加的配件分类信息*/
        Map<String, GoodsCategoryDTO> goodsCategoryHashMap = Maps.newHashMap();
        /*需要批量新增的配件品牌信息*/
        Map<String, GoodsBrandDTO> goodsBrandMap = Maps.newHashMap();

        Map<String, Goods> goodsParamMap = Maps.newHashMap();
        for (int i = 0; i < goodses.size(); i++) {
            Goods good = goodses.get(i);
            int rowNumber = good.getRowNumber() + 1;
            //判断导入的配件名_配件型号组合，在数据库中是否存在
            String goodNameFormat = good.getFormat();
            if (goodsParamMap.containsKey(goodNameFormat) || getGoodFormats.contains(goodNameFormat)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, goodNameFormat, "零件号");
                String faildMes = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, goodNameFormat, "零件号");
                faildMessages.add(faildMessage);
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                continue;
            }
            good.setShopId(shopId);
            good.setCreator(userId);
            good.setModifier(userId);
            good.setTqmallStatus(4);
            setCatId(goodsCategoryMap, goodsCategoryHashMap, good);
            setGoodsBrand(goodBrandsMap, goodsBrandMap, good);
            String goodsSn = goodsService.generatGoodsSn(i, goodNum);
            good.setGoodsSn(goodsSn);
            goods.add(good);
            goodsParamMap.put(goodNameFormat, good);
        }

        goodsImportContext.setExcelContents(goods);
        goodsImportContext.setGoodsBrandMap(goodsBrandMap);
        goodsImportContext.setGoodsCategoryHashMap(goodsCategoryHashMap);
    }

    private void setRowFalidMessage(Map<Integer, List<String>> rowFaildMessages, int rowNumber, String faildMessage) {
        if (rowFaildMessages.containsKey(rowNumber)) {
            List<String> faild = rowFaildMessages.get(rowNumber);
            faild.add(faildMessage);
        } else {
            List<String> failds = Lists.newArrayList();
            failds.add(faildMessage);
            rowFaildMessages.put(rowNumber, failds);
        }
    }

    private List<String> getGoodFormats(List<Goods> goodsParams, Long shopId) {
        List<String> goodsFormat = Lists.transform(goodsParams, new Function<Goods, String>() {
            @Override
            public String apply(Goods input) {
                return input.getFormat();
            }
        });
        if (CollectionUtils.isEmpty(goodsFormat)) {
            return Lists.newArrayList();
        }
        List<Goods> goods_format = goodsService.findGoodsByFormat(shopId, goodsFormat.toArray(new String[] { }));
        return Lists.transform(goods_format, new Function<Goods, String>() {
            @Override
            public String apply(Goods input) {
                return input.getFormat();
            }
        });
    }

    private Map<String, GoodsCategoryDTO> getGoodsCategoryMap(List<Goods> goodsParams, Long shopId) {
        if (CollectionUtils.isEmpty(goodsParams)) {
            return Maps.newHashMap();
        }
        Set<String> goodsCats = Sets.newHashSet();
        for (Goods goodsParam : goodsParams) {
            goodsCats.add(goodsParam.getGoodsCat());
        }
        List<GoodsCategoryDTO> goodsCategories = goodsCategoryFacade.select(shopId, 2, goodsCats.toArray(new String[]{}));
        Map<String, GoodsCategoryDTO> goodsCategoryMap = Maps.newHashMap();
        for (GoodsCategoryDTO goodsCategory : goodsCategories) {
            goodsCategoryMap.put(goodsCategory.getCatName(), goodsCategory);
        }
        return goodsCategoryMap;
    }

    private Map<String, GoodsBrandDTO> getGoodsBrandMap(List<Goods> goodsParams, Long shopId) {
        if (CollectionUtils.isEmpty(goodsParams)) {
            return Maps.newHashMap();
        }
        List<String> goodsBrand = Lists.transform(goodsParams, new Function<Goods, String>() {
            @Override
            public String apply(Goods input) {
                return input.getBrandName();
            }
        });
        List<GoodsBrandDTO> goodsBrands = goodsBrandFacade.selectByBrandNames(shopId, goodsBrand.toArray(new String[] { }));

        Map<String, GoodsBrandDTO> goodsBrandMap = Maps.newHashMap();
        for (GoodsBrandDTO brand : goodsBrands) {
            goodsBrandMap.put(brand.getBrandName(), brand);
        }
        return goodsBrandMap;
    }

    private void setCatId(Map<String, GoodsCategoryDTO> goodsCatMap, Map<String, GoodsCategoryDTO> goodsCategoryHashMap, Goods good) {
        String catName = good.getGoodsCat();
        Long shopId = good.getShopId();
        Long userId = good.getCreator();

        if (!goodsCatMap.containsKey(catName)) {
            Optional<GoodsCategory> goodsCategoryOptional = goodsCategoryService.select(DEFAULT_CAT_NAME, shopId, 0l);
            GoodsCategory goodsCategory = null;
            if (!goodsCategoryOptional.isPresent()) {
                goodsCategory = new GoodsCategory();
                goodsCategory.setShopId(shopId);
                goodsCategory.setCreator(userId);
                goodsCategory.setParentId(0L);
                goodsCategory.setSort(0L);
                goodsCategory.setTqmallCatId(0L);
                goodsCategory.setTqmallStatus(2);
                goodsCategory.setGoodsTypeId(0L);
                goodsCategory.setCatName(DEFAULT_CAT_NAME);
                goodsCategoryService.save(goodsCategory);
            }else {
                goodsCategory = goodsCategoryOptional.get();
            }
            GoodsCategoryDTO category = new GoodsCategoryDTO();
            category.setShopId(shopId);
            category.setCreator(userId);
            category.setTqmallStatus(2);
            category.setCatName(catName);
            category.setParentId(goodsCategory.getId());
            goodsCategoryHashMap.put(catName, category);
        } else {
            GoodsCategoryDTO goodsCategory = goodsCatMap.get(catName);
            good.setCatId(goodsCategory.getId());
        }
    }

    private void setGoodsBrand(Map<String, GoodsBrandDTO> goodsBrandMap, Map<String, GoodsBrandDTO> goodsBrandHashMap, Goods good) {
        if (StringUtils.isBlank(good.getBrandName())) {
            return;
        }
        Long shopId = good.getShopId();
        Long userId = good.getCreator();

        String goodBrandName = good.getBrandName();
        if (!goodsBrandMap.containsKey(goodBrandName)) {
            GoodsBrandDTO goodsBrand = new GoodsBrandDTO();
            goodsBrand.setShopId(shopId);
            goodsBrand.setCreator(userId);
            goodsBrand.setFirstLetter("");
            goodsBrand.setBrandDesc("");
            goodsBrand.setSort(0);
            goodsBrand.setTqmallStatus(4);
            goodsBrand.setBrandName(goodBrandName);
            goodsBrandHashMap.put(goodBrandName, goodsBrand);
        } else {
            GoodsBrandDTO goodsBrand = goodsBrandMap.get(goodBrandName);
            good.setBrandId(goodsBrand.getId());
        }
    }
}
