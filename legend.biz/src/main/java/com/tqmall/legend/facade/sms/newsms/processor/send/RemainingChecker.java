package com.tqmall.legend.facade.sms.newsms.processor.send;

/**
 * Created by majian on 16/11/24.
 */
public interface RemainingChecker {

    boolean isRemainingEnough(Long shopId, String UUID);
}
