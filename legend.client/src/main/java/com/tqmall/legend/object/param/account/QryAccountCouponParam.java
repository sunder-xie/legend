package com.tqmall.legend.object.param.account;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wushuai on 2016/07/12.
 */
@Data
public class QryAccountCouponParam implements Serializable {
    private static final long serialVersionUID = -7493840166838306943L;
    private Long userGlobalId;
    private String mobile;//车主电话
    private Integer usedStatus;//是否使用0未使用1已使用
    private Long offset;
    private Integer limit;
    private Integer expireStatus;//1.未过期,2.已过期
}