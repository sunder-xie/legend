package com.tqmall.legend.web.timer.shopnote;

import com.google.common.collect.Maps;
import com.tqmall.common.AbstractTimer;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.service.shopnote.MaintainNoteService;
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
public class MaintainTimer extends AbstractTimer {
    @Resource
    private ShopService shopService;

    @Resource
    private MaintainNoteService maintainNoteService;


    private final String DEFAULT_KEY = "SHOP_NOTE_MAINTAIN";

    public MaintainTimer() {
        super(MaintainTimer.class, "MaintainJob", 5000, 4);
    }

    @Override
    public void execute() {
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        final long sTime = System.currentTimeMillis();
        try {
            Long result = jedis.setnx(DEFAULT_KEY, String.valueOf(sTime));
            if (result == 1) {//多机部署只有一台设备会在运行Job
                jedis.expire(DEFAULT_KEY, 300);
                Map shopParam = Maps.newHashMap();
                final Date current_time = DateUtil.getStartTime();
                List<Shop> shops = shopService.select(shopParam);//获取所有待产生信息的店铺
                for (final Shop shop : shops) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            maintainNoteService.processMaintainCustomerNoteInfo(shop.getId(),current_time);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[提醒中心]保养提醒定时任务异常, 异常信息:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }


    }
}
