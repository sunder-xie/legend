package com.tqmall.legend.biz.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/12/15.
 *
 * GET方法 参数拼接
 */
@Slf4j
public class ParamsGenerator {

    /**
     * http get请求,拼接参数key=value&key1=value1
     */
    public static String warpParams(Map<String, Object> paramMap) {
        if (CollectionUtils.isEmpty(paramMap)) {
            return "";
        }
        try {
            List<String> paramList = new ArrayList<>(paramMap.size());
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                paramList.add(String.format("%s=%s", entry.getKey(), UriUtils.encodeQueryParam(String.valueOf(entry
                                .getValue()), "utf-8")));
            }
            String params = StringUtils.join(paramList.toArray(), "&");
            log.info("参数拼接成功:{}", params);
            return params;
        } catch (UnsupportedEncodingException e) {
            log.error("unsupported encoding exception:{}", e);
            return "";
        }
    }

}
