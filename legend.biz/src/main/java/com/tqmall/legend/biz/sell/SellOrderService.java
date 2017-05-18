package com.tqmall.legend.biz.sell;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.sell.SellOrder;

import java.util.List;

/**
 * Created by xiangdong.qu on 17/2/23 09:26.
 */
public interface SellOrderService extends BaseService {

    /**
     * 根据订单Id获取订单信息
     *
     * @param sellOrderId
     *
     * @return
     */
    public SellOrder selectById(Long sellOrderId);

    /**
     * 保存购买订单
     *
     * @param sellOrder
     *
     * @return
     */
    public int saveSellOrder(SellOrder sellOrder);

    /**
     * 根据购买订单号获取订单详情
     *
     * @param sellOrderSn
     *
     * @return
     */
    public SellOrder getSellOrderBySn(String sellOrderSn);

    /**
     * 根据购买订单号和电话 获取订单详情
     *
     * @param mobile
     * @param sellOrderSn
     *
     * @return
     */
    public SellOrder getSellOrderByMobileAndSn(String mobile, String sellOrderSn);

    /**
     * 根据电话号码获取 购买订单列表
     *
     * @param mobile
     *
     * @return
     */
    public List<SellOrder> getSellOrderListByMobile(String mobile);

    /**
     * 根据id更新sellOrder
     *
     * @param sellOrder
     *
     * @return
     */
    public int updateSellOrderById(SellOrder sellOrder);

}
