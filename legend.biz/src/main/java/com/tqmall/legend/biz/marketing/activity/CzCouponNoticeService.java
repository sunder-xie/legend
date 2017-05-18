package com.tqmall.legend.biz.marketing.activity;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/10.
 */
public interface CzCouponNoticeService {
    /**
     * 获取车主公告信息
     *
     * @param param 入参
     * @return
     */
    public List<CzCouponNotice> select(Map param);

    /**
     * 根据id获取车主公告信息
     *
     * @param id 入参
     * @return
     */
    public CzCouponNotice getById(Long id);

    /**
     * 保存/更新车主公告优惠信息
     *
     * @param czCouponNotice 入参
     * @return
     */
    public Result saveOrUpdate(CzCouponNotice czCouponNotice);

    /**
     * 车主app端回写更新门店公告优惠信息
     *
     * @param czCouponNotice 入参
     * @return
     */
    public Result update(CzCouponNotice czCouponNotice);

    /**
     * 获取总数量
     *
     * @param param 入参
     * @return
     */
    public Integer selectCount(Map<String, Object> param);

}
