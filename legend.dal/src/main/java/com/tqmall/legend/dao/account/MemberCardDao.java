package com.tqmall.legend.dao.account;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.MemberCard;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface MemberCardDao extends BaseDao<MemberCard> {
    int countByAccountId(@Param("shopId") Long shopId, @Param("accountId") Long accountId);

    public List<MemberCard> isExistCardNumber(@Param("shopId")Long shopId, @Param("cardNumber") String cardNumber);

    public List<MemberCard> findByAccountIds(List<Long> accountIds);

    public List<MemberCard> findAccountIdsWithDel(List<Long> accountIds);

    List<CommonPair<Long, Integer>> findUsedCardType(@Param("shopId")Long shopId);


    /**
     * 从输入的accountIds中找出已办卡的accountId
     * @param accountIds
     * @param shopId
     * @return
     */
    List<Long> selectGrantedAccountIds(@Param("accountIds") List<Long> accountIds, @Param("shopId") Long shopId);

    /**
     * 从输入的卡号中找出已存在的卡号
     * @param shopId
     * @param cardNumbers
     * @return
     */
    List<String> selectExistedCardNumbers(@Param("shopId") Long shopId, @Param("cardNumbers") List<String> cardNumbers);

    List<MemberCard> findAllSortedCard(@Param("offset") Integer offset, @Param("limit") Integer limit);

    List<MemberCard> selectByAccountIds(@Param("shopId") Long shopId, @Param("accountIds") Collection<Long> accountIds);

    /**
     * 根据会员卡id查询包括已删除的会员卡信息
     * @param shopId
     * @param memberCardIds
     * @return
     */
    List<MemberCard> getMemberCardWithDeletedByIds(@Param("shopId") Long shopId,@Param("ids") List<Long> memberCardIds);

    MemberCard findByIdContainDeleted(@Param("shopId")Long shopId, @Param("cardId")Long cardId);

    MemberCard findByRawIdContainDeleted(@Param("shopId")Long shopId, @Param("rawCardId")Long cardId);

    /**
     * 获取门店最大的shopId
     * @param shopId
     * @return
     */
    MemberCard findMaxMemberCardId(@Param("shopId") Long shopId);

    /**
     * 根据账户信息获取会员卡列表
     * @param accountId
     * @return
     */
    List<MemberCard> getMemberCardListByAccountId(@Param("accountId")Long accountId);
}
