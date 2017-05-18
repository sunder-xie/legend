package com.tqmall.legend.biz.order;

import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.pub.order.OrderEvaluateVo;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO 梳理工单 对外服务接口
 * <p/>
 * Created by lixiao on 14-10-28.
 */
public interface OrderInfoService {

    public List<OrderInfo> select(Map<String, Object> searchMap);

    public OrderInfo selectById(Long id);

    OrderInfo selectById(Long id, Long shopId);

    public Integer selectCount(Map<String, Object> searchMap);

    public Page<OrderInfo> getOrderInfoPage(Pageable pageable, Map<String, Object> searchParams);

    public List<OrderInfo> selectByContactMobileAndLicense(Map<String, Object> searchParams);

    /**
     * create by jason 2015-09-18
     * 获取dayNumMin到dayNumMax的工单
     *
     * @param mobileList 工单ID
     * @param dayNumMin  天数
     * @param dayNumMax  天数
     */
    public List<OrderEvaluateVo> selectByMobileAndDayNum(List<String> mobileList, Integer dayNumMin, Integer dayNumMax);

    /**
     * 从 billcenter 获取工单收款金额
     *
     * @param shopId
     * @param orderList
     */
    public List<OrderInfo> getOrderDebitAmountByBillcenter(Long shopId, List<OrderInfo> orderList);

    /**
     * 根据ids批量查
     *
     * @param shopId
     * @param idList
     * @return
     */
    List<OrderInfo> selectByIdsAndShopId(Long shopId, List<Long> idList);

    /**
     * 查询未回访工单
     *
     * @param shopId
     * @param confirmTimeGt
     * @param confirmTimeLt
     * @return
     */
    List<OrderInfo> findUnVisitList(Long shopId, Date confirmTimeGt, Date confirmTimeLt);

    /**
     * 更新工单
     *
     * @param orderInfo
     * @return
     */
    int updateById(OrderInfo orderInfo);

    /**


    /**
     * 更新工单回访状态为已回访
     *
     * @param shopId
     * @param orderId
     * @return
     */
    int updateOrderVisit(Long shopId, Long orderId);

    /**
     * key = orderId value = orderInfo
     * @param orderIds
     * @return
     */
    Map<Long, OrderInfo> selectMapByIds(Collection<Long> orderIds);

    List<Long> selectReceiverByShopId(Long shopId);
}
