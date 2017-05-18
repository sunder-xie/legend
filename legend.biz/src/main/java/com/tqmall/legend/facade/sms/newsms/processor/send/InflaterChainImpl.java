package com.tqmall.legend.facade.sms.newsms.processor.send;

import com.tqmall.legend.facade.sms.newsms.util.TemplateParser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by majian on 16/11/25.
 */
@Service
public class InflaterChainImpl implements InflaterChain{

    @Override
    public String inflate(String template, Map<String, String> data) {
        String[] placeHolderNames = new TemplateParser(template, "${", "}").getPlaceHolderNames();
        if (placeHolderNames == null) {
            return template;
        }
        for (String placeHolderName : placeHolderNames) {
            String regex = "\\$\\{" +placeHolderName + "}";
            String replacement = data.get(placeHolderName);
            if (replacement != null) {
                template = template.replaceAll(regex, replacement);
            }
        }

        return template;
    }

}
