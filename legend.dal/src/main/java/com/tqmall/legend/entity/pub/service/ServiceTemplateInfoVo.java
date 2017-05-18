package com.tqmall.legend.entity.pub.service;

import com.tqmall.legend.entity.shop.ServiceInfoList;
import com.tqmall.legend.entity.shop.ShopServiceTag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by changqiang.ke on 16/1/26.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceTemplateInfoVo {
    private Long id;                                    //模版id
    private String name;                                //服务名称
    private BigDecimal servicePrice;                    //服务价格
    private String serviceNote;                         //服务描述
    private String imgUrl;                              //蒲公英模板服务小图片地址
    private String thirdImgUrl;                         //微信模板服务图片地址
    private Integer priceType;                          //价格类型
    private List<ShopServiceTag> marketingFlagList;     //营销标
    private List<ShopServiceTag> qualityFlagList;       //品质标
    private Long firstCateId;                           //服务一级类别id
    private String firstCateName;                       //服务一级类别名称
}
