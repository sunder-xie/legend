package com.tqmall.legend.facade.sms.newsms;

import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import com.tqmall.legend.facade.sms.newsms.param.PreSendParam;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;

/**
 * Created by majian on 16/11/24.
 */
public interface SmsCenter {

    String templatePreProcess(Long shopId, String template);

    MarketingSmsTempBO preSend(PreSendParam preSendParam);

    Integer send(SendParam sendParam);

    Integer send(SendParam sendParam, Callback callback);
}
