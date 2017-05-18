package com.tqmall.legend.entity.goods;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class GoodsCategory extends BaseEntity {

    private Long goodsTypeId;
    private String catName;
    private Long parentId;
    private Long sort;
    private Long shopId;
    private Long tqmallCatId;
    private Integer tqmallStatus;
    /**
     * 自定义分类
     */
    private Integer stdCatId;

    List<GoodsCategory> childList;

    /**
     * 如果标准分类为空,则为自定义分类
     * @return
     */
    public boolean isCustomCat(){
        return stdCatId == null;
    }

}

