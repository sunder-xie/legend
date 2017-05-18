package com.tqmall.common;


import com.tqmall.common.util.DateUtil;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * Created by yuchengdu on 16/7/13.
 */
public class ShotDateJsonDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText();
        return DateUtil.convertStringToDate(str);
    }
}
