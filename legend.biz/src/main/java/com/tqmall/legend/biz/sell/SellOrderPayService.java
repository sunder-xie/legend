package com.tqmall.legend.biz.sell;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.sell.SellOrderPay;

/**
 * Created by xiangdong.qu on 17/2/23 09:28.
 */
public interface SellOrderPayService extends BaseService {

    /**
     * 创建支付记录
     *
     * @param sellOrderPay 创建支付记录
     *
     * @return
     */
    public SellOrderPay createSellOrderPay(SellOrderPay sellOrderPay);

    /**
     * 根据支付商品订单号 获取支付记录
     *
     * @param payOrderSn 支付商品单号
     *
     * @return
     */
    public SellOrderPay getSellOrderPayBySn(String payOrderSn);

    /**
     * 根据id更新sellOrderPay
     *
     * @param sellOrderPay
     *
     * @return
     */
    public int updateSellOrderPayById(SellOrderPay sellOrderPay);

    /**
     * 根据售卖订单id获取支付单信息
     *
     * @param sellOrderId 售卖订单Id
     *
     * @return
     */
    public SellOrderPay getSellOrderPayBySellOrderId(Long sellOrderId);

}
