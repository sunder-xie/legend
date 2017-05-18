package com.tqmall.legend.object.result.appoint;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zsy on 16/3/26.
 */
@Data
public class ShopServiceDTO implements Serializable{
    private static final long serialVersionUID = -6683778811855933524L;

    private Long shopId;//门店id
    private String name;//门店名称
    private String address;//门店详细地址
    private String userGlobalId;
    private Long city;//市
    private Long province;//省
    private Long district;//街
    private Long street;//区
    private Integer priceType;//服务类型：1 正常价格数值显示 2 到店洽谈 3 免费
    private BigDecimal servicePrice;//服务价格
    private Long serviceId;//服务id
    private Long serviceTplId;//模板服务id
}
