package com.tqmall.legend.facade.sms.newsms.processor.presend;

/**
 * Created by majian on 16/11/24.
 */
public interface ProcessorChain {
    void doProcess(PreSendContext context);
}
