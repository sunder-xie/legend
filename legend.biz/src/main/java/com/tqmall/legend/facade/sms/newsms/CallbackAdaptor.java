package com.tqmall.legend.facade.sms.newsms;

import com.tqmall.legend.facade.sms.newsms.processor.send.SendBO;

/**
 * Created by majian on 16/12/21.
 */
public class CallbackAdaptor implements Callback {

    @Override
    public void onEachSuccess(SendBO sendBO) {

    }

    @Override
    public void onEachFail(SendBO sendBO) {

    }

    @Override
    public void onFinish(Long logId) {

    }
}
