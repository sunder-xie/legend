package com.tqmall.legend.biz.jms.ddlwechat;

import com.google.gson.Gson;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.jms.core.DefaultEventController;
import com.tqmall.legend.biz.jms.core.EventControlConfig;
import com.tqmall.legend.biz.jms.core.SendRefuseException;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jason on 15/8/17.
 */
public class MessagePlatformDdlWechat {

    Logger logger = LoggerFactory.getLogger(MessagePlatformDdlWechat.class);
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String onOff;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String defaultExchange;
    @Getter
    @Setter
    private String routingKey;

    private DefaultEventController eventController;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private ShopService shopService;

    public void init() throws IOException {
        EventControlConfig config = new EventControlConfig(host, 5672, username, password, "tqmall", 3000, 0, 0, null);
        eventController = DefaultEventController.getInstance(config);
    }


    private boolean sendMessage(String routingKey, String exchange, Map<String, String> msgInfo) {
        String msg = null;
        try {
            msg = new Gson().toJson(msgInfo);
            if (null == eventController) {
                return false;
            }
            eventController.getEopEventTemplate().send(exchange, routingKey, msg);
        } catch (SendRefuseException e) {
            logger.error("send message error.info=" + msg, e);
            return false;
        }
        return true;
    }

    public void pushMsgToDdlWechat(Map<String, String> message, Long shopId) {
        String wechatConf = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.SHOPMSGCONF.getCode(), "wechat_conf");
        String smsConf = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.SHOPMSGCONF.getCode(), "sms_conf");

        message.put("wechatPush", wechatConf);//微信推送状态
        message.put("smsPush", smsConf);//短信推送状态
        Long ucShopId = shopService.getUserGlobalId(shopId);
        if (null != ucShopId) {
            message.put("shopId", ucShopId.toString());//短信推送状态
            sendMessage(routingKey, defaultExchange, message);
            logger.info("push信息到微信端成功,exchange:{},routingKey:{},msg:{}",defaultExchange,routingKey, LogUtils.objectToString(message));
        }else {
            logger.info("push信息到微信端失败:id为:{}门店的userGlobalId为空", shopId);
        }
    }
}
