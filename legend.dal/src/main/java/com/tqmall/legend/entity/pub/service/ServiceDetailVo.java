package com.tqmall.legend.entity.pub.service;

import com.tqmall.legend.entity.shop.ShopServiceTag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jason on 15/8/24.
 * app端商家详情接口对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceDetailVo {

    //服务ID
    private Long serviceId;
    //userGlobalID
    private String userGlobalId;
    //服务名称
    private String serviceName;
    private String imgUrl;//服务小图
    //服务信息json
    private String serviceInfo;
    //服务说明
    private String serviceNote;
    //价格
    private BigDecimal servicePrice;
    private BigDecimal marketPrice;//市场价
    //服务价格类型 1 正常价格数值显示 2 到店洽谈 3 免费
    private Long priceType;
    //营销标
    private List<ShopServiceTag> marketingFlagList;
    //品质标
    private List<ShopServiceTag> qualityFlagList;
    //服务状态
    private Long status;//状态 0有效 -1无效
    //服务标志 TQFW淘汽服务 CZFW车主服务
    private String flags;
    //第三方（如橙牛）模板服务图片详细信息，只存图片url的数组json字符串
    private String thirdServiceInfo;

}
