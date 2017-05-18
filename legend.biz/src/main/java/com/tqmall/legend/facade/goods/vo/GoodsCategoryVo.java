package com.tqmall.legend.facade.goods.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 16/12/1.
 */
@Getter
@Setter
public class GoodsCategoryVo {
    private Integer id;//类别id
    private String name;//类别名称
    private String vehicleCode;
    private boolean isCustomCat;//是否是自定义类别,默认不是自定义
    private List<GoodsCategoryVo> list;

    public GoodsCategoryVo(){
        this.isCustomCat = false;
    }
}