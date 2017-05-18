package com.tqmall.legend.entity.pub.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Created by jason on 15/8/18.
 * 橙牛服务套餐门店接口对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopServiceCnVo {
    //user_global_id
    private String userGlobalId;
    //服务ID
    private Long serviceId;
    //第三方服务图片
    private String thirdImgUrl;
    //淘汽服务价格
    private BigDecimal servicePrice;



}
