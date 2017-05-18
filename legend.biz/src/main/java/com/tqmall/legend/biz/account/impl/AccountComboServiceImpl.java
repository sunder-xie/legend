package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.AccountComboFlowDetailService;
import com.tqmall.legend.biz.account.AccountComboService;
import com.tqmall.legend.biz.account.AccountFlowService;
import com.tqmall.legend.biz.account.ComboServiceRelService;
import com.tqmall.legend.biz.account.bo.ComboPageParam;
import com.tqmall.legend.biz.account.bo.ConsumeComboBo;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.dao.account.AccountComboDao;
import com.tqmall.legend.dao.account.AccountComboServiceRelDao;
import com.tqmall.legend.dao.account.ComboInfoDao;
import com.tqmall.legend.dao.account.ComboInfoServiceRelDao;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.log.MarketCardCouponLog;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by majian on 16/6/17.
 */
@Service
@Slf4j
public class AccountComboServiceImpl extends BaseServiceImpl implements AccountComboService {

    @Autowired
    private ComboInfoDao comboInfoDao;
    @Autowired
    private ComboInfoServiceRelDao comboInfoServiceRelDao;
    @Autowired
    private AccountComboServiceRelDao accountComboServiceRelDao;
    @Autowired
    private AccountComboDao accountComboDao;
    @Autowired
    private AccountComboFlowDetailService accountComboFlowDetailService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private ComboServiceRelService comboServiceRelService;

    @Override
    public void consumeCombo(Long shopId, Long userId, List<ConsumeComboBo> consumeComboBoList, Map<Long, Long> flowIdMap) {
        //1.查找消费的服务
        int size = consumeComboBoList.size();
        Long[] comboServiceRelIds = new Long[size];
        Map<Long, Integer> consumeBoMap = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            ConsumeComboBo consumeComboBo = consumeComboBoList.get(i);
            comboServiceRelIds[i] = consumeComboBo.getComboServiceRelId();
            consumeBoMap.put(consumeComboBo.getComboServiceRelId(),
                    consumeComboBo.getServiceUsedCount());

        }
        List<AccountComboServiceRel> accountComboServiceRelList =
                accountComboServiceRelDao.selectByIds(comboServiceRelIds);

        if (Langs.isEmpty(accountComboServiceRelList)) {
            return;
        }

        //2.更改服务的使用数量,获取待检查的combo集合
        Set<Long> updateComboIdSet = new HashSet<>();//待检查的combo集合
        for (AccountComboServiceRel item : accountComboServiceRelList) {
            Integer usedCount = consumeBoMap.get(item.getId());
            Integer oldUsedCount = item.getUsedServiceCount();
            Integer newUsedCount = oldUsedCount + usedCount;
            item.setUsedServiceCount(newUsedCount);
            item.setModifier(userId);
            accountComboServiceRelDao.updateById(item);
            //若服务用完,则将该项服务所在的combo加入待检查集合
            if (newUsedCount.equals(item.getTotalServiceCount())) {
                updateComboIdSet.add(item.getComboId());
            }
        }

