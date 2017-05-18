package com.tqmall.legend.biz.account;


import com.tqmall.legend.biz.account.bo.CouponCreateBo;
import com.tqmall.legend.entity.account.CouponInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CouponInfoService {

    List<CouponInfo> select (Map param);

    /**
     * 根据优惠券类型id获取优惠券类型信息
     * @param shopId
     * @param ids
     * @return
     */
    List<CouponInfo> findByIds(Long shopId, Long ... ids);

    List<CouponInfo> findByIds(Long shopId, Collection<Long> ids);

    CouponInfo selectById(Long id, Long shopId);

    void updateBase(CouponInfo couponInfo);

    List<CouponInfo> selectWithCount(Map<String, Object> param);


    String composeUseRangeDescript(CouponInfo couponInfo);

    Integer selectCount(Map<String, Object> param);


    Map<String,CouponInfo> getMapOfCouponName(Long shopId);

    /**
     * 判断优惠券名字是否重复
     * @param shopId
     * @param couponId
     * @param name
     * @return
     */
    Boolean isCouponNameRepeated(Long shopId, Long couponId, String name);

    CouponInfo find(Long shopId, String name);

    /**
     * 创建优惠券类型
     * @param couponCreateBo
     */
    void create(CouponCreateBo couponCreateBo);

    /**
     * 修改优惠券类型
     * @param couponUpdateBo
     */
    void update(CouponCreateBo couponUpdateBo);

    void delete(Long shopId, Long couponInfoId);
    void batchAttachCouponService(Long shopId, List<CouponInfo> couponInfos);

    /**
     * 批量设置优惠券的服务使用范围(couponServiceList)
     * @param couponInfoList
     */
    void attachUseRange(List<CouponInfo> couponInfoList);

    /**
     * 根据优惠券名获取优惠券类型信息
     * @param shopId
     * @param names
     * @return
     */
    List<CouponInfo> findCouponInfoByNames(Long shopId,List<String> names);
}
