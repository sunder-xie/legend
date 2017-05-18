package com.tqmall.yunxiu.web.pub;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;


@Controller
@RequestMapping("pub/wechat")
public class WeChatController extends BaseController {

    Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Value("${legend.wechat.domain}")
    private String weChatUrl;

    /**
     * 微信分享接口
     * @param url 需要调用的URL
     */
    @RequestMapping("share")
    @ResponseBody
    public Object weChat(String url){
        logger.info("调用微信分享接口开始的URL:{}", url);
        try {
            url = URLDecoder.decode(url,"UTF-8").toString();
        } catch (UnsupportedEncodingException e) {
            logger.error("URLDecoder解析出错URL:{}", url);
        }
        url = url.replaceAll("&","%26");
        logger.info("调用微信分享接口结算的URL:{}", url);

        String result = HttpUtil.sendGet(weChatUrl + "/loam/weChat/share/jsSdk", "url=" + url);
        if (StringUtils.isBlank(result)) {
            logger.error("调用微信接口异常,返回数据为空.");
        } else {
            try {
                Map<String, Object> resultMap = JSONUtil.getMapper().readValue(result, Map.class);
                return resultMap;
            } catch (Exception e) {
                logger.error("JSONUtil转换出错,原因为:{}", e);
            }
        }
        return Result.wrapErrorResult("9999","调用微信分享接口失败");
    }

}
