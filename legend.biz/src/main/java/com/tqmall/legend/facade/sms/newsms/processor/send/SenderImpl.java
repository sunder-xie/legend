package com.tqmall.legend.facade.sms.newsms.processor.send;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.biz.marketing.MarketingSmsLogServie;
import com.tqmall.legend.biz.marketing.MarketingSmsService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.entity.marketing.MarketingSms;
import com.tqmall.legend.facade.sms.newsms.Callback;
import com.tqmall.legend.facade.sms.newsms.SendPositionEnum;
import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.util.GsonUtil;
import com.tqmall.legend.facade.sms.newsms.util.RedisKeyParser;
import com.tqmall.legend.log.MarketSmsLog;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by majian on 16/11/25.
 */
@Service
@Slf4j
public class SenderImpl implements Sender {
    @Autowired
    private TemplateData2SendBOConverter templateData2SendBOConverter;
    @Autowired
    private SmsService smsService;
    @Autowired
    private MarketingSmsService marketingSmsService;
    @Autowired
    private MarketingShopRelService marketingShopRelService;
    @Autowired
    private MarketingSmsLogServie marketingSmsLogServie;

    @Override
    public Integer doSend(Long shopId, Long userId, String operator, String UUID, Callback callback) {
        Jedis jedis = null;
        int count = 0;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String templateKey = RedisKeyParser.getTemplateKey(UUID);
            String template = jedis.get(templateKey);
            String positionKey = RedisKeyParser.getPositionKey(UUID);
            String position = jedis.get(positionKey);
            String contentKey = RedisKeyParser.getContentKey(UUID);
            int pos = Integer.parseInt(position);
            Long smsLogId = marketingSmsLogServie.logConsume(shopId, userId, operator, 0, pos, template);

            int index = 0;
            count = 0;
            while (true) {
                int LIMIT = 1000;
                int start = index * LIMIT;
                int end = (index + 1) * 1000 - 1;
                List<String> templateDataStrList = jedis.lrange(contentKey, start, end);
                if (CollectionUtils.isEmpty(templateDataStrList)) {
                    jedis.del(contentKey);
                    break;
                }
                List<SendBO> sendBOList = getSendBOs(template, templateDataStrList);
                int pageCount = sendPage(sendBOList, callback);
                count += pageCount;
                recordPageFlow(sendBOList, operator, shopId, userId, smsLogId);
                index++;
            }

            if (count > 0) {
                marketingShopRelService.updateRemain(shopId, count);
                marketingSmsLogServie.updateSmsNum(smsLogId, count, shopId);

                // 记录log
                log.info(MarketSmsLog.sendSmsLog(shopId, count));

                callback.onFinish(smsLogId);
            } else {
                marketingSmsLogServie.deleteById(shopId, smsLogId);
            }
            jedis.del(templateKey);
            jedis.del(positionKey);
            jedis.del(contentKey);
            jedis.del(RedisKeyParser.getNumberKey(UUID));
        } catch (Exception e) {
            throw e;
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return count;
    }

    private void recordPageFlow(List<SendBO> sendBOList, String operator, Long shopId, Long userId, Long smsLogId) {
        List<MarketingSms> smsList = Lists.newArrayList();
        for (SendBO sendBO : sendBOList) {
            MarketingSms marketingSms = generateSendFlow(sendBO, operator, shopId, userId, smsLogId);
            smsList.add(marketingSms);
        }
        marketingSmsService.batchInsert(smsList);

    }
    private MarketingSms generateSendFlow(SendBO sendBO, String operator, Long shopId, Long userId, Long smsLogId) {
        MarketingSms sms = new MarketingSms();
        sms.setShopId(shopId);
        sms.setCreator(userId);
        sms.setModifier(userId);
        sms.setMobiles(sendBO.getMobile());
        sms.setLicenses(sendBO.getLicenses());
        sms.setCustomerName(sendBO.getCustomerName());
        sms.setReceiverNum(1l);
        sms.setContent(sendBO.getContent());
        sms.setSmsNum(Long.valueOf(sendBO.getNumber()));
        sms.setOperator(operator);
        sms.setSendTime(new Date());
        sms.setSmsLogId(smsLogId);
        Boolean sendSuccess = sendBO.isSuccessful();
        if (sendSuccess) {
            sms.setStatus(1);
        }else{
            sms.setStatus(2);
            log.error("发送短信失败:手机号为{}",sms.getMobiles());
        }
        return sms;
    }

    private int sendPage(List<SendBO> sendBOList, Callback callback) {
        int count = 0;
        for (SendBO sendBO : sendBOList) {
            Integer singleCount = sendMessage(sendBO);
            Boolean success = false;
            if (singleCount > 0) {
                success = true;
                callback.onEachSuccess(sendBO);
            }
            sendBO.setSuccessful(success);
            count += singleCount;
        }
        return count;
    }

    private int sendMessage(SendBO sendBO) {
        int sendNumber = 0;
        //设置发送短信配置
        SmsBase smsBase = new SmsBase();
        smsBase.setAction(Constants.LEGEND_MARKETING_SMS_TPL);
        smsBase.setMobile(sendBO.getMobile());
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("content", sendBO.getContent());
        smsBase.setData(smsMap);
        Boolean sendCode =smsService.sendMsg(smsBase,"发送单条信息");

        if (sendCode) {
            sendNumber = sendBO.getNumber();
        }
        return sendNumber;
    }


    private List<SendBO> getSendBOs(String template, List<String> templateDataStrList) {Gson gson = GsonUtil.getDefaultGson();
        List<TemplateData> templateDataList = Lists.newArrayList();
        for (String templateDataStr : templateDataStrList) {
            TemplateData templateData = gson.fromJson(templateDataStr, TemplateData.class);
            templateDataList.add(templateData);
        }
        List<SendBO> sendBOList = Lists.newArrayList();
        for (TemplateData templateData : templateDataList) {
            SendBO sendBO = templateData2SendBOConverter.convert(templateData, template);
            sendBOList.add(sendBO);
        }
        return sendBOList;
    }
}
