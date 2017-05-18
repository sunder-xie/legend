package com.tqmall.legend.web.marketing.ng;

import org.springframework.web.util.HtmlUtils;

import java.util.Map;

public class HtmlParamDecoder {

    public static void decode(Map<String, Object> params, String s) {
        if (params.get(s) != null) {
            String result = HtmlUtils.htmlUnescape(params.get(s).toString());
            params.put(s, result);
        }
    }
}