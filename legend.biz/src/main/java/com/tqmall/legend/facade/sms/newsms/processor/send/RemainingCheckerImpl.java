package com.tqmall.legend.facade.sms.newsms.processor.send;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import com.tqmall.legend.facade.sms.newsms.util.RedisKeyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by majian on 16/11/24.
 */
@Slf4j
@Service
public class RemainingCheckerImpl implements RemainingChecker {
    @Autowired
    private MarketingShopRelService marketingShopRelService;

    @Override
    public boolean isRemainingEnough(Long shopId, String UUID) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String numberKey = RedisKeyParser.getNumberKey(UUID);
            String totalNumberStr = jedis.get(numberKey);
            if (totalNumberStr == null) {
                throw new BizException("jedis找不到numberKey=" + numberKey + "的totalNumberStr");
            }
            int totalNumber = Integer.parseInt(totalNumberStr);

            MarketingShopRel shopRel = marketingShopRelService.selectOneById(shopId);

            long leftNum = 0l;
            if (shopRel != null) {
                leftNum = shopRel.getSmsNum();
            }
            if (totalNumber > leftNum) {
                return false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return true;

    }
}
