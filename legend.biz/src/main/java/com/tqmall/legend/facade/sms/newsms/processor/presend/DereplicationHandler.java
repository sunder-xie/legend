package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;

import java.util.List;

/**
 * Created by majian on 16/11/30.
 */
public interface DereplicationHandler {
    List<TemplateData> dereplicate(List<Long> carIds, Long shopId);
}
