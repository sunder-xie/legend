package com.tqmall.legend.facade.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandParam;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zsy on 16/11/25.
 */
public interface GoodsBrandFacade {

    /**
     * 门店品牌搜索 根据first_letter顺序排序
     *
     * @param shopId
     * @return
     */
    List<GoodsBrandDTO> getGoodsBrands(Long shopId);


    /**
     * 如果没有重复记录就添加
     *
     * @param goodsBrand
     * @return
     */
    GoodsBrandDTO addWithoutRepeat(GoodsBrandDTO goodsBrand);


    /**
     * 添加自定义品牌
     *
     * @param goodsBrand
     * @return
     */
    Long addDefinedBrand(GoodsBrandDTO goodsBrand);


    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    GoodsBrandDTO selectById(Long id);


    /**
     * 根据门店id和品牌集合，获取配件品牌列表
     *
     * @param shopId     门店id
     * @param brandNames 品牌名称集合
     * @return
     */
    List<GoodsBrandDTO> selectByBrandNames(Long shopId, String... brandNames);


    /**
     * 保存品牌
     *
     * @param goodsBrands 配件品牌集合
     */
    void batchSave(List<GoodsBrandDTO> goodsBrands);

    /**
     * 自定义配件品牌名称搜索
     * @param goodsBrand
     * @param pageable
     * @return
     */
    Result<DefaultPage<GoodsBrandDTO>> getGoodsBrandsFromSearch(GoodsBrandSearchParam goodsBrandSearchParam,Pageable pageable);

    /**
     * 保存更新自定义配件品牌
     * @return
     */
    String save(GoodsBrandParam goodsBrandParam);

    /**
     * 删除自定义配件品牌
     * @param shopId
     * @param id
     * @return
     */
    String delete(Long shopId,Long id);

}
