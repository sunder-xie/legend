package com.tqmall.legend.biz.pay.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.finance.model.param.pay.MsgPayParam;
import com.tqmall.finance.service.pay.aliPay.MessagePayService;
import com.tqmall.legend.biz.pay.OnlinePayService;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.SmsPayLogDao;
import com.tqmall.legend.entity.order.SerialTypeEnum;
import com.tqmall.legend.entity.shop.SmsPayLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghui on 3/16/16.
 */
@Service
@Slf4j
public class OnlinePayServiceImpl implements OnlinePayService {
    @Autowired
    private MessagePayService messagePayService;
    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private SmsPayLogDao smsPayLogDao;

    @Override
    public String payByZhifubao(UserInfo userInfo, BigDecimal payFee,Integer smsSum, String remark, String backUrl) {

        /**
         * 生成支付订单号
         */
        HashMap<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("userId", userInfo.getUserId());
        paramMap.put("shopId", userInfo.getShopId());
        paramMap.put("serialType", SerialTypeEnum.ONLINE_PAY.getCode());
        String orderSn = this.serialNumberService.getSerialNumber(paramMap);
        orderSn += "-" + userInfo.getShopId();

        SmsPayLog smsPayLog = new SmsPayLog();
        smsPayLog.setOrderSn(orderSn);
        smsPayLog.setRemark(remark);
        smsPayLog.setPayFee(payFee);
        smsPayLog.setPayWay(1);//支付宝支付
        smsPayLog.setSmsNum(smsSum);
        smsPayLog.setShopId(userInfo.getShopId());
        smsPayLog.setStatus(0);//未支付
        smsPayLog.setCreator(userInfo.getUserId());
        smsPayLog.setModifier(userInfo.getUserId());
        smsPayLog.setOperator(userInfo.getName());

        MsgPayParam payParam = new MsgPayParam();
        payParam.setPayFee(payFee);
        payParam.setOrderSn(orderSn);
        payParam.setSubject("tqmall-sms-charge");
        payParam.setShowUrl(backUrl);
        payParam.setSource("LEGEND");
        com.tqmall.core.common.entity.Result<Map<String, String>> result = messagePayService.getParameter(payParam);
        if (log.isInfoEnabled()) {
            log.info("从finance获取支付宝跳转信息.{}", LogUtils.funToString(payParam, result));
        }
        if (result != null && result.isSuccess()) {
            this.smsPayLogDao.insert(smsPayLog);
            return result.getData().get("param");
        } else {
            return "从账务中心获取支付信息失败";
        }
    }

    @Override
    public Result<String> payByZhifubaoCommon(String orderSn, BigDecimal payFee, String backUrl, String subject,Integer busType) {
        MsgPayParam payParam = new MsgPayParam();
        payParam.setPayFee(payFee);
        payParam.setOrderSn(orderSn);
        payParam.setSubject(subject);
        payParam.setShowUrl(backUrl);
        payParam.setSource("LEGEND");
        payParam.setBusType(busType);
        com.tqmall.core.common.entity.Result<Map<String, String>> result = messagePayService.getParameter(payParam);
        if (log.isInfoEnabled()) {
            log.info("从finance获取支付宝form提交信息.{}", LogUtils.funToString(payParam, result));
        }
        if (result != null && result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData().get("param"));
        } else {
            return Result.wrapErrorResult("","从finance获取支付信息失败");
        }
    }
}
