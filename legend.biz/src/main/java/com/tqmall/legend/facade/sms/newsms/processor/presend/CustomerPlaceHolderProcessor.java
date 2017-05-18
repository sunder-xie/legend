package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import org.springframework.stereotype.Service;

/**
 * Created by majian on 16/11/28.
 * 与customer相关的占位符替换处理器
 * customerName
 * mobile
 */
@Service
public class CustomerPlaceHolderProcessor implements SmsProcessor {

    @Override
    public void process(PreSendContext context) {

        for (TemplateData templateData : context.getPagedTemplateDataList()) {
            templateData.put("客户姓名", templateData.getCustomerName());
        }
    }

}
