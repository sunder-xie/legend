package com.tqmall.legend.entity.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class AccountComboServiceRel extends BaseEntity {

    private Long shopId;//门店id
    private Long comboId;//计次卡实例id
    private Long serviceId;//服务项目id
    private String serviceName;//服务项目名
    private Integer totalServiceCount;//总服务项目次数
    private Integer usedServiceCount;//已使用的服务项目次数
    private Integer leftServiceCount;

    public Integer getLeftServiceCount() {
        if (leftServiceCount != null) {
            return leftServiceCount;
        } else {
            return totalServiceCount - usedServiceCount;
        }
    }

}

