package com.tqmall.legend.facade.discount.bo;

import com.tqmall.legend.entity.account.CouponTypeEnum;
import com.tqmall.legend.enums.coupon.CouponInfoUseRangeEnum;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 优惠券折扣信息
 *
 * @Author 辉辉大侠
 * @Date:10:11 AM 02/03/2017
 */
@Data
public class AccountCouponDiscountBo {
    public AccountCouponDiscountBo() {
        this.selected = false;
        this.available = true;
        this.discount = BigDecimal.ZERO;
    }

    private Long couponId;

    private CouponTypeEnum couponType;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券券码
     */
    private String couponSn;
    /**
     * 优惠券类型id
     */
    private Long couponTypeId;
    /**
     * 优惠券抵扣金额（计算出来）
     */
    private BigDecimal discount;
    /**
     * 优惠券抵扣金额（人工修改的后的值）
     */
    private BigDecimal finalDiscount;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 优惠券是否使用
     */
    private boolean selected;
    /**
     * 优惠券生效时间
     */
    private Date effectiveDate;
    /**
     * 优惠券失效时间
     */
    private Date expireDate;
    /**
     * 当优惠券不可用时具体的提示信息
     */
    private String message;
    /**
     * 使用时是否有金额限制
     */
    private boolean amountLimit;
    /**
     * 使用时工单的最小金额
     */
    private BigDecimal minAmountLimit;
    /**
     * 一张工单只允许使用一张该类型的优惠券
     */
    private boolean singleUse;
    /**
     * 是否能与会员卡共同使用
     */
    private boolean compatibleWithCard;
    /**
     * 若为现金券,则有使用范围限制
     */
    private CouponInfoUseRangeEnum range;
    /**
     * 优惠券可用的服务id
     */
    private Set<Long> rangeServiceIds;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户手机号码
     */
    private String mobile;
    private Long accountId;
    /**
     * 使用规则描述
     */
    private String ruleDesc;

    /**
     * 过期时间
     * @return
     */
    public String getExpireDateStr(){
        return DateFormatUtils.toYMD(expireDate);
    }
}
