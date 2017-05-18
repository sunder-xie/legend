package com.tqmall.legend.facade.insurance;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.SearchFormForCashCouponParam;
import com.tqmall.insurance.domain.param.insurance.cashcoupon.UseCashCouponParam;
import com.tqmall.insurance.domain.result.cashcoupon.CashCouponDetailDTO;
import com.tqmall.insurance.domain.result.cashcoupon.ShopCashCouponDTO;
import com.tqmall.mana.client.beans.cashcoupon.CreateRuleConfigResultDTO;

/**
 * Created by zhouheng on 17/3/11.
 */
public interface InsuranceCashCouponFacade {

    /**
     * 获取门店优惠券信息
     ** @return
     */
    ShopCashCouponDTO getShopCashCouponInfo(SearchFormForCashCouponParam param);

    /**
     * 确认用券
     */
    Result useCashCoupon(UseCashCouponParam uesCashCouponParam);

    /**
     *
     * @param keyDesc
     * @return
     */
    String getCouponDesc(String keyDesc);

    /**
     *
     * @param searchCashCouponParam
     * @return
     */
    PagingResult<CashCouponDetailDTO> queryCashCouponPage(SearchCashCouponParam searchCashCouponParam);

    /**
     * 判断门店是否开通现金券
     * @param cityCode
     * @return
     */
    boolean judgeIsOpenCashCoupon(String cityCode);


    CreateRuleConfigResultDTO getCashCouponSettleRule(String cityCode);

}
