package com.tqmall.legend.dao.account;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountCardFlowDetail;
import com.tqmall.legend.entity.account.MemberCardUsedSummay;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@MyBatisRepository
public interface AccountCardFlowDetailDao extends BaseDao<AccountCardFlowDetail> {

    Integer getRechargeCustomerCount(@Param("shopId") Long shopId,
                                     @Param("startTime") Date date1,
                                     @Param("endTime") Date date2);

    BigDecimal getRechargeSummaryAmount(@Param("shopId") Long shopId,
                                        @Param("startTime") Date date1,
                                        @Param("endTime") Date date2);

    List<AccountCardFlowDetail> getCardFlowDetailByFlowIds(@Param("shopId") Long shopId,@Param("flowIds") List flowIds);

    int updateCardNumberByFlowId(@Param("flowId") Long flowId, @Param("cardId") Long cardId);

    /**
     * 查询会员卡累计消费情况
     * @param shopID
     * @param cardId
     * @return
     */
    MemberCardUsedSummay sumUsedInfo(@Param("shopId") Long shopID,@Param("cardId") Long cardId);

    /**
     * 查询会员卡充值流水id集合
     * @param shopId
     * @param memberCardIdList
     * @return
     */
    List<Long> getRechargeTradeFlowIds(@Param("shopId") Long shopId, @Param("memberCardIdList") List<Long> memberCardIdList);

    int getCardConsumeNum(@Param("shopId") Long shopId, @Param("cardIds") List<Long> cardIds);

    List<CommonPair<Long, Long>> getFlowIdAndCardIdRel(@Param("shopId") Long shopId, @Param("flowIds") List flowIds);
}
