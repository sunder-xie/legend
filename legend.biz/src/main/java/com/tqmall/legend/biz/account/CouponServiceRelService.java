package com.tqmall.legend.biz.account;

import com.tqmall.legend.entity.account.CouponServiceRel;

import java.util.Collection;
import java.util.List;

/**
 * Created by majian on 16/9/30.
 */
public interface CouponServiceRelService {
    /**
     * 查优惠券关联的服务
     * @param couponInfoId
     * @param shopId
     * @return
     */
    List<CouponServiceRel> list(Long couponInfoId, Long shopId);

    List<CouponServiceRel> list(Long shopId, Collection<Long> couponInfoIds);

    /**
     * 批量保存
     * @param serviceRels
     */
    void save(List<CouponServiceRel> serviceRels);

    void deleteByCouponInfoId(Long shopId, Long couponInfoId);

    List<CouponServiceRel> selectByServiceId(long serviceId);
}
