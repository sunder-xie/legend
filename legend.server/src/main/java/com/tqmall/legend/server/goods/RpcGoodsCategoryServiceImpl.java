package com.tqmall.legend.server.goods;

import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.result.goods.GoodsHotCategoryDTO;
import com.tqmall.legend.facade.goods.GoodsCategoryFacade;
import com.tqmall.legend.facade.goods.GoodsHotCategoryFacade;
import com.tqmall.legend.object.result.goods.GoodsCategoryDTO;
import com.tqmall.legend.service.goods.RpcGoodsCategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by zsy on 16/11/22.
 */
@Service("rpcGoodsCategoryService")
public class RpcGoodsCategoryServiceImpl implements RpcGoodsCategoryService {
    @Autowired
    private GoodsCategoryFacade goodsCategoryFacade;
    @Autowired
    private GoodsHotCategoryFacade goodsHotCategoryFacade;

    /**
     * 根据门店id获取配件类别
     *
     * @param shopId
     * @param nameLike  配件类别模糊查询  可不填
     * @param customCat 配件类别是否查询自定义 可不填（true：查询自定义，false：不查询），默认查询自定义类别
     * @return
     */
    @Override
    public Result<List<GoodsCategoryDTO>> getGoodsCategoryList(final Long shopId, final String nameLike, final Boolean customCat) {
        return new ApiTemplate<List<GoodsCategoryDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<GoodsCategoryDTO> process() throws BizException {
                List<GoodsCategoryDTO> shopServiceCateDTOList = goodsCategoryFacade.getGoodsCategoryListForApp(nameLike, customCat, shopId);
                return shopServiceCateDTOList;
            }
        }.execute();
    }

    @Override
    public Result<List<GoodsCategoryDTO>> getHotGoodsCategoryList(final Long shopId, final String nameLike) {
        return new ApiTemplate<List<GoodsCategoryDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<GoodsCategoryDTO> process() throws BizException {
                List<GoodsCategoryDTO> goodsCategoryDTOList = Lists.newArrayList();
                List<GoodsHotCategoryDTO> goodsHotCategoryList = goodsHotCategoryFacade.selectByCatNameLike(nameLike);
                if (CollectionUtils.isEmpty(goodsHotCategoryList)) {
                    return goodsCategoryDTOList;
                }
                for (GoodsHotCategoryDTO goodsHotCategory : goodsHotCategoryList) {
                    GoodsCategoryDTO goodsCategoryDTO = new GoodsCategoryDTO();
                    goodsCategoryDTO.setCustomCat(false);
                    goodsCategoryDTO.setName(goodsHotCategory.getCatName());
                    goodsCategoryDTO.setId(goodsHotCategory.getTqmallCatId().intValue());
                    goodsCategoryDTOList.add(goodsCategoryDTO);
                }
                return goodsCategoryDTOList;
            }
        }.execute();
    }
}
