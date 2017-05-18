package com.tqmall.legend.entity.account;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class CardServiceRel extends BaseEntity {

    private Long shopId;//门店id
    private Long serviceCatId;//服务项目id
    private String serviceCatName;//服务项目名
    private BigDecimal discount;//服务项目折扣
    private Long cardInfoId;//会员卡类型id

}