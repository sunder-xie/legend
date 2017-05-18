package com.tqmall.legend.biz.sms;

import com.tqmall.legend.biz.sms.vo.SmsBase;

/**
 * Created by sven on 16/8/19.
 */

public interface SmsService {

    /**
     * 发送短信
     *
     * @return
     */
    boolean sendMsg(SmsBase smsBase, String msg);

    /**
     * 获取验证码
     *
     * @return
     */
    String generateCode();
}