        //3.检查combo的服务是否已全部用完,修改用完的combo的状态
        if (!CollectionUtils.isEmpty(updateComboIdSet)) {
            Iterator<Long> iterator = updateComboIdSet.iterator();
            while (iterator.hasNext()) {
                Long comboId = iterator.next();
                List<AccountComboServiceRel> accountComboServiceRelList1 =
                        accountComboServiceRelDao.selectByComboId(comboId);
                //判断某个combo对应的Service是否全部用完
                boolean isExhausted = true;
                Iterator<AccountComboServiceRel> iterator1 = accountComboServiceRelList1.iterator();
                while (iterator1.hasNext()) {
                    AccountComboServiceRel accountComboServiceRel = iterator1.next();
                    if (!accountComboServiceRel.getTotalServiceCount()
                            .equals(accountComboServiceRel.getUsedServiceCount())) {
                        isExhausted = false;
                        break;
                    }
                }
                //若服务用完,更改combo的状态
                if (isExhausted) {
                    AccountCombo combo = new AccountCombo();
                    combo.setId(comboId);
                    combo.setModifier(userId);
                    combo.setComboStatus(AccountCombo.EXHAUSTED);
                    accountComboDao.updateById(combo);
                }
            }
        }
        //4.记录交易流水
        accountComboFlowDetailService.recordDetailForConsume(shopId, userId, flowIdMap, consumeBoMap, accountComboServiceRelList);

    }



    @Override
    public void reverseConsume(Long userId, Long shopId, Long reverseFlowId, AccountComboFlowDetail comboDetail) {
        if (comboDetail != null) {
            Long comboId = comboDetail.getComboId();

            //减回服务的已使用次数
            AccountComboServiceRel comboService =
                    accountComboServiceRelDao.selectById(comboDetail.getServiceId());
            Integer usedServiceCount = comboService.getUsedServiceCount();
            Integer newUsedServiceCount = usedServiceCount - comboDetail.getChangeCount();
            comboService.setUsedServiceCount(newUsedServiceCount);
            accountComboServiceRelDao.updateById(comboService);
            //插反流水
            accountComboFlowDetailService.recordRevertDetailForConsume(userId, shopId, reverseFlowId, comboDetail);
            //更改服务对应套餐的状态
            AccountCombo combo = accountComboDao.selectById(comboId);
            Integer comboStatus = combo.getComboStatus();
            if (comboStatus.equals(1)) {
                combo.setComboStatus(0);
                accountComboDao.updateById(combo);
            }
            log.info("reverse combo service consume succeed");
        } else {
            log.error("comboDetail is null");
        }
    }

    @Override
    public Boolean comboIsUsed(Long comboId, Long shopId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("comboId", comboId);
        List<AccountComboServiceRel> serviceRelList = accountComboServiceRelDao.select(param);
        if (!CollectionUtils.isEmpty(serviceRelList)) {
            for (AccountComboServiceRel item : serviceRelList) {
                if (!item.getUsedServiceCount().equals(0)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void reverseRecharge(Long shopId, Long comboId) {
        Map comboParam = Maps.newHashMap();
        comboParam.put("shopId",shopId);
        comboParam.put("id",comboId);
        accountComboDao.delete(comboParam);

        Map serviceParam = Maps.newHashMap();
        serviceParam.put("shopId", shopId);
        serviceParam.put("comboId",comboId);
        accountComboServiceRelDao.delete(serviceParam);
    }

    @Override
    public AccountCombo importCombo(AccountCombo combo, Long shopId, Long userId) {
        combo.setShopId(shopId);
        combo.setCreator(userId);
        combo.setModifier(userId);
        combo.setComboStatus(0);
        accountComboDao.insert(combo);

        List<AccountComboServiceRel> serviceList = combo.getServiceList();
        if (!CollectionUtils.isEmpty(serviceList)) {
            for (AccountComboServiceRel service : serviceList) {
                service.setShopId(shopId);
                service.setCreator(userId);
                service.setModifier(userId);
                service.setComboId(combo.getId());
                accountComboServiceRelDao.insert(service);
            }
        }
        return combo;
    }


    @Override
    public List<AccountCombo> listCombo(Long shopId,Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        param.put("comboStatus", AccountCombo.NOT_EXHAUSTED);
        List<AccountCombo> accountComboList = accountComboDao.select(param);
        for (AccountCombo item : accountComboList) {
            List<AccountComboServiceRel> serviceList = accountComboServiceRelDao.selectByComboId(item.getId());
            item.setServiceList(serviceList);
        }
        return accountComboList;
    }

    @Override
    public List<AccountCombo> listComboByAccountIds(List<Long> accountIds, Long shopId) {
        if (accountIds == null) {
            return Lists.newArrayList();
        }
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountIds", accountIds);
        param.put("comboStatus", AccountCombo.NOT_EXHAUSTED);
        List<AccountCombo> accountComboList = accountComboDao.select(param);
        return getAccountCombosWithServiceList(shopId, accountComboList);
    }

    private List<AccountCombo> getAccountCombosWithServiceList(Long shopId, List<AccountCombo> accountComboList) {
        if (CollectionUtils.isEmpty(accountComboList)) {
            return Lists.newArrayList();
        }
        List<Long> accountComboIds = Lists.newArrayList();
        for (AccountCombo item : accountComboList) {
            accountComboIds.add(item.getId());
        }
        List<AccountComboServiceRel> accountComboServiceRels = accountComboServiceRelDao.selectByComboIds(shopId,accountComboIds);
        Multimap<Long,AccountComboServiceRel> accountComboServiceRelMap = HashMultimap.create();
        if (!CollectionUtils.isEmpty(accountComboServiceRels)) {
            for (AccountComboServiceRel accountComboServiceRel : accountComboServiceRels) {
                accountComboServiceRelMap.put(accountComboServiceRel.getComboId(),accountComboServiceRel);
            }
            for (AccountCombo item : accountComboList) {
                List<AccountComboServiceRel> accountComboServiceRelList = Lists.newArrayList(accountComboServiceRelMap.get(item.getId()));
                item.setServiceList(accountComboServiceRelList);
            }
        }

        return accountComboList;
    }

    @Override
    @Deprecated
    public List<AccountCombo> listAvailableCombo(Long shopId, Long accountId) {
        List<AccountCombo> accountCombos = listCombo(shopId,accountId);
        return getUnExpiredAccountCombos(accountCombos);
    }

    private List<AccountCombo> getUnExpiredAccountCombos(List<AccountCombo> accountCombos) {
        if (!CollectionUtils.isEmpty(accountCombos)) {
            List<AccountCombo> availableComboList = new ArrayList<>();
            for (AccountCombo item : accountCombos) {
                if (!item.isExpired()) {
                    availableComboList.add(item);
                }
            }
            return availableComboList;
        }
        return Lists.newArrayList();
    }

    @Override
    public List<AccountCombo> listAvailableComboByAccountIds(Long shopId, List<Long> accountIds) {
        List<AccountCombo> accountCombos = listComboByAccountIds(accountIds,shopId);
        return getUnExpiredAccountCombos(accountCombos);
    }

    @Override
    @Deprecated
    public List<AccountCombo> listAllCombo(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountId", accountId);
        List<AccountCombo> accountComboList = accountComboDao.select(param);
        return getAccountCombosWithServiceList(shopId, accountComboList);
    }

    @Override
    public List<AccountCombo> listAllComboByAccountIds(Long shopId, List<Long> accountIds) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("accountIds", accountIds);
        List<AccountCombo> accountComboList = accountComboDao.select(param);
        return getAccountCombosWithServiceList(shopId, accountComboList);
    }

    @Override
    public Integer selectCount(Long shopId, Long accountId) {
        Map param = Maps.newHashMap();
        param.put("accountId", accountId);
        param.put("comboStatus", AccountCombo.NOT_EXHAUSTED);
        return accountComboDao.selectCount(param);
    }

    @Override
    public AccountCombo getComboWithService(Long shopId, Long comboId) {

        AccountCombo combo = findById(shopId, comboId);
        List<AccountComboServiceRel> serviceList = comboServiceRelService.findByComboId(shopId,comboId);
        combo.setServiceList(serviceList);
        return combo;
    }

    @Override
    public AccountCombo findById(Long shopId, Long comboId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", comboId);
        List<AccountCombo> combos = accountComboDao.select(param);
        if (combos.isEmpty()) {
            return null;
        }
        return combos.get(0);

    }

    @Override
    public AccountTradeFlow rechargeCombo(RechargeComboBo rechargeComboBo, Long shopId, Long userId) {

        ComboInfo comboInfo = comboInfoDao.selectById(rechargeComboBo.getComboInfoId());
        if (comboInfo == null) {
            throw new BizException("无此计次卡类型");
        }
        //1.插accountCombo
        AccountCombo combo = addAccountCombo(rechargeComboBo, shopId, userId, comboInfo);
        //2.插对应serviceRel
        List<ComboInfoServiceRel> comboInfoServiceRelList =
                comboInfoServiceRelDao.selectByComboInfoId(rechargeComboBo.getComboInfoId());
        addComboServiceRel(shopId, userId, combo, comboInfoServiceRelList);

        //3.记充值流水
        AccountTradeFlow flow = accountFlowService.recordFlowForComboCharge(rechargeComboBo, shopId, userId, combo);
        return flow;


    }

    private void addComboServiceRel(Long shopId, Long userId, AccountCombo combo, List<ComboInfoServiceRel> comboInfoServiceRelList) {
        List<AccountComboServiceRel> comboServiceRelList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comboInfoServiceRelList)) {
            for (ComboInfoServiceRel item : comboInfoServiceRelList) {
                AccountComboServiceRel comboServiceRel = new AccountComboServiceRel();
                comboServiceRel.setCreator(userId);
                comboServiceRel.setModifier(userId);
                comboServiceRel.setShopId(shopId);
                comboServiceRel.setComboId(combo.getId());
                comboServiceRel.setServiceId(item.getServiceId());
                comboServiceRel.setServiceName(item.getServiceName());
                comboServiceRel.setTotalServiceCount(item.getServiceCount());
                comboServiceRel.setUsedServiceCount(0);
                comboServiceRelList.add(comboServiceRel);
            }
            accountComboServiceRelDao.batchInsert(comboServiceRelList);
        }
    }

    private AccountCombo addAccountCombo(RechargeComboBo rechargeComboBo, Long shopId, Long userId, ComboInfo comboInfo) {
        AccountCombo combo = new AccountCombo();
        combo.setCreator(userId);
        combo.setModifier(userId);
        combo.setShopId(shopId);
        if(comboInfo.getCustomizeTime()==ComboInfo.NON_CUSTOM_TIME){
            combo.setEffectiveDate(new Date());
            combo.setExpireDate(DateUtil.addDate(new Date(),
                    comboInfo.getEffectivePeriodDays().intValue()));
        }else if(comboInfo.getCustomizeTime() == ComboInfo.CUSTOMIZED_TIME){
            combo.setEffectiveDate(comboInfo.getEffectiveDate());
            combo.setExpireDate(comboInfo.getExpireDate());
        }
        combo.setReceiver(rechargeComboBo.getRecieverId());
        combo.setReceiverName(rechargeComboBo.getRecieverName());
        combo.setAccountId(rechargeComboBo.getAccountId());
        combo.setComboStatus(AccountCombo.NOT_EXHAUSTED);
        combo.setComboInfoId(comboInfo.getId());
        combo.setComboName(comboInfo.getComboName());

        ShopManager shopManager = null;
        if (userId != null) {
            shopManager = shopManagerService.selectById(userId);
        }
        if(shopManager != null){
            combo.setOperatorName(shopManager.getName());
        } else {
            combo.setOperatorName("");
        }
        accountComboDao.insert(combo);

        // 记录log
        log.info(MarketCardCouponLog.grantComboLog(shopId));
        return combo;
    }

    @Override
    public List<AccountCombo> getAccountCombos(Map param) {
        return accountComboDao.select(param);
    }

    @Override
    public List<Long> getAccountIdsByComboInfoId(Long shopId, Long id) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("comboInfoId",id);
        List<AccountCombo> accountCombos = accountComboDao.select(param);
        List<Long> comboIds = null;
        comboIds = Lists.transform(accountCombos, new Function<AccountCombo, Long>() {
            @Override
            public Long apply(AccountCombo accountCombo) {
                return accountCombo.getId();
            }
        });
        return comboIds;
    }
    @Override
    public Integer count(Map<String, Object> comboSearchMap) {
        if(comboSearchMap==null ||comboSearchMap.isEmpty()){
            return 0;
        }
        return accountComboDao.selectCount(comboSearchMap);
    }

    @Override
    public List<AccountCombo> selectComboAndService(Map<String, Object> comboSearchMap) {
        if(comboSearchMap==null ||comboSearchMap.isEmpty()){
            return new ArrayList<>();
        }
        List<AccountCombo> accountComboList = accountComboDao.select(comboSearchMap);
        if(CollectionUtils.isEmpty(accountComboList)){
            return accountComboList;
        }
        //.查询计次卡关联的服务
        Set<Long> comboIds = new HashSet<>();
        for (AccountCombo accountCombo : accountComboList) {
            comboIds.add(accountCombo.getId());
        }
        Map<String,Object> searchComboSvRelMap = new HashMap<>();
        searchComboSvRelMap.put("comboIds",comboIds);
        List<AccountComboServiceRel> accountComboServiceRelList = accountComboServiceRelDao.select(searchComboSvRelMap);
        //.归并同comboId的服务关系
        Map<Long,List<AccountComboServiceRel>> accountComboServiceRelMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(accountComboServiceRelList)){
            for (AccountComboServiceRel accountComboServiceRel : accountComboServiceRelList) {
                List<AccountComboServiceRel> accountComboServiceRelList1 = accountComboServiceRelMap.get(accountComboServiceRel.getComboId());
                if(accountComboServiceRelList1==null){
                    accountComboServiceRelList1 = new ArrayList<>();
                }
                accountComboServiceRelList1.add(accountComboServiceRel);
                accountComboServiceRelMap.put(accountComboServiceRel.getComboId(),accountComboServiceRelList1);
            }
        }
        //.组装数据
        for (AccountCombo accountCombo : accountComboList) {
            List<AccountComboServiceRel> serviceList = accountComboServiceRelMap.get(accountCombo.getId());
            accountCombo.setServiceList(serviceList);
        }
        return accountComboList;
    }


    @Override
    public List<AccountCombo> listForWechat(ComboPageParam param) {
        Long shopId = param.getShopId();
        List<AccountCombo> rawList = listByAccountIds(shopId, param.getAccountIds());
        List<AccountCombo> stateFilteredList = filterByStatus(rawList, param.getComboStatus());
        List<AccountCombo> expireFilteredList = filterByExpire(stateFilteredList, param.getExpireStautus());
        battchAttachInfo(shopId, expireFilteredList);
        battchAttachService(shopId, expireFilteredList);
        Collections.sort(expireFilteredList, new Comparator<AccountCombo>() {
            @Override
            public int compare(AccountCombo o1, AccountCombo o2) {
                int firstSort = o1.getComboStatus().compareTo(o2.getComboStatus());
                int secondSort = firstSort == 0 ? o1.getExpireDate().compareTo(o2.getEffectiveDate()) : firstSort;
                int thirdSort = secondSort == 0 ? o2.getGmtModified().compareTo(o1.getGmtModified()) : secondSort;
                return thirdSort;
            }
        });
        return expireFilteredList;
    }

    @Override
    public void batchSave(List<AccountCombo> accountCombos) {
        super.batchInsert(accountComboDao,accountCombos,1000);
    }

    @Override
    public List<AccountCombo> findByAccountIds(Long shopId, List<Long> accountIds, String startTime) {
        return accountComboDao.findByAccountIds(shopId, accountIds, startTime);
    }

    @Override
    public void insert(AccountCombo accountCombo) {
        super.save(accountComboDao,accountCombo);
    }

    /**
     * 批量查询账户下未过期的计次卡数量
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    @Override
    public Map<Long, Long> getUnExpireComboNum(Long shopId, Collection<Long> accountIds) {
        Assert.isTrue(shopId != null && shopId > 0);
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> unExpireComboNumMap = Maps.newHashMap();
        List<CommonPair<Long, Long>> unExpireComboNumList = accountComboDao.getUnExpireComboNum(shopId, accountIds);
        if (Langs.isNotEmpty(unExpireComboNumList)) {
            for (CommonPair<Long, Long> pair : unExpireComboNumList) {
                unExpireComboNumMap.put(pair.getDataF(), pair.getDataS());
            }
        }
        return unExpireComboNumMap;
    }

    /**
     * 批量查询账户下未过期的计次卡
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    @Override
    public List<AccountCombo> getUnExpireComboList(Long shopId, Collection<Long> accountIds) {
        Assert.isTrue(shopId != null && shopId > 0);
        if (Langs.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return accountComboDao.getUnExpireComboList(shopId, accountIds);
    }

    @Override
    public List<AccountCombo> listByIds(Collection<Long> ids) {
        if (Langs.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Long[] idArr = new Long[ids.size()];
        return accountComboDao.selectByIds(ids.toArray(idArr));
    }


    private void battchAttachService(Long shopId, List<AccountCombo> source) {
        if (CollectionUtils.isEmpty(source)) {
            return;
        }
        List<Long> comboIdList = Lists.transform(source, new Function<AccountCombo, Long>() {
            @Override
            public Long apply(AccountCombo input) {
                return input.getId();
            }
        });

        if (CollectionUtils.isEmpty(comboIdList)) {
            return;
        }
        List<AccountComboServiceRel> comboServiceList = comboServiceRelService.listByComboIds(shopId, comboIdList);

        ImmutableMap<Long, AccountCombo> comboMap = Maps.uniqueIndex(source, new Function<AccountCombo, Long>() {
            @Override
            public Long apply(AccountCombo input) {
                return input.getId();
            }
        });

        for (AccountComboServiceRel comboService : comboServiceList) {
            Long comboId = comboService.getComboId();
            AccountCombo combo = comboMap.get(comboId);
            if (combo != null) {
                combo.getServiceList().add(comboService);
            }
        }
    }

    private void battchAttachInfo(Long shopId, List<AccountCombo> source) {
        if (CollectionUtils.isEmpty(source)) {
            return;
        }
        final List<Long> infoIdList = Lists.transform(source, new Function<AccountCombo, Long>() {
            @Override
            public Long apply(AccountCombo input) {
                return input.getComboInfoId();
            }
        });
        if (CollectionUtils.isEmpty(infoIdList)){
            return;
        }
        Set<Long> infoIdSet = Sets.newHashSet(infoIdList);
        List<ComboInfo> comboInfos = comboInfoDao.selectByIdss(shopId, infoIdSet);

        ImmutableMap<Long, ComboInfo> comboInfoMap = Maps.uniqueIndex(comboInfos, new Function<ComboInfo, Long>() {
            @Override
            public Long apply(ComboInfo input) {
                return input.getId();
            }
        });
        for (AccountCombo combo : source) {
            Long comboInfoId = combo.getComboInfoId();
            ComboInfo comboInfo = comboInfoMap.get(comboInfoId);
            combo.setComboInfo(comboInfo);
        }

    }

    private List<AccountCombo> filterByExpire(List<AccountCombo> source, Integer expireStautus) {
        if (expireStautus == null) {
            return source;
        }
        List<AccountCombo> target = Lists.newArrayList();
        if (expireStautus.equals(1)) {
            for (AccountCombo combo : source) {
                if (!combo.isExpired()) {
                    target.add(combo);
                }
            }
        }

        if (expireStautus.equals(2)) {
            for (AccountCombo combo : source) {
                if (combo.isExpired()) {
                    target.add(combo);
                }
            }
        }

        return target;
    }

    private List<AccountCombo> filterByStatus(List<AccountCombo> source, Integer status) {
        if (status == null) {
            return source;
        }
        List<AccountCombo> target = Lists.newArrayList();
        for (AccountCombo combo : source) {
            if (combo.getComboStatus().equals(status)) {
                target.add(combo);
            }
        }
        return target;
    }
    private List<AccountCombo> listByAccountIds(Long shopId, List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return Lists.newArrayList();
        }
        return accountComboDao.selectByAccountIds(shopId, accountIds);

    }
}
