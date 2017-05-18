package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface AccountComboServiceRelDao extends BaseDao<AccountComboServiceRel> {

    List<AccountComboServiceRel> selectByComboId(@Param("comboId") Long comboId);

    List<AccountComboServiceRel> selectByComboIds(@Param("shopId") Long shopId, @Param("comboIds") Collection<Long> comboIds);
}
