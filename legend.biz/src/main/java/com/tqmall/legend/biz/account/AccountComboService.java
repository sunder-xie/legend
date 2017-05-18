package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.account.bo.ComboPageParam;
import com.tqmall.legend.biz.account.bo.ConsumeComboBo;
import com.tqmall.legend.biz.account.bo.RechargeComboBo;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.AccountTradeFlow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/6/17.
 */
public interface AccountComboService {
    /**
     * 获取客户的计次卡列表(计次卡包含服务信息)
     *
     * @param accountId
     * @param shopId
     * @return
     */
    List<AccountCombo> listCombo(Long accountId, Long shopId);

    /**
     * 获取客户的计次卡列表(包含服务信息)
     * @param accountIds
     * @param shopId
     * @return
     */
    List<AccountCombo> listComboByAccountIds(List<Long> accountIds,Long shopId);

    /**
     * 获取客户的可用计次卡列表(计次卡包含服务信息)
     *
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountCombo> listAvailableCombo(Long shopId, Long accountId);

    /**
     * 获取客户的可用计次卡列表(计次卡包含服务信息)
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCombo> listAvailableComboByAccountIds(Long shopId, List<Long> accountIds);


    /**
     * 获取客户的所有计次卡列表(计次卡包含服务信息)
     *
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountCombo> listAllCombo(Long shopId, Long accountId);

    /**
     * 获取客户的所有计次卡列表(计次卡包含服务信息)
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCombo> listAllComboByAccountIds(Long shopId, List<Long> accountIds);

    /**
     * @param shopId
     * @param account
     * @return
     */
    Integer selectCount(Long shopId, Long account);

    /**
     * 获取计次卡实例(包含服务信息)
     *
     * @param shopId
     * @param comboId
     * @return
     */
    AccountCombo getComboWithService(Long shopId, Long comboId);

    /**
     * 获取计次卡实例(不包含服务信息)
     *
     * @param shopId
     * @param comboId
     * @return
     */
    AccountCombo findById(Long shopId, Long comboId);

    /**
     * 充值计次卡(计次卡办理)
     * comboRechargeBo not null
     *
     * @param comboRechargeBo
     * @param shopId
     * @param userId
     * @return
     */
    AccountTradeFlow rechargeCombo(RechargeComboBo comboRechargeBo, Long shopId, Long userId);

    /**
     * 使用计次卡结算
     * consumeComboBoList not empty
     *
     * @param consumeComboBoList
     * @param shopId
     * @param userId
     * @return
     */
    void consumeCombo(Long shopId, Long userId, List<ConsumeComboBo> consumeComboBoList, Map<Long, Long> flowIdMap);

    /**
     * 根据流水详情撤销消费
     *
     * @param userId
     * @param shopId
     * @param flowId
     * @param comboDetail
     */
    void reverseConsume(Long userId, Long shopId, Long flowId, AccountComboFlowDetail comboDetail);

    Boolean comboIsUsed(Long comboId, Long shopId);

    void reverseRecharge(Long shopId, Long comboId);

    AccountCombo importCombo(AccountCombo combo, Long shopId, Long userId);

    List<AccountCombo> getAccountCombos(Map param);

    /**
     * 根据计次卡类型获取accountIds
     *
     * @param shopId
     * @param id
     * @return
     */
    List<Long> getAccountIdsByComboInfoId(Long shopId, Long id);


    /**
     * 计数查询(查询条件全部来源于入参)
     *
     * @param comboSearchMap
     * @return
     */
    public Integer count(Map<String, Object> comboSearchMap);

    /**
     * 列表查询(查询条件全部来源于入参)
     *
     * @param comboSearchMap
     * @return
     */
    public List<AccountCombo> selectComboAndService(Map<String, Object> comboSearchMap);


    List<AccountCombo> listForWechat(ComboPageParam param);

    void batchSave(List<AccountCombo> accountCombos);

    /**
     * 获取对应账户id的信息
     *
     * @param shopId
     * @param accountIds
     * @param startTime
     * @return
     */
    List<AccountCombo> findByAccountIds(Long shopId, List<Long> accountIds, String startTime);

    void insert(AccountCombo accountCombo);

    /**
     * 批量查询账户下未过期的计次卡数量
     * @param shopId
     * @param accountIds
     * @return
     */
    Map<Long, Long> getUnExpireComboNum(Long shopId, Collection<Long> accountIds);

    /**
     * 批量查询账户下未过期的计次卡
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCombo> getUnExpireComboList(Long shopId, Collection<Long> accountIds);

    List<AccountCombo> listByIds(Collection<Long> ids);
}
