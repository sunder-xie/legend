package com.tqmall.legend.common;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 为了处理TradeLog中Attributes字段的处理，把Map序列化为json存储到attributes字段中区
     *
     * @param attributes 需要序列化的Map对象
     * @return
     */
    public static String mapToJsonStr(Map<String, Object> attributes) {
        Map<String, Object> attr = attributes == null ? new HashMap<String, Object>() : attributes;

        try {
            return objectMapper.writeValueAsString(attr);
        } catch (IOException e) {
            logger.error("序列化attributes，从Map到Json报错", e);
        }
        return "";
    }

    /**
     * 为了处理TradeLog中Attributes字段的处理，把Json反向序列化为Map
     *
     * @param attributes 需要序列化的Map对象
     * @return
     */

    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonStrToMap(String attributes) {
        try {
            return objectMapper.readValue(attributes, HashMap.class);
        } catch (IOException e) {
            logger.error("反序列化序列化attributes，从Json到HashMap报错.attributes=" + attributes, e);
        }
        return new HashMap<>();
    }


    /**
     * 字符串是JSON格式
     *
     * @param object
     * @return true:json false:非json
     */
    public static boolean StringIsJson(@NotNull String object) {
        try {
            new JsonParser().parse(object);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }


}
