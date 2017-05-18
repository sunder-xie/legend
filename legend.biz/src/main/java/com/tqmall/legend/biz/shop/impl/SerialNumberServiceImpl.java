package com.tqmall.legend.biz.shop.impl;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.dao.shop.SerialNumberDao;
import com.tqmall.legend.entity.shop.SerialNumber;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version V1.0
 * @Description: 编号Service
 * @date 2015-11-10
 */
@Service
public class SerialNumberServiceImpl extends BaseServiceImpl implements SerialNumberService {
    Logger logger = LoggerFactory.getLogger(SerialNumberServiceImpl.class);

    @Autowired
    private SerialNumberDao serialNumberDao;

    /**
     * 批量更新时使用的公共mapList
     */
    private Map<String,String> batchMap = null;

    @Override
    public String getSerialNumber(Map map) {

        String serialNumber = "";
        try {
            String serial_prefix="";
            SimpleDateFormat fmt = new SimpleDateFormat("yyMM");
            if(map.containsKey("historyDate")){
                serial_prefix = fmt.format(map.get("historyDate"));
            }else{
                //生成编号前缀如YJ1511
                serial_prefix = fmt.format(new Date());
            }

            if (map.containsKey("serialType") && map.containsKey("shopId") && map.containsKey("userId")) {
                String serialPrefix = map.get("serialType") + serial_prefix;
                map.put("serialPrefix", serialPrefix);
                Long serial_amount = getSerialNumberFromRedis(map);

                String serialType = (String) map.get("serialType");
                Long shopId = (Long) map.get("shopId");

                // 本月第一条
                if (serial_amount.compareTo(0L) == 0) {
                    serialNumber = serialPrefix + "0001";
                    Long userId = (Long) map.get("userId");

                    SerialNumber sn = new SerialNumber();
                    sn.setCreator(userId);
                    sn.setModifier(userId);
                    sn.setSerialType(serialType);
                    sn.setSerialPrefix(serialPrefix);
                    sn.setSerialAmount(1L);
                    sn.setShopId(shopId);
                    serialNumberDao.insert(sn);
                } else {
                    Long next_serial_amount = serial_amount + 1;
                    if (serial_amount.compareTo(10000L) > -1) {
                        serialNumber = serialPrefix + next_serial_amount.toString();
                    } else {
                        serialNumber = serialPrefix + String.format("%04d", next_serial_amount);
                    }

                    List<SerialNumber> serialNumbers = serialNumberDao.select(map);
                    if (!CollectionUtils.isEmpty(serialNumbers)) {
                        SerialNumber serNumber = serialNumbers.get(0);
                        serNumber.setSerialAmount(next_serial_amount);
                        int result = serialNumberDao.updateById(serNumber);
                        if (result == 1) {
                            updateRedis(shopId, serialType, serialPrefix, next_serial_amount);
                        }
                    }
                }

            } else {
                logger.error("在生成编号时,入参存在异常{}", JSONUtil.object2Json(map));
            }
        }catch (Exception e){
            logger.error("在生成编号时,系统发生异常,信息：{}",e);

        }
        return serialNumber;
    }

    /**
     * 不通过redis获取工单编号
     * 可以在批量中使用
     * 批量调用完后通过syncRedis()方法批量插入redis
     * 主体方法来自getSerialNumber(map)
     * @param map
     * @return
     */
    @Override
    public String getSerialNumbers(Map map) {
        String serialNumber = "";
        try {
            String serial_prefix="";
            SimpleDateFormat fmt = new SimpleDateFormat("yyMM");
            if(map.containsKey("historyDate")){
                serial_prefix = fmt.format(map.get("historyDate"));
            }else{
                //生成编号前缀如YJ1511
                serial_prefix = fmt.format(new Date());
            }
            if (map.containsKey("serialType") && map.containsKey("shopId") && map.containsKey("userId")) {
                String serialPrefix = map.get("serialType") + serial_prefix;
                map.put("serialPrefix", serialPrefix);
                String serialType = (String) map.get("serialType");
                Long shopId = (Long) map.get("shopId");
                // 本月第一条
                Long serialAmount = 0L;
                List<SerialNumber> serialNumbers = serialNumberDao.select(map);
                if (!CollectionUtils.isEmpty(serialNumbers)) {
                    serialAmount = serialNumbers.get(0).getSerialAmount();
                }
                if (serialAmount.compareTo(0L) == 0) {
                    serialNumber = serialPrefix + "0001";
                    Long userId = (Long) map.get("userId");
                    SerialNumber sn = new SerialNumber();
                    sn.setCreator(userId);
                    sn.setModifier(userId);
                    sn.setSerialType(serialType);
                    sn.setSerialPrefix(serialPrefix);
                    sn.setSerialAmount(1L);
                    sn.setShopId(shopId);
                    serialNumberDao.insert(sn);
                } else {
                    if (!CollectionUtils.isEmpty(serialNumbers)) {
                        SerialNumber serNumber = serialNumbers.get(0);
                        Long nextSerialAmount = serialAmount + 1;
                        if (serialAmount.compareTo(10000L) > -1) {
                            serialNumber = serialPrefix + nextSerialAmount.toString();
                        } else {
                            serialNumber = serialPrefix + String.format("%04d", nextSerialAmount);
                        }
                        serNumber.setSerialAmount(nextSerialAmount);
                        int result = serialNumberDao.updateById(serNumber);
                        if (result == 1) {
                            batchUpdateRedis(shopId, serialType, serialPrefix, nextSerialAmount);
                        }
                    }
                }

            } else {
                logger.error("在生成编号时,入参存在异常{}", JSONUtil.object2Json(map));
            }
        }catch (Exception e){
            logger.error("在生成编号时,系统发生异常,信息：{}",e);

        }
        return serialNumber;
    }

