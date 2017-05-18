package com.tqmall.legend.entity.settlement;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class Payment extends BaseEntity {

    private Long shopId;
    private String name;
    private Integer paymentTag;//标准结算类目1现金2刷卡3会员卡4第三方支付5转账6支票7其它，1优先级最高
    private Integer showStatus;//收款方式显示状态，0不展示，1展示
}


