package com.tqmall.common.security;

import com.tqmall.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 15/12/14.
 *
 * 签名加密
 *
 */
@Slf4j
public class SignatureUtils {

    public static final String appKey = "f095b85215d2c46bc64cbfd1b5af539a";
    public static final String appSecret = "fe46feaf754c96ce7c99d3e2c13010d0";

    /**
     * md5加密方式：将appsecret 拼接到参数字符串头、尾进行md5加密后，再转化成大写，
     * 格式是：byte2hex(md5(secretkey1value1key2value2...secret)) 
     * 最终结果是32位的大写字符串
     *
     */
    public static Map<String, Object> paramsSignature(Map<String, Object> paramMap) {
        paramMap.put("appkey", appKey);
        paramMap.put("timestamp", DateUtil.convertDateToYMDHMS(new Date()));
        paramMap.put("v", "1.0");
        //字母排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray, String.CASE_INSENSITIVE_ORDER);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appSecret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }
        stringBuilder.append(appSecret);
        String codes = stringBuilder.toString();
        paramMap.put("sign", DigestUtils.md5Hex(codes).toUpperCase());

        return paramMap;
    }

}
