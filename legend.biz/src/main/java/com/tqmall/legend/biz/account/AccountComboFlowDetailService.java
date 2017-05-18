package com.tqmall.legend.biz.account;

import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountComboFlowDetail;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.legend.entity.account.vo.RechargeComboFlowBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/6/17.
 */
public interface AccountComboFlowDetailService {
    /**
     * 根据主流水Id获取流水详情
     *
     * @param shopId
     * @param flowId
     * @return
     */
    List<AccountComboFlowDetail> findByFlowId(Long shopId, Long flowId);

    /**
     * 分页获取几次卡发放流水
     *
     * @param pageable
     * @param shopId
     * @return
     */
    Page<RechargeComboFlowBo> getPageRechargeFlow(Pageable pageable, Long shopId);

    /**
     * 记录消费明细
     * @param shopId
     * @param userId
     * @param consumeBoMap
     * @param accountComboServiceRelList
     */
    void recordDetailForConsume(Long shopId, Long userId, Map<Long, Long> flowIdMap, Map<Long, Integer> consumeBoMap, List<AccountComboServiceRel> accountComboServiceRelList);

    /**
     * 记录消费撤销明细
     * @param userId
     * @param shopId
     * @param reverseFlowId
     * @param comboDetail
     */
    void recordRevertDetailForConsume(Long userId, Long shopId, Long reverseFlowId, AccountComboFlowDetail comboDetail);

    /**
     * 记录充值明细
     * @param shopId
     * @param userId
     * @param accountTradeFlowId
     */
    void recordDetailForCharge(Long shopId, Long userId, Long accountTradeFlowId, AccountCombo combo);

    /**
     * 记录充值撤销明细
     * @param userId
     * @param shopId
     * @param detailList
     * @param reverseFlowID
     */
    void recordRevertDetailForRecharge(Long userId, Long shopId, List<AccountComboFlowDetail> detailList, Long reverseFlowID);

    void recordFlowForComboImport(AccountCombo combo, Long shopId, Long userId, Long flowId);

    List<AccountComboFlowDetail> getAccountComboFlowDetails(Map param);

    /**
     * 查询计次卡交易流水
     * @param shopID
     * @param comboId
     * @return
     */
    List<AccountComboFlowDetail> listByComboId(Long shopID, Long comboId);

    void batchSave(List<AccountComboFlowDetail> accountComboFlowDetails);
}
