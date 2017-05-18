package com.tqmall.legend.web.timer.shopnote;

import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by twg on 16/3/8.
 * 门店提醒信息
 */
@Component("shopNoteInvalidTimer")
@Slf4j
public class ShopNoteInvalidTimer {
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;

    private static final String DEFAULT_KEY = "SHOP_NOTE_INVALID";

    public void process() {
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        log.info("====门店提醒失效清洗开始====");
        long sTime = System.currentTimeMillis();
        try {
            Long result = jedis.setnx(DEFAULT_KEY, String.valueOf(sTime));
            if (result == 1) {//多机部署只有一台设备会在运行Job
                jedis.expire(DEFAULT_KEY, 300);

                if (shopNoteInfoService.expiredNoteInfo()) {
                    log.info("在多机模式下门店提醒失效状态更新完成");
                }
                log.info("门店提醒失效清洗耗时:{}ms.", System.currentTimeMillis() - sTime);
            }
        } catch (Exception e) {
            log.error("error occurred in ShopNoteInvalidTimer:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

}
