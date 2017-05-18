package com.tqmall.legend.biz.order.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderInvoiceLogService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.order.OrderInvoiceLogDao;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderInvoiceLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15-1-15.
 */
@Service
public class OrderInvoiceLogServiceImpl extends BaseServiceImpl implements OrderInvoiceLogService {
    public static final Logger logger = LoggerFactory.getLogger(OrderInvoiceLogServiceImpl.class);
    @Autowired
    private OrderInvoiceLogDao orderInvoiceLogDao;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Transactional
    public Result saveInvoice(OrderInvoiceLog orderInvoiceLog) {
        Result paramCheckResult = paramCheckSaveInvoice(orderInvoiceLog);
        if(!paramCheckResult.isSuccess()){
            return paramCheckResult;
        }

        orderInvoiceLogDao.insert(orderInvoiceLog);
        Long orderId = orderInvoiceLog.getOrderId();
        OrderInfo orderInfo = orderInfoDao.selectById(orderId);
        orderInfo.setInvoiceType(orderInvoiceLog.getInvoiceType());
        return Result.wrapSuccessfulResult(orderInfoDao.updateById(orderInfo));

    }

    @Override
    public OrderInvoiceLog getInvoice(long orderId,long shopId) {
        Map<String,Object> paramer = new HashMap<>();
        paramer.put("shopId",shopId);
        paramer.put("orderId",orderId);
        paramer.put("offset", 0);
        paramer.put("limit", 1);

        List<OrderInvoiceLog> resultList= orderInvoiceLogDao.select(paramer);

        if(!CollectionUtils.isEmpty(resultList)){
            return resultList.get(0);
        }
        return null;
    }

    private Result paramCheckSaveInvoice(OrderInvoiceLog orderInvoiceLog) {
        if (null == orderInvoiceLog) {
            logger.warn("开票单为空");
            return Result.wrapErrorResult("", "数据错误");
        }
        if (StringUtils.isBlank(orderInvoiceLog.getOrderSn())) {
            logger.warn("orderSn错误:" + orderInvoiceLog.getOrderSn());
            return Result.wrapErrorResult("", "工单编号错误");
        }
        if (null == orderInvoiceLog.getInvoiceType() || (orderInvoiceLog.getInvoiceType() != 1
            && orderInvoiceLog.getInvoiceType() != 2)) {
            logger.warn("InvoiceType错误:" + orderInvoiceLog.getInvoiceType());
            return Result.wrapErrorResult("", "开票类型错误");
        }
        if (StringUtils.isBlank(orderInvoiceLog.getInvoiceSn())) {
            logger.warn("InvoiceSn错误:" + orderInvoiceLog.getInvoiceSn());
            return Result.wrapErrorResult("", "税票号为空");
        }
        if (StringUtils.isBlank(orderInvoiceLog.getCompany())) {
            logger.warn("Company错误:" + orderInvoiceLog.getCompany());
            return Result.wrapErrorResult("", "开票单位为空");
        }
        if (null == orderInvoiceLog.getPrice()
            || orderInvoiceLog.getPrice().compareTo(BigDecimal.ZERO) != 1) {
            logger.warn("Price错误:" + orderInvoiceLog.getPrice());
            return Result.wrapErrorResult("", "开票金额小于或等于0，请重新输入");
        }
        if(orderInvoiceLog.getPrice().compareTo(new BigDecimal("9999999999.99")) == 1){
            logger.warn("Price错误:" + orderInvoiceLog.getPrice());
            return Result.wrapErrorResult("", "开票金额太大，请重新输入");
        }
        return Result.wrapSuccessfulResult(true);
    }
}
