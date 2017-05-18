package com.tqmall.legend.object.param.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/11/23.
 */
@Getter
@Setter
public class GoodsSearchParam implements Serializable {

    private static final long serialVersionUID = -7387268625616040989L;

    private Long shopId;
    private String goodsSn;
    private String goodsName;
    private String goodsFormat;
    private String goodsType;
    private String carInfoLike;     //适配车型
    private String goodsCatLike;    //配件类型名称
    private Integer brandId;        //配件品牌id
    private Integer zeroStockRange; //库存状态
    private Integer onsaleStatus;   //上架状态
    private String keyword;         //关键词(配件名称、配件编号、零件号)goods_sn_like,name_like,format_like
    private Integer size = 10;      //数量默认10
    private Integer page = 1;       //页面默认1
    private List<String> sortList;  //排序，如：id:desc  gmt_create:asc
}
