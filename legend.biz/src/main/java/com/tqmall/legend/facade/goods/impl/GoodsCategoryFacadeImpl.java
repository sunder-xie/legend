package com.tqmall.legend.facade.goods.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.goods.GoodsCategoryParam;
import com.tqmall.itemcenter.object.param.goods.GoodsCategorySearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsCustomCategoryDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsCategoryService;
import com.tqmall.legend.biz.account.CardGoodsRelService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.facade.goods.GoodsCategoryFacade;
import com.tqmall.legend.facade.goods.vo.GoodsCategoryVo;
import com.tqmall.legend.object.result.goods.GoodsCategoryDTO;
import com.tqmall.search.dubbo.client.goods.category.param.CategoryParam;
import com.tqmall.search.dubbo.client.goods.category.result.CategoryVO;
import com.tqmall.search.dubbo.client.goods.category.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsy on 16/11/23.
 * 配件分类
 */
@Slf4j
@Service
public class GoodsCategoryFacadeImpl implements GoodsCategoryFacade {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RpcGoodsCategoryService rpcGoodsCategoryService;
    @Autowired
    private CardGoodsRelService cardGoodsRelService;

    /**
     * 配件分类搜索
     *
     * @param key
     * @param customCat 默认true
     * @param shopId
     * @return 3个等级的类别list  如：发动机系统 - 发动机- 机油泵链轮
     */
    @Override
    public List<GoodsCategoryDTO> getGoodsCategoryListForApp(String key, Boolean customCat, Long shopId) {
        List<GoodsCategoryDTO> goodsCategoryDTOList = Lists.newArrayList();
        List<CategoryVO> categoryVOList = getSearchGoodsCategory(key);
        if (!CollectionUtils.isEmpty(categoryVOList)) {
            List<CategoryVO> secondVoList = Lists.newArrayList();
            List<CategoryVO> thirdVoList = Lists.newArrayList();
            for (CategoryVO categoryVO : categoryVOList) {
                List<CategoryVO> secondList = categoryVO.getList();
                secondVoList.addAll(secondList);
            }
            for (CategoryVO categoryVO : secondVoList) {
                List<CategoryVO> thirdList = categoryVO.getList();
                thirdVoList.addAll(thirdList);
            }
            for (CategoryVO categoryVO : thirdVoList) {
                GoodsCategoryDTO goodsCategoryDTO = new GoodsCategoryDTO();
                goodsCategoryDTO.setCustomCat(false);
                goodsCategoryDTO.setName(categoryVO.getName());
                goodsCategoryDTO.setId(categoryVO.getId());
                goodsCategoryDTOList.add(goodsCategoryDTO);
            }
        }
        if (customCat == null || customCat) {
            List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> goodsCategoryList = rpcGoodsCategoryService.selectByNameLike(key, shopId);
            if (!CollectionUtils.isEmpty(goodsCategoryList)) {
                for (com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO goodsCategory : goodsCategoryList) {
                    GoodsCategoryDTO goodsCategoryDTO = new GoodsCategoryDTO();
                    goodsCategoryDTO.setCustomCat(true);
                    goodsCategoryDTO.setName(goodsCategory.getCatName());
                    goodsCategoryDTO.setId(goodsCategory.getId().intValue());
                    goodsCategoryDTOList.add(goodsCategoryDTO);
                }
            }
        }
        return goodsCategoryDTOList;
    }

    @Override
    public List<GoodsCategoryVo> getGoodsCategoryListForPc(String key, Boolean customCat, Long shopId) {
        List<GoodsCategoryVo> GoodsCategoryVoList = Lists.newArrayList();
        List<CategoryVO> categoryVOList = getSearchGoodsCategory(key);
        if (!CollectionUtils.isEmpty(categoryVOList)) {
            for (CategoryVO firstVo : categoryVOList) {
                //一级分类
                GoodsCategoryVo firstGoodsCategoryVo = new GoodsCategoryVo();
                goodsCategoryCopy(firstVo, firstGoodsCategoryVo);
                List<CategoryVO> secondList = firstVo.getList();
                List<GoodsCategoryVo> secondGoodsCategoryVoList = Lists.newArrayList();
                for (CategoryVO secondVo : secondList) {
                    //二级分类
                    GoodsCategoryVo secondFirstGoodsCategoryVo = new GoodsCategoryVo();
                    goodsCategoryCopy(secondVo, secondFirstGoodsCategoryVo);
                    secondGoodsCategoryVoList.add(secondFirstGoodsCategoryVo);
                    List<CategoryVO> thirdList = secondVo.getList();
                    List<GoodsCategoryVo> thirdGoodsCategoryVoList = Lists.newArrayList();
                    for (CategoryVO thirdVo : thirdList) {
                        //三级分类
                        GoodsCategoryVo thirdFirstGoodsCategoryVo = new GoodsCategoryVo();
                        goodsCategoryCopy(thirdVo, thirdFirstGoodsCategoryVo);
                        thirdGoodsCategoryVoList.add(thirdFirstGoodsCategoryVo);
                    }
                    secondFirstGoodsCategoryVo.setList(thirdGoodsCategoryVoList);
                }
                firstGoodsCategoryVo.setList(secondGoodsCategoryVoList);
                GoodsCategoryVoList.add(firstGoodsCategoryVo);
            }

        }
        if (customCat == null || customCat) {
            List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> goodsCategoryList = rpcGoodsCategoryService.selectByNameLike(key, shopId);
            if (!CollectionUtils.isEmpty(goodsCategoryList)) {
                GoodsCategoryVo firstDTO = new GoodsCategoryVo();
                GoodsCategoryVo secondDTO = new GoodsCategoryVo();
                firstDTO.setCustomCat(true);
                secondDTO.setCustomCat(true);
                firstDTO.setName("自定义分类");
                secondDTO.setName("自定义分类");
                List<GoodsCategoryVo> secondList = new ArrayList<>();
                secondList.add(secondDTO);
                firstDTO.setList(secondList);
                List<GoodsCategoryVo> thirdList = new ArrayList<>();
                secondDTO.setList(thirdList);
                for (com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO goodsCategory : goodsCategoryList) {
                    GoodsCategoryVo thirdDTO = new GoodsCategoryVo();
                    thirdDTO.setCustomCat(true);
                    thirdDTO.setName(goodsCategory.getCatName());
                    thirdDTO.setId(goodsCategory.getId().intValue());
                    thirdList.add(thirdDTO);
                }
                GoodsCategoryVoList.add(firstDTO);
            }
        }
        return GoodsCategoryVoList;
    }

