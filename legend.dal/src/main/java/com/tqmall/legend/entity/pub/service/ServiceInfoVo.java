package com.tqmall.legend.entity.pub.service;

import com.tqmall.legend.entity.shop.ShopServiceTag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jason on 15/7/16.
 * 车主服务套餐接口对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceInfoVo {

    //一级类目ID
    private Long firstCateId;
    //一级类目名称
    private String firstCateName;
    //二级类目ID
    private Long secondCateId;
    //二级类目名称
    private String secondCateName;
    //服务套餐名称
    private String serviceSuiteName;
    //服务套餐价格
    private BigDecimal servicePrice;
    //是否推荐
    private Integer isRecommend;
    //服务内容
    private String serviceNote;
    //user_global_id
    private String userGlobalId;
    //flags='CZFW'车主服务 ='TQFW' 淘汽服务
    private String flags;
    private Long serviceId;
    //服务图片
    private String imgUrl;
    //第三方服务图片
    private String thirdImgUrl;
    //服务价格类型 1 正常价格数值显示 2 到店洽谈 3 免费
    private Long priceType;
    //排序
    private Long sort;
    //营销标
    private List<ShopServiceTag> marketingFlagList;
    //品质标
    private List<ShopServiceTag> qualityFlagList;
    //-1 TQFW不参加 0 参加活动
    private Long status;



}
