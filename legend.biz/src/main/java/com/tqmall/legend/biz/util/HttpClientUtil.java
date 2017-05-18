package com.tqmall.legend.biz.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/12/15.
 *
 * httpClient
 */
@Slf4j
public class HttpClientUtil {

    public static String doPost(String requestURL, Map<String, Object> map) {
        return executeRequest(requestURL, map);
    }

    public static String sendFile(String requestURL, Map<String, Object> params, Map<String, File> fileParams) {
        return executeRequest(requestURL, params, fileParams);
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static String executeRequest(String url, Map<String, Object> map) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列
        List<BasicNameValuePair> formparams = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("ClientProtocolException:{}", e);
        } catch (UnsupportedEncodingException e1) {
            log.error("UnsupportedEncodingException:{}", e1);
        } catch (IOException e) {
            log.error("IOException:{}", e);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error("IOException:{}", e);
            }
        }
        return null;
    }

    /**
     * 有图片的时候请求
     */
    public static String executeRequest(String requestURL, Map<String, Object> params, Map<String, File> fileParams) {

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(requestURL);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // 上传文件
            if (fileParams != null && fileParams.size() > 0) {
                for (String name : fileParams.keySet()) {
                    multipartEntityBuilder.addPart(name, new FileBody(fileParams.get(name)));
                }

            }
            // 普通参数
            ContentType contentType = ContentType.create("application/x-www-form-urlencoded", Charset.forName("UTF-8"));
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                multipartEntityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue().toString(), contentType));
            }

            httppost.setEntity(multipartEntityBuilder.build());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
