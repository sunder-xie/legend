package com.tqmall.legend.facade.magic.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * 钣喷中心与类型关联VO对象
 * Created by shulin on 2017/2/27.
 */
@Setter
@Getter
public class BPShopTagRelVo {
    private Long id;
    private Long shopId;//门店id
    private String shopName;//门店名称
    private Long tagId;//共享中心类型id
    private String tagName;//共享中心店类型名称
    private Long city;//市id
    private String cityName;//城市名称
}
