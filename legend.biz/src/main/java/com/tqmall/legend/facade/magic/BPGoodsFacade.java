package com.tqmall.legend.facade.magic;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.magic.vo.GoodsExtVo;

/**
 * 业务场景：设置-配件资料-新增/更新油漆资料
 * Created by shulin on 16/11/11.
 */
public interface BPGoodsFacade {
    /**
     * 添加油漆资料-钣喷专用
     *
     * @param goodsExtVo
     * @return
     */
    boolean addBPGoodsInfo(GoodsExtVo goodsExtVo);

    /**
     * 修改油漆资料-钣喷专用
     *
     * @param goodsExtVo
     * @return
     */
    boolean modifyBPGoodsInfo(GoodsExtVo goodsExtVo);

    /**
     * 删除油漆资料-钣喷专用
     * @param id
     * @param shopId
     * @return
     */
    boolean removeBPGoodsInfo(Long id,Long shopId);


    /**
     * 判断物料资料是否是钣喷油漆资料，主要用于从电商获取数据添加配件资料的时候判断
     * @param goodsId 电商表中的ID
     * @return
     */
    Result isBPGoods(Long goodsId);


}
