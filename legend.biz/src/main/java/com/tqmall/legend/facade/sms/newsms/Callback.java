package com.tqmall.legend.facade.sms.newsms;

import com.tqmall.legend.facade.sms.newsms.processor.send.SendBO;

/**
 * Created by majian on 16/12/21.
 */
public interface Callback {
    /**
     * 单条短信发送成功时回调
     *
     * @param sendBO
     */
    void onEachSuccess(SendBO sendBO);

    /**
     * 单条短信发送失败时回调
     *
     * @param sendBO
     */
    void onEachFail(SendBO sendBO);

    /**
     * 用于获取本次群发的批次号
     *
     * @param logId
     */
    void onFinish(Long logId);
}