    /**
     * 通过管道同步内存中的数据到Redis中
     */
    @Override
    public void syncRedis() {
        if(CollectionUtils.isEmpty(batchMap)){
            return;
        }
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        Pipeline pipelined = jedis.pipelined();
        Set<String> keySet = batchMap.keySet();
        for (String key : keySet) {
            String value = batchMap.get(key);
            if(value != null){
                pipelined.setex(key, 30, value);
                logger.debug("批量更新redis数据,key:{},value:{}",key,value);
            }
        }
        pipelined.sync();
        JedisPoolUtils.returnMasterRes(jedis);
    }

    /**
     * 支持批量的更新redis
     * 运行完后通过syncRedis()方法批量插入redis
     * @param shopId
     * @param serialType
     * @param serialPrefix
     * @param nextSerialAmount
     */
    private void batchUpdateRedis(Long shopId, String serialType, String serialPrefix, Long nextSerialAmount) {
        String redisKey = getRedisKey(shopId, serialType, serialPrefix);
        if(batchMap == null){
            batchMap = new HashMap();
        }
        batchMap.put(redisKey,nextSerialAmount.toString());
    }

    /**
     * 从redis获取对应类型的编号
     *
     * @param map
     * @return
     */
    public Long getSerialNumberFromRedis(Map map) {
        Long serialAmount = 0L;
        if (map.containsKey("shopId") &&
                map.containsKey("serialType") &&
                map.containsKey("serialPrefix")) {

            Jedis jedis = null;
            try {
                Long shopId = (Long) map.get("shopId");
                String serial_type = (String) map.get("serialType");
                String serial_prefix = (String) map.get("serialPrefix");
                String redisKey = getRedisKey(shopId, serial_type, serial_prefix);
                if (logger.isDebugEnabled()) {
                    logger.debug("通过门店shopId = {},编号生成类型serialType= {},编号生成前缀serialPrefix={}生成redis的键值为{}", shopId, serial_type, serial_prefix, redisKey);
                }
                jedis = JedisPoolUtils.getMasterJedis();
                String serial_amount = jedis.get(redisKey);
                if (StringUtils.isBlank(serial_amount)) {
                    serialAmount = serialNumber(map);
                    jedis.setex(redisKey, 30, serialAmount.toString());
                    logger.info("通过门店shopId = {},编号生成类型serialType= {},编号生成前缀serialPrefix={}生成redis的键值为{}从数据库获取serial_amount={}", shopId, serial_type, serial_prefix, redisKey, serialAmount);
                } else {
                    serialAmount = Long.valueOf(serial_amount);
                    logger.info("通过门店shopId = {},编号生成类型serialType= {},编号生成前缀serialPrefix={}生成redis的键值为{}从redis获取编号serial_amount={}", shopId, serial_type, serial_prefix, redisKey, serialAmount);
                }
            } catch (Exception e) {
                logger.error("根据{}从redis获取编号异常,信息:{}", JSONUtil.object2Json(map),e);
                serialAmount = serialNumber(map);
            } finally {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }
        return serialAmount;
    }

    private void updateRedis(Long shopId, String serial_type, String serial_prefix, Long serial_amount) {
        String redisKey = getRedisKey(shopId, serial_type, serial_prefix);
        Jedis jedis = JedisPoolUtils.getMasterJedis();
        try {
            jedis.setex(redisKey, 30, serial_amount.toString());
            logger.info("通过门店shopId = {},编号生成类型serialType= {},编号生成前缀serialPrefix={}生成redis的键值为{}更新redis缓冲值为{}成功", shopId, serial_type, serial_prefix, redisKey, serial_amount);
        } catch (Exception e) {
            logger.error("通过门店shopId = {},编号生成类型serialType= {},编号生成前缀serialPrefix={}生成redis的键值为{}更新redis缓冲值为{}异常,信息:{}", shopId, serial_type, serial_prefix, redisKey, serial_amount,e);
            jedis.del(redisKey);
            logger.info("清空redis中key:{}", redisKey);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    private Long serialNumber(Map map) {
        Long serialAmount = 0L;
        List<SerialNumber> serialNumbers = serialNumberDao.select(map);
        if (!CollectionUtils.isEmpty(serialNumbers)) {
            serialAmount = serialNumbers.get(0).getSerialAmount();
        }
        return serialAmount;
    }

    /**
     * 生成redis的键值YJ:31:YJ1511
     *
     * @param shopId        门店id
     * @param serial_type   编号生成类型
     * @param serial_prefix 编号生成前缀
     * @return
     */
    private String getRedisKey(Long shopId, String serial_type, String serial_prefix) {
        StringBuffer redisKey = new StringBuffer(serial_type);
        redisKey.append(":");
        redisKey.append(shopId);
        redisKey.append(":");
        redisKey.append(serial_prefix);
        return redisKey.toString();
    }

}