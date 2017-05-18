package com.tqmall.legend.facade.sell.impl;

import com.tqmall.legend.biz.sell.SellOrderPayLogService;
import com.tqmall.legend.biz.sell.SellOrderPayService;
import com.tqmall.legend.biz.sell.SellOrderService;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.entity.sell.SellOrderPay;
import com.tqmall.legend.entity.sell.SellOrderPayLog;
import com.tqmall.legend.facade.sell.SellOrderPayTslService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xiangdong.qu on 17/2/23 10:18.
 */
@Service
public class SellOrderPayTslServiceImpl implements SellOrderPayTslService {

    @Autowired
    private SellOrderService sellOrderService;

    @Autowired
    private SellOrderPayService sellOrderPayService;

    @Autowired
    private SellOrderPayLogService sellOrderPayLogService;

    /**
     * 更新订单的支付信息
     *
     * @param sellOrder       购买订单
     * @param sellOrderPay    支付记录
     * @param sellOrderPayLog 支付记录状态变更
     */
    @Override
    @Transactional
    public void updateSellOrderPayStatus(SellOrder sellOrder, SellOrderPay sellOrderPay, SellOrderPayLog sellOrderPayLog) {
        sellOrderService.updateSellOrderById(sellOrder);
        sellOrderPayService.updateSellOrderPayById(sellOrderPay);
        sellOrderPayLogService.insertLog(sellOrderPayLog);
    }
}
