package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 辉辉大侠 on 8/29/16.
 */
@Data
public class CouponInfo4WechatDTO implements Serializable {
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券类型id
     */
    private Long id;
    /**
     * 优惠券描述信息
     */
    private String remark;
    /**
     * 允许与会员卡共同使用.0:不允许;1:允许
     */
    private Integer compatibleWithCard;
    /**
     * 一张工单只允许使用一张优惠券.0:不允许;1:允许
     */
    private Integer singleUse;
    /**
     * 金额限制.0:无金额限制
     */
    private BigDecimal amountLimit;
    /**
     * 使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
     */
    private Integer useRange;
    /**
     * 使用范围文字描述
     */
    private String useRangeDesc;
    /**
     * 使用范围文字描述详情
     */
    private String useRangeDescDetail;

    /**
     * 是否自定义时间.0:非自定义时间;1.自定义时间
     */
    private Integer customizeTime;
    /**
     *有效期
     */
    private Integer effectivePeriodDays;
    /**
     * 生效时间
     */
    private Date effectiveDate;
    /**
     * 失效时间
     */
    private Date expireDate;//失效时间

    /**
     * 抵扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 是否通用券
     */
    private boolean universal;
}
