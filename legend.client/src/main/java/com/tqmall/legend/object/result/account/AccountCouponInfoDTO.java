package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wushuai on 2016/07/12.
 */
@Data
public class AccountCouponInfoDTO implements Serializable{

    private static final long serialVersionUID = 3531380875204991176L;
    private Long id;
    private Date gmtModified;
    private String couponCode;//优惠劵码
    private Integer couponSource;//来源：0充值1赠送
    private Date effectiveDate;//生效时间
    private String effectiveDateStr;
    private Date expireDate;//失效时间
    private String expireDateStr;
    private Integer usedStatus;//是否使用0未使用1已使用
    private Long accountId;//账户id
    private Long couponInfoId;//优惠劵id
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券
    private String couponName;//优惠劵名称
    private Long suiteId;//来源某个套餐
    private Long flowId;//充值流水id
    private String flowSn;//流水号
    private String operatorName;//操作人
    private Integer couponNum;//优惠券张数
    private String suiteName;//套餐名称

    private CouponInfoDTO couponInfoDTO;//优惠券类型信息
}
