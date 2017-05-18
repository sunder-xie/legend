package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.CouponInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface CouponInfoDao extends BaseDao<CouponInfo> {
    /**
     * 根据优惠券名获取优惠券类型信息
     * @param shopId
     * @param names
     * @return
     */
    List<CouponInfo> findCouponInfoByNames(@Param("shopId")Long shopId,@Param("names")List<String> names);

}
