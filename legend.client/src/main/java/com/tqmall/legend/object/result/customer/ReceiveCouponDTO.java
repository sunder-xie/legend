package com.tqmall.legend.object.result.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 领优惠券成功后返回结果,包含券实例信息和券类型信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiveCouponDTO implements Serializable{
    private static final long serialVersionUID = -4876797985652517196L;
    //券实例信息 start
    private String couponCode;//优惠劵码
    private Integer couponSource;//来源：0充值1赠送
    private Integer usedStatus;//是否使用0未使用1已使用
    private Date effectiveDate;//生效时间
    private Date expireDate;//失效时间
    //券实例信息 end

    //券类型信息 start
    private String couponName;//优惠劵名称
    private Integer useRange;//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private Integer customizeTime;//是否自定义时间.0:非自定义时间;1.自定义时间
    private Integer compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private Integer compatibleWithOtherAccount;//允许他人使用.0:不允许;1:允许
    private Integer compatibleWithOtherCoupon;//允许与其他优惠券共同使用.0:不允许;1:允许
    private Integer singleUse;//一张工单只允许使用一张优惠券.0:不允许;1:允许
    private BigDecimal discountAmount;//抵扣金额
    private BigDecimal amountLimit;//金额限制.0:无金额限制
    private Integer couponStatus;//状态1上架2下架
    //券类型信息 end

    //微信端需要用到的信息 begin
    private String couponTypeDescription;//卡券类型描述(云修侧的使用规则singleUse,compatibleWithCard)
    //微信端需要用到的信息 end
}
