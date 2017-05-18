package com.tqmall.legend.facade.sell;

import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.entity.sell.SellOrderPay;
import com.tqmall.legend.entity.sell.SellOrderPayLog;

/**
 * Created by xiangdong.qu on 17/2/23 10:18.
 */
public interface SellOrderPayTslService {
    /**
     * 更显订单的支付信息
     *
     * @param sellOrder       购买订单
     * @param sellOrderPay    支付记录
     * @param sellOrderPayLog 支付记录状态变更
     */
    public void updateSellOrderPayStatus(SellOrder sellOrder, SellOrderPay sellOrderPay, SellOrderPayLog sellOrderPayLog);
}
