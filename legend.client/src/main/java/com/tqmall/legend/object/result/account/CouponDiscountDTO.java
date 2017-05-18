package com.tqmall.legend.object.result.account;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xin on 2017/3/9.
 */
@Getter
@Setter
public class CouponDiscountDTO implements Serializable {
    private Long accountCouponId;
    private String couponName;
    private Integer couponType;
    private String typeName; // 类型
    private Date effectiveDate; // 生效时间
    private Date expireDate; // 失效时间
    private String description; // 描述
    private Integer useRange;//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private String useRangeDescript;
    private Long flowId;
}
