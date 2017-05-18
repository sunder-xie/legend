package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.CouponSuite;
import com.tqmall.legend.entity.account.SuiteCouponRel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CouponSuiteService {

    void insert(CouponSuite couponSuite);

    List<CouponSuite> select(Map param);

    List<CouponSuite> selectDetail(Map param);

    boolean update(CouponSuite couponSuite);

    CouponSuite getCouponSuiteById(Long id);

    List<SuiteCouponRel> getSuiteCouponRelList(Long shopId,Long suiteId);

    List<SuiteCouponRel> getSuiteCouponRelListBySuiteIds(Long shopId,Long[] suiteId);

    CouponSuite selectById(Long id, Long shopId);

    void delete(Long shopId);

    Integer selectCount(Map<String, Object> param);

    void batchInsert(List<CouponSuite> couponSuiteList);

    void updateUsedCount(Long shopId, Long couponSuiteId);

    /**
     * 批量查套餐列表
     * @param shopID
     * @param suiteInfoIds
     * @return
     */
    List<CouponSuite> listSuite(Long shopID, Collection<Long> suiteInfoIds);
}
