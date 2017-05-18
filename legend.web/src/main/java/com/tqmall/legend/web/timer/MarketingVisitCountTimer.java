package com.tqmall.legend.web.timer;

import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.SerializeUtil;
import com.tqmall.legend.biz.marketing.MarketingActivityService;
import com.tqmall.legend.entity.marketing.MarketingCase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/10/13.
 * 门店营销活动地址被访问量定时刷新
 */
@Component("marketingVisitCountTimer")
@Slf4j
public class MarketingVisitCountTimer {
    private static Logger logger = LoggerFactory.getLogger(MarketingVisitCountTimer.class);

    @Autowired
    private MarketingActivityService marketingActivityService;
    public void process(){
        getValueFromJedis();
    }

    private void getValueFromJedis(){
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            byte[] redis = jedis.get(Constants.MARKETING_VISIT_COUNT_PREFIX.getBytes("UTF-8"));
            Map<String,Long> value = (Map<String,Long>) SerializeUtil.unserialize(redis);
            if(!CollectionUtils.isEmpty(value)){
                for (Map.Entry<String, Long> entry : value.entrySet()) {
                    String[] _k = entry.getKey().split("_");
                    Long _v = entry.getValue();
                    Map map = Maps.newConcurrentMap();
                    map.put("shopId",_k[1]);
                    map.put("templateId",_k[2]);

                    List<MarketingCase> marketingCases = marketingActivityService.select(map);
                    if(!CollectionUtils.isEmpty(marketingCases)){
                        MarketingCase marketingCase = marketingCases.get(0);
                        if(marketingCase.getVisitCount().compareTo(_v) !=0){
                            marketingCase.setVisitCount(_v);
                            marketingActivityService.updateById(marketingCase);
                            logger.info("定时更新门店营销活动访问量成功");
                        }
                    }
                }
            }
        }catch (UnsupportedEncodingException e){
            logger.error("定时更新门店营销活动访问量异常,错误信息", e);
        }finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }
}
