package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动模板字段配置类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarketingColumnConfig extends BaseEntity {
    private Long templateId;//门店模板id
    private Integer name; //shop表中的name字段，0：未配置，1：配置
    private Integer address;//shop表中的地址字段，0：未配置，1：配置
    private Integer mobile;//shop表中的mobile字段，0：未配置，1：配置
    private Integer serviceName;//shop_service_info表中的name字段，0：未配置，1：配置
    private Integer serviceNote;//shop_service_info表中的service_note字段，0：未配置，1：配置
    private Integer servicePrice;//shop_service_info表中的service_price字段，0：未配置，1：配置

}
