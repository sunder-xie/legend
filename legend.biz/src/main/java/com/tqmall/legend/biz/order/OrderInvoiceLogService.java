package com.tqmall.legend.biz.order;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInvoiceLog;

/**
 * Created by lixiao on 15-1-15.
 */
public interface OrderInvoiceLogService {

    Result saveInvoice(OrderInvoiceLog orderInvoiceLog);

    OrderInvoiceLog getInvoice(long orderId,long shopId);
}
