package com.tqmall.legend.dao.account;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountCombo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface AccountComboDao extends BaseDao<AccountCombo> {
    List<CommonPair<Long, Integer>> findUsedCardType(@Param("shopId") Long shopId);

    List<AccountCombo> selectByAccountIds(@Param("shopId") Long shopId, @Param("accountIds") List<Long> accountIds);

    List<AccountCombo> findByAccountIds(@Param("shopId") Long shopId, @Param("accountIds") List<Long> accountIds, @Param("startTime")String startTime);

    List<CommonPair<Long, Long>> getUnExpireComboNum(@Param("shopId") Long shopId, @Param("accountIds") Collection<Long> accountIds);

    List<AccountCombo> getUnExpireComboList(@Param("shopId") Long shopId, @Param("accountIds") Collection<Long> accountIds);
}
