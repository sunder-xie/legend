package com.tqmall.legend.dao.account;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.AccountCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface AccountCouponDao extends BaseDao<AccountCoupon> {

    AccountCoupon isExist(@Param("shopId")Long shopId, @Param("accountId")Long accountId);

    List<CommonPair<Long, Integer>> selectUsedCount(Map map);

    List<String> selectExistsSn(@Param("snList") String[] snList);

    List<AccountCoupon> getCountByGroupFlowIdAndCouponInfoId(Map param);

    List<AccountCoupon> getListByGroupFlowIdAndCouponInfoId(Map param);

    Integer countOwnedCouponByTypeId(@Param("shopId")Long shopId, @Param("accountId")Long accountId, @Param("couponTypeId") Long couponTypeId);

    /**
     * 通过优惠券账户交易流水id，获取优惠券账户信息
     * @param shopId
     * @param flowIds
     * @return
     */
    List<AccountCoupon> findAccountCouponByFlowIds(@Param("shopId")Long shopId,@Param("flowIds")List<Long> flowIds);

    List<CommonPair<Long, Long>> getUnExpireCouponNum(@Param("shopId") Long shopId, @Param("accountIds") Collection<Long> accountIds);

    List<AccountCoupon> getUnExpireCouponList(@Param("shopId") Long shopId, @Param("accountIds") Collection<Long> accountIds);
}
