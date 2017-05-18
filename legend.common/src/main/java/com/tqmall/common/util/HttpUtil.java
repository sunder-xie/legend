package com.tqmall.common.util;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * ok Created by luoyan on 14-8-12.
 */
public class HttpUtil {
    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final Joiner.MapJoiner KV_JOINER = Joiner.on("&").withKeyValueSeparator("=");
    private static final String defaultContentType = "application/x-www-form-urlencoded";

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url         发送请求的URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param connTimeOut 连接超时时间
     * @param readTimeOut 获取结果超时时间
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, int connTimeOut, int readTimeOut, Charset charset) {
        String resultStr = "" ;
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(connTimeOut);
            connection.setReadTimeout(readTimeOut);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            String line;
            StringBuilder result = new StringBuilder(4096);
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            resultStr = result.toString();
            logTrace(urlNameString, null, resultStr);
        } catch (Exception e) {
            logger.error("get data error", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return resultStr;
    }

    public static String sendGet(String url, String param, int connTimeOut, int readTimeOut) {
        return sendGet(url, param, connTimeOut, readTimeOut, Charset.forName("utf-8"));
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, 1000, 3000);
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        return sendPost(url, param, 2000, 60000);
    }

    public static String sendPost(String url, String param, boolean isAjax) {
        return sendPost(url, param, 2000, 60000, isAjax);
    }


    public static String sendPost(String url, String param, int connTimeOut, int readTimeOut) {
        return sendPost(url, param, connTimeOut, readTimeOut, false);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param connTimeOut 连接超时时间
     * @param readTimeOut 获取结果超时时间
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, int connTimeOut, int readTimeOut, boolean isAjax) {
        PrintWriter out = null;
        BufferedReader in = null;
        String resultStr = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            if (isAjax) {
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            }

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(connTimeOut);
            conn.setReadTimeout(readTimeOut);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder(4096);
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            resultStr = result.toString();
            logTrace(url, param, resultStr);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常{}", e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return resultStr;
    }

    private static void logTrace(String url, String param, String responseText) {
        if (logger.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append(IOUtils.LINE_SEPARATOR).append("Request URL:").append(url).append(',');
            if (StringUtils.isNotBlank(param)) {
                sb.append("Param:").append(param == null ? "" : param);
            }
            sb.append(IOUtils.LINE_SEPARATOR).append("Response Text:")
                    .append(responseText);
            logger.debug(sb.toString());
        }
    }

    /**
     * GET 请求
     *
     * @param url    请求URL
     * @param params 请求参数
     * @return 相应数据String
     */
    public static String get(String url, Map<String, Object> params) {
        if (!url.contains("?")) {
            url = url + "?";
        }
        if (!url.endsWith("?")) {
            url = url + "&";
        }
        if (MapUtils.isNotEmpty(params)) {
            url = url + KV_JOINER.join(params);
        }
        logger.info("url:{}", url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(2000).build();
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(request);
            return getContent(response);
        } catch (IOException e) {
            logger.error("http get error:{}", url);
            return null;
        }

    }

    /**
     * 对应GET方法的泛型请求
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String url, Map<String, Object> params, Class<T> clazz) {
        String response = get(url, params);
        return JSONUtil.readValue(response, clazz);
    }

    /**
     * POST 请求
     *
     * @param url         请求URL
     * @param params      请求参数
     * @param charset     编码格式 eg：UTF-8
     * @param contentType eg：application/x-www-form-urlencoded
     * @return
     */
    public static String post(String url, Map<String, String> params, Charset charset, String contentType) {
        HttpPost request = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, charset == null ? Charsets.UTF_8
                : charset);
        urlEncodedFormEntity.setContentType(StringUtils.isEmpty(contentType) ? defaultContentType : contentType);
        request.setEntity(urlEncodedFormEntity);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(2000).build();
            request.setConfig(requestConfig);
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(request);
            return getContent(response);
        } catch (IOException e) {
            logger.error("post error url:{} params:{}", url, params);
            return null;
        }
    }

    /**
     * 对应POST的泛型请求
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T post(String url, Map<String, String> params,Class<T> clazz){
        String response =post(url,params,Charsets.UTF_8,defaultContentType);
        return JSONUtil.readValue(response, clazz);
    }

    /**
     * 从response中获取相应数据
     * @param response
     * @return
     */
    private static String getContent(HttpResponse response) {
        HttpEntity entity = null;
        String content = null;
        try {
            StatusLine status = response.getStatusLine();
            entity = response.getEntity();
            if (status != null && status.getStatusCode() == HttpStatus.SC_OK) {
                content = EntityUtils.toString(entity, Charsets.UTF_8);
            }
        } catch (IOException e) {
            logger.error("getContent error：{}",e);
        } finally {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.error("consume error:{}", e);
            }
        }
        return content;
    }
}
