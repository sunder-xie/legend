package com.tqmall.legend.entity.pub.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by jason on 15/8/18.
 * 橙牛服务套餐接口对象
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceInfoCnVo {

    //适配规格
    private List<String> adaptStandardList;
    //门店服务ID对象
    private List<ShopServiceCnVo> ShopServiceCnList;
    //第三方服务图片
    private String thirdImgUrl;
    //淘汽服务名称
    private String serviceName;
    //淘汽服务价格
    private BigDecimal servicePrice;
}
