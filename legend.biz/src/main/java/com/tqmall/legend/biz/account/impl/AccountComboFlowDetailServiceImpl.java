package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountComboFlowDetailService;
import com.tqmall.legend.biz.account.AccountComboService;
import com.tqmall.legend.biz.account.ComboServiceRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.dao.account.AccountComboFlowDetailDao;
import com.tqmall.legend.dao.account.AccountTradeFlowDao;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.legend.entity.account.AccountConsumeTypeEnum;
import com.tqmall.legend.entity.account.AccountTradTypeEnum;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.vo.RechargeComboFlowBo;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by majian on 16/6/17.
 */
@Service
@Slf4j
public class AccountComboFlowDetailServiceImpl extends BaseServiceImpl implements AccountComboFlowDetailService {

    @Autowired
    private AccountComboFlowDetailDao accountComboFlowDetailDao;
    @Autowired
    private AccountTradeFlowDao accountTradeFlowDao;
    @Autowired
    private ComboServiceRelService comboServiceRelService;
    @Autowired
    private AccountComboService accountComboService;

    @Override
    public List<AccountComboFlowDetail> findByFlowId(Long shopId, Long flowId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountTradeFlowId", flowId);
        return accountComboFlowDetailDao.select(param);
    }

    @Override
    public Page<RechargeComboFlowBo> getPageRechargeFlow(Pageable pageable, Long shopId) {
        Map searchParams = Maps.newHashMap();
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        List<Integer> consumeTypes = Lists.newArrayList();
        consumeTypes.add(AccountConsumeTypeEnum.CHARGE.getCode());
        consumeTypes.add(AccountConsumeTypeEnum.INIT.getCode());

        searchParams.put("tradeType", AccountTradTypeEnum.COMBO.getCode());
        searchParams.put("consumeTypes", consumeTypes);
        searchParams.put("shopId", shopId);
        Integer totalSize = accountTradeFlowDao.selectCount(searchParams);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<RechargeComboFlowBo> data = new ArrayList<>();
        List<AccountTradeFlow> accountTradeFlowList = accountTradeFlowDao.select(searchParams);
        if (!CollectionUtils.isEmpty(accountTradeFlowList)) {
            //1.get flowIds
            int size = accountTradeFlowList.size();
            Long[] flowIds = new Long[size];
            for (int i = 0; i < size; i++) {
                AccountTradeFlow accountTradeFlow = accountTradeFlowList.get(i);
                flowIds[i] = accountTradeFlow.getId();
            }

            List<AccountComboFlowDetail> flowDetailList = accountComboFlowDetailDao.selectByFlowIds(Arrays.asList(flowIds));
            //去重复
            Map<Long, AccountComboFlowDetail> flowDetailMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(flowDetailList)) {
                for (AccountComboFlowDetail item : flowDetailList) {
                    if (!flowDetailMap.containsKey(item.getAccountTradeFlowId())) {
                        flowDetailMap.put(item.getAccountTradeFlowId(), item);
                    }
                }
            }

            for (AccountTradeFlow flow : accountTradeFlowList) {
                AccountComboFlowDetail flowDetail = flowDetailMap.get(flow.getId());
                RechargeComboFlowBo rechargeComboFlowBo = new RechargeComboFlowBo();
                rechargeComboFlowBo.setFlowId(flow.getId());
                rechargeComboFlowBo.setAccountId(flow.getAccountId());
                rechargeComboFlowBo.setDate(flow.getGmtCreate());
                rechargeComboFlowBo.setCustomerName(flow.getCustomerName());
                rechargeComboFlowBo.setPhone(flow.getMobile());
                rechargeComboFlowBo.setPayAmount(flow.getPayAmount());
                if (flowDetail != null) {
                    rechargeComboFlowBo.setComboName(flowDetail.getComboName());
                }
                rechargeComboFlowBo.setRecieverName(flow.getReceiverName());
                rechargeComboFlowBo.setCreatorName(flow.getOperatorName());
                rechargeComboFlowBo.setIsReversed(flow.getIsReversed());
                data.add(rechargeComboFlowBo);
            }

        }
        DefaultPage<RechargeComboFlowBo> page = new DefaultPage<RechargeComboFlowBo>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public void recordDetailForConsume(Long shopId, Long userId, Map<Long, Long> flowIdMap, Map<Long, Integer> consumeBoMap, List<AccountComboServiceRel> accountComboServiceRelList) {
        List<AccountComboFlowDetail> accountComboFlowDetailList = new ArrayList<>();
        for (AccountComboServiceRel item : accountComboServiceRelList) {
            Long comboId = item.getComboId();
            AccountCombo combo = accountComboService.getComboWithService(shopId, comboId);
            if (combo != null) {
                Long flowId = flowIdMap.get(combo.getAccountId());
                if (flowId != null) {
                    AccountComboFlowDetail flowDetail = new AccountComboFlowDetail();
                    flowDetail.setCreator(userId);
                    flowDetail.setModifier(userId);
                    flowDetail.setShopId(shopId);
                    flowDetail.setComboId(comboId);
                    flowDetail.setServiceId(item.getId());
                    flowDetail.setServiceName(item.getServiceName());
                    flowDetail.setConsumeType(AccountComboFlowDetail.CONSUME);
                    flowDetail.setChangeCount(consumeBoMap.get(item.getId()));
                    flowDetail.setAccountTradeFlowId(flowId);
                    flowDetail.setComboName(combo.getComboName());
                    accountComboFlowDetailList.add(flowDetail);
                }
            }
        }
        if (Langs.isNotEmpty(accountComboFlowDetailList)) {
            accountComboFlowDetailDao.batchInsert(accountComboFlowDetailList);
        }
    }

