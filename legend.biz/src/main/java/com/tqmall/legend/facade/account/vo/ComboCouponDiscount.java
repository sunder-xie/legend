package com.tqmall.legend.facade.account.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 计次卡和优惠券优惠
 */
@Getter
@Setter
public class ComboCouponDiscount {
    private Long id;
    private String typeName; // 类型
    private String name; // 名称
    private int num; // 卡券数
    private String expireDateStr; // 失效时间
}