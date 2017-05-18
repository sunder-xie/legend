package com.tqmall.legend.web.utils.gson;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * BigDecimal Type Adapter for gson
 * <p/>
 * Created by dongc on 15/8/6.
 */
public class BigDecimalTypeAdapter implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
            throws JsonParseException {
        BigDecimal src;
        try {
            src = json.getAsBigDecimal();
        } catch (NumberFormatException e) {
            src = BigDecimal.ZERO;
        }
        return src;
    }

    @Override
    public JsonElement serialize(BigDecimal src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
