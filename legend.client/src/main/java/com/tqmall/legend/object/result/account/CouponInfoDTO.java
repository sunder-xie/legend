package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wanghui on 2016/07/12.
 */
@Data
public class CouponInfoDTO implements Serializable {

    private static final long serialVersionUID = 3426101953774774205L;
    private Long id;
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券;2:通用券
    private String couponTypeName;
    private String couponName;//优惠劵名称
    private BigDecimal discount;//折扣
    private Integer useRange;//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private Integer customizeTime;//是否自定义时间.0:非自定义时间;1.自定义时间
    private Integer effectivePeriodDays;//有效期(天)
    private Date effectiveDate;//生效时间
    private Date expireDate;//失效时间
    private Integer compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private Integer compatibleWithOtherAccount;//允许他人使用.0:不允许;1:允许
    private Integer compatibleWithOtherCoupon;//允许与其他优惠券共同使用.0:不允许;1:允许
    private Integer singleUse;//一张工单只允许使用一张优惠券.0:不允许;1:允许
    private BigDecimal discountAmount;//抵扣金额
    private BigDecimal amountLimit;//金额限制.0:无金额限制
    private Integer couponStatus;//状态1上架2下架
    private String description;//描述,包括1.全场通用或是只限服务工时;2.满xxx元使用;3.是否允许使用多张优惠券;4.是否允许与会员卡共同使用;

}

