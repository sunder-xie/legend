package com.tqmall.legend.dao.account;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface AccountInfoDao extends BaseDao<AccountInfo> {

    AccountInfo getAccountInfoByCustomerIdAndShopId(@Param("shopId")Long shopId,@Param("customerId")Long customerId);

    List<AccountInfo> selectByCustomerIds(@Param("shopId")Long shopId, @Param("customerIds")Collection<Long> customerIds);

    List<CommonPair<Long,Long>> getCustomerIdAccountIdPair(Long shopId);
}
