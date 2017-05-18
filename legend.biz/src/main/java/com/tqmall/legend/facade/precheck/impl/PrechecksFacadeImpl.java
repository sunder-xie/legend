package com.tqmall.legend.facade.precheck.impl;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.Constants;
import com.tqmall.legend.entity.precheck.PrecheckDetails;
import com.tqmall.legend.entity.precheck.PrecheckRequest;
import com.tqmall.legend.facade.precheck.PrechecksFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsy on 16/7/9.
 */
@Slf4j
@Service
public class PrechecksFacadeImpl implements PrechecksFacade {

    @Autowired
    private PrechecksService prechecksService;

    /**
     * 预检单新增/编辑共用代码
     *
     * @param appearance
     * @param precheckOtherDetail
     * @param goodsInCar
     * @param customerRequest
     * @param shopId
     * @return
     */
    @Override
    public Map<String, Object> addAndUpdateShare(String appearance, String precheckOtherDetail, String goodsInCar, String customerRequest,Long shopId) {
        Map<String, String> tmpItemsAppearance = new Gson().fromJson(appearance, new TypeToken<Map<String, String>>() {
        }.getType());
        Map<String, String> tmpItemsOtherDetail = new Gson().fromJson(precheckOtherDetail, new TypeToken<Map<String, String>>() {
        }.getType());
        Map<String, Boolean> goodsInCarMap = new Gson().fromJson(goodsInCar, new TypeToken<Map<String, Boolean>>() {
        }.getType());
        List<PrecheckRequest> precheckRequestList = new Gson().fromJson(customerRequest, new TypeToken<List<PrecheckRequest>>() {
        }.getType());
        List<PrecheckDetails> precheckDetailsList = getItemList(tmpItemsAppearance, tmpItemsOtherDetail, goodsInCarMap, shopId);
        Map<String, Object> map = new HashMap<>();
        map.put("precheckDetailsList", precheckDetailsList);
        map.put("precheckRequestList", precheckRequestList);
        return map;
    }

