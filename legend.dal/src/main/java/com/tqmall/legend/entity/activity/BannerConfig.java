package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/2/24.
 * banner图配置
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BannerConfig extends BaseEntity {

    private String imgUrl;//活动banner图url
    private String jumpUrl;//活动banner图跳转url
    private Integer position;//所在位置：1引流活动
    private Long sort;//排序，默认降序
}

