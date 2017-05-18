package com.tqmall.legend.entity.marketing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class MarketingShopRel extends BaseEntity {

    private Long shopId;
    private Long smsNum=0L;

    //短信账户超过同行比率，100%表示超过所有的
    private String smsCompareRate;
}

