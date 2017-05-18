package com.tqmall.legend.web.fileImport.process;

import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.AccountCouponFlowDetailService;
import com.tqmall.legend.biz.account.AccountCouponService;
import com.tqmall.legend.biz.account.AccountTradeFlowService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountConsumeTypeEnum;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountCouponFlowDetail;
import com.tqmall.legend.entity.account.AccountTradTypeEnum;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.facade.account.CouponFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by twg on 16/12/7.
 */
@Slf4j
@Component
public class AccountCouponImportProcess implements FileImportProcess {
    @Autowired
    private CouponFacadeService couponFacadeService;
    @Autowired
    private AccountCouponService accountCouponService;
    @Autowired
    private AccountTradeFlowService accountTradeFlowService;
    @Autowired
    private AccountCouponFlowDetailService accountCouponFlowDetailService;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is AccountCouponImportProcess");
        }
        Long shopId = fileImportContext.getShopId();
        Long userId = fileImportContext.getUserId();
        List<AccountCoupon> accountCouponList = fileImportContext.getExcelContents();

        if (CollectionUtils.isEmpty(accountCouponList)) {
            return;
        }

        /*优惠券账户交易明细批量添加*/
        List<AccountCouponFlowDetail> accountCouponFlowDetails = Lists.newArrayList();

        for (AccountCoupon accountCoupon : accountCouponList) {
            AccountTradeFlow tradeFlow = accountTradeFlowService.generateAccountTradeBase(shopId, userId);
            tradeFlow.setIsReversed(0);
            tradeFlow.setTradeType(AccountTradTypeEnum.COUPON.getCode());
            tradeFlow.setConsumeType(AccountConsumeTypeEnum.INIT.getCode());
            tradeFlow.setCouponExplain(accountCoupon.getCouponName() + "+" + accountCoupon.getCouponNum());
            tradeFlow.setCustomerName(accountCoupon.getCustomerName());
            tradeFlow.setMobile(accountCoupon.getMobile());
            tradeFlow.setAccountId(accountCoupon.getAccountId());
            accountTradeFlowService.insert(tradeFlow);

            for (int i = 0; i < accountCoupon.getCouponNum(); i++) {
                AccountCoupon coupon = new AccountCoupon();
                BeanUtils.copyProperties(accountCoupon, coupon);
                coupon.setCouponCode(couponFacadeService.genCouponSN());
                coupon.setFlowId(tradeFlow.getId());
                coupon.setEffectiveDate(new Date());
                coupon.setFlowSn(tradeFlow.getFlowSn());
                coupon.setOperatorName(tradeFlow.getOperatorName());
                accountCouponService.insert(coupon);

                AccountCouponFlowDetail flowDetail = new AccountCouponFlowDetail();
                flowDetail.setCreator(userId);
                flowDetail.setModifier(userId);
                flowDetail.setShopId(shopId);
                flowDetail.setChangeCount(1);
                flowDetail.setCouponId(coupon.getId());
                flowDetail.setCouponCode(coupon.getCouponCode());
                flowDetail.setCouponName(coupon.getCouponName());
                flowDetail.setAccountTradeFlowId(tradeFlow.getId());
                flowDetail.setConsumeType(AccountCouponFlowDetail.ConsumeTypeEnum.IMPORT.getCode());
                accountCouponFlowDetails.add(flowDetail);
            }
        }
        accountCouponFlowDetailService.batchInsert(accountCouponFlowDetails);

        fileImportContext.setSuccessNum(accountCouponList.size());

    }
}
