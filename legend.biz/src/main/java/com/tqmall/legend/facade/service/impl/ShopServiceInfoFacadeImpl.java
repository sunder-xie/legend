package com.tqmall.legend.facade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceInfoDTO;
import com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceCateService;
import com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceInfoService;
import com.tqmall.legend.biz.account.CardServiceRelService;
import com.tqmall.legend.biz.account.ComboInfoServiceRelService;
import com.tqmall.legend.biz.account.CouponServiceRelService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.AppointServiceService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.*;
import com.tqmall.legend.biz.shop.bo.ServiceStatisBo;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.CardServiceRel;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import com.tqmall.legend.entity.account.CouponServiceRel;
import com.tqmall.legend.entity.activity.EditStatusEnum;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.shop.*;
import com.tqmall.legend.enums.service.ShareServiceEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoAppPublishStatusEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoFlagsEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoStatusEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.magic.vo.ProxyServicesVo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.service.ShopServiceStatisAttachProcessor;
import com.tqmall.legend.facade.service.vo.AppServiceVo;
import com.tqmall.legend.facade.service.vo.SaveShopServiceInfoVo;
import com.tqmall.legend.facade.service.vo.ShopServiceInfoVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.shopservice.param.LegendShopServiceParam;
import com.tqmall.search.dubbo.client.legend.shopservice.result.LegendShopServiceDTO;
import com.tqmall.search.dubbo.client.legend.shopservice.service.LegendShopServiceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by zsy on 16/5/18.
 */
@Service
@Slf4j
public class ShopServiceInfoFacadeImpl implements ShopServiceInfoFacade {
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private CarLevelService carLevelService;
    @Value("${i.search.url}")
    private String iSearchUrl;
    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private SnFactory snFactory;
    @Autowired
    private ServiceTemplateCateRelService serviceTemplateCateRelService;
    @Autowired
    private AppointServiceService appointServiceService;
    @Autowired
    private RpcShopServiceInfoService rpcShopServiceInfoService;
    @Autowired
    private ComboInfoServiceRelService comboInfoServiceRelService;
    @Autowired
    private CouponServiceRelService couponServiceRelService;
    @Autowired
    private RpcShopServiceCateService rpcShopServiceCateService;
    @Autowired
    private CardServiceRelService cardServiceRelService;


    @Override
    public Result<List<ShopServiceInfo>> getSameServiceInfo(Long serviceId, Long shopId) {
        ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(serviceId);
        if (shopServiceInfo == null) {
            return Result.wrapErrorResult(LegendErrorCode.SERVICE_NOT_EXSIT.getCode(), LegendErrorCode.SERVICE_NOT_EXSIT.getErrorMessage());
        }
        List<ShopServiceInfo> shopServiceInfoList = Lists.newArrayList();
        Long parentId = shopServiceInfo.getParentId();
        if (Long.compare(parentId, 0) == 0) {
            shopServiceInfoList.add(shopServiceInfo);
            return Result.wrapSuccessfulResult(shopServiceInfoList);
        }
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("parentId", parentId);
        searchMap.put("categoryId", shopServiceInfo.getCategoryId());
        shopServiceInfoList = shopServiceInfoService.select(searchMap);
        return Result.wrapSuccessfulResult(shopServiceInfoList);
    }

    @Override
    public ShopServiceInfo save(SaveShopServiceInfoVo saveShopServiceInfoVo, UserInfo userInfo) {
        if (userInfo == null || saveShopServiceInfoVo == null || userInfo.getShopId() == null) {
            throw new BizException("有必传参数为空");
        }
        //.查询服务模版是否存在
        Long serviceTplId = saveShopServiceInfoVo.getServiceTplId();
        ServiceTemplate serviceTemplate = null;
        if (serviceTplId != null) {
            serviceTemplate = serviceTemplateService.selectById(serviceTplId);
            if (serviceTemplate == null || serviceTemplate.getStatus() == null || serviceTemplate.getStatus() == -1) {
                throw new BizException("服务模版不存在或无效");
            }
        }
        Long serviceId = saveShopServiceInfoVo.getShopServiceId();
        Long shopId = userInfo.getShopId();
        //.获取服务实例
        ShopServiceInfo shopServiceInfo = null;
        if (serviceId != null) {
            shopServiceInfo = shopServiceInfoService.selectById(serviceId, shopId);
            if (shopServiceInfo == null || shopServiceInfo.getStatus() == null) {
                throw new BizException("门店服务实体不存在或服务状态status为空");
            }
            if (shopServiceInfo.getStatus() != ServiceInfoStatusEnum.VALID.getCode()) {
                //.新增一个服务
                shopServiceInfo = _copyFromTpl(userInfo, serviceTemplate);
            }
        } else {
            //.查询服务模版对应的服务实体是否已经生成
            shopServiceInfo = shopServiceInfoService.getByTplId(shopId, serviceTplId, ServiceInfoStatusEnum.VALID.getCode());
        }
        if (shopServiceInfo == null) {
            //.新增一个服务
            shopServiceInfo = _copyFromTpl(userInfo, serviceTemplate);
        }
        //.编辑服务
        if (serviceTemplate != null && serviceTemplate.getEditStatus() != null && serviceTemplate.getEditStatus() == 1) {
            //可编辑价格
            shopServiceInfo.setServicePrice(saveShopServiceInfoVo.getServicePrice());
            shopServiceInfo.setGmtModified(new Date());
        }
        shopServiceInfo.setMarketPrice(saveShopServiceInfoVo.getMarketPrice());
        shopServiceInfo.setDownPayment(saveShopServiceInfoVo.getDownPayment());
        //.保存服务
        if (shopServiceInfo.getId() == null) {
            shopServiceInfoService.add(shopServiceInfo, userInfo);
        } else {
            shopServiceInfo.setModifier(userInfo.getUserId());
            shopServiceInfoService.update(shopServiceInfo);
        }
        if (shopServiceInfo == null || shopServiceInfo.getId() == null) {
            throw new BizException("保存失败");
        }
        return shopServiceInfo;
    }

