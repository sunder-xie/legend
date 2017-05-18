package com.tqmall.legend.facade.sms.newsms.processor.send;

import com.tqmall.legend.facade.sms.newsms.Callback;

/**
 * Created by majian on 16/11/24.
 */
public interface Sender {
    Integer doSend(Long shopId, Long userId, String operator, String UUID, Callback callback);
}
