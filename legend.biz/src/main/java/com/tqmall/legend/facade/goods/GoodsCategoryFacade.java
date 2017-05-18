package com.tqmall.legend.facade.goods;

import com.google.common.base.Optional;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.itemcenter.object.param.goods.GoodsCategoryParam;
import com.tqmall.itemcenter.object.param.goods.GoodsCategorySearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsCustomCategoryDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.goods.vo.GoodsCategoryVo;
import com.tqmall.legend.object.result.goods.GoodsCategoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zsy on 16/11/23.
 * 配件分类
 */
public interface GoodsCategoryFacade {

    /**
     * 根据名称搜索配件类别
     *
     * 包含：电商类目搜索、门店自定义类目搜索,不存在3级
     * @param key
     * @param customCat 默认true
     * @param shopId
     * @return
     */
    List<GoodsCategoryDTO> getGoodsCategoryListForApp(String key, Boolean customCat, Long shopId);

    /**
     * 根据名称搜索配件类别
     *
     * 包含：电商类目搜索、门店自定义类目搜索，存在3级分类
     * @param key
     * @param customCat
     * @param shopId
     * @return
     */
    List<GoodsCategoryVo> getGoodsCategoryListForPc(String key, Boolean customCat, Long shopId);

    /**
     * 保存更新自定义配件类别
     * @return
     */
    String save(GoodsCategoryParam goodsCategoryParam);

    /**
     * 删除自定义配件类别
     * @param shopId
     * @param id
     * @return
     */
    String delete(Long shopId,Long id);


    /**
     * 自定义配件类别搜索
     * @param goodsCategory
     * @param pageable
     * @return
     */
    com.tqmall.core.common.entity.Result<DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>> getGoodsCategoriesFromSearch(GoodsCategorySearchParam goodsCategorySearchParam, Pageable pageable);

    /**
     * 配件类别删除
     *
     * @param goodsCategoryParam
     * @return
     */
    com.tqmall.core.common.entity.Result<String> delete(GoodsCategoryParam goodsCategoryParam);

    /**
     * 根据门店id获取自定义分类
     *
     * @param shopId
     * @return
     */
    List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> getCustomCategoriesByShopId(Long shopId);

    /**
     * 获取商品类别
     *
     * @param catName 配件分类名
     * @param shopId  门店id
     * @return
     */
    List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(String catName, Long shopId);


    /**
     * 获取商品类别
     *
     * @param catName     配件分类名
     * @param shopId      门店id
     * @param goodsTypeId 配件类型
     * @return
     */
    Optional<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(String catName, Long shopId, Long goodsTypeId);


    /**
     * 获取配件类别
     *
     * @param shopId       门店ID
     * @param tqmallStatus 配件来源
     * @param catNames     配件名称集合
     * @return
     */
    List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> select(Long shopId, int tqmallStatus, String... catNames);

    /**
     * 批量保存配件
     *
     * @param goodsCategoryDTOs
     */
    void batchSave(List<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO> goodsCategoryDTOs);

}
