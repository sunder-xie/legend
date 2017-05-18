package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动门店服务基本信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarketingCaseService extends BaseEntity {
    private Long caseId;//门店活动id
    private Long shopId;//门店id
    private Long serviceId;//门店服务id
    private String serviceName; //门店服务名称
    private String serviceNote;//门店服务描述
    private String servicePrice;//门店服务价格
}
