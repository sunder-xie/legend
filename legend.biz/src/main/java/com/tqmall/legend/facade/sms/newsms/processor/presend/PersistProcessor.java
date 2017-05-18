package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.util.GsonUtil;
import com.tqmall.legend.facade.sms.newsms.util.RedisKeyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by majian on 16/11/28.
 * 持久化的处理器
 */
@Service
@Slf4j
public class PersistProcessor implements SmsProcessor {

    @Override
    public void process(PreSendContext context) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String redisKey = context.getRedisKey();
            List<TemplateData> templateDataList = context.getPagedTemplateDataList();

            Gson defaultGson = GsonUtil.getDefaultGson();
            List<String> templateDataJsonList = Lists.newArrayList();
            for (TemplateData templateData : templateDataList) {
                String json = defaultGson.toJson(templateData);
                templateDataJsonList.add(json);
            }
            String contentKey = RedisKeyParser.getContentKey(redisKey);
            if (!CollectionUtils.isEmpty(templateDataJsonList)) {
                jedis.lpush(contentKey, templateDataJsonList.toArray(new String[0]));
            }
        } catch (Exception e) {
            log.error("短信发送--持久化第{}页模板填充数据失败", context.getPageIndex(), e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }


}
