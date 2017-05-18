package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AccountTradeFlowDao extends BaseDao<AccountTradeFlow> {

    BigDecimal getLeftValue(Map param);

    List<AccountTradeFlow> getAccountTradeFlowList(Map param);


    BigDecimal getAccountCardChangeAmount(Map param);


    List<AccountTradeFlow> selectByIds(@Param("shopID") Long shopID,
                                       @Param("ids") Collection<Long> flowIds);

    AccountTradeFlow selectByIdAndShopId(@Param("shopId") Long shopId, @Param("id") Long id);

    /**
     * 获取某个时间段批量添加的账户交易流水
     *
     * @param shopId
     * @param accountIds  账户ids
     * @param consumeType
     * @return
     */
    List<AccountTradeFlow> findFlowByAccountIdsMobilesAndConsumeType(@Param("shopId") Long shopId, @Param("accountIds") List<Long> accountIds, @Param("consumeType") int consumeType, @Param("importFlag") String importFlag);

    /**
     * 获取账户的累计充值金额
     *
     * @param shopId
     * @param accountId
     * @return
     */
    BigDecimal getTotalChargeAmountByCardIds(@Param("shopId") Long shopId, @Param("cardIds") List<Long> cardIds);

    /**
     * 查询会员卡累计收款金额
     *
     * @param shopId
     * @param ids
     * @return
     */
    BigDecimal getTotalPayAmount(@Param("shopId") Long shopId, @Param("ids") List<Long> ids);
}