    /**
     * @param userInfo
     * @param serviceTemplate
     * @return
     */
    private ShopServiceInfo _copyFromTpl(UserInfo userInfo, ServiceTemplate serviceTemplate) {
        if (serviceTemplate == null) {
            throw new BizException("服务模版不能为空");
        }
        Assert.notNull(userInfo, "操作员信息不能为空");
        Assert.notNull(userInfo.getShopId(), "操作员的门店id不能为空");
        Assert.notNull(serviceTemplate, "服务模版不能为空");
        Assert.notNull(serviceTemplate.getId(), "服务模版id不能为空");
        ShopServiceInfo shopServiceInfo = new ShopServiceInfo();
        BeanUtils.copyProperties(serviceTemplate, shopServiceInfo);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        shopServiceInfo.setShopId(shopId);
        shopServiceInfo.setType(1);//常规服务
        shopServiceInfo.setAuditStatus(1);//1审核通过,不需要审核
        shopServiceInfo.setCategoryId(serviceTemplate.getCateId());
        shopServiceInfo.setCreator(userId);
        shopServiceInfo.setModifier(userId);
        shopServiceInfo.setGmtCreate(new Date());
        shopServiceInfo.setGmtModified(new Date());
        shopServiceInfo.setServiceSn(snFactory.generateSn(SnFactory.SERVICE_SN_INCREMENT, shopId, SnFactory.SERVICE));
        shopServiceInfo.setParentId(serviceTemplate.getId());
        shopServiceInfo.setAppPublishStatus(serviceTemplate.getAppPublishStatus());
        shopServiceInfo.setEditStatus(serviceTemplate.getEditStatus());
        shopServiceInfo.setDeleteStatus(serviceTemplate.getDeleteStatus());
        shopServiceInfo.setId(null);
        return shopServiceInfo;
    }

