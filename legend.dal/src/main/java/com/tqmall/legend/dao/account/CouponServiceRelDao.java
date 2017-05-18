package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.CouponServiceRel;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface CouponServiceRelDao extends BaseDao<CouponServiceRel> {

    List<CouponServiceRel> selectByCouponInfoIds(@Param("shopId") Long shopId, @Param("couponInfoIds") Collection<Long> couponInfoIds);
}
