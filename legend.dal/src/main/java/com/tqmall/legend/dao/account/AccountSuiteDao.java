package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountSuite;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface AccountSuiteDao extends BaseDao<AccountSuite> {

    List<AccountSuite> selectByFlowIds(@Param("shopId") Long shopId, @Param("flowIds") Collection<Long> flowIds);
}
