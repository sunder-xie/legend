package com.tqmall.legend.web.marketing;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.WriterException;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.SerializeUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.marketing.MarketingActivityService;
import com.tqmall.legend.biz.marketing.MarketingColumnConfigService;
import com.tqmall.legend.biz.marketing.MarketingTemplateCityService;
import com.tqmall.legend.biz.marketing.MarketingTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.entity.marketing.*;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.CodeGeneration;
import com.tqmall.oss.OSSClientUtil;
import com.tqmall.oss.OSSConstants;
import com.tqmall.oss.ObjectKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 15/8/7.
 * 门店营销活动
 */
@Controller
@RequestMapping("shop/activity")
public class MarketingCaseController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MarketingCaseController.class);

    private static final String MARKETING_TEMPLATE_PREFIX = "MARKETING_TEMPLATE_";
    private static final String MARKETING_TEMPLATE_CASE_PREFIX = "MARKETING_TEMPLATE_CASE_";

    @Autowired
    private ShopService shopService;

    @Autowired
    private MarketingActivityService marketingActivityService;

    @Autowired
    private MarketingTemplateCityService marketingTemplateCityService;

    @Autowired
    private MarketingTemplateService marketingTemplateService;

    @Autowired
    private MarketingColumnConfigService marketingColumnConfigService;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Value("${tqmall.oss.bucketName}")
    private String tqmallBucketName;

    @HttpRequestLog
    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("moduleUrl", "marketing");
        model.addAttribute("subModule", "promotion");
        return "yqx/page/activity/act-list";
    }

    /**
     * 首页
     *
     * @param model SpringMode
     * @return 返回门店营销活动列表页面
     */
    @RequestMapping("list")
    @ResponseBody
    public Result list(Model model) {
        try {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            Shop shop = shopService.selectById(shopId);
            //先从web端获取活动模板，否则从后台获取模板信息
            Map<String, Object> param = Maps.newConcurrentMap();
            param.put("shopId", shopId);
            List<MarketingCase> marketingCases = marketingActivityService.select(param);
            List<MarketingCase> marketingCaseList = Lists.newArrayList();

            List<Long> city = Lists.newArrayList();
            city.add(0L);
            city.add(shop.getCity());
            param.put("city", city);

            List<MarketingTemplateCity> marketingTemplateCities = marketingTemplateCityService.select(param);
            //新的模板实例需要一开始就保存信息
            List<MarketingCase> marketCaseList = Lists.newArrayList();

            if (!CollectionUtils.isEmpty(marketingCases)) {
                //再和模板比较判断只显示上架活动
                List<Long> templateIds = Lists.newArrayList();
                for (MarketingTemplateCity marketingTemplateCity : marketingTemplateCities) {
                    templateIds.add(marketingTemplateCity.getTemplateId());
                }
                if(!CollectionUtils.isEmpty(templateIds)){
                    List<MarketingTemplate> marketingTemplates = getMarketingTemplates(param, templateIds);

                    for (MarketingTemplate marketingTemplate : marketingTemplates) {
                        boolean isMatch = false;

                        for (MarketingCase marketingCase : marketingCases) {
                            if (marketingCase.getTemplateId().compareTo(marketingTemplate.getId()) == 0) {
                                marketingCase.setTitle(marketingTemplate.getTitle());
                                marketingCase.setContent(marketingTemplate.getContent());
                                marketingCaseList.add(marketingCase);
                                isMatch = true;
                                break;
                            }
                        }

                        if (!isMatch) {
                            MarketingCase marketingCase = new MarketingCase();
                            marketingCase.setTitle(marketingTemplate.getTitle());
                            marketingCase.setContent(marketingTemplate.getContent());
                            marketingCase.setServiceNum(marketingTemplate.getServiceNum());
                            marketingCase.setTemplateUrl(marketingTemplate.getTemplateUrl());
                            marketingCase.setTemplateId(marketingTemplate.getId());
                            marketingCase.setShopId(shopId);
                            marketingCase.setStatus(MarketingStatusEnum.CASE_STATUS_ISSUE.getKey());
                            //add by twg 2015-10-13
                            marketingCase.setCreator(userInfo.getUserId());
                            marketingCase.setModifier(userInfo.getUserId());
                            String url = createTemplateUrl(marketingCase,userInfo);
                            //生成二维码
                            String imUrl = createRedisData(MARKETING_TEMPLATE_PREFIX + marketingCase.getTemplateId(), url);
                            marketingCase.setImgUrl(imUrl);
                            marketingCase.setTemplateUrl(url);
                            marketCaseList.add(marketingCase);
                            //end
                            marketingCaseList.add(marketingCase);
                        }

                    }
                }
                if(!CollectionUtils.isEmpty(marketCaseList)){
                    Integer count = marketingActivityService.batchInsert(marketCaseList);
                    if(count.intValue() == marketCaseList.size()){
                        logger.info("批量插入活动模板完成");
                    }else {
                        logger.info("批量插入活动模板不完整应该{}条，实际插入{}条",marketCaseList.size(),count);
                    }
                }
                model.addAttribute("marketingCaseList", marketingCaseList);

            } else {
                if (!CollectionUtils.isEmpty(marketingTemplateCities)) {
                    List<Long> templateIds = Lists.newArrayList();
                    for (MarketingTemplateCity marketingTemplateCity : marketingTemplateCities) {
                        templateIds.add(marketingTemplateCity.getTemplateId());
                    }
                    if(!CollectionUtils.isEmpty(templateIds)){
                        List<MarketingTemplate> marketingTemplates = getMarketingTemplates(param, templateIds);


                        for (MarketingTemplate marketingTemplate : marketingTemplates) {
                            MarketingCase marketingCase = new MarketingCase();
                            marketingCase.setTitle(marketingTemplate.getTitle());
                            marketingCase.setContent(marketingTemplate.getContent());
                            marketingCase.setServiceNum(marketingTemplate.getServiceNum());
                            marketingCase.setTemplateUrl(marketingTemplate.getTemplateUrl());
                            marketingCase.setTemplateId(marketingTemplate.getId());
                            marketingCase.setShopId(shopId);
                            marketingCase.setStatus(MarketingStatusEnum.CASE_STATUS_ISSUE.getKey());

                            //add by twg 2015-10-13
                            marketingCase.setCreator(userInfo.getUserId());
                            marketingCase.setModifier(userInfo.getUserId());
                            String url = createTemplateUrl(marketingCase,userInfo);
                            //生成二维码
                            String imUrl = createRedisData(MARKETING_TEMPLATE_PREFIX + marketingCase.getTemplateId(), url);
                            marketingCase.setImgUrl(imUrl);
                            marketingCase.setTemplateUrl(url);
                            marketCaseList.add(marketingCase);
                            //end

                            marketingCaseList.add(marketingCase);
                        }
                    }

                    if(!CollectionUtils.isEmpty(marketCaseList)){
                        Integer count = marketingActivityService.batchInsert(marketCaseList);
                        if(count.intValue() == marketCaseList.size()){
                            logger.info("批量插入活动模板完成");
                        }else {
                            logger.info("批量插入活动模板不完整应该{}条，实际插入{}条",marketCaseList.size(),count);
                        }
                    }

                    model.addAttribute("marketingCaseList", marketingCaseList);

                }
            }

            return Result.wrapSuccessfulResult(marketingCaseList);

        }catch (Exception e){
            logger.error("返回门店营销活动列表页面,异常原因：{}",e);
            return Result.wrapErrorResult("","返回门店营销活动列表页面,"+e.getMessage());

        }

    }

    /**
     * 创建门店活动实例的门店信息和显示的配置字段信息
     *
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
        if (!CollectionUtils.isEmpty(templateIds)) {
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
        }

        return marketingTemplates;
    }

    /**
     * 营销活动编辑页
     *
     * @param id    门店营销活动id
     * @param model SpringMode
     * @return 返回门店营销活动编辑页面
     */
    @RequestMapping("edit")
    public String activityEdit(@RequestParam("id") Long id,@RequestParam("templateId") Long templateId, Model model) {
        model.addAttribute("moduleUrl", "marketing");
        model.addAttribute("subModule", "promotion");

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);

        List<Long> templateIds = Lists.newArrayList();
        Map<String, Object> param = Maps.newConcurrentMap();
        param.put("shopId", shopId);

        if (null != id) {
            //
            param.put("id", id);
            List<MarketingCase> marketingCases = marketingActivityService.select(param);
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
                        marketingCase.setTemplateUrl(marketingTemplate.getTemplateUrl());

                        //获取门店活动模板字段配置列信息
                        MarketingColumnConfig marketingColumnConfig = marketingTemplate.getMarketingColumnConfig();
                        createMarketingCase(marketingCase, marketingColumnConfig, shop);

                        model.addAttribute("marketingCase", marketingCase);
                    }
                }
            }
        } else if (null != templateId) {

            templateIds.add(templateId);
            List<MarketingTemplate> marketingTemplates = getMarketingTemplates(param, templateIds);

            if (!CollectionUtils.isEmpty(marketingTemplates)) {
                MarketingTemplate marketingTemplate = marketingTemplates.get(0);

                MarketingCase marketingCase = new MarketingCase();
                marketingCase.setTitle(marketingTemplate.getTitle());
                marketingCase.setContent(marketingTemplate.getContent());
                marketingCase.setServiceNum(marketingTemplate.getServiceNum());
                marketingCase.setTemplateUrl(marketingTemplate.getTemplateUrl());
                marketingCase.setTemplateId(marketingTemplate.getId());
                marketingCase.setShopId(shopId);
                marketingCase.setStatus(MarketingStatusEnum.CASE_STATUS_ISSUE.getKey());
                //获取门店活动模板字段配置列信息
                MarketingColumnConfig marketingColumnConfig = marketingTemplate.getMarketingColumnConfig();
                createMarketingCase(marketingCase, marketingColumnConfig, shop);
                model.addAttribute("marketingCase", marketingCase);

            }

        }

        return "yqx/page/activity/act-edit";
    }

    /**
     * 创建活动分享URL
     * @param marketingCase 活动模板实例
     * @return
     */
    private String createTemplateUrl(MarketingCase marketingCase,UserInfo userInfo){
        StringBuilder sb = new StringBuilder(150);
        String templateUrl = marketingCase.getTemplateUrl();
        int flg = templateUrl.indexOf("?");

        if (flg > 0) {
            sb.append(templateUrl);
            sb.append("&shopId=" + userInfo.getShopId());
        }else {
            sb.append(templateUrl);
            sb.append("?shopId=" + userInfo.getShopId());
        }
        if (null != marketingCase.getId()) {
            sb.append("&id=" + marketingCase.getId());
        }
        sb.append("&templateId=" + marketingCase.getTemplateId());
        return sb.toString();
    }



    /**
     * 门店营销活动保存和更新
     *
     * @param marketingCase 门店营销活动实例
     * @return 返回Result
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result saveAndUpdate(@RequestBody MarketingCase marketingCase) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer serviceNum = marketingCase.getServiceNum();
        List<MarketingCaseService> serviceInfos = marketingCase.getServiceInfos();
        if (null == marketingCase.getTemplateId()) {
            return Result.wrapErrorResult("9999", "当前营销活动已下架");
        }
        if (serviceInfos.size() > serviceNum) {
            return Result.wrapErrorResult("9999", "营销活动服务项目数量超出" + serviceNum + "条,请核对信息");
        }

        String url = createTemplateUrl(marketingCase,userInfo);

        int num = 0;
        if (null != marketingCase.getId()) {
            String imgUrl = createRedisData(MARKETING_TEMPLATE_CASE_PREFIX + marketingCase.getId(), url);
            marketingCase.setImgUrl(imgUrl);
            marketingCase.setTemplateUrl(url);
            num = marketingActivityService.update(marketingCase, userInfo);
        } else {
            String imgUrl = createRedisData(MARKETING_TEMPLATE_PREFIX + marketingCase.getTemplateId(), url);
            marketingCase.setImgUrl(imgUrl);
            marketingCase.setTemplateUrl(url);
            num = marketingActivityService.save(marketingCase, userInfo);
        }
        if (num > 0) {
            return Result.wrapSuccessfulResult(marketingCase);
        } else {
            return Result.wrapErrorResult("9999", "保存失败");
        }
    }

    /**
     * 保存门店活动二维码在redis
     *
     * @param marketingId 门店活动id
     * @param url         门店活动URL
     */
    private String createRedisData(String marketingId, String url) {

        ByteArrayOutputStream bs = null;
        String filePath = "";
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            byte[] redis = jedis.get(url.getBytes("UTF-8"));
            filePath = (String) SerializeUtil.unserialize(redis);
            if (!StringUtils.isBlank(filePath)) {
                return filePath;
            }
            logger.info("门店活动生成二维码URL为：" + url);
            bs = CodeGeneration.create(url);
            String fileSuffix = "jpg";
            //上传原图
            String fileKey = ObjectKeyUtil.generateOrigObjectkey(fileSuffix);
            //是否测试环境
            if ("Y".equals(ossClientUtil.getIsTest())){
                fileKey = OSSConstants.TEST_OSS_URL + fileKey;
            }else {
                fileKey = OSSConstants.IMG_OSS_URL + fileKey;
            }
            filePath = ossClientUtil.putObject(tqmallBucketName,fileKey,bs.toByteArray());
            logger.info("在门店活动模板编辑时模板路径为：" + url + "生成二维码存放路径为：" + filePath);

            jedis.setex(url.getBytes("UTF-8"), CacheConstants.MARKETING_TEMPLATE_URL_KEY_EXP_TIME, SerializeUtil.serialize(filePath));
        } catch (WriterException e) {
            logger.error("在门店活动模板编辑时模板路径为：" + url + "生成二维码出错，错误信息为：" + e);
        } catch (UnsupportedEncodingException e) {
            logger.error("通过门店活动实例id为：" + marketingId + "二维码存放路径为：" + filePath + "信息存放在redis错误,错误信息", e);
        } catch (IOException e) {
            logger.error("在门店活动模板编辑时模板路径为：" + url + "生成二维码字节数组出错，错误信息为：" + e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
        return filePath;
    }


}
