package com.tqmall.legend.entity.tqcheck;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lifeilong on 2016/4/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TqCheckCategory extends BaseEntity {
    private Integer category;   //类目
    private String categoryName;    //类目名称
    private String categoryDetailName;  //检测明细
    private Integer categoryItem;   //子类别
    private String categoryItemName;    //子类别名称
    private Long sort;      //排序
}
