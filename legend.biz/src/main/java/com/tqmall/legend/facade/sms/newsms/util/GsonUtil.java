package com.tqmall.legend.facade.sms.newsms.util;

import com.google.gson.Gson;

/**
 * Created by majian on 16/11/25.
 */
public class GsonUtil {
    private static final Gson defaultGson = new Gson();
    public static Gson getDefaultGson() {
        return defaultGson;
    }
}
