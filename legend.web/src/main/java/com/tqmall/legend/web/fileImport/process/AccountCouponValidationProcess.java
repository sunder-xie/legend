package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.CouponInfoService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.CouponInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.web.fileImport.vo.AccountCouponImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/12/9.
 */
@Slf4j
@Component
public class AccountCouponValidationProcess implements FileImportProcess {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private CouponInfoService couponInfoService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is AccountCouponValidationProcess");
        }
        AccountCouponImportContext accountCouponImportContext = (AccountCouponImportContext) fileImportContext;

        Long shopId = accountCouponImportContext.getShopId();
        Long userId = accountCouponImportContext.getUserId();
        List<AccountCoupon> accountCoupons = accountCouponImportContext.getExcelContents();
        List<String> faildMessages = accountCouponImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = accountCouponImportContext.getRowFaildMessages();

        Set<String> mobiles = Sets.newHashSet();
        for (AccountCoupon accountCoupon : accountCoupons) {
            mobiles.add(accountCoupon.getMobile());
        }

        /***************账户相关公共部分开始**********************/
        Multimap<String, Long> mobileAccountMap = getMobileAccountInfo(shopId, mobiles);
        Map<Long, AccountInfo> accountInfoMap = getAccountInfoMap(mobileAccountMap);
        /***************账户相关公共部分结束**********************/

        /*获取优惠券类型信息*/
        Map<String, CouponInfo> couponInfoMap = getCouponInfoMap(shopId, accountCoupons);
        List<AccountCoupon> accountCouponList = Lists.newArrayList();
        for (int i = 0; i < accountCoupons.size(); i++) {
            boolean hasFaild = false;
            AccountCoupon accountCoupon = accountCoupons.get(i);

            int rowNumber = accountCoupon.getRowNumber() + 1;
            String mobile = accountCoupon.getMobile();
            String couponName = accountCoupon.getCouponName();
            if (!mobileAccountMap.containsKey(mobile)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_NOT_ACCOUNT, rowNumber, mobile);
                String faildMes = String.format(ImportFailedMessages.FAILED_MOBILE_NOT_ACCOUNT, mobile);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages,rowNumber,faildMes);
            }
            Collection<Long> accountIds = mobileAccountMap.get(mobile);
            int size = accountIds.size();
            if (size == 1) {
                Long accountId = accountIds.iterator().next();
                accountCoupon.setAccountId(accountId);
                if(accountInfoMap.containsKey(accountId)){
                    accountCoupon.setCustomerName(accountInfoMap.get(accountId).getCustomerName());
                }
            } else if (size > 1) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_MORE_ACCOUNT, rowNumber, mobile);
                String faildMes = String.format(ImportFailedMessages.FAILED_MOBILE_MORE_ACCOUNT,  mobile);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages,rowNumber,faildMes);
            }
            if (!couponInfoMap.containsKey(couponName)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_COUPON_NOT_TYPE, rowNumber, couponName);
                String faildMes = String.format(ImportFailedMessages.FAILED_COUPON_NOT_TYPE, couponName);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages,rowNumber,faildMes);
            }
            CouponInfo couponInfo = couponInfoMap.get(couponName);
            if (couponInfo != null) {
                accountCoupon.setCouponInfoId(couponInfo.getId());
                accountCoupon.setCouponType(couponInfo.getCouponType());
                accountCoupon.setEffectiveDate(couponInfo.getEffectiveDate());
            }
            accountCoupon.setShopId(shopId);
            accountCoupon.setCreator(userId);
            accountCoupon.setUsedStatus(AccountCoupon.StateEnum.UN_USED.getCode());
            accountCoupon.setCouponSource(AccountCoupon.SourceEnum.IMPORT.getCode());
            if (!hasFaild) {
                accountCouponList.add(accountCoupon);
            }
        }
        accountCouponImportContext.setExcelContents(accountCouponList);

    }

    private void setRowFalidMessage(Map<Integer, List<String>> rowFaildMessages, int rowNumber, String faildMessage) {
        if (rowFaildMessages.containsKey(rowNumber)) {
            List<String> faild = rowFaildMessages.get(rowNumber);
            faild.add(faildMessage);
        } else {
            List<String> failds = Lists.newArrayList();
            failds.add(faildMessage);
            rowFaildMessages.put(rowNumber, failds);
        }
    }


    private Multimap<String, Long> getMobileAccountInfo(Long shopId, Set<String> mobiles) {
        /*一个手机号对应多个客户信息*/
        Multimap<String, Long> mobileCustomerIdMap = HashMultimap.create();
        List<Customer> customerList = customerService.getCustomerByMobiles(mobiles, shopId);
        for (Customer customer : customerList) {
            mobileCustomerIdMap.put(customer.getMobile(), customer.getId());
        }
        Collection<Long> customerIds = mobileCustomerIdMap.values();
        /*账户和客户对应信息*/
        Map<Long, Long> customerIdAccountIdMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(customerIds)) {
            List<Long> customerIdList = Lists.newArrayList(customerIds);
            List<AccountInfo> accountInfoList = accountInfoService.getInfoByCustomerIds(customerIdList);
            for (AccountInfo accountInfo : accountInfoList) {
                customerIdAccountIdMap.put(accountInfo.getCustomerId(), accountInfo.getId());
            }
        }

        /*一个手机号对应多个账户信息*/
        Multimap<String, Long> mobileAccountIdMap = HashMultimap.create();
        if (!mobileCustomerIdMap.isEmpty() && !customerIdAccountIdMap.isEmpty()) {
            Collection<Map.Entry<String, Long>> mobileCustomerIdEntries = mobileCustomerIdMap.entries();
            for (Map.Entry<String, Long> mobileCustomerIdEntry : mobileCustomerIdEntries) {
                if (customerIdAccountIdMap.containsKey(mobileCustomerIdEntry.getValue())) {
                    Long accountId = customerIdAccountIdMap.get(mobileCustomerIdEntry.getValue());
                    mobileAccountIdMap.put(mobileCustomerIdEntry.getKey(), accountId);
                }
            }
        }
        return mobileAccountIdMap;
    }

    private Map<Long, AccountInfo> getAccountInfoMap(Multimap<String, Long> mobileAccountMap) {
        Collection<Long> accountIds = mobileAccountMap.values();
        if(CollectionUtils.isEmpty(accountIds)){
            return Maps.newHashMap();
        }
        List<Long> accountIdList = Lists.newArrayList(accountIds);
        List<AccountInfo> accountInfos = accountInfoService.getInfoByIds(accountIdList);
        return Maps.uniqueIndex(accountInfos, new Function<AccountInfo, Long>() {
            @Override
            public Long apply(AccountInfo input) {
                return input.getId();
            }
        });
    }

    private Map<String, CouponInfo> getCouponInfoMap(Long shopId, List<AccountCoupon> accountCoupons) {
        List<String> couponNames = Lists.transform(accountCoupons, new Function<AccountCoupon, String>() {
            @Override
            public String apply(AccountCoupon input) {
                return input.getCouponName();
            }
        });
        List<CouponInfo> couponInfos = couponInfoService.findCouponInfoByNames(shopId, couponNames);
        return Maps.uniqueIndex(couponInfos, new Function<CouponInfo, String>() {
            @Override
            public String apply(CouponInfo input) {
                return input.getCouponName();
            }
        });
    }
}
