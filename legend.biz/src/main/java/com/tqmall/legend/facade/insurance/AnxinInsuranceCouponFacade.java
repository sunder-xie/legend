package com.tqmall.legend.facade.insurance;

import com.tqmall.insurance.domain.param.insurance.coupon.LegendCouponParam;
import com.tqmall.insurance.domain.result.coupon.ShopCouponDetailDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.vo.InsuranceCouponUserVo;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sven on 2016/12/7.
 */
public interface AnxinInsuranceCouponFacade {
    /**
     * 获取优惠券列表
     *
     * @param shopId 门店id
     * @param mobile 客户手机
     * @param amount
     * @return
     */
    List<InsuranceCouponUserVo> getCouponList(Integer shopId, String mobile, BigDecimal amount);

    /**
     * 校验优惠券是否可用
     *
     * @param ucShopId
     * @param couponId
     * @param mode
     * @param amount
     * @return
     */
    void checkcoupon(Integer ucShopId, Integer couponId, Integer mode, BigDecimal amount);

    /**
     * 解冻优惠券
     *
     * @param ucShopId
     * @param couponId
     */
    void thawCoupon(Integer ucShopId, Integer couponId);

    /**
     * 使用优惠券
     *
     * @param ucShopId
     * @param couponId
     * @param OrderSn
     * @param mode
     */
    void useCoupon(Integer ucShopId, Integer couponId, String OrderSn, Integer mode);

    /**
     * 获取门店优惠券统计数据
     *
     * @param ucShopId
     * @return
     */
    ShopCouponDetailDTO selectCouponStatistics(Integer ucShopId);

    /**
     * 获取核销优惠券列表
     *
     * @param param
     * @return
     */
    DefaultPage<InsuranceCouponUserVo> couponSettleList(LegendCouponParam param, Pageable pageable);
}
