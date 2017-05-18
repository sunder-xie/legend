package com.tqmall.legend.facade.sms.newsms.processor.send;

import java.util.Map;

/**
 * Created by majian on 16/11/30.
 */
public interface InflaterChain {
    String inflate(String template, Map<String, String> data);
}
