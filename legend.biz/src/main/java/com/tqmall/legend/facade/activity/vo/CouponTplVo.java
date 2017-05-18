package com.tqmall.legend.facade.activity.vo;

import com.tqmall.insurance.domain.result.coupon.InsuranceCouponTemplateDTO;
import lombok.Data;

/**
 * Created by pituo on 16/11/23.
 */
@Data
public class CouponTplVo extends InsuranceCouponTemplateDTO {
    private Integer couponSource;//优惠券来源: 1 保险，2 云修
    private Long couponTotal;//参与活动的优惠券数量
    private Integer receivedCouponCount;//已领取优惠券数量
    /**优惠券规则说明**/
    private String couponRuleDescriptionDelHtml;
}
