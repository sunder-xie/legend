package com.tqmall.legend.entity.account;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;
import org.apache.commons.collections.CollectionUtils;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class CouponInfo extends BaseEntity {

    private Long shopId;//门店id
    private Integer couponType;//优惠劵类型,0:折扣卷;1:现金券;2:通用券
    private String couponTypeName;
    private String couponName;//优惠劵名称
    private BigDecimal discount;//折扣
    private Integer useRange;//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private Integer customizeTime;//是否自定义时间.0:非自定义时间;1.自定义时间
    private Integer effectivePeriodDays;//有效期(天)
    private Date effectiveDate;//生效时间
    private String effectiveDateStr;
    private Date expireDate;//失效时间
    private String expireDateStr;
    private Integer compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private Integer compatibleWithOtherAccount;//允许他人使用.0:不允许;1:允许
    private Integer compatibleWithOtherCoupon;//允许与其他优惠券共同使用.0:不允许;1:允许
    private Integer singleUse;//一张工单只允许使用一张优惠券.0:N;1:Y
    private BigDecimal discountAmount;//抵扣金额
    private BigDecimal amountLimit;//金额限制.0:无金额限制
    private Integer couponStatus;//状态1上架2下架
    private List<CouponServiceRel> couponServiceList;//服务
    private Integer usedCount;
    private String gmtCreateStr;
    private Integer grantCount;
    private Integer expireCount;
    private String remark;
    private String useRangeDescript;
    private String validityPeriodStr;//有效期描述

    public String getRuleStr(){
        StringBuilder sb = new StringBuilder();
        if(compatibleWithCard != null && compatibleWithCard == 1){
            sb.append("允许与会员卡共同使用");
        } else {
            sb.append("不允许与会员卡共同使用");
        }
        sb.append(";");
//        if(compatibleWithOtherAccount != null && compatibleWithOtherAccount == 1){
//            sb.append("允许他人使用");
//        } else {
//            sb.append("不允许他人使用");
//        }
//        sb.append(";");
//        if(compatibleWithOtherCoupon != null && compatibleWithOtherCoupon == 1){
//            sb.append("允许与其他优惠券共同使用");
//        } else {
//            sb.append("不允许与其他优惠券共同使用");
//        }
//        sb.append(";");
        if(singleUse != null && singleUse == 1){
            sb.append("只允许使用一张优惠券");
        } else {
            sb.append("允许使用多张优惠券");
        }
        return sb.toString();
    }

    public Integer getUsedCount(){
        if(usedCount == null){
            return 0;
        }
        return usedCount;
    }

    public String getCouponTypeName(){
        if(couponType == null){
            return null;
        }else if(couponType == 0){
            return "折扣券";
        }else if(couponType == 1){
            return "现金券";
        }else{
            return "通用券";
        }
    }

    public String getEffectiveDateStr() {
        if (effectiveDateStr != null) {
            return effectiveDateStr;
        }
        if (effectiveDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(effectiveDate);
        }
        return null;
    }

    public String getExpireDateStr() {
        if (expireDateStr != null) {
            return expireDateStr;
        }
        if (expireDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(expireDate);
        }
        return null;
    }

    public String getGmtCreateStr() {
        if (gmtCreateStr != null) {
            return gmtCreateStr;
        }
        if (gmtCreate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(gmtCreate);
        }
        return null;
    }

    public enum StateEnum {
        ENABLED(1),
        DISABLED(2);
        int value;
        StateEnum(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    public String getUseRangeDescript(){
        if (useRange == null) {
            return "";
        }
        if (useRange != CouponInfoUseRangeEnum.ZXZDFWXMDZ.getValue()) {
            return CouponInfoUseRangeEnum.getNameByValue(useRange);
        } else {
            if (!CollectionUtils.isEmpty(couponServiceList)) {
                List<String> serviceNameList = Lists.transform(couponServiceList, new Function<CouponServiceRel, String>() {
                    @Override
                    public String apply(CouponServiceRel input) {
                        return input.getServiceName();
                    }
                });
                return Joiner.on(",").join(serviceNameList);
            }
        }
        return "";
    }

    public String getValidityPeriodStr() {
        if (validityPeriodStr != null && !validityPeriodStr.equals("")) {
            return validityPeriodStr;
        }
        if (customizeTime == null) {
            return "";
        }
        if (customizeTime == 0) {
            //0:非自定义时间
            return effectivePeriodDays + "天";
        } else if (customizeTime == 1) {
            //1.自定义时间
            return getEffectiveDateStr() + "至" + getExpireDateStr();
        }
        return "";
    }
}

