package com.tqmall.legend.web.timer.shopnote;

import com.google.common.collect.Maps;
import com.tqmall.common.AbstractTimer;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.service.shopnote.VisitNoteService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 回访提醒
 * <p/>
 * Created by yuchengdu on 16/7/29.
 */
@Component
public class VisitTimer extends AbstractTimer {

    @Resource
    private ShopService shopService;

    @Resource
    private VisitNoteService visitNoteService;


    private static final String DEFAULT_KEY = "SHOP_NOTE_Visit";

    public VisitTimer() {
        super(VisitTimer.class, "VisitJob", 5000, 3);
    }

    @Override
    public void execute() {
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        final long sTime = System.currentTimeMillis();
        try {
            Long result = jedis.setnx(DEFAULT_KEY, String.valueOf(sTime));
            if (result == 1) {//多机部署只有一台设备会在运行Job
                jedis.expire(DEFAULT_KEY, 300);
                final Date currentTime = DateUtil.getStartTime();
                List<Shop> shops = findAllShopList();
                if (CollectionUtils.isEmpty(shops)) {
                    return;
                }
                for (final Shop shop : shops) {
                    //按照店铺为维度放入线程执行任务队列
                    final Long shopId = shop.getId();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            visitNoteService.processVisitNoteInfo(shopId, currentTime);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[提醒中心]回访提醒定时任务异常, 异常信息:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    /**
     * 获取所有门店
     * @return
     */
    private List<Shop> findAllShopList() {
        Map shopParam = Maps.newHashMap();
        return shopService.select(shopParam);
    }

}
