package com.tqmall.legend.biz.bo.goods;

import com.tqmall.itemcenter.object.result.goods.GoodsAttrRelDTO;
import com.tqmall.itemcenter.object.result.goods.GoodsCarDTO;
import com.tqmall.legend.entity.goods.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 配件实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsBo implements Serializable {

    private static final long serialVersionUID = -445819406019429745L;

    // 归属门店
    private Long shopId;
    // 当前操作人
    private Long userId;
    // 基本信息
    private Goods goods;
    // 配件属性
    private List<GoodsAttrRelDTO> goodsAttrRelList;
    // 适用车型
    private List<GoodsCarDTO> goodsCarList;

}
