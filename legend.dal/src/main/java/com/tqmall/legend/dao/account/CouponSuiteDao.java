package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.CouponSuite;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface CouponSuiteDao extends BaseDao<CouponSuite> {

    /**
     * 批量查询
     * @param shopId
     * @param suiteInfoIds
     * @return
     */
    List<CouponSuite> selectByIdss(@Param("shopId") Long shopId, @Param("ids") Collection<Long> suiteInfoIds);
}
