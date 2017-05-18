package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.account.bo.CardUpgradeBo;
import com.tqmall.legend.biz.account.vo.BackCardVo;
import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountCardFlowDetail;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.vo.MemberCardChargeVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghui on 6/2/16.
 */
public interface MemberCardService extends BaseService {
    /**
     * 判断是否存在会员卡
     *
     * @param shop      门店id
     * @param accountId 账户id
     */
    boolean isExist(Long shop, Long accountId);

    /**
     * 判断会员卡是否存在
     *
     * @param shopId
     * @param cardNumber
     * @return
     */
    boolean isExistCardNumber(Long shopId, String cardNumber, Long memberCardId);

    boolean saveMemberCard(MemberCard memberCard);

    MemberCard getMemberCardById(Long shopId, Long cardId);

    MemberCard getMemberCardByNumber(Long shopId, String cardNumber);

    /**
     * 查询账户对应的会员卡特权信息
     */
    Result queryAccountMemberCardType(Long shopId, Long accountId);

    /**
     * 会员卡消费
     *
     * @param shopId
     * @param userId
     * @param memberCard
     * @param payAmount
     * @return
     */
    MemberCard consume(Long shopId, Long userId, MemberCard memberCard, BigDecimal payAmount);

    MemberCard revertRecharge(Long shopId, Long userId, Long cardId, BigDecimal amount);

    MemberCard revertConsume(Long shopId, Long userId, Long cardId, BigDecimal payAmount);

    /**
     * 会员卡充值
     *
     * @param shopId
     * @param operatorId
     * @param memberCardChargeVo
     * @return
     */
    AccountTradeFlow recharge(Long shopId, Long operatorId, MemberCardChargeVo memberCardChargeVo);

    void delete(Long shopId);

    void addExpenseCount(MemberCard card);

    void subExpenseCount(MemberCard card);

    Page<MemberCard> getMemberCardPages(Pageable pageable, Map searchMap);


    List<MemberCard> findByAccountIds(List<Long> accountIds);

    boolean update(MemberCard memberCard);

    /**
     * 会员卡退卡操作
     *
     */
    boolean backCard(BackCardVo backCardVo);

    boolean insert(MemberCard memberCard);


    MemberCard findById(Long id);

    /**
     * 根据flowId查找主流水
     *
     * @param shopId
     * @param flowId
     * @return
     */
    AccountTradeFlow findFlowByFlowId(Long shopId, Long flowId);

    /**
     * 根据flowId查找会员卡流水
     *
     * @param shopId
     * @param flowId
     * @return
     */
    AccountCardFlowDetail findCardFlowByFlowId(Long shopId, Long flowId);

    /**
     * 根据卡号获取会员卡详细信息
     *
     * @param shopId
     * @param cardNumber
     * @return
     */
    MemberCard findByCardNumber(Long shopId, String cardNumber);

    void deleteMemberCard(Long cardId);

    public List<MemberCard> findAccountIdsWithDel(List<Long> accountIds);

    Integer batchInsert(List<MemberCard> memberCardList);

    /**
     * 获取门店所有已办卡的accountId
     *
     * @param shopId
     * @return
     */
    List<Long> selectAccountIdsByShopId(Long shopId);

    /**
     * 从输入的accountIds中找出已办卡的accountId
     *
     * @param accountIds
     * @param shopId
     * @return
     */
    List<Long> selectGrantedAccountIds(List<Long> accountIds, Long shopId);

    /**
     * 从输入的卡号中找出已存在的卡号
     *
     * @param shopId
     * @param subList
     * @return
     */
    List<String> selectExistedCardNumbers(Long shopId, List<String> subList);

    /**
     * 判断会员卡是否过期
     *
     * @param date 过期日期
     * @return
     */
    boolean isMemberCardExpire(Date date);

    List<MemberCard> getMemberCards(Map param);

    /**
     * 根据会员卡类型id查找办理该会员卡的账户id
     *
     * @param id
     * @param shopId
     * @return
     */
    List<Long> getAccountIdListByMemberCardInfoId(Long id, Long shopId, String cardNum);

    List<MemberCard> listByAccountIds(Long shopId, Collection<Long> accountIds);

    boolean upgradeCard(CardUpgradeBo cardUpgradeBo);

    /**
     * 根据会员卡id查找会员卡信息（包含已删除的）
     * @param shopId
     * @param cardId
     * @return
     */
    MemberCard findByIdContainDeleted(Long shopId, Long cardId);

    /**
     * 根据会员卡id查询包括已删除的会员卡信息
     * @param shopId
     * @param memberCardIds
     * @return
     */
    List<MemberCard> getMemberCardWithDeletedByIds(Long shopId,List<Long> memberCardIds);

    /**
     * 获取某个会员卡的升级路径
     * @param shopId
     * @param cardId
     * @return
     */
    List<MemberCard> getUpgradedCardsContainDeletedById(Long shopId, Long cardId);

    MemberCard getUpgradedMemberCard(Long shopId, Long cardId);

    /**
     * 判断是否可以办理会员卡
     * @return
     */
    boolean grantMemberCardAble(Long shopId,Long accountId,Long cardTypeId);

    /**
     * 获取门店最大id的会员卡信息
     * @param shopId
     * @return
     */
    MemberCard getMaxIdMemberCard(Long shopId);

    /**
     * 根据账户信息获取会员卡列表
     * @param accountId
     * @return
     */
    List<MemberCard> getMemberCardWithCardInfoListByAccountId(Long accountId);

    /**
     * 获取未过期的会员卡id
     * @return
     */
    List<MemberCard> getUnExpiredMemberCardListByAccountId(Long shopId,Long accountId);

    List<MemberCard> getUnExpiredMemberCardListByAccountIds(Long shopId, Collection<Long> accountIds);

    /**
     * 检测会员卡是否重复
     * @param memberCards
     * @param typeId
     * @return
     */
    boolean checkDuplicate(List<MemberCard> memberCards , Long typeId);

    /**
     * 根据账户id，获取会员卡张数
     * @param shopId
     * @param accountId
     * @return
     */
    int getCountByAccountId(Long shopId,Long accountId);

}
