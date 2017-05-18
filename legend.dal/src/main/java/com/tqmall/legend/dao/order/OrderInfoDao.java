package com.tqmall.legend.dao.order;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.order.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface OrderInfoDao extends BaseDao<OrderInfo> {


    OrderInfo selectByIdAndShopId(Map<String, Object> params);

    /**
     * create by jason 2015-07-10
     * 2C-APP 根据联系人获得工单列表
     *
     * @param params
     */
    List<OrderInfo> selectByContactMobileAndLicense(Map<String, Object> params);

    /**
     * 根据orderIds和shopId获取工单列表
     * @param orderIds
     * @param shopId
     * @return
     */
    public List<OrderInfo> selectByIdsAndShopId( @Param("orderIds") List<Long> orderIds , @Param("shopId") Long shopId);

    /**
     * 查询未回访工单
     *
     * @param shopId
     * @param confirmTimeGt
     * @param confirmTimeLt
     * @return
     */
    List<OrderInfo> findUnVisitList(@Param("shopId") Long shopId,
                                    @Param("confirmTimeGt") Date confirmTimeGt,
                                    @Param("confirmTimeLt") Date confirmTimeLt);

    /**
     * 更新工单回访状态为已回访
     *
     * @param shopId
     * @param orderId
     * @return
     */
    int updateOrderVisit(@Param("shopId") Long shopId, @Param("orderId") Long orderId);

    /**
     * 查询门店所有开过单的服务顾问ids
     *
     * @param shopId
     * @return
     */
    List<Long> selectReceiverByShopId(@Param("shopId") Long shopId);
}
