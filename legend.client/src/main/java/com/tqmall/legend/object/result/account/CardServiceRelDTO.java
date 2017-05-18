package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 03/11/2016.
 */
@Data
public class CardServiceRelDTO implements Serializable {
    private Long serviceCatId;//服务项目id
    private String serviceCatName;//服务项目名
    private BigDecimal discount;//服务项目折扣
}
