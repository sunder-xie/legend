package com.tqmall.legend.facade.service.vo;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 微信活动服务Vo
 * Created by wushuai on 16/10/29.
 */
@Data
public class WechatActServiceVo extends ShopServiceInfo {
    //公共
    private Long realAmount;//可预约服务数量
    private Long fakeAmount;//虚拟可预约服务数量

    //砍价
    private BigDecimal floorPrice;//服务底价

    //拼团
    private BigDecimal groupPrice;//团购价格
    private Long customerNumber;//拼团人数
}
