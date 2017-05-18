package com.tqmall.legend.object.result.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/11/22.
 */
@Getter
@Setter
public class ShopServiceCateDTO implements Serializable{
    private static final long serialVersionUID = 5628457173675308244L;

    private Long id;//主键id
    private String name;//类别名称
    private Long shopId;//门店id
    private Long parentId;//父类id
    private Integer cateType;//类别类型，0门店服务，1淘汽、车主服务，2，标准服务类别
    private Integer cateTag;//标准服务标签，默认0，标准7大类：1保养2洗车3美容4检修5钣喷6救援7其他
    private String iconUrl; //类型图标url
    private String defaultImgUrl;//该类目下服务的默认图片;
    private Integer cateSort;//类别排序,默认降序
}
