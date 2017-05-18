package com.tqmall.legend.facade.sms;

import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;

import java.util.Map;

/**
 * Created by majian on 16/12/21.
 */
public interface SmsSendFacade {
    //<license, smsLogId>
    Map<String, Long> sendForGather(SendParam sendParam);

    Integer sendForNote(SendParam sendParam, int noteType);

    /**
     * 发送手机验证码
     * @param mobile 手机号
     * @param sendCode 验证码
     * @param smsBase 短信模板信息
     * @param validTime 多长时间才能再发送
     * @return
     */
    Boolean sendMobileCore(String mobile, String sendCode, SmsBase smsBase, int validTime);
}
