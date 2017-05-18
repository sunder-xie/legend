package com.tqmall.legend.facade.wechat.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.CouponConfigDTO;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wushuai on 16/9/8.
 */
@Data
public class WechatFavormallCouponVo extends CouponConfigDTO {
    private String couponName;//优惠劵名称
    private BigDecimal amountLimit;//金额限制.0:无金额限制
    private BigDecimal discountAmount;//抵扣金额
    private String amountLimiStr;//金额限制描述
    private Integer useRange;//使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private String useRangeDescript;//使用范围描述,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
    private Integer effectivePeriodDays;//有效期(天)
    private Date effectiveDate;//生效时间
    private Date expireDate;//失效时间
    private String effectiveStr;//有效期描述信息
    private Integer compatibleWithCard;//允许与会员卡共同使用.0:不允许;1:允许
    private Integer compatibleWithOtherAccount;//允许他人使用.0:不允许;1:允许
    private Integer compatibleWithOtherCoupon;//允许与其他优惠券共同使用.0:不允许;1:允许
    private Integer singleUse;//一张工单只允许使用一张优惠券.0:不允许;1:允许
    private String compatibleWithStr;//使用规则描述信息

    public String getAmountLimiStr() {
        if (getAmountLimit() != null && getAmountLimit().compareTo(new BigDecimal("0")) > 0) {
            return "满" + getAmountLimit() + "元使用;";
        }
        return "";
    }

    public String getUseRangeStr() {

        if (getUseRange() != null) {
            return CouponInfoUseRangeEnum.getNameByValue(getUseRange()) + ";";
        }
        return "";
    }

    public String getEffectiveStr() {
        if (getEffectivePeriodDays() != null && getEffectivePeriodDays() > 0) {
            return getEffectivePeriodDays() + "天 发送后立即可用;";
        }
        if (getEffectiveDate() != null && getExpireDate() != null) {
            return DateUtil.convertDateToYMD(getEffectiveDate()) + "-" + DateUtil.convertDateToYMD(getExpireDate());
        }
        return "";
    }

    public String getCompatibleWithStr() {
        StringBuffer compatibleWithStrSbf = new StringBuffer("");
        if (getCompatibleWithCard() != null && getCompatibleWithCard() == 0) {
            compatibleWithStrSbf.append("不允许与会员卡共同使用;");
        } else {
            compatibleWithStrSbf.append("允许与会员卡共同使用;");
        }
        if (getSingleUse() != null && getSingleUse() == 1) {
            compatibleWithStrSbf.append("一张工单只允许使用此优惠券一次;");
        } else {
            compatibleWithStrSbf.append("一张工单允许使用此优惠券多次;");
        }
        return compatibleWithStrSbf.toString();
    }

    public String getUseRangeDescript(){
        if(useRange!=null && CouponInfoUseRangeEnum.ZXZDFWXMDZ.getValue()==useRange.intValue()){
            return "仅限" + useRangeDescript + "使用";
        }
        return useRangeDescript;
    }
}
