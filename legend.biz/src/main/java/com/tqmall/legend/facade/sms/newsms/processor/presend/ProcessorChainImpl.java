package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.google.common.collect.Lists;
import com.tqmall.legend.facade.sms.newsms.util.TemplateParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/11/24.
 */
@Service
@Log4j
public class ProcessorChainImpl implements ProcessorChain {
    @Resource(name = "smsCustomerProcessorMap")
    private Map<String,SmsProcessor> processorMap;
    @Autowired
    private PersistProcessor lastProcessor;
    @Autowired
    private SmsNumberProcessor secondLastProcessor;

    private List<SmsProcessor> getRelatedProcessors(String template) {
        List<SmsProcessor> processors = Lists.newArrayList();
        addNeededProcessors(template, processors);
        processors.add(secondLastProcessor);
        processors.add(lastProcessor);
        return processors;
    }

    private void addNeededProcessors(String template, List<SmsProcessor> processors) {
        TemplateParser templateParser = new TemplateParser(template, "${", "}");
        String[] placeHolderNames = templateParser.getPlaceHolderNames();
        if (placeHolderNames == null) {
            log.warn("no placeholder processor used");
            return;
        }
        for (String placeHolderName : placeHolderNames) {
            SmsProcessor replaceProcessor = processorMap.get(placeHolderName);
            if (replaceProcessor != null) {
                processors.add(replaceProcessor);
            }
        }
    }

    @Override
    public void doProcess(PreSendContext context) {
        List<SmsProcessor> processors = getRelatedProcessors(context.getTemplate());
        for (SmsProcessor processor : processors) {
            processor.process(context);
        }
    }
}
