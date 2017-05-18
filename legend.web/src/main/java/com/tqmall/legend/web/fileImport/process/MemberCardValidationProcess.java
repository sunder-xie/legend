package com.tqmall.legend.web.fileImport.process;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.common.fileImport.ImportFailedMessages;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.web.fileImport.vo.MemberCardImportContext;
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
public class MemberCardValidationProcess implements FileImportProcess {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;


    @Override
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is MemberCardValidationProcess");
        }
        MemberCardImportContext memberCardImportContext = (MemberCardImportContext) fileImportContext;

        Long shopId = memberCardImportContext.getShopId();
        Long userId = memberCardImportContext.getUserId();
        String userName = memberCardImportContext.getUserName();
        List<MemberCard> memberCards = memberCardImportContext.getExcelContents();
        List<String> faildMessages = memberCardImportContext.getFaildMessage();
        Map<Integer, List<String>> rowFaildMessages = memberCardImportContext.getRowFaildMessages();

        Set<String> mobiles = Sets.newHashSet();
        for (MemberCard memberCard : memberCards) {
            mobiles.add(memberCard.getMobile());
        }
        /***************账户相关公共部分开始**********************/
        Multimap<String, Long> mobileAccountMap = getMobileAccountInfo(shopId, mobiles);
        Map<Long, AccountInfo> accountInfoMap = getAccountInfoMap(mobileAccountMap);
        /***************账户相关公共部分结束**********************/

        Map<String, MemberCardInfo> memberCardInfoMap = getCardTypeNameMap(shopId, memberCards);
        /*已存在的会员卡号*/
        List<String> memberCardList = getCardNumbers(shopId, memberCards);
        /*已存在的账号，有多张会员卡*/
        Multimap<Long, MemberCard> cardMap = accountHasMuliMemberCardMap(mobileAccountMap);

        Map<String, MemberCard> memberCardMap = Maps.newHashMap();

        /*解析成功的数据*/
        List<MemberCard> memberCardParams = Lists.newArrayList();
        for (MemberCard memberCard : memberCards) {
            boolean hasFaild = false;
            int rowNumber = memberCard.getRowNumber() + 1;
            String mobile = memberCard.getMobile();
            String cardNumber = memberCard.getCardNumber();
            String typeName = memberCard.getCardTypeName();

            if (!mobileAccountMap.containsKey(mobile)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_NOT_ACCOUNT, rowNumber, mobile);
                String failedMs = String.format(ImportFailedMessages.FAILED_MOBILE_NOT_ACCOUNT,  mobile);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
            }
            if (memberCardList.contains(cardNumber) || memberCardMap.containsKey(cardNumber)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_REPEAT_MESSAGE, rowNumber, cardNumber, "会员卡号");
                String failedMs = String.format(ImportFailedMessages.FAILED_REPEAT_MESSAGE, cardNumber, "会员卡号");
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
            }
            if (!memberCardInfoMap.containsKey(typeName)) {
                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MEMBER_NOT_TYPE, rowNumber, typeName);
                String failedMs = String.format(ImportFailedMessages.FAILED_MEMBER_NOT_TYPE,  typeName);
                faildMessages.add(faildMessage);
                hasFaild = true;
                setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
            }
            /**
             * 1.手机号对应有多个账号时不能导入。
             * 2.同一个账号下只能有3张有效会员卡。
             * 3.同一个账号下3张有效会员卡的类型都不同。
             */
            if(mobileAccountMap.containsKey(mobile)){
                Collection<Long> accountIds = mobileAccountMap.get(mobile);
                int size = accountIds.size();
                if (size == 1) {
                    Long accountId = accountIds.iterator().next();
                    if (cardMap.containsKey(accountId) && !memberCard.isExpired()){
                        Collection<MemberCard> cardCollections = cardMap.get(accountId);
                        if (cardCollections.size()> 2){
                            String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_HAS_MEMBER, rowNumber, mobile,cardCollections.size());
                            String failedMs = String.format(ImportFailedMessages.FAILED_MOBILE_HAS_MEMBER, mobile,cardCollections.size());
                            faildMessages.add(faildMessage);
                            hasFaild = true;
                            setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
                        }
                        for (MemberCard card : cardCollections) {
                            if (typeName.contains(card.getCardTypeName())){
                                String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_HAS_MEMBER_TYPE, rowNumber, mobile,typeName);
                                String failedMs = String.format(ImportFailedMessages.FAILED_MOBILE_HAS_MEMBER_TYPE, mobile,typeName);
                                faildMessages.add(faildMessage);
                                hasFaild = true;
                                setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
                            }
                        }
                    }
                    if (!hasFaild && !memberCard.isExpired()){
                        cardMap.put(accountId,memberCard);
                    }
                    memberCard.setAccountId(accountId);
                    if(accountInfoMap.containsKey(accountId)){
                        memberCard.setCustomerName(accountInfoMap.get(accountId).getCustomerName());
                    }
                } else if (size > 1) {
                    String faildMessage = String.format(ImportFailedMessages.DEFAULT_MOBILE_MORE_ACCOUNT, rowNumber, mobile);
                    String failedMs = String.format(ImportFailedMessages.FAILED_MOBILE_MORE_ACCOUNT, mobile);
                    faildMessages.add(faildMessage);
                    hasFaild = true;
                    setRowFalidMessage(rowFaildMessages, rowNumber, failedMs);
                }
            }

            memberCard.setShopId(shopId);
            memberCard.setCreator(userId);
            memberCard.setPublisher(userId);
            memberCard.setPublisherName(userName);
            MemberCardInfo memberCardInfo = memberCardInfoMap.get(typeName);
            if (memberCardInfo != null) {
                memberCard.setCardTypeId(memberCardInfo.getId());
            }
            if (!hasFaild) {
                memberCardParams.add(memberCard);
                memberCardMap.put(cardNumber, memberCard);
            }
        }

        memberCardImportContext.setExcelContents(memberCardParams);
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

    private Map<String, MemberCardInfo> getCardTypeNameMap(Long shopId, List<MemberCard> memberCards) {
        List<String> typeNames = Lists.transform(memberCards, new Function<MemberCard, String>() {
            @Override
            public String apply(MemberCard input) {
                return input.getCardTypeName();
            }
        });
        List<MemberCardInfo> cardInfos = memberCardInfoService.selectByNames(typeNames, shopId);
        return Maps.uniqueIndex(cardInfos, new Function<MemberCardInfo, String>() {
            @Override
            public String apply(MemberCardInfo input) {
                return input.getTypeName();
            }
        });
    }

    private List<String> getCardNumbers(Long shopId, List<MemberCard> memberCards) {
        List<String> cardNumbers = Lists.transform(memberCards, new Function<MemberCard, String>() {
            @Override
            public String apply(MemberCard input) {
                return input.getCardNumber();
            }
        });
        return memberCardService.selectExistedCardNumbers(shopId, cardNumbers);
    }

    //一个账号有多张会员卡
    private Multimap<Long, MemberCard> accountHasMuliMemberCardMap(Multimap<String, Long> mobileAccountMap) {
        Collection<Long> accountIds = mobileAccountMap.values();
        if(CollectionUtils.isEmpty(accountIds)){
            return HashMultimap.create();
        }
        List<Long> accountIdList = Lists.newArrayList(accountIds);
        List<MemberCard> members = memberCardService.findByAccountIds(accountIdList);
        //一个账号有多张会员卡
        Multimap<Long, MemberCard> memberCardMultimap = HashMultimap.create();
        for (MemberCard member : members) {
            if (!member.isExpired()){
                memberCardMultimap.put(member.getAccountId(),member);
            }
        }
        return memberCardMultimap;
    }
}
