package com.tqmall.legend.facade.sms.newsms.processor.send;


import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by majian on 16/11/25.
 */
@Service
public class TemplateData2SendBOConverter {
    @Autowired
    private InflaterChain inflater;

    public SendBO convert(TemplateData source, String template) {
        if (source == null) {
            return null;
        }
        SendBO target = new SendBO();
        target.setMobile(source.getMobile());
        target.setCustomerName(source.getCustomerName());
        target.setLicenses(source.getLicenses());
        target.setContent(inflater.inflate(template, source.getPlaceHolderMap()));
        target.setNumber(source.getNeedNumber());
        return target;
    }
}
