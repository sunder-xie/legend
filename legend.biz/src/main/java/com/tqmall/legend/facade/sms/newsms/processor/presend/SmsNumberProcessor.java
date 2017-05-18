package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.util.SmsNumberCalculator;
import com.tqmall.legend.facade.sms.newsms.util.TemplateParser;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by majian on 16/11/29.
 */
@Service
public class SmsNumberProcessor implements SmsProcessor {
    @Override
    public void process(PreSendContext context) {

        List<TemplateData> templateDataList = context.getPagedTemplateDataList();
        TemplateParser templateParser = new TemplateParser(context.getTemplate(), "${", "}");
        int totalNumber = 0;
        for (TemplateData templateData : templateDataList) {
            int length = templateData.getContentLength() + templateParser.getLengthExcludeHolders();
            int number = SmsNumberCalculator.getNumber(length);
            templateData.setNeedNumber(number);
            totalNumber += number;
        }
        context.addNeedNumber(totalNumber);
    }
}
