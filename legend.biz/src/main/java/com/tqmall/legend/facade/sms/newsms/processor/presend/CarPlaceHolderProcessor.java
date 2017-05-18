package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import org.springframework.stereotype.Service;

/**
 * Created by majian on 16/11/25.
 * 与车辆相关的处理器
 * license
 */
@Service
public class CarPlaceHolderProcessor implements SmsProcessor {

    @Override
    public void process(PreSendContext context) {
        for (TemplateData templateData : context.getPagedTemplateDataList()) {
            templateData.put("车牌", templateData.getLicenses());
        }
    }
}
