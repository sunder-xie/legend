package com.tqmall.legend.facade.sms.newsms;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import com.tqmall.legend.facade.sms.newsms.param.PreSendParam;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;
import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.processor.presend.DereplicationHandler;
import com.tqmall.legend.facade.sms.newsms.processor.presend.PreSendContext;
import com.tqmall.legend.facade.sms.newsms.processor.presend.ProcessorChain;
import com.tqmall.legend.facade.sms.newsms.processor.send.RemainingChecker;
import com.tqmall.legend.facade.sms.newsms.processor.send.Sender;
import com.tqmall.legend.facade.sms.newsms.processor.template.TemplatePreProcessContext;
import com.tqmall.legend.facade.sms.newsms.processor.template.TemplatePreProcessorChain;
import com.tqmall.legend.facade.sms.newsms.util.LockHelper;
import com.tqmall.legend.facade.sms.newsms.util.RedisKeyParser;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by majian on 16/11/24.
 */
@Service
@Slf4j
public class SmsCenterImpl implements SmsCenter {
    @Autowired
    private DereplicationHandler dereplicationHandler;
    @Autowired
    private ProcessorChain processorChain;
    @Autowired
    private TemplatePreProcessorChain templatePreProcessorChain;
    @Autowired
    private RemainingChecker remainingChecker;
    @Autowired
    private Sender sender;

    @Override
    public String templatePreProcess(Long shopId, String template) {
        TemplatePreProcessContext context = new TemplatePreProcessContext(shopId, template);
        templatePreProcessorChain.doProcess(context);
        return context.getTemplate();
    }

    @Override
    public MarketingSmsTempBO preSend(PreSendParam preSendParam) {
        long sTime = System.currentTimeMillis();
        PreSendContext context = new PreSendContext();
        Long shopId = preSendParam.getShopId();
        String template = templatePreProcess(shopId, preSendParam.getSmsTemplate());
        context.setShopId(shopId);
        context.setTemplate(template);
        context.setPosition(preSendParam.getPosition());
        String smsUUID = "LEGEND:SMS:" + UUID.randomUUID().toString() + "_" + shopId;
        context.setRedisKey(smsUUID);

        List<Long> carIds = preSendParam.getCarIds();
        List<TemplateData> templateDataList = dereplicationHandler.dereplicate(carIds, shopId);
        context.setTemplateDataList(templateDataList);
        int pageNum = context.getPageNumber();
        for (int i = 1; i <= pageNum; i++) {
            context.setPageIndex(i);
            processorChain.doProcess(context);
        }

        persist(context);

        MarketingSmsTempBO result = new MarketingSmsTempBO(context.getNeedNumber(), smsUUID, context.getMobileNumber());
        if (log.isInfoEnabled()) {
            log.info("process sms cost {} ms", System.currentTimeMillis() - sTime);
        }
        return result;
    }

    @Override
    public Integer send(SendParam sendParam) {
        return send(sendParam, new CallbackAdaptor());
    }

    private void persist(PreSendContext context) {
        String redisKey = context.getRedisKey();
        String numberKey = RedisKeyParser.getNumberKey(redisKey);
        String templateKey = RedisKeyParser.getTemplateKey(redisKey);
        String contentKey = RedisKeyParser.getContentKey(redisKey);
        String positionKey = RedisKeyParser.getPositionKey(redisKey);
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            jedis.set(numberKey, String.valueOf(context.getNeedNumber()));
            jedis.expire(numberKey,600);
            jedis.set(templateKey, context.getTemplate());
            jedis.expire(templateKey, 600);
            jedis.set(positionKey, context.getPosition().toString());
            jedis.expire(positionKey, 600);
            jedis.expire(contentKey, 600);
        } catch (Exception e) {
            log.error("短信发送--持久化短信数量和模板出错", e);
            throw e;
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    @Override
    public Integer send(SendParam sendParam, Callback callback) {
        String lockKey = Constants.SMS_SEND_LOCK_KEY + sendParam.getShopId();
        Integer usedCount = null;
        try {
            boolean isSuccess = LockHelper.getLock(lockKey);
            if (!isSuccess) {
                throw new BizException("获取短信锁失败");
            }
            //1.check balance
            boolean isEnough = remainingChecker.isRemainingEnough(sendParam.getShopId(), sendParam.getUUID());
            if (!isEnough) {
                throw new BizException("短信余额不足");
            }
            //2.send
            usedCount = sender.doSend(sendParam.getShopId(), sendParam.getUserId(), sendParam.getOperator(), sendParam.getUUID(),
                                      callback);
        } catch (BizException e) {
            throw e;
        } finally {
            //3.release key
            LockHelper.releaseLock(lockKey);
        }

        return usedCount;
    }
}
