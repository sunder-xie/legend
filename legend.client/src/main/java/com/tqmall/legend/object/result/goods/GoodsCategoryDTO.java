package com.tqmall.legend.object.result.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/11/22.
 * 配件类别
 */
@Getter
@Setter
public class GoodsCategoryDTO implements Serializable{
    private static final long serialVersionUID = -8481873832796392226L;
    private Integer id;//类别id
    private String name;//类别名称
    private boolean isCustomCat;//是否是自定义类别,默认不是自定义
}
