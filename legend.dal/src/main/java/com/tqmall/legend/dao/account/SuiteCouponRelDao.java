package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.SuiteCouponRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface SuiteCouponRelDao extends BaseDao<SuiteCouponRel> {
    List<SuiteCouponRel> getSuiteCouponRelListBySuiteIds(@Param("shopId")Long shopId,@Param("suiteIds")Long[] suiteIds);

}
