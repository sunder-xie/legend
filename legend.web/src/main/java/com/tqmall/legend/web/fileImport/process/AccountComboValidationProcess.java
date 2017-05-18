package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.biz.account.ComboInfoServiceRelService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.ComboInfo;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.web.fileImport.vo.AccountComboImportContext;
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
public class AccountComboValidationProcess implements FileImportProcess {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private ComboInfoServiceRelService comboInfoServiceRelService;

    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is AccountComboValidationProcess");
        }
        AccountComboImportContext accountComboImportContext = (AccountComboImportContext) fileImportContext;
        Long shopId = accountComboImportContext.getShopId();
        Long userId = accountComboImportContext.getUserId();
        List<AccountCombo> accountCombos = accountComboImportContext.getExcelContents();
        List<String> faildMessages = accountComboImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = accountComboImportContext.getRowFaildMessages();

        Set<String> mobiles = Sets.newHashSet();
        for (AccountCombo accountCombo : accountCombos) {
            mobiles.add(accountCombo.getMobile());
        }
        /***************账户相关公共部分开始**********************/
        Multimap<String, Long> mobileAccountMap = getMobileAccountInfo(shopId, mobiles);
        Map<Long, AccountInfo> accountInfoMap = getAccountInfoMap(mobileAccountMap);
        /***************账户相关公共部分结束**********************/

        ComboAndServiceRel comboAndServiceRel = new ComboAndServiceRel(shopId, accountCombos);
        comboAndServiceRel.invoke();
        Map<String, ComboInfo> comboInfoMap = comboAndServiceRel.getComboInfoMap();
        Multimap<Long, ComboInfoServiceRel> comboInfoServiceRelMap = comboAndServiceRel.getComboInfoServiceRelMap();
        accountComboImportContext.setComboInfoServiceRelMap(comboInfoServiceRelMap);

        List<AccountCombo> accountComboList = Lists.newArrayList();
        for (int i = 0; i < accountCombos.size(); i++) {
            boolean hasFaild = false;
            AccountCombo accountCombo = accountCombos.get(i);
            int rowNumber = accountCombo.getRowNumber() + 1;
            String mobile = accountCombo.getMobile();
            String comboName = accountCombo.getComboName();
            String serviceName = accountCombo.getServiceName();
            Integer serviceCount = accountCombo.getServiceCount();
            if (!mobileAccountMap.containsKey(mobile)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_NOT_ACCOUNT, rowNumber, mobile);
                String faildMes = String.format(ImportFailedMessages.FAILED_MOBILE_NOT_ACCOUNT, mobile);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
            }
            Collection<Long> accountIds = mobileAccountMap.get(mobile);
            int size = accountIds.size();
            if (size == 1) {
                Long accountId = accountIds.iterator().next();
                accountCombo.setAccountId(accountId);
                if(accountInfoMap.containsKey(accountId)){
                    accountCombo.setCustomerName(accountInfoMap.get(accountId).getCustomerName());
                }
            } else if (size > 1) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_MORE_ACCOUNT, rowNumber, mobile);
                String faildMes = String.format(ImportFailedMessages.FAILED_MOBILE_MORE_ACCOUNT,  mobile);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
            }
            if (!comboInfoMap.containsKey(comboName)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_COMBO_NOT_TYPE, rowNumber, comboName);
                String faildMes = String.format(ImportFailedMessages.FAILED_COMBO_NOT_TYPE, comboName);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
            }
            ComboInfo comboInfo = comboInfoMap.get(comboName);
            if (comboInfo != null) {
                Long comboInfoId = comboInfo.getId();
                accountCombo.setComboInfoId(comboInfoId);
                accountCombo.setEffectiveDate(comboInfo.getEffectiveDate());
                Collection<ComboInfoServiceRel> comboInfoServiceRels = comboInfoServiceRelMap.get(comboInfoId);
                List<ComboInfoServiceRel> serviceRels = Lists.newArrayList(comboInfoServiceRels);
                List<String> serviceNames = Lists.transform(serviceRels, new Function<ComboInfoServiceRel, String>() {
                    @Override
                    public String apply(ComboInfoServiceRel input) {
                        return input.getServiceName();
                    }
                });
                if (!comboInfoServiceRelMap.containsKey(comboInfoId) || !serviceNames.contains(serviceName)) {
                    String faildMessage = String.format(ImportFailedMessages.DEFAULT_COMBOINFO_NOT_SERVICE, rowNumber, comboName, serviceName);
                    String faildMes = String.format(ImportFailedMessages.FAILED_COMBOINFO_NOT_SERVICE, comboName, serviceName);
                    faildMessages.add(faildMessage);
                    hasFaild = true;
                    setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                }
                for (ComboInfoServiceRel serviceRel : serviceRels) {
                    if (serviceRel.getServiceName().equals(serviceName) && serviceCount > serviceRel.getServiceCount()) {
                        String faildMessage = String.format(ImportFailedMessages.DEFAULT_COMBOINFO_SERVICE_COUNT, rowNumber, comboName, serviceName, serviceCount, serviceRel.getServiceCount());
                        String faildMes = String.format(ImportFailedMessages.FAILED_COMBOINFO_SERVICE_COUNT, comboName, serviceName, serviceCount, serviceRel.getServiceCount());
                        faildMessages.add(faildMessage);
                        hasFaild = true;
                        setRowFalidMessage(rowFaildMessages, rowNumber, faildMes);
                    }
                }
            }

            accountCombo.setShopId(shopId);
            accountCombo.setCreator(userId);
            if (!hasFaild) {
                accountComboList.add(accountCombo);
            }
        }
        accountComboImportContext.setExcelContents(accountComboList);
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
        if (CollectionUtils.isEmpty(accountIds)) {
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

    private class ComboAndServiceRel {
        private Long shopId;
        private List<AccountCombo> accountCombos;
        private Map<String, ComboInfo> comboInfoMap;
        private Multimap<Long, ComboInfoServiceRel> comboInfoServiceRelMap = HashMultimap.create();

        public ComboAndServiceRel(Long shopId, List<AccountCombo> accountCombos) {
            this.shopId = shopId;
            this.accountCombos = accountCombos;
        }

        public Map<String, ComboInfo> getComboInfoMap() {
            return comboInfoMap;
        }

        public Multimap<Long, ComboInfoServiceRel> getComboInfoServiceRelMap() {
            return comboInfoServiceRelMap;
        }

        public void invoke() {
    /*获取计次卡类型信息，计次卡类型关联的服务项目信息*/
            List<String> comboNames = Lists.transform(accountCombos, new Function<AccountCombo, String>() {
                @Override
                public String apply(AccountCombo input) {
                    return input.getComboName();
                }
            });
            List<ComboInfo> comboInfos = comboInfoService.findComboInfoByNames(shopId, comboNames);

            this.comboInfoMap = Maps.uniqueIndex(comboInfos, new Function<ComboInfo, String>() {
                @Override
                public String apply(ComboInfo input) {
                    return input.getComboName();
                }
            });
            List<Long> comboInfoIds = Lists.transform(comboInfos, new Function<ComboInfo, Long>() {
                @Override
                public Long apply(ComboInfo input) {
                    return input.getId();
                }
            });
            if (!CollectionUtils.isEmpty(comboInfoIds)) {
                List<ComboInfoServiceRel> comboInfoServiceRels = comboInfoServiceRelService.findByComboInfoIds(shopId, comboInfoIds);
                for (ComboInfoServiceRel comboInfoServiceRel : comboInfoServiceRels) {
                    comboInfoServiceRelMap.put(comboInfoServiceRel.getComboInfoId(), comboInfoServiceRel);
                }
            }
        }
    }
}