    @Override
    public Map<Long, Map<String, String>> getPrecheckItemValuesMap() {
        Map<Long, Map<String, String>> precheckItemValuesMap = Maps.newHashMap();
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String precheckItemValues = jedis.get(Constants.PRECHECK_ITEMS_VALUES);
            if(StringUtils.isBlank(precheckItemValues)){
                precheckItemValuesMap = prechecksService.getAllPrecheckValues();
                if(!CollectionUtils.isEmpty(precheckItemValuesMap)){
                    String precheckItemValuesMapStr = new Gson().toJson(precheckItemValuesMap);
                    jedis.set(Constants.PRECHECK_ITEMS_VALUES,precheckItemValuesMapStr);
                    jedis.expire(Constants.PRECHECK_ITEMS_VALUES, CacheConstants.PRECHECK_KEY_EXP_TIME);
                }
            }else{
                precheckItemValuesMap = new Gson().fromJson(precheckItemValues, new TypeToken<Map<Long, Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            log.error("【获取预检单itemValue】获取值出现异常",e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return precheckItemValuesMap;
    }

    @Override
    public Map<Long, Map<String, String>> getPrecheckItemsMap() {
        Map<Long, Map<String, String>> precheckItemsMap = Maps.newHashMap();
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String precheckItems = jedis.get(Constants.PRECHECK_ITEMS);
            if (StringUtils.isBlank(precheckItems)) {
                precheckItemsMap = prechecksService.getAllPrecheckItems();
                if (!CollectionUtils.isEmpty(precheckItemsMap)) {
                    String precheckItemsMapStr = new Gson().toJson(precheckItemsMap);
                    jedis.set(Constants.PRECHECK_ITEMS, precheckItemsMapStr);
                    jedis.expire(Constants.PRECHECK_ITEMS, CacheConstants.PRECHECK_KEY_EXP_TIME);
                }
            }else{
                precheckItemsMap = new Gson().fromJson(precheckItems, new TypeToken<Map<Long, Map<String, String>>>() {
                }.getType());
            }
        } catch (Exception e) {
            log.error("【获取预检单item】获取值出现异常", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return precheckItemsMap;
    }



    /**
     * 新增预检单数据整理
     *
     * @param tmpItemsAppearance
     * @param tmpItemsOtherDetail
     * @param goodsInCarMap
     * @param shopId
     * @return
     */
    @Override
    public List<PrecheckDetails> getItemList(Map<String, String> tmpItemsAppearance,
                                              Map<String, String> tmpItemsOtherDetail,
                                              Map<String, Boolean> goodsInCarMap, Long shopId) {
        Map<Long, Map<String, String>> precheckItemValues = getPrecheckItemValuesMap();
        Map<Long, Map<String, String>> precheckItems = getPrecheckItemsMap();
        List<PrecheckDetails> precheckDetailsList = new ArrayList<>();
        Map<String, Long> allDetailCombobox = new HashMap<>();
        Map<String, String> allDetailInput = new HashMap<>();
        //合并检测项
        if(!CollectionUtils.isEmpty(goodsInCarMap)){
            Set<Map.Entry<String, Boolean>> goodsInCarSet = goodsInCarMap.entrySet();
            Iterator<Map.Entry<String, Boolean>> iterTmp = goodsInCarSet.iterator();
            while (iterTmp.hasNext()) {
                Map.Entry<String, Boolean> item = iterTmp.next();
                if (item.getValue()) {
                    allDetailCombobox.put(item.getKey(), Constants.precheckItemMapping.get(item.getKey()));
                }
            }
        }
        if(!CollectionUtils.isEmpty(tmpItemsAppearance)){
            //将combobox数据手动
            //tmpItemsAppearance外观检测数据重新封装
            for (String key : tmpItemsAppearance.keySet()) {
                if ("out_lights".equals(key)) {
                    allDetailInput.put(key, tmpItemsAppearance.get(key));
                } else {
                    allDetailCombobox.put(key, Long.parseLong(tmpItemsAppearance.get(key)));
                }
            }
        }
        if(!CollectionUtils.isEmpty(tmpItemsOtherDetail)){
            for (String key : tmpItemsOtherDetail.keySet()) {
                if ("oil_meter".equals(key) || "left_front_hub".equals(key) || "right_front_hub".equals(key)
                        || "left_rear_hub".equals(key) || "right_rear_hub".equals(key)) {
                    allDetailCombobox.put(key, Long.parseLong(tmpItemsOtherDetail.get(key)));
                } else {
                    allDetailInput.put(key, tmpItemsOtherDetail.get(key));
                }
            }
        }
        if(!CollectionUtils.isEmpty(allDetailCombobox)){
            //处理所有combobox数据
            Set<Map.Entry<String, Long>> allItemSet = allDetailCombobox.entrySet();
            Iterator<Map.Entry<String, Long>> iter = allItemSet.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Long> item = iter.next();
                if (item.getValue() == null)
                    item.setValue(0L);
                if (item.getValue() == 0L)
                    continue;
                PrecheckDetails precheckItem = new PrecheckDetails();
                precheckItem.setPrecheckValueId(item.getValue());
                precheckItem.setPrecheckItemId(Constants.precheckItemMapping.get(item.getKey()));
                //获得实际检测项目的信息
                Map<String, String> itemDesc = precheckItems.get(precheckItem.getPrecheckItemId());
                //获得检测项目对应的值
                Map<String, String> itemValueDesc = precheckItemValues.get(item.getValue());
                //生成检测项目
                precheckItem.setPrecheckItemId(Long.valueOf(itemDesc.get("id")));
                if (null != itemValueDesc) {
                    precheckItem.setPrecheckValueId(Long.valueOf(itemValueDesc.get("id")));
                    precheckItem.setPrecheckValue(itemValueDesc.get("value"));
                    precheckItem.setPrecheckValueType(itemValueDesc.get("valueType"));
                }

                precheckItem.setShopId(shopId);
                precheckItem.setPrecheckItemName(itemDesc.get("name"));
                precheckItem.setPrecheckItemType(Long.valueOf(itemDesc.get("itemType")));
                precheckDetailsList.add(precheckItem);
            }
        }
        if(!CollectionUtils.isEmpty(allDetailInput)){
            //处理inputbox
            Set<Map.Entry<String, String>> allItemSet_input = allDetailInput.entrySet();
            Iterator<Map.Entry<String, String>> iter_input = allItemSet_input.iterator();
            while (iter_input.hasNext()) {
                Map.Entry<String, String> item = iter_input.next();
                if (StringUtil.isStringEmpty(item.getValue()))
                    continue;
                PrecheckDetails precheckItem = new PrecheckDetails();
                precheckItem.setPrecheckItemId(Constants.precheckItemMapping.get(item.getKey()));
                //获得实际检测项目的信息
                Map<String, String> itemDesc = precheckItems.get(precheckItem.getPrecheckItemId());
                //生成检测项目
                precheckItem.setPrecheckItemId(Long.valueOf(itemDesc.get("id")));
                precheckItem.setSuggestion(item.getValue());
                precheckItem.setShopId(shopId);
                precheckItem.setPrecheckItemName(itemDesc.get("name"));
                precheckItem.setPrecheckItemType(Long.valueOf(itemDesc.get("itemType")));
                precheckDetailsList.add(precheckItem);
            }
        }
        return precheckDetailsList;
    }

    /**
     * 清楚redis的预检单相关的数据
     * @return
     */
    @Override
    public boolean clearPrecheckRedis() {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            if(jedis.exists(Constants.PRECHECK_ITEMS)){
                log.info("【预检单】清除redis缓存key:PRECHECK_ITEMS");
                jedis.del(Constants.PRECHECK_ITEMS);
            }
            if(jedis.exists(Constants.PRECHECK_ITEMS_VALUES)){
                log.info("【预检单】清除redis缓存key:PRECHECK_ITEMS_VALUES");
                jedis.del(Constants.PRECHECK_ITEMS_VALUES);
            }
            return true;
        } catch (Exception e) {
            log.error("【预检单】清除redis缓存key出现异常", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return false;
    }
}
