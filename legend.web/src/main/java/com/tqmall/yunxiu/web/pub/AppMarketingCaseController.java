package com.tqmall.yunxiu.web.pub;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.SerializeUtil;
import com.tqmall.legend.biz.marketing.AppActivityService;
import com.tqmall.legend.biz.marketing.MarketingActivityService;
import com.tqmall.legend.biz.marketing.MarketingColumnConfigService;
import com.tqmall.legend.biz.marketing.MarketingTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.entity.marketing.AppActivity;
import com.tqmall.legend.entity.marketing.MarketingCase;
import com.tqmall.legend.entity.marketing.MarketingCaseService;
import com.tqmall.legend.entity.marketing.MarketingColumnConfig;
import com.tqmall.legend.entity.marketing.MarketingStatusEnum;
import com.tqmall.legend.entity.marketing.MarketingTemplate;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.timer.MarketingVisitCountTimer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/6.
 * <p/>
 * 给App提供门店营销活动接口
 */
@Controller
@RequestMapping("/pub/shop/marketing")
public class AppMarketingCaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AppMarketingCaseController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private MarketingActivityService marketingActivityService;

    @Autowired
    private MarketingTemplateService marketingTemplateService;

    @Autowired
    private MarketingColumnConfigService marketingColumnConfigService;

    @Autowired
    private MarketingVisitCountTimer marketingVisitCountTimer;

    @Autowired
    private AppActivityService appActivityService;


    /**
     * 通过url读取缓存，进行访问量的统计
     *
     * @return
     */
    @RequestMapping("visit_count")
    @ResponseBody
    public Result visitCount(){
        marketingVisitCountTimer.process();
        return Result.wrapSuccessfulResult(true);
    }

    //手机扫二维码跳转H5页面
    @RequestMapping(value = {"","index"},method = RequestMethod.GET)
    public String index(@RequestParam(value = "shopId",required = false)Long shopId,
                        @RequestParam(value = "id",required = false)Long id,
                        @RequestParam(value = "templateId",required = false)Long templateId,
                        @RequestParam(value = "tpl",required = false)String tpl,Model model){
        String flags = "?";
        if(!StringUtils.isBlank(tpl)){
            if(tpl.contains("?")){
                flags = "&";
            }
        }
        if(null != shopId && null != id && !StringUtils.isBlank(tpl)){
            pushValueToJedis(shopId,templateId);
            return "redirect:/dandelion/html/push_page/" + tpl + flags + "shopId="+shopId+"&id="+id+"&templateId="+templateId;
        }else if(null != shopId && null != templateId && !StringUtils.isBlank(tpl)) {
            pushValueToJedis(shopId,templateId);
            return "redirect:/dandelion/html/push_page/" + tpl + flags + "shopId="+shopId+"&templateId="+templateId;
        }else if(!StringUtils.isBlank(tpl)){
            return "redirect:/dandelion/html/push_page/"+tpl;
        }
        return "redirect:/dandelion/html/push_page/index.html";
    }

    /**
     * 访问活动地址时加1暂时存放在缓存中,定时同步更新访问值
     * @param shopId 门店id
     * @param templateId 营销活动模板id
     */
    private void pushValueToJedis(Long shopId,Long templateId){
        Jedis jedis = null;
        String key = "_"+shopId+"_"+templateId;
        Map map = Maps.newConcurrentMap();
        String[]_k= key.split("_");
        map.put("shopId",_k[1]);
        map.put("templateId",_k[2]);
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            byte[] redis = jedis.get(Constants.MARKETING_VISIT_COUNT_PREFIX.getBytes("UTF-8"));
            Map<String,Long> value = Maps.newConcurrentMap();
            if(redis == null){
                jedis.set(Constants.MARKETING_VISIT_COUNT_PREFIX.getBytes("UTF-8"), SerializeUtil.serialize(value));
            }
            value = (Map<String,Long>)SerializeUtil.unserialize(redis);
            if(null != value && !value.isEmpty()){
                Long v = value.get(key);
                if(null == v){
                    createVisitCount(key, map, value);
                }else {
                    value.put(key,++v);
                }
            }else {
                value = Maps.newConcurrentMap();
                createVisitCount(key, map, value);
            }
            jedis.set(Constants.MARKETING_VISIT_COUNT_PREFIX.getBytes("UTF-8"), SerializeUtil.serialize(value));
            jedis.expire(Constants.MARKETING_VISIT_COUNT_PREFIX.getBytes("UTF-8"), CacheConstants.VISIT_COUNT_KEY_EXP_TIME);
        }catch (UnsupportedEncodingException e){
            logger.error("访问活动地址时shopId={"+shopId+"} templateId={"+templateId+"} ,错误信息", e);
        }finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    /**
     * 当redis过期时保证数据的同步
     * @param key
     * @param map
     * @param value
     */
    private void createVisitCount(String key, Map map, Map<String, Long> value) {
        List<MarketingCase> marketingCases = marketingActivityService.select(map);
        if(!CollectionUtils.isEmpty(marketingCases)){
            MarketingCase marketingCase = marketingCases.get(0);
            Long visitCount = marketingCase.getVisitCount();
            value.put(key,++visitCount);
        }
    }

    @RequestMapping(value = "get",method = RequestMethod.GET)
    @ResponseBody
    public Result getMarketing(@RequestParam("shopId") Long shopId,@RequestParam(value = "id",required = false)Long id,
                               @RequestParam(value = "templateId",required = true)Long templateId,Model model){
        Shop shop = shopService.selectById(shopId);
        //先从web端获取活动模板，否则从后台获取模板信息
        Map<String, Object> param = Maps.newConcurrentMap();
        if (null != id) {
            param.put("id", id);
        }
        if(null != templateId && null != shopId){
            param.put("templateId", templateId);
            param.put("shopId", shopId);
            List<MarketingCase> marketingCases = marketingActivityService.select(param);
            List<Long> templateIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(marketingCases)) {
                MarketingCase marketingCase = marketingCases.get(0);
                param.clear();
                param.put("caseId", marketingCase.getId());
                param.put("shopId", marketingCase.getShopId());
                List<MarketingCaseService> marketingCaseServices = marketingActivityService.selectCaseService(param);
                marketingCase.setServiceInfos(marketingCaseServices);

                templateIds.add(marketingCase.getTemplateId());
                if(!CollectionUtils.isEmpty(templateIds)){
                    List<MarketingTemplate> marketingTemplates = getMarketingTemplates(param, templateIds);
                    if (!CollectionUtils.isEmpty(marketingTemplates)) {
                        MarketingTemplate marketingTemplate = marketingTemplates.get(0);
                        marketingCase.setServiceNum(marketingTemplate.getServiceNum());
                        marketingCase.setStatus(MarketingStatusEnum.CASE_STATUS_ISSUE.getKey());
                        marketingCase.setMarketingColumnConfig(marketingTemplate.getMarketingColumnConfig());
                        //获取门店活动模板字段配置列信息
                        MarketingColumnConfig marketingColumnConfig = marketingTemplate.getMarketingColumnConfig();
                        createMarketingCase(marketingCase, marketingColumnConfig, shop);
                        return Result.wrapSuccessfulResult(marketingCase);
                    }
                }
            }
        }


        return Result.wrapErrorResult("9999", "该营销活动已下架");
    }

    /**
     * 获取只显示上架的模板列表
     *
     * @param param       入参
     * @param templateIds 模板id集合
     * @return 返回模板列表
     */
    private List<MarketingTemplate> getMarketingTemplates(Map<String, Object> param, List<Long> templateIds) {
        param.clear();
        param.put("ids", templateIds);
        param.put("status", MarketingStatusEnum.MARKETING_STATUS_UP.getKey());
        //获取城市站的默认模板列表
        List<MarketingTemplate> marketingTemplates = marketingTemplateService.select(param);
        templateIds.clear();
        for (MarketingTemplate marketingTemplate : marketingTemplates) {
            templateIds.add(marketingTemplate.getId());
        }
        param.clear();
        param.put("templateIds", templateIds);
        //获取城市站模板的字段配置信息
        List<MarketingColumnConfig> marketingColumnConfigs = marketingColumnConfigService.select(param);
        for (MarketingTemplate marketingTemplate : marketingTemplates) {
            for (MarketingColumnConfig marketingColumnConfig : marketingColumnConfigs) {
                if (marketingColumnConfig.getTemplateId().compareTo(marketingTemplate.getId()) == 0) {
                    marketingTemplate.setMarketingColumnConfig(marketingColumnConfig);
                    break;
                }
            }
        }

        return marketingTemplates;
    }

    /**
     * 创建门店活动实例的门店信息和显示的配置字段信息
     * @param marketingCase         门店活动实例
     * @param marketingColumnConfig 模板配置的字段信息
     * @param shop                  门店信息
     * @return 返回门店活动实例
     */
    private MarketingCase createMarketingCase(MarketingCase marketingCase, MarketingColumnConfig marketingColumnConfig, Shop shop) {

        marketingCase.setMarketingColumnConfig(marketingColumnConfig);
        Integer name = marketingColumnConfig.getName();
        Integer address = marketingColumnConfig.getAddress();
        Integer mobile = marketingColumnConfig.getMobile();
        //判断门店字段和门店服务字段是否配置
        if (name.compareTo(MarketingStatusEnum.COLUMN_CONFIG_UP.getKey()) == 0 ||
                address.compareTo(MarketingStatusEnum.COLUMN_CONFIG_UP.getKey()) == 0 ||
                mobile.compareTo(MarketingStatusEnum.COLUMN_CONFIG_UP.getKey()) == 0) {

            marketingCase.setShop(shop);
        }
        return marketingCase;
    }

    /**
     * 获得车主有效的活动信息
     * create by jason 2015-10-28
     *
     */
    @RequestMapping(value = "act", method = RequestMethod.GET)
    @ResponseBody
    public Result getActInfo(@RequestParam(value = "actId", required = true) Long actId) {
        try {
            Map map = new HashMap();

            map.put("id", actId);
            map.put("actStatus", 2);
            map.put("endTimeGt", new Date());
            logger.info("获取车主有效的活动信息参数:{}", map);
            AppActivity result = appActivityService.warpActivity(map);
            if (null != result) {
                return Result.wrapSuccessfulResult(result);
            } else {
                return Result.wrapSuccessfulResult("返回活动数据为空");
            }
        } catch (Exception e) {
            logger.error("获得数据出错:{}", e);
            return Result.wrapErrorResult("-1", "获得数据出错!");
        }
    }
}
