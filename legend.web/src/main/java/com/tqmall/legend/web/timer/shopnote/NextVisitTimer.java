package com.tqmall.legend.web.timer.shopnote;

import com.google.common.collect.Maps;
import com.tqmall.common.AbstractTimer;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.service.shopnote.NextVisitNoteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yuchengdu on 16/7/29.
 */
@Component
public class NextVisitTimer extends AbstractTimer {
    @Resource
    private ShopService shopService;

    @Resource
    private NextVisitNoteService nextVisitNoteService;


    private final String DEFAULT_KEY = "SHOP_NOTE_NEXT_VISIT";

    public NextVisitTimer() {
        super(NextVisitTimer.class, "NextVisitJob", 5000, 3);
    }

    @Override
    public void execute() {
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        try {
            Long result = jedis.setnx(DEFAULT_KEY, String.valueOf(System.currentTimeMillis()));
            if (result == 1) {//多机部署只有一台设备会在运行Job
                jedis.expire(DEFAULT_KEY, 300);
                Map shopParam = Maps.newHashMap();
                final Date current_time = DateUtil.getStartTime();//当前时间所在天的起始时间eg:2016-07-30 00:00:00
                List<Shop> shops = shopService.select(shopParam);//获取所有待产生信息的店铺
                for (final Shop shop : shops) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            nextVisitNoteService.processNextVisitCustomerNoteInfo(shop.getId(), current_time);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[提醒中心]下次回访提醒定时任务异常, 异常信息:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }
}