    private void goodsCategoryCopy(CategoryVO firstVo, GoodsCategoryVo firstGoodsCategoryVo) {
        firstGoodsCategoryVo.setId(firstVo.getId());
        firstGoodsCategoryVo.setName(firstVo.getName());
        firstGoodsCategoryVo.setVehicleCode(firstVo.getVehicleCode());
        firstGoodsCategoryVo.setCustomCat(false);
    }

    private List<CategoryVO> getSearchGoodsCategory(String key) {
        CategoryParam categoryParam = new CategoryParam();
        categoryParam.setVehicleCode("C");
        categoryParam.setSize(2000);
        categoryParam.setQ(key);
        com.tqmall.search.common.result.Result<Page<CategoryVO>> goodsCategoryResult = categoryService.getGoodsCategory(categoryParam);
        if (!goodsCategoryResult.isSuccess() || goodsCategoryResult.getData() == null) {
            throw new BizException("类别查询失败");
        }
        return goodsCategoryResult.getData().getContent();
    }


    @Override
    public com.tqmall.core.common.entity.Result<DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>> getGoodsCategoriesFromSearch(GoodsCategorySearchParam goodsCategorySearchParam, Pageable pageable) {
        //
        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        PagingResult<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> pagingResult = rpcGoodsCategoryService.getGoodsCategoriesFromSearch(goodsCategorySearchParam);
        if (!pagingResult.isSuccess()){
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(new DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>(new ArrayList<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>(),pageable,0));
        }
        int total = pagingResult.getTotal();
        DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> defaultPage = new DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>(pagingResult.getList(), pageRequest, total);
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(defaultPage);
    }

    @Override
    public Result<String> delete(GoodsCategoryParam goodsCategoryParam) {
        return rpcGoodsCategoryService.delete(goodsCategoryParam);
    }

    @Override
    public List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> getCustomCategoriesByShopId(Long shopId) {
        return rpcGoodsCategoryService.getCustomCategoriesByShopId(shopId);
    }

    @Override
    public List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(String catName, Long shopId) {
        return rpcGoodsCategoryService.select(catName, shopId);
    }

    @Override
    public Optional<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(String catName, Long shopId, Long goodsTypeId) {
        return rpcGoodsCategoryService.select(catName, shopId, goodsTypeId);
    }

    @Override
    public List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(Long shopId, int tqmallStatus, String... catNames) {
        return rpcGoodsCategoryService.select(shopId, tqmallStatus, catNames);
    }

    @Override
    public void batchSave(List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> goodsCategoryDTOs) {
        rpcGoodsCategoryService.batchSave(goodsCategoryDTOs);
    }

    @Override
    public String save(GoodsCategoryParam goodsCategoryParam) {
        com.tqmall.core.common.entity.Result<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> result = rpcGoodsCategoryService.saveAndUpdate(goodsCategoryParam);
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return "配件类别保存成功";
    }

    @Override
    public String delete(Long shopId, Long id) {
        List<CardGoodsRel> cardGoodsRels = cardGoodsRelService.selectCardGoodsByCatId(shopId, id);
        if (!CollectionUtils.isEmpty(cardGoodsRels)){
            throw new BizException("有会员卡关联此配件类别不能删除");
        }
        GoodsCategoryParam goodsCategoryParam = new GoodsCategoryParam();
        goodsCategoryParam.setId(id);
        goodsCategoryParam.setShopId(shopId);
        com.tqmall.core.common.entity.Result<String> result = rpcGoodsCategoryService.delete(goodsCategoryParam);
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return "配件类别删除成功";
    }
}
