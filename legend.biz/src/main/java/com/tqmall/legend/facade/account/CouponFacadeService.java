package com.tqmall.legend.facade.account;

/**
 * Created by wanghui on 6/12/16.
 */
public interface CouponFacadeService {
    /**
     * 生成优惠券SN
     * @return
     */
    String genCouponSN();

    boolean removeCachedSn(String sn);
}