    @Override
    public void recordRevertDetailForConsume(Long userId, Long shopId, Long reverseFlowId, AccountComboFlowDetail comboDetail) {
        AccountComboFlowDetail reverseFlowDetail = new AccountComboFlowDetail();
        reverseFlowDetail.setCreator(userId);
        reverseFlowDetail.setModifier(userId);
        reverseFlowDetail.setShopId(shopId);
        reverseFlowDetail.setComboId(comboDetail.getComboId());
        reverseFlowDetail.setServiceId(comboDetail.getServiceId());
        reverseFlowDetail.setServiceName(comboDetail.getServiceName());
        reverseFlowDetail.setConsumeType(AccountComboFlowDetail.REVERSE_CONSUME);
        reverseFlowDetail.setChangeCount(comboDetail.getChangeCount());
        reverseFlowDetail.setAccountTradeFlowId(reverseFlowId);
        reverseFlowDetail.setComboName(comboDetail.getComboName());
        accountComboFlowDetailDao.insert(reverseFlowDetail);
    }

    @Override
    public void recordDetailForCharge(Long shopId, Long userId, Long accountTradeFlowId, AccountCombo combo) {
        List<AccountComboServiceRel> comboServiceRelList = comboServiceRelService.findByComboId(shopId, combo.getId());
        if (!CollectionUtils.isEmpty(comboServiceRelList)) {
            List<AccountComboFlowDetail> flowDetailList = new ArrayList<>();
            for (AccountComboServiceRel item : comboServiceRelList) {
                AccountComboFlowDetail detail = new AccountComboFlowDetail();
                detail.setCreator(userId);
                detail.setModifier(userId);
                detail.setShopId(shopId);
                detail.setComboId(item.getComboId());
                detail.setComboName(combo.getComboName());
                detail.setServiceId(item.getId());
                detail.setServiceName(item.getServiceName());
                detail.setChangeCount(item.getTotalServiceCount());
                detail.setConsumeType(1);
                detail.setAccountTradeFlowId(accountTradeFlowId);
                flowDetailList.add(detail);
            }
            accountComboFlowDetailDao.batchInsert(flowDetailList);
        }
    }

    @Override
    public void recordRevertDetailForRecharge(Long userId, Long shopId, List<AccountComboFlowDetail> detailList, Long reverseFlowID) {
        if (!CollectionUtils.isEmpty(detailList)) {
            List<AccountComboFlowDetail> reverseDetailList = new ArrayList<>();
            for (AccountComboFlowDetail item : detailList) {
                AccountComboFlowDetail reverseDetail = new AccountComboFlowDetail();
                reverseDetail.setCreator(userId);
                reverseDetail.setModifier(userId);
                reverseDetail.setShopId(shopId);
                reverseDetail.setComboId(item.getComboId());
                reverseDetail.setServiceId(item.getServiceId());
                reverseDetail.setServiceName(item.getServiceName());
                reverseDetail.setChangeCount(item.getChangeCount());
                reverseDetail.setConsumeType(AccountComboFlowDetail.ConsumeTypeEnum.CHARGE_REVERT.getCode());
                reverseDetail.setComboName(item.getComboName());
                reverseDetail.setAccountTradeFlowId(reverseFlowID);
                reverseDetailList.add(reverseDetail);
            }
            accountComboFlowDetailDao.batchInsert(reverseDetailList);
        }
    }

    @Override
    public void recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId, Long flowId) {
        List<AccountComboServiceRel> serviceList = combo.getServiceList();
        if (!CollectionUtils.isEmpty(serviceList)) {
            List<AccountComboFlowDetail> detailList = Lists.newArrayList();
            for (AccountComboServiceRel service : serviceList) {
                AccountComboFlowDetail detail = new AccountComboFlowDetail();
                detail.setShopId(shopId);
                detail.setCreator(userId);
                detail.setModifier(userId);
                detail.setComboId(combo.getId());
                detail.setServiceId(service.getServiceId());
                detail.setServiceName(service.getServiceName());
                detail.setChangeCount(service.getTotalServiceCount() - service.getUsedServiceCount());
                detail.setConsumeType(AccountComboFlowDetail.ConsumeTypeEnum.IMPORT.getCode());
                detail.setComboName(combo.getComboName());
                detail.setAccountTradeFlowId(flowId);
                detailList.add(detail);
            }
            accountComboFlowDetailDao.batchInsert(detailList);
        }
    }


    @Override
    public List<AccountComboFlowDetail> getAccountComboFlowDetails(Map param) {
        return accountComboFlowDetailDao.select(param);
    }

    @Override
    public List<AccountComboFlowDetail> listByComboId(Long shopID, Long comboId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopID);
        param.put("comboId",comboId);
        return accountComboFlowDetailDao.select(param);
    }

    @Override
    public void batchSave(List<AccountComboFlowDetail> accountComboFlowDetails) {
        super.batchInsert(accountComboFlowDetailDao,accountComboFlowDetails,1000);
    }
}
