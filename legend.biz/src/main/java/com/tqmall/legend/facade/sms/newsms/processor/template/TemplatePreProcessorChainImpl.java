package com.tqmall.legend.facade.sms.newsms.processor.template;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.sms.newsms.util.TemplateParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/11/30.
 */
@Service
@Slf4j
public class TemplatePreProcessorChainImpl implements TemplatePreProcessorChain{
    @Resource(name = "smsPreProcessorMap")
    private Map<String, TemplatePreProcessor> preProcessorMap;


    @Override
    public void doProcess(TemplatePreProcessContext context) {
        String template = context.getTemplate();
        List<TemplatePreProcessor> preProcessors = getRelatedProcessors(template);

        for (TemplatePreProcessor preProcessor : preProcessors) {
            preProcessor.process(context);
        }
    }

    private List<TemplatePreProcessor> getRelatedProcessors(String template) {
        List<TemplatePreProcessor> preProcessors = Lists.newArrayList();
        String[] placeHolderNames = new TemplateParser(template, "${", "}").getPlaceHolderNames();
        if (placeHolderNames == null) {
            log.warn("no placeholder processor used");
            return preProcessors;
        }
        for (String placeHolderName : placeHolderNames) {
            TemplatePreProcessor replaceProcessor = preProcessorMap.get(placeHolderName);
            if (replaceProcessor != null) {
                preProcessors.add(replaceProcessor);
            }
        }
        return preProcessors;
    }
}
