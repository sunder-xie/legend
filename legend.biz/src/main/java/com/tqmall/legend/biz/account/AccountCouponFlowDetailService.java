package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountCouponFlowDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface AccountCouponFlowDetailService {
    public Integer insert(AccountCouponFlowDetail accountCouponFlowDetail);

    public List<AccountCouponFlowDetail> select(Map param);

    public Page<AccountCouponFlowDetail> getPage(Pageable pageable,
                                                 Map<String, Object> searchParams);
    void batchInsert(List<AccountCouponFlowDetail> accountCouponFlowDetails);

    void delete(Long shopId);

    List<AccountCouponFlowDetail> findByFlowId(Long shopId, Long flowId);

    void recordDetailForCharge(Long shopId, Long creator, Long accountTradeFlowId, List<AccountCoupon> result);

    void recordDetailForConsume(Long shopId, Long userId, Map<Long, Long> flowIdMap, List<AccountCoupon> accountCoupons);

    void recordRevertDetailForConsume(Long userId, Long shopId, Long reverseFlowId,AccountCouponFlowDetail detail);

    void recordRevertDetailForRecharge(Long userId, Long shopId, List<AccountCouponFlowDetail> detailList, Long reverseFlowID);

    void recordDetailForCouponImport(AccountCoupon coupon, Long shopId, Long userId, Long flowId);
}
