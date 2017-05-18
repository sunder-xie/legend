package com.tqmall.legend.web.timer;

import com.tqmall.common.Constants;
import com.tqmall.core.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by twg on 15/10/27.
 */
@Component("customerCarBrandAndModelTimer")
@Slf4j
public class CustomerCarBrandAndModelTimer {
    public void process(){
        _clearCache(Constants.COMM_CAR_BRAND_PREFIX);
    }

    private void _clearCache(String cacheKey) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            jedis.del(cacheKey);
            log.info("清除常用车辆品牌和车型缓存完成");
        } catch (Exception e) {
            log.error("清除常用车辆品牌和车型缓存异常", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }
}
