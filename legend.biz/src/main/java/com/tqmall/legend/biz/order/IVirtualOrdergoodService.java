package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.VirtualOrdergood;

import java.util.List;
import java.util.Set;

/**
 * Created by dongc on 15/9/11.
 */
public interface IVirtualOrdergoodService {


    /**
     * save virtual OrderGood
     *
     * @param orderGoods
     * @return
     */
    int save(VirtualOrdergood orderGoods);

    /**
     * update virtual OrderGood
     *
     * @param virtualOrdergood
     * @return
     */
    int update(VirtualOrdergood virtualOrdergood);

    /**
     * 查询物料
     *
     * @param orderId
     * @param shopId
     * @param l
     * @return
     */
    List<VirtualOrdergood> queryOrderGoods(Long orderId, Long shopId, long l);


    /**
     * 查询物料
     *
     * @param orderId
     * @param shopId
     * @return
     */
    List<VirtualOrdergood> queryOrderGoods(Long orderId, Long shopId);

    /**
     * 批量删除
     *
     * @param orderIdSet
     */
    int deleteByIds(Set<Long> orderIdSet);
}
