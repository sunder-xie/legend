package com.tqmall.legend.facade.service.vo;

import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wushuai on 16/8/2.
 */
@Data
public class ServiceTemplateVo extends ServiceTemplate {
    private ShopServiceInfo shopServiceInfo;
    private Long realAmount;//车主端可预约服务数量
    private Long fakeAmount;//车主端虚拟可预约服务数量

    //砍价
    private BigDecimal floorPrice;//车主端服务底价

    //拼团
    private Long customerNumber;//拼团人数
    private BigDecimal groupPrice;//团购价格
}
