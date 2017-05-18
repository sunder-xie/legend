package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceGoodsSuite extends BaseEntity {

    private String suiteName;
    private Long serviceId;
    private String serviceInfo;
    private String goodsInfo;
    private Long goodsNumber;
    private BigDecimal suitePrice;
    private Long shopId;

}

