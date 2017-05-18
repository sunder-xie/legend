package com.tqmall.legend.facade.sms.newsms.processor.template;

/**
 * Created by majian on 16/11/30.
 */
public interface TemplatePreProcessorChain {

    void doProcess(TemplatePreProcessContext context);
}
