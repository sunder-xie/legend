package com.tqmall.legend.facade.goods;

import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.goods.GoodsUnitSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsUnitDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 配件单位
 */
public interface GoodsUnitFacade {

    /**
     * 根据单位名称 模糊查询配件单位
     * <p>
     * '%unitNameLike%'
     *
     * @param unitNameLike 单位名称
     * @param shopId       门店ID
     * @return
     */
    List<GoodsUnitDTO> selectByNameLike(String unitNameLike, Long shopId);

    /**
     * 如果没有重复记录就添加
     *
     * @param goodsUnitDTO
     * @return
     */
    Result addWithoutRepeat(GoodsUnitDTO goodsUnitDTO);

    /**
     * 判断配件单位是否存在
     *
     * @param measureUnitName 单位名称
     * @return {'true':存在;'false':不存在}
     */
    boolean checkGoodsUnitIsExist(String measureUnitName, Long shopId);

    /**
     * 删除门店配件单位
     *
     * @param shopId
     * @return
     */
    boolean deleteGoodsUnit(Long shopId);

    /**
     * 根据id删除配件单位
     *
     * @param ids
     * @return
     */
    boolean deleteGoodsUnitByIds(List<Long> ids);

    /**
     * 配件单位分页
     *
     * @param goodsUnitSearchParam
     * @return
     */
    Result<DefaultPage<GoodsUnitDTO>> getPage(GoodsUnitSearchParam goodsUnitSearchParam, Pageable pageable);

}
