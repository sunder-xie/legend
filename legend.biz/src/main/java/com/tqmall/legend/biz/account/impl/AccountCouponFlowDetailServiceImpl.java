package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountCouponFlowDetailService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.AccountCouponFlowDetailDao;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountCouponFlowDetail;
import com.tqmall.wheel.lang.Langs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class AccountCouponFlowDetailServiceImpl extends BaseServiceImpl implements AccountCouponFlowDetailService {
    @Autowired
    private AccountCouponFlowDetailDao accountCouponFlowDetailDao;

    @Override
    public Integer insert(AccountCouponFlowDetail accountCouponFlowDetail) {
        return accountCouponFlowDetailDao.insert(accountCouponFlowDetail);
    }

    @Override
    public List<AccountCouponFlowDetail> select(Map param) {
        return accountCouponFlowDetailDao.select(param);
    }

    @Override
    public Page<AccountCouponFlowDetail> getPage(Pageable pageable,
                                                 Map<String, Object> searchParams) {
        return this.getPage(accountCouponFlowDetailDao,pageable,searchParams);
    }

    @Override
    public void batchInsert(List<AccountCouponFlowDetail> accountCouponFlowDetails) {
        super.batchInsert(accountCouponFlowDetailDao,accountCouponFlowDetails,1000);
    }

    @Override
    public void delete(Long shopId) {
        Map param = Maps.newHashMap();
        if(shopId != null){
            param.put("shopId",shopId);
        }
        accountCouponFlowDetailDao.delete(param);
    }

    @Override
    public List<AccountCouponFlowDetail> findByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountTradeFlowId",flowId);
        return  accountCouponFlowDetailDao.select(param);
    }

    @Override
    public void recordDetailForConsume(Long shopId, Long userId, Map<Long, Long> flowIdMap, List<AccountCoupon> accountCoupons) {
        List<AccountCouponFlowDetail> accountCouponFlowDetails = new ArrayList<>();
        for (AccountCoupon accountCoupon : accountCoupons) {
            Long flowId = flowIdMap.get(accountCoupon.getAccountId());
            if (flowId != null) {
                AccountCouponFlowDetail accountCouponFlowDetail = new AccountCouponFlowDetail();
                accountCouponFlowDetail.setShopId(shopId);
                accountCouponFlowDetail.setCreator(userId);
                accountCouponFlowDetail.setCouponId(accountCoupon.getId());
                accountCouponFlowDetail.setCouponCode(accountCoupon.getCouponCode());
                accountCouponFlowDetail.setCouponName(accountCoupon.getCouponName());
                accountCouponFlowDetail.setChangeCount(1);
                accountCouponFlowDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.CONSUME.getCode());
                accountCouponFlowDetail.setAccountTradeFlowId(flowId);
                accountCouponFlowDetails.add(accountCouponFlowDetail);
            }
        }
        if (Langs.isNotEmpty(accountCouponFlowDetails)) {
            accountCouponFlowDetailDao.batchInsert(accountCouponFlowDetails);
        }
    }


    @Override
    public void recordRevertDetailForConsume(Long userId, Long shopId, Long reverseFlowId,AccountCouponFlowDetail detail) {
        AccountCouponFlowDetail reverseDetail = new AccountCouponFlowDetail();
        reverseDetail.setCreator(userId);
        reverseDetail.setModifier(userId);
        reverseDetail.setShopId(shopId);
        reverseDetail.setCouponId(detail.getCouponId());
        reverseDetail.setCouponCode(detail.getCouponCode());
        reverseDetail.setChangeCount(detail.getChangeCount());
        reverseDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.CONSUME_REVERT.getCode());
        reverseDetail.setAccountTradeFlowId(reverseFlowId);
        reverseDetail.setCouponName(detail.getCouponName());
        accountCouponFlowDetailDao.insert(reverseDetail);
    }

    @Override
    public void recordRevertDetailForRecharge(Long userId, Long shopId, List<AccountCouponFlowDetail> detailList, Long reverseFlowID) {
        if (!CollectionUtils.isEmpty(detailList)) {
            List<AccountCouponFlowDetail> reverseDetailList = new ArrayList<>();
            for (AccountCouponFlowDetail item : detailList) {
                AccountCouponFlowDetail reverseDetail = new AccountCouponFlowDetail();
                reverseDetail.setCreator(userId);
                reverseDetail.setModifier(userId);
                reverseDetail.setShopId(shopId);
                reverseDetail.setCouponId(item.getCouponId());
                reverseDetail.setCouponCode(item.getCouponCode());
                reverseDetail.setChangeCount(item.getChangeCount());
                reverseDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.CHARGE_REVERT.getCode());
                reverseDetail.setCouponName(item.getCouponName());
                reverseDetail.setAccountTradeFlowId(reverseFlowID);
                reverseDetailList.add(reverseDetail);
            }
            accountCouponFlowDetailDao.batchInsert(reverseDetailList);
        }
    }

    @Override
    public void recordDetailForCouponImport(AccountCoupon coupon, Long shopId, Long userId, Long flowId) {
        AccountCouponFlowDetail flowDetail = new AccountCouponFlowDetail();
        flowDetail.setCreator(userId);
        flowDetail.setModifier(userId);
        flowDetail.setShopId(shopId);
        flowDetail.setCouponId(coupon.getId());
        flowDetail.setCouponCode(coupon.getCouponCode());
        flowDetail.setChangeCount(1);
        flowDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.IMPORT.getCode());
        flowDetail.setCouponName(coupon.getCouponName());
        flowDetail.setAccountTradeFlowId(flowId);
        accountCouponFlowDetailDao.insert(flowDetail);
    }

    @Override
    public void recordDetailForCharge(Long shopId, Long creator, Long accountTradeFlowId, List<AccountCoupon> result) {
        List<AccountCouponFlowDetail> accountCouponFlowDetails = new LinkedList<>();
        for (AccountCoupon accountCoupon : result) {
            AccountCouponFlowDetail accountCouponFlowDetail = new AccountCouponFlowDetail();
            accountCouponFlowDetail.setShopId(shopId);
            accountCouponFlowDetail.setCreator(creator);
            accountCouponFlowDetail.setCouponCode(accountCoupon.getCouponCode());
            accountCouponFlowDetail.setChangeCount(1);
            accountCouponFlowDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.CHARGE.getCode());
            accountCouponFlowDetail.setAccountTradeFlowId(accountTradeFlowId);
            accountCouponFlowDetail.setCouponName(accountCoupon.getCouponName());
            accountCouponFlowDetail.setCouponId(accountCoupon.getId());
            accountCouponFlowDetails.add(accountCouponFlowDetail);
        }
        accountCouponFlowDetailDao.batchInsert(accountCouponFlowDetails);
    }
}
