package com.tqmall.legend.facade.discount.bo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:10:45 AM 02/03/2017
 */
@Data
public class DiscountContext {
    public DiscountContext() {
        this.discountAmount = BigDecimal.ZERO;
    }
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 工单id
     */
    private Long orderId;
    /**
     * 车辆id
     */
    private Long customerCarId;
    /**
     * 车主id
     */
    private Long customerId;
    /**
     * 车牌信息
     */
    private String carLicense;
    /**
     * 使用他人账户时输入的手机号码信息
     */
    private String guestMobile;
    /**
     * 工单总计
     */
    private BigDecimal orderAmount;
    /**
     * 工单服务金额
     */
    private BigDecimal orderServiceAmount;
    /**
     * 工单物料金额
     */
    private BigDecimal orderGoodsAmount;
    /**
     * 附加项目金额
     */
    private BigDecimal orderFeeAmount;
    /**
     * !!工单抵扣后的金额
     */
    private BigDecimal discountAmount;
    /**
     * 工单关联的服务列表信息
     */
    private List<DiscountServiceBo> discountServiceList;
    /**
     * 工单关联的物料列表信息
     */
    private List<DiscountGoodsBo> discountGoodsList;
    /**
     * 拥有账户折扣信息
     */
    private AccountDiscountBo accountDiscount;
    /**
     * 使用他人账户折扣信息
     */
    private AccountDiscountBo guestAccountDiscount;
    /**
     * 绑定账户折扣信息
     */
    private List<AccountDiscountBo> bindAccountDiscountList;
    /**
     * 已选中的折扣信息
     */
    private DiscountSelectedBo selected;

    public List<AccountCouponDiscountBo> getAllCouponList() {
        List<AccountCouponDiscountBo> couponList = Lists.newArrayList();
        if (isNotNull(getAccountDiscount())) {
            if (isNotEmpty(getAccountDiscount().getCouponDiscountList())) {
                couponList.addAll(getAccountDiscount().getCouponDiscountList());
            }
        }
        if (isNotEmpty(getBindAccountDiscountList())) {
            for (AccountDiscountBo account : getBindAccountDiscountList()) {
                if (isNotEmpty(account.getCouponDiscountList())) {
                    couponList.addAll(account.getCouponDiscountList());
                }
            }
        }
        if (isNotNull(getGuestAccountDiscount())) {
            if (isNotEmpty(getGuestAccountDiscount().getCouponDiscountList())) {
                couponList.addAll(getGuestAccountDiscount().getCouponDiscountList());
            }
        }
        return couponList;
    }

    public List<AccountCardDiscountBo> getAllCardList() {
        List<AccountCardDiscountBo> cardList = Lists.newArrayList();
        if (isNotNull(getAccountDiscount())) {
            if (isNotEmpty(getAccountDiscount().getCardDiscountList())) {
                cardList.addAll(getAccountDiscount().getCardDiscountList());
            }
        }
        if (isNotEmpty(getBindAccountDiscountList())) {
            for (AccountDiscountBo account : getBindAccountDiscountList()) {
                if (isNotEmpty(account.getCardDiscountList())) {
                    cardList.addAll(account.getCardDiscountList());
                }
            }
        }

        if (isNotNull(getGuestAccountDiscount())) {
            if (isNotEmpty(getGuestAccountDiscount().getCardDiscountList())) {
                cardList.addAll(getGuestAccountDiscount().getCardDiscountList());
            }
        }
        return cardList;
    }

    public List<AccountComboDiscountBo> getAllComboServiceList() {
        List<AccountComboDiscountBo> comboServiceList = Lists.newArrayList();
        if (isNotNull(getAccountDiscount())) {
            if (isNotEmpty(getAccountDiscount().getComboDiscountList())) {
                comboServiceList.addAll(getAccountDiscount().getComboDiscountList());
            }
        }

        if (isNotEmpty(getBindAccountDiscountList())) {
            for (AccountDiscountBo account : getBindAccountDiscountList()) {
                if (isNotEmpty(account.getComboDiscountList())) {
                    comboServiceList.addAll(account.getComboDiscountList());
                }
            }
        }

        if (isNotNull(getGuestAccountDiscount())) {
            if (isNotEmpty(getGuestAccountDiscount().getComboDiscountList())) {
                comboServiceList.addAll(getGuestAccountDiscount().getComboDiscountList());
            }
        }
        return comboServiceList;
    }
}
