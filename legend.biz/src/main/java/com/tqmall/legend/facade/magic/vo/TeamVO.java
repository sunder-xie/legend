package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by shulin on 16/7/1.
 */
@Data
public class TeamVO {
    private Long id;
    private Long creator;
    private Long modifier;
    private Long shopId;//所属门店id
    private String name;//班组名称
    private String remark;//备注
    List<ShopManagerExtVO> shopManagerExtVOList; //班组对应的员工列表
}
