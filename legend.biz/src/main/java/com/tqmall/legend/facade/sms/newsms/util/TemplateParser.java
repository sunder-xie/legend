package com.tqmall.legend.facade.sms.newsms.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by majian on 16/11/29.
 */
public class TemplateParser {
    private String template;
    private String open;//起始符 如 "${"
    private String close;//结束符 如 "}"

    public TemplateParser(String template, String open, String close) {
        this.template = template;
        this.open = open;
        this.close = close;
    }

    public String[] getPlaceHolderNames() {
        return StringUtils.substringsBetween(template, open, close);
    }

    public int getLengthExcludeHolders() {
        int totalLength = template.length();
        String[] placeHolderNames = getPlaceHolderNames();
        if (placeHolderNames == null) {
            return totalLength;
        }
        for (String placeHolderName : placeHolderNames) {
            totalLength -= placeHolderName.length() + open.length() + close.length();
        }
        return totalLength;
    }
}
