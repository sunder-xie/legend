package com.tqmall.legend.entity.customer;

/**
 * Created by jason on 15/7/19.
 */

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class AppointServiceVo extends BaseEntity {

    private Long shopId;
    private Long appointId;
    private String appointSn;
    private Long serviceId;
    private Long parentServiceId;//服务父ID
    private String serviceNote;
    private String serviceName;
    private String categoryName;//服务类别
    private BigDecimal servicePrice;//工时费(服务原价)
    private BigDecimal discountAmount;//服务优惠金额
    private String goodsIdStr;//服务对应的goodsID集合,逗号隔开

    private Long templateId;//服务模板id

    private Long suiteNum;//0对应基本服务,1带物料服务,2.套餐服务

    private ShopServiceInfo shopServiceInfo;

}