    /**
     * 钣喷中心默认选择委托服务查询接口
     *
     * @param shopId
     * @param proxyServicesVo
     * @return
     */
    @Override
    public OrderServices getOrderServicesByProxyServices(Long shopId, ProxyServicesVo proxyServicesVo) {
        if (shopId == null || proxyServicesVo == null) {
            return null;
        }
        String serviceName = proxyServicesVo.getServiceName();
        String serviceType = proxyServicesVo.getServiceType();
        BigDecimal sharePrice = proxyServicesVo.getSharePrice();
        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(serviceType) || sharePrice == null) {
            return null;
        }
        //查询类别
        Map<String, Object> cateMap = Maps.newHashMap();
        cateMap.put("nameLike", serviceType);
        cateMap.put("shopId", shopId);
        List<ShopServiceCate> shopServiceCateList = shopServiceCateService.select(cateMap);
        if (CollectionUtils.isNotEmpty(shopServiceCateList)) {
            List<Long> cateIds = Lists.newArrayList();
            for (ShopServiceCate shopServiceCate : shopServiceCateList) {
                cateIds.add(shopServiceCate.getId());
            }
            //查询服务、类别相同的门店服务
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("name", serviceName);
            searchMap.put("shopId", shopId);
            searchMap.put("categoryIds", cateIds);
            searchMap.put("sharePrice", sharePrice);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(searchMap);
            if (CollectionUtils.isNotEmpty(shopServiceInfoList)) {
                ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                OrderServices orderServices = new OrderServices();
                orderServices.setServiceName(shopServiceInfo.getName());
                orderServices.setServiceId(shopServiceInfo.getId());
                orderServices.setServiceSn(shopServiceInfo.getServiceSn());
                orderServices.setFlags(shopServiceInfo.getFlags());
                orderServices.setType(shopServiceInfo.getType());
                orderServices.setServiceCatName(serviceType);
                orderServices.setServiceCatId(shopServiceInfo.getCategoryId());
                BigDecimal serviceHour = proxyServicesVo.getServiceHour();
                if (serviceHour == null) {
                    serviceHour = BigDecimal.ZERO;
                }
                orderServices.setServicePrice(sharePrice);
                orderServices.setServiceHour(serviceHour);
                orderServices.setServiceAmount(sharePrice.multiply(serviceHour));
                orderServices.setDiscount(BigDecimal.ZERO);
                orderServices.setServiceNote(shopServiceInfo.getServiceNote());
                return orderServices;
            }
        }
        return null;
    }

    /**
     * 初始化钣喷服务
     *
     * @param shopId
     * @param userId
     */
    @Override
    @Transactional
    public void initBpService(Long shopId, Long userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("flags", ServiceFlagsEnum.BPFW.getFlags());
        param.put("status", 0);//有效 TODO 枚举处理
        List<ServiceTemplate> serviceTemplates = serviceTemplateService.select(param);
        if (!org.springframework.util.CollectionUtils.isEmpty(serviceTemplates)) {
            Map<String, ServiceTemplate> templateMap = new HashMap<>();
            for (ServiceTemplate serviceTemplate : serviceTemplates) {
                templateMap.put(serviceTemplate.getName(), serviceTemplate);
            }
            //初始化门店服务车辆级别
            CarLevel carLevel = carLevelService.getCarLevelByShopIdAndName(shopId, "通用");
            Long carLevelId = carLevel.getId();
            //需要插入的数据
            List<ShopServiceInfo> shopServiceInfos = new ArrayList<>();
            ShareServiceEnum[] shareServiceEnums = ShareServiceEnum.getMessages();
            for (ShareServiceEnum shareServiceEnum : shareServiceEnums) {
                //获取模板服务信息
                String serviceName = shareServiceEnum.getServiceName();
                if (templateMap.containsKey(serviceName)) {
                    ServiceTemplate serviceTemplate = templateMap.get(serviceName);
                    //初始化门店的服务类别
                    String cateName = shareServiceEnum.getCateName();
                    List<ShopServiceCate> shopServiceCates = getShopServiceCate(shopId, cateName);
                    ShopServiceCate shopServiceCate;
                    if (org.springframework.util.CollectionUtils.isEmpty(shopServiceCates)) {
                        shopServiceCate = addShopServiceCate(shopId, userId, cateName);
                    } else {
                        shopServiceCate = shopServiceCates.get(0);
                    }
                    Long categoryId = shopServiceCate.getId();
                    String[] sharePrices = shareServiceEnum.getSharePrices();
                    BigDecimal surfaceNum = shareServiceEnum.getSurfaceNum();
                    for (String sharePrice : sharePrices) {
                        //数据不存在则添加
                        Map<String, Object> map = new HashMap<>();
                        map.put("categoryId", categoryId);
                        map.put("parentId", serviceTemplate.getId());
                        map.put("shopId", shopId);
                        map.put("sharePrice", sharePrice);
                        Integer count = shopServiceInfoService.selectCount(map);
                        if (count == 0) {
                            ShopServiceInfo shopServiceInfo = new ShopServiceInfo();
                            buildShopServiceInfo(shopServiceInfo, serviceTemplate, shopId, userId, categoryId);
                            shopServiceInfo.setSharePrice(new BigDecimal(sharePrice));
                            shopServiceInfo.setServicePrice(new BigDecimal(sharePrice));
                            shopServiceInfo.setCarLevelId(carLevelId);
                            shopServiceInfo.setSurfaceNum(surfaceNum);
                            shopServiceInfos.add(shopServiceInfo);
                        }
                    }
                }
            }
            shopServiceInfoService.batchInsert(shopServiceInfos);
        }
    }

    @Override
    public String getShopService(String serviceSn, String serviceName, String type, String suiteNumLT, Long categoryId, Long shopId) {
        StringBuffer requestParam = new StringBuffer("1=1");
        if (shopId != null && shopId != 0) {
            requestParam.append("&shopId=");
            requestParam.append(shopId);
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(serviceSn)) {
            requestParam.append("&serviceSn=");
            requestParam.append(serviceSn);
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(serviceName)) {
            try {
                String serviceNameTran = URLEncoder.encode(serviceName, "utf-8");
                requestParam.append("&serviceName=");
                requestParam.append(serviceNameTran);
            } catch (UnsupportedEncodingException e) {
                log.error("服务搜素接口，urlencode异常", e);
            }
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(type)) {
            requestParam.append("&type=");
            requestParam.append(type);
        }
        if (categoryId != null) {
            requestParam.append("&categoryId=");
            requestParam.append(categoryId);
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(suiteNumLT)) {
            requestParam.append("&suiteNumLT=");
            requestParam.append(suiteNumLT);
        }
        requestParam.append("&status=0,-2");//0正常-1TQFW下架-2 CZFW下架
        requestParam.append("&size=50");
        // iSearchUrl
        String requestUrl = iSearchUrl + "elasticsearch/cloudRepair/legendShopServer/shopServer";
        log.info("requestUrl:{}", requestUrl);
        log.info("requestParam:{}", requestParam.toString());

        // 相应实体
        JSONObject respJSON = new JSONObject();
        try {
            String result = HttpUtil.sendGet(requestUrl, requestParam.toString(), 1000, 60000);
            JSONObject reqSearchResult = JSON.parseObject(result);
            JSONObject reqSearchResponse = reqSearchResult.getJSONObject("response");
            JSONArray reqSearchList = reqSearchResponse.getJSONArray("list");
            respJSON.put("success", true);
            respJSON.put("data", reqSearchList);
        } catch (Exception e) {
            log.error("调用搜索服务，查询服务项目失败！，异常信息：{}", e);
        }
        return respJSON.toString();
    }

    @Override
    public List<ShopServiceInfo> getShopServiceInfoByCateTag(Long shopId, Integer cateTag, String nameLike) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("cateTag", cateTag);
        searchMap.put("serviceNameLike", nameLike);
        searchMap.put("statuss", new String[]{"0", "-2"});
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(searchMap);
        List<ShopServiceInfo> serviceInfoList = Lists.newArrayList();
        //过滤洗车服务
        List<ShopServiceInfo> carWashList = shopServiceInfoService.getBZCarWashList(shopId);
        Map<Long, ShopServiceInfo> carWashMap = Maps.newHashMap();
        for (ShopServiceInfo shopServiceInfo : carWashList) {
            Long id = shopServiceInfo.getId();
            carWashMap.put(id, shopServiceInfo);
        }
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            Long id = shopServiceInfo.getId();
            if (!carWashMap.containsKey(id)) {
                serviceInfoList.add(shopServiceInfo);
            }
        }
        return serviceInfoList;
    }

    /**
     * 创建门店服务
     *
     * @param item
     * @param serviceTemplate
     * @param shopId
     * @param userId
     */
    private void buildShopServiceInfo(ShopServiceInfo item, ServiceTemplate serviceTemplate, Long shopId, Long userId, Long cateId) {
        Long parentId = serviceTemplate.getId();
        //模板信息
        item.setName(serviceTemplate.getName());
        item.setServiceSn(serviceTemplate.getServiceSn());
        item.setServicePrice(serviceTemplate.getServicePrice());
        item.setFlags(serviceTemplate.getFlags());
        item.setServiceNote(serviceTemplate.getServiceNote());
        item.setStatus(Long.valueOf(serviceTemplate.getStatus()));
        item.setImgUrl(serviceTemplate.getImgUrl());
        item.setThirdImgUrl(serviceTemplate.getThirdImgUrl());
        item.setThirdServiceInfo(serviceTemplate.getThirdServiceInfo());
        item.setSort(Long.valueOf(serviceTemplate.getSort()));
        item.setPriceType(Long.valueOf(serviceTemplate.getPriceType()));
        item.setServiceInfo(serviceTemplate.getServiceInfo());
        if (cateId == null) {
            item.setCategoryId(serviceTemplate.getCateId());
        } else {
            item.setCategoryId(cateId);
        }
        item.setCateTag(serviceTemplate.getCateTag());
        //门店服务自己的字段信息
        item.setParentId(parentId);
        item.setShopId(shopId);
        item.setCreator(userId);
        item.setType(1);//常规服务
        item.setEditStatus(serviceTemplate.getEditStatus());
        item.setAppPublishStatus(serviceTemplate.getAppPublishStatus());
        item.setDeleteStatus(serviceTemplate.getDeleteStatus());
    }

    /**
     * 获取门店服务类别
     *
     * @param shopId
     * @param name
     * @return
     */
    private List<ShopServiceCate> getShopServiceCate(Long shopId, String name) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("shopId", shopId);
        param.put("cateType", 0);
        return shopServiceCateService.selectNoCache(param);
    }

    /**
     * 新增门店服务类别
     *
     * @param shopId
     * @param userId
     * @param name
     * @return
     */
    private ShopServiceCate addShopServiceCate(Long shopId, Long userId, String name) {
        ShopServiceCate shopServiceCate = new ShopServiceCate();
        shopServiceCate.setName(name);
        shopServiceCate.setShopId(shopId);
        shopServiceCate.setCateType(0);
        shopServiceCate.setCateTag(5);
        shopServiceCate.setCreator(userId);
        shopServiceCateService.insert(shopServiceCate);
        return shopServiceCate;
    }

    private Map<String, Object> _getAppServiceQryCondition(Long shopId, Long parentAppCateId, Integer isRecommend, Integer appPublishStatus, Integer limit, Long offset) {
        Assert.notNull(appPublishStatus, "查询条件车主服务发布状态不能为空");
        Map<String, Object> qryServiceInfoParam = new HashMap<>();
        qryServiceInfoParam.put("shopId", shopId);
        qryServiceInfoParam.put("isRecommend", isRecommend);
        qryServiceInfoParam.put("appPublishStatus", appPublishStatus);//发布车主服务状态:0未发布,1已发布
        qryServiceInfoParam.put("sorts", new String[]{"sort asc"});//发布车主服务状态:0未发布,1已发布
        if (appPublishStatus.intValue() == ServiceInfoAppPublishStatusEnum.PUBLISHED.getCode()) {
            //查询已发布的服务时才需要status=0有效
            qryServiceInfoParam.put("status", 0);
        } else {
            Set<String> flages = new HashSet<String>() {{
                add(ServiceFlagsEnum.CZFW.getFlags());
                add(ServiceFlagsEnum.BZFW.getFlags());
            }};
            qryServiceInfoParam.put("flagsList", flages);
        }
        qryServiceInfoParam.put("limit", limit);
        qryServiceInfoParam.put("offset", offset);
        if (parentAppCateId != null) {
            Map<String, Object> qryServiceCateParam = new HashMap<>();
            qryServiceCateParam.put("parentId", parentAppCateId);
            qryServiceCateParam.put("cateType", 1);//0门店服务，1淘汽、车主服务，2，标准服务类别
            List<ShopServiceCate> shopServiceCateList = shopServiceCateService.selectNoCache(qryServiceCateParam);
            Set<Long> appCateIds = new HashSet<>();
            if (CollectionUtils.isEmpty(shopServiceCateList)) {
                qryServiceInfoParam.put("appCateIds", appCateIds);
                return qryServiceInfoParam;
            }
            for (ShopServiceCate shopServiceCate : shopServiceCateList) {
                appCateIds.add(shopServiceCate.getId());
            }
            qryServiceInfoParam.put("appCateIds", appCateIds);
        }
        return qryServiceInfoParam;

    }

    @Override
    public List<AppServiceVo> getPublishedAppService(Long shopId, Long parentAppCateId, Integer isRecommend) {
        List<AppServiceVo> appServiceVoList = new ArrayList<>();
        Map<String, Object> qryServiceInfoParam = _getAppServiceQryCondition(shopId, parentAppCateId, isRecommend, 1, null, null);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryServiceInfoParam);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return appServiceVoList;
        }
        //服务套餐价格处理
        setSuitServicePrice(shopServiceInfoList);
        //预约数量处理
        List<Long> serviceIds = Lists.transform(shopServiceInfoList, new Function<ShopServiceInfo, Long>() {
            @Override
            public Long apply(ShopServiceInfo shopServiceInfo) {
                return shopServiceInfo.getId();
            }
        });
        Map<Long, Integer> serviceAppointCountMap = appointFacade.getAppointCount(serviceIds.toArray(new Long[] { }));
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            AppServiceVo appServiceVo = new AppServiceVo();
            BeanUtils.copyProperties(shopServiceInfo, appServiceVo);
            Integer appointCount = serviceAppointCountMap.get(shopServiceInfo.getId()) == null ? 0 : serviceAppointCountMap.get(shopServiceInfo.getId());
            appServiceVo.setAppointCount(appointCount);
            appServiceVoList.add(appServiceVo);
        }
        return appServiceVoList;
    }

    /**
     * 服务套餐价格处理
     *
     * @param shopServiceInfoList
     */
    public void setSuitServicePrice(List<ShopServiceInfo> shopServiceInfoList) {
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return;
        }
        Set<Long> suitIds = new HashSet<>();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum().longValue() > 0) {
                suitIds.add(shopServiceInfo.getId());
            }
        }
        List<ServiceGoodsSuite> serviceGoodsSuiteList = serviceGoodsSuiteService.selectByServiceIds(suitIds.toArray(new Long[]{}));
        Map<Long, BigDecimal> suitPriceMap = new HashMap<>();//key:servcieId,value:suitePrice
        if (!CollectionUtils.isEmpty(serviceGoodsSuiteList)) {
            for (ServiceGoodsSuite serviceGoodsSuite : serviceGoodsSuiteList) {
                suitPriceMap.put(serviceGoodsSuite.getServiceId(), serviceGoodsSuite.getSuitePrice());
            }
        }
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum().longValue() > 0) {
                shopServiceInfo.setServicePrice(suitPriceMap.get(shopServiceInfo.getId()));
            }
        }
    }

    @Override
    public Page<AppServiceVo> getPrepublishAppServicePage(Long shopId, Long parentAppCateId, Integer isRecommend, Integer limit, Long offset) {
        PageRequest pageRequest = new PageRequest(offset.intValue() / limit, limit);
        Page<AppServiceVo> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        Map<String, Object> qryServiceInfoParam = _getAppServiceQryCondition(shopId, parentAppCateId, isRecommend, 0, limit, offset);
        int count = shopServiceInfoService.selectCountAllStatus(qryServiceInfoParam);
        if (count < 1) {
            return page;
        }
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryServiceInfoParam);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return page;
        }
        //.套餐服务价格处理
        setSuitServicePrice(shopServiceInfoList);
        List<AppServiceVo> appServiceVoList = new ArrayList<>();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            AppServiceVo appServiceVo = new AppServiceVo();
            BeanUtils.copyProperties(shopServiceInfo, appServiceVo);
            appServiceVoList.add(appServiceVo);
        }
        page = new DefaultPage(appServiceVoList, pageRequest, count);
        return page;
    }

    @Override
    public boolean cancelPublishAppService(UserInfo userInfo, Long serviceId) {
        log.info("取消将服务发布到车主端,入参userInfo:{},serviceId:{}", userInfo, serviceId);
        ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(serviceId, userInfo.getShopId());
        if (shopServiceInfo == null) {
            log.error("取消将服务发布到车主端serviceId:{},shopId:{}查询不到对应服务", serviceId, userInfo.getShopId());
            throw new BizException("对应定的服务不存在");
        }
        shopServiceInfo.setAppPublishStatus(0);//发布车主服务状态:0未发布,1已发布
        int result = shopServiceInfoService.updateById(shopServiceInfo, userInfo);
        if (result < 1) {
            throw new BizException("保存服务失败");
        }
        return true;
    }

    @Override
    @Transactional
    public String saveAppServiceListAndGetViewUrl(List<AppServiceVo> appServiceVoList, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        _saveAppServiceList(appServiceVoList, userInfo);
        com.tqmall.core.common.entity.Result<String> viewUrlResult = weChatShopService.getShopdetailUrl(userGlobalId);
        log.info("[dubbo]调用ddl-wechat活着门店微信公众号主页url,入參:{},success:{}", userGlobalId, viewUrlResult.isSuccess());
        return viewUrlResult.getData();
    }

    private void _saveAppServiceList(List<AppServiceVo> appServiceVoList, UserInfo userInfo) {
        if (CollectionUtils.isEmpty(appServiceVoList)) {
            return;
        }
        Long shopId = userInfo.getShopId();
        Set<Long> serviceIds = new HashSet<>();
        for (AppServiceVo appServiceVo : appServiceVoList) {
            serviceIds.add(appServiceVo.getId());
        }
        log.info("保存车主服务列表并调ddl-wechat接口获取预览地址,入參servcieIds:{},userInfo:{}", serviceIds, LogUtils.objectToString(userInfo));
        if (CollectionUtils.isEmpty(serviceIds)) {
            throw new BizException("需要保存的服务列表为空");
        }
        Map<String, Object> qryServcieInfoParam = new HashMap<>();
        qryServcieInfoParam.put("ids", serviceIds);
        qryServcieInfoParam.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(qryServcieInfoParam);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            throw new BizException("查询不到对应的服务列表");
        }
        ImmutableMap<Long, ShopServiceInfo> shopServiceInfoMap = Maps.uniqueIndex(shopServiceInfoList, new Function<ShopServiceInfo, Long>() {
            @Override
            public Long apply(ShopServiceInfo shopServiceInfo) {
                return shopServiceInfo.getId();
            }
        });
        for (AppServiceVo appServiceVo : appServiceVoList) {
            ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(appServiceVo.getId());
            if (shopServiceInfo == null) {
                log.info("更新服务信息,serviceId:{}没有对应的服务", appServiceVo.getId());
                throw new BizException("没有对应的服务");
            }
            BeanUtils.copyProperties(appServiceVo, shopServiceInfo);
            shopServiceInfo.setFlags(ServiceInfoFlagsEnum.CZFW.getCode());
            if (appServiceVo.getAppPublishStatus() != null && appServiceVo.getAppPublishStatus() == 1) {//发布车主服务状态:0未发布,1已发布
                //发布车主服务
                shopServiceInfo.setStatus(0l);//状态 0有效 -1 TQFW下架 -2 CZFW下架
            }
            shopServiceInfoService.updateById(shopServiceInfo, userInfo);
            log.info("更新车主服务,shopServiceInfo:{},userInfo:{}", LogUtils.objectToString(shopServiceInfo), LogUtils.objectToString(userInfo));
        }
    }

    @Override
    public List<ServiceStatisBo> getServiceStatisBase(Long shopId, final Long catId) {

        Map<Long, Long> secondCateMap = shopServiceCateService.getSecondCate();

        if (catId != null) {
            Map<Long, Long> secondCateMapForSomeCat = Maps.filterEntries(secondCateMap, new Predicate<Map.Entry<Long, Long>>() {
                @Override
                public boolean apply(Map.Entry<Long, Long> input) {
                    return input.getValue().equals(catId);
                }
            });
            secondCateMap = secondCateMapForSomeCat;
        }
        Set<Long> catIdSet = secondCateMap.keySet();

        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectByCatIds(catIdSet);

        if (shopId != null) {
            List<ShopServiceInfo> serviceInfoListOfShop = Lists.newArrayList();
            for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                if (shopServiceInfo.getShopId().equals(shopId)) {
                    serviceInfoListOfShop.add(shopServiceInfo);
                }
            }
            shopServiceInfoList = serviceInfoListOfShop;
        }

        final Map<Long, Long> finalSecondCateMap = secondCateMap;

        //过滤c端不需要的门店相关的数据
        final Map<Long, Shop> allShopMap = shopService.getAllShopMap();
        List<ShopServiceInfo> filtedShopServiceInfoList = Lists.newArrayList();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            Long shopId1 = shopServiceInfo.getShopId();
            if (allShopMap.containsKey(shopId1)) {
                if (!allShopMap.get(shopId1).getUserGlobalId().equals("0")) {
                    filtedShopServiceInfoList.add(shopServiceInfo);
                }
            }
        }

        List<ServiceStatisBo> serviceStatisBoList = Lists.newArrayList(Lists.transform(filtedShopServiceInfoList, new Function<ShopServiceInfo, ServiceStatisBo>() {
            @Override
            public ServiceStatisBo apply(ShopServiceInfo input) {
                ServiceStatisBo serviceStatisBo = new ServiceStatisBo();
                long catId = input.getAppCateId().longValue();
                serviceStatisBo.setCatId(catId);
                serviceStatisBo.setServiceId(input.getId());
                serviceStatisBo.setShopId(input.getShopId());
                serviceStatisBo.setServiceName(input.getName());
                serviceStatisBo.setTopCatId(finalSecondCateMap.get(catId));
                return serviceStatisBo;
            }
        }));
        return serviceStatisBoList;
    }

    @Override
    public List<ServiceStatisBo> attachPointUsages(List<ServiceStatisBo> serviceStatisBoList) {
        return attachInfos(serviceStatisBoList, new ShopServiceStatisAttachProcessor() {
            @Override
            public void attach(Collection<Long> serviceIds, List<ServiceStatisBo> serviceStatisBoList) {
                Long[] serviceIdArray = serviceIds.toArray(new Long[serviceIds.size()]);
                Map<Long, Integer> appointCountMap = appointFacade.getAppointCount(serviceIdArray);
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList) {
                    Long serviceId = serviceStatisBo.getServiceId();
                    Integer serviceUsageCount = appointCountMap.get(serviceId);
                    if (serviceUsageCount != null) {
                        serviceStatisBo.setUsageInPoints(serviceUsageCount);
                    }
                }
            }
        });

    }

    private List<ServiceStatisBo> attachInfos(List<ServiceStatisBo> serviceStatisBoList, ShopServiceStatisAttachProcessor attachProcessor) {
        List<Long> serviceIdList = Lists.transform(serviceStatisBoList, new Function<ServiceStatisBo, Long>() {
            @Override
            public Long apply(ServiceStatisBo input) {
                return input.getServiceId();
            }
        });

        if (CollectionUtils.isEmpty(serviceIdList)) {
            return Lists.newArrayList();
        }

        for (int index = 0, size = serviceIdList.size(); index < size; index += 50) {
            int endIndex = index + 50;
            endIndex = endIndex > size ? size : endIndex;
            List<Long> serviceIdPartList = serviceIdList.subList(index, endIndex);
            attachProcessor.attach(serviceIdPartList, serviceStatisBoList);
        }

        return serviceStatisBoList;
    }

    @Override
    public List<ServiceStatisBo> attachOrderUsages(List<ServiceStatisBo> serviceStatisBoList) {
        return attachInfos(serviceStatisBoList, new ShopServiceStatisAttachProcessor() {
            @Override
            public void attach(Collection<Long> serviceIds, List<ServiceStatisBo> serviceStatisBoList) {
                Map<Long, Integer> serviceOrderUsageMap = orderServicesService.getServiceOrderUsageMap(serviceIds);
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList) {
                    Long serviceId = serviceStatisBo.getServiceId();
                    Integer serviceUsageCount = serviceOrderUsageMap.get(serviceId);
                    if (serviceUsageCount != null) {
                        serviceStatisBo.setUsageInOrders(serviceUsageCount);
                    }
                }
            }
        });
    }

    @Override
    public List<ServiceStatisBo> attachOrderConfrimUsages(List<ServiceStatisBo> serviceStatisBoList) {
        return attachInfos(serviceStatisBoList, new ShopServiceStatisAttachProcessor() {
            @Override
            public void attach(Collection<Long> serviceIds, List<ServiceStatisBo> serviceStatisBoList) {
                Map<Long, Integer> serviceConfirmedOrderMap = orderServicesService.getServiceConfirmedOrderMap(serviceIds);
                for (ServiceStatisBo serviceStatisBo : serviceStatisBoList) {
                    Long serviceId = serviceStatisBo.getServiceId();
                    Integer serviceUsageCount = serviceConfirmedOrderMap.get(serviceId);
                    if (serviceUsageCount != null) {
                        serviceStatisBo.setUsageInConfirmOrders(serviceUsageCount);
                    }
                }

            }
        });
    }

    @Override
    public void updateNormalService(List<ShopServiceInfo> shopServiceInfoList, UserInfo userInfo) {
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return;
        }
        //标准服务模版ids
        List<Long> serviceTplIdList = Lists.transform(shopServiceInfoList, new Function<ShopServiceInfo, Long>() {
            @Override
            public Long apply(ShopServiceInfo shopServiceInfo) {
                return shopServiceInfo.getParentId();
            }
        });
        Map<String, Object> qryServiceCateRelParam = new HashMap<>();
        qryServiceCateRelParam.put("templateIds", serviceTplIdList);
        Map<Long, List<ServiceTemplateCateRel>> serviceTemplateCateRelMap = serviceTemplateCateRelService.getServiceTemplateCateRel(qryServiceCateRelParam);//key:templateId
        //查询所有的车主服务类型
        Map<String, Object> qryServiceCateParam = new HashMap<>();
        qryServiceCateParam.put("cateType", 1);//0门店服务，1淘汽、车主服务，2，标准服务类别
        List<ShopServiceCate> shopServiceCateList = shopServiceCateService.selectNoCache(qryServiceCateParam);
        Set<Long> appCateIds = new HashSet<>();//所有的车主服务类型集合
        if (!org.springframework.util.CollectionUtils.isEmpty(shopServiceCateList)) {
            for (ShopServiceCate shopServiceCate : shopServiceCateList) {
                appCateIds.add(shopServiceCate.getId());
            }
        }
        Long shopId = userInfo.getShopId();
        //查询门店标准服务
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("shopId", shopId);
        searchMap.put("searchBZFW", 0);
        List<ShopServiceInfo> existServiceList = shopServiceInfoService.select(searchMap);
        Map<Long, ShopServiceInfo> existServiceMap = new HashMap<>();//key:serviceTplId
        for (ShopServiceInfo shopServiceInfo : existServiceList) {
            existServiceMap.put(shopServiceInfo.getParentId(), shopServiceInfo);
        }
        //筛选需要添加是数据和更新价格的数据
        List<ShopServiceInfo> insertList = new ArrayList<>();
        for (ShopServiceInfo shopServiceInfoInput : shopServiceInfoList) {
            Long serviceTplId = shopServiceInfoInput.getParentId();
            BigDecimal servicePrice = shopServiceInfoInput.getServicePrice();
            //服务存在则更新，否则添加
            ShopServiceInfo existShopServiceInfo = existServiceMap.get(serviceTplId);
            if (existShopServiceInfo != null) {
                BigDecimal sp = existShopServiceInfo.getServicePrice();
                if (sp.compareTo(servicePrice) != 0) {//金额有变化才需要更新
                    existShopServiceInfo.setServicePrice(servicePrice);
                    //从服务模版上获取appCateId
                    setAppCateId(serviceTemplateCateRelMap, appCateIds, existShopServiceInfo);
                    existShopServiceInfo.setServiceNote(shopServiceInfoInput.getServiceNote());
                    existShopServiceInfo.setThirdServiceInfo(shopServiceInfoInput.getThirdServiceInfo());
                    log.info("更新标准服务：门店id{}，更新数据{}", shopId, existShopServiceInfo);
                    shopServiceInfoService.update(existShopServiceInfo);
                }
            } else {
                ShopServiceInfo shopServiceInfoNew = new ShopServiceInfo();
                shopServiceInfoNew.setShopId(shopId);
                shopServiceInfoNew.setName(shopServiceInfoInput.getName());
                shopServiceInfoNew.setFlags("BZFW");
                shopServiceInfoNew.setServicePrice(servicePrice);
                shopServiceInfoNew.setServiceSn(shopServiceInfoInput.getServiceSn());
                shopServiceInfoNew.setImgUrl(shopServiceInfoInput.getImgUrl());
                shopServiceInfoNew.setParentId(serviceTplId);
                shopServiceInfoNew.setCreator(userInfo.getUserId());
                shopServiceInfoNew.setCategoryId(shopServiceInfoInput.getCategoryId());
                shopServiceInfoNew.setCateTag(shopServiceInfoInput.getCateTag());
                shopServiceInfoNew.setType(1);//常规服务
                shopServiceInfoNew.setSort(shopServiceInfoInput.getSort());
                shopServiceInfoNew.setEditStatus(EditStatusEnum.PART_EDIT.getCode());
                //从服务模版上获取appCateId
                setAppCateId(serviceTemplateCateRelMap, appCateIds, shopServiceInfoNew);
                shopServiceInfoNew.setServiceNote(shopServiceInfoInput.getServiceNote());
                shopServiceInfoNew.setThirdServiceInfo(shopServiceInfoInput.getThirdServiceInfo());
                insertList.add(shopServiceInfoNew);
            }
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(insertList)) {
            Integer totalSize = shopServiceInfoService.initNormalShopServiceInfo(insertList);
            log.info("初始化标准服务:门店id为{},共添加{}条标准服务", shopId, totalSize);
        }
    }

    /**
     * 从服务模版关联的车主服务类别appCateId设置到服务实例上
     *
     * @param serviceTemplateCateRelMap
     * @param appCateIds
     * @param shopServiceInfo
     */
    private void setAppCateId(Map<Long, List<ServiceTemplateCateRel>> serviceTemplateCateRelMap, Set<Long> appCateIds, ShopServiceInfo shopServiceInfo) {
        List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelMap.get(shopServiceInfo.getParentId());
        if (!org.springframework.util.CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
            for (ServiceTemplateCateRel serviceTemplateCateRel : serviceTemplateCateRelList) {
                if (appCateIds.contains(serviceTemplateCateRel.getCateId())) {
                    shopServiceInfo.setAppCateId(serviceTemplateCateRel.getCateId() == null ? 0 : serviceTemplateCateRel.getCateId().intValue());
                    break;
                }
            }
        }
    }

    @Override
    public void deleteByIdAndShopId(long id, long shopId) {
        //如果服务已经被工单和预约单引用,或者已经发布不能删除
        //.工单引用
        Map<String,Object> servcieParam = new HashMap();
        servcieParam.put("shopId", shopId);
        servcieParam.put("serviceId", id);
        //.基本服务
        //.工单引用不能删除
        List<OrderServices> orderServicesList = orderServicesService.select(servcieParam);
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            throw new BizException("该服务已开工单不能删除");
        }
        //.预约单引用不能删除
        List<AppointServiceVo> appointServiceList = appointServiceService.select(servcieParam);
        if (!CollectionUtils.isEmpty(appointServiceList)) {
            throw new BizException("该服务已开预约单不能删除");
        }

        Map<String,Object> parentServcieParam = new HashMap();
        parentServcieParam.put("shopId", shopId);
        parentServcieParam.put("parentServiceId", id);

        //.套餐
        //.工单引用不能删除
        List<OrderServices> orderServicesSuiteList = orderServicesService.select(parentServcieParam);
        if (!org.springframework.util.CollectionUtils.isEmpty(orderServicesSuiteList)) {
            throw new BizException("该服务套餐已开工单不能删除");
        }
        //.预约单引用不能删除
        List<AppointServiceVo> appointServiceSuiteList = appointServiceService.select(parentServcieParam);
        if (!CollectionUtils.isEmpty(appointServiceSuiteList)) {
            throw new BizException("该服务套餐已开预约单不能删除");
        }

        //.被计次卡关联的服务不能删除
        List<ComboInfoServiceRel> comboInfoServiceRelList = comboInfoServiceRelService.findByServiceId(id);
        if (!CollectionUtils.isEmpty(comboInfoServiceRelList)) {
            throw new BizException("有计次卡关联此服务不能删除");
        }

        //.被优惠券关联的服务不能删除
        List<CouponServiceRel> couponServiceRelList = couponServiceRelService.selectByServiceId(id);
        if (!CollectionUtils.isEmpty(couponServiceRelList)) {
            throw new BizException("有优惠券关联此服务不能删除");
        }


        com.tqmall.core.common.entity.Result<ShopServiceInfoDTO> shopServiceInfoDTOResult = rpcShopServiceInfoService.selectById(shopId,id);
        if (!shopServiceInfoDTOResult.isSuccess()){
            throw new BizException(shopServiceInfoDTOResult.getMessage());
        }
        ShopServiceInfoDTO shopServiceInfoDTO = shopServiceInfoDTOResult.getData();
        if(shopServiceInfoDTO==null){
            throw new BizException("服务资料不存在");
        }
        String flags = shopServiceInfoDTO.getFlags();
        if (ServiceFlagsEnum.CZFW.getFlags().equals(flags)
                || ServiceFlagsEnum.TQFW.getFlags().equals(flags)
                || new Integer(1).equals(shopServiceInfoDTO.getAppPublishStatus())) {
            int status = shopServiceInfoDTO.getStatus();
            if (ServiceInfoStatusEnum.VALID.getCode() == status) {
                throw new BizException("服务已经发布到车主端不能删除");
            }
        }
        com.tqmall.core.common.entity.Result<Boolean> delResult = rpcShopServiceInfoService.deleteByIdAndShopId(id, shopId);
        if (!delResult.isSuccess() || !delResult.getData()) {
            throw new BizException(delResult.getMessage());
        }
    }

    /**
     * 校验重复，如名称、编号等
     * @param shopId
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean checkWithShopId(Long shopId, String key, String value) {
        Map map = new HashMap();
        map.put("shopId", shopId);
        map.put(key, value);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(map);
        if (org.springframework.util.CollectionUtils.isEmpty(shopServiceInfoList)) {
            return true;
        }
        return false;
    }

    @Override
    public List<ShopServiceInfo> selectServiceByIds(List<Long> ids) {
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectByIds(ids);
        return shopServiceInfoList;
    }

    @Override
    public void deleteShopServiceCate(long id, long shopId) {
        //.被会员卡引用的服务类别不能删除
        List<CardServiceRel> cardServiceRelList = cardServiceRelService.selectByServiceCateId(id);
        if (!CollectionUtils.isEmpty(cardServiceRelList)) {
            throw new BizException("有会员卡关联此服务类别不能删除");
        }
        com.tqmall.core.common.entity.Result<Boolean> result = rpcShopServiceCateService.delete(shopId, id);
        if (!result.isSuccess() || !result.getData()) {
            throw new BizException(result.getMessage());
        }
    }
}
