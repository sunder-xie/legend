package com.tqmall.legend.facade.activity.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.dandelion.wechat.client.dto.JoinActivityLimitDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActAppointmentDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActBuriedDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActDetailListDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActServiceDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActServiceListDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityDiscountCouponDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyShareDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyShareDetailDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyUserDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyUserDetailDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountCouponDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountCouponInfoDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountCouponUserDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountCouponUserInfoDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountServiceDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountServicesDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.GroupBuyServiceDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopActDiscountDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopActDiscountListDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopActivityPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopActivityPageModuleDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateActivityIdDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateActivityPageDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateActivityPageModuleDTO;
import com.tqmall.dandelion.wechat.client.param.wechat.ActPageModuleServiceParam;
import com.tqmall.dandelion.wechat.client.param.wechat.GroupBuyUserParam;
import com.tqmall.dandelion.wechat.client.param.wechat.ShopActParam;
import com.tqmall.dandelion.wechat.client.wechat.activity.WeChatActBuriedService;
import com.tqmall.dandelion.wechat.client.wechat.activity.WeChatShopActService;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.insurance.domain.param.insurance.coupon.CouponTemplateParam;
import com.tqmall.insurance.domain.result.InsuranceShopConfigDTO;
import com.tqmall.insurance.domain.result.common.PageEntityDTO;
import com.tqmall.insurance.domain.result.coupon.InsuranceCouponTemplateDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceShopConfigService;
import com.tqmall.insurance.service.insurance.coupon.RpcInsuranceCouponService;
import com.tqmall.legend.biz.activity.ActivityTemplateScopeRelService;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.ActivityTemplateServiceRelService;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.activity.IShopActivityServiceRelService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;
import com.tqmall.legend.entity.activity.ActivityTemplateServiceRel;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.activity.ShopActivityServiceRel;
import com.tqmall.legend.entity.shop.ServiceFlagsEnum;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.activity.ShopActivityStatusEnum;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoAppPublishStatusEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.enums.wechat.WechatActModuleTypeEnum;
import com.tqmall.legend.facade.activity.WechatActivityFacade;
import com.tqmall.legend.facade.activity.vo.ActivityAppointDataVo;
import com.tqmall.legend.facade.activity.vo.ActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.ActivityGroupBuyShareVo;
import com.tqmall.legend.facade.activity.vo.BarginAppointDataVo;
import com.tqmall.legend.facade.activity.vo.BarginCouponDataVo;
import com.tqmall.legend.facade.activity.vo.CouponTplVo;
import com.tqmall.legend.facade.activity.vo.SaveWechatActivityModuleVo;
import com.tqmall.legend.facade.activity.vo.SaveWechatActivityPageVo;
import com.tqmall.legend.facade.activity.vo.SaveWechatActivityVo;
import com.tqmall.legend.facade.activity.vo.ShopActivityVo;
import com.tqmall.legend.facade.service.vo.ServiceTemplateVo;
import com.tqmall.legend.facade.service.vo.WechatActServiceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tqmall.legend.enums.wechat.WechatActModuleTypeEnum.DISCOUNT;

/**
 * Created by wushuai on 16/8/2.
 */
@Slf4j
@Service
public class WechatActivityFacadeImpl implements WechatActivityFacade {
    @Autowired
    private IShopActivityService shopActivityService;
    @Autowired
    private ActivityTemplateService activityTemplateService;
    @Autowired
    private ActivityTemplateScopeRelService activityTemplateScopeRelService;
    @Autowired
    private WeChatActBuriedService weChatActBuriedService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private IShopActivityServiceRelService shopActivityServiceRelService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ActivityTemplateServiceRelService activityTemplateServiceRelService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private WeChatShopActService weChatShopActService;
    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private RpcInsuranceShopConfigService rpcInsuranceShopConfigService;
    @Autowired
    private RpcInsuranceCouponService rpcInsuranceCouponService;
    @Override
    public Page<ShopActivityVo> getActivityPage(Map<String, Object> param) {
        Assert.notNull(param,"查询条件不能为空");
        Long shopId = Long.valueOf(param.get("shopId").toString());
        Assert.notNull(shopId,"查询条件中的shopid不能为空");
        Integer offset = 0;
        Integer limit = 10;
        Integer joinStatus = 1;//1.全部,2.已参加,3.未参加
        if (param.get("offset")!=null) {
            offset = Integer.parseInt(param.get("offset").toString());
        }
        if (param.get("limit")!=null) {
            limit = Integer.parseInt(param.get("limit").toString());
        }
        if(limit<1){
            limit=10;
        }
        Integer pageNum = offset /limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        if (param.get("joinStatus")!=null) {
            joinStatus = Integer.parseInt(param.get("joinStatus").toString());
        }
        Integer count = 0;
        List<Long> actIds = new ArrayList<>();
        List<ShopActivityVo> shopActivityVoList = new ArrayList<>();
        param.remove("limit");
        param.remove("offset");
        //.查询活动范围
        Map<String, Object> scopeRelParam = new HashMap<>();
        scopeRelParam.put("scopeId", shopId);
        scopeRelParam.put("sorts", new String[] { " gmt_modified desc" });
        List<ActivityTemplateScopeRel> activityTemplateScopeRelList = activityTemplateScopeRelService.select(scopeRelParam);
        Set<Long> scopeRelTpIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(activityTemplateScopeRelList)) {
            for (ActivityTemplateScopeRel activityTemplateScopeRel : activityTemplateScopeRelList) {
                scopeRelTpIdSet.add(activityTemplateScopeRel.getActTplId());
            }
        }
        //.查询活动模版
        param.put("actStatus",2);//只查询已经发布的模版
        List<ActivityTemplate> activityTemplateList = activityTemplateService.select(param);
        Map<Long,ActivityTemplate> activityTemplateMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(activityTemplateList)) {
            for (ActivityTemplate activityTemplate : activityTemplateList) {
                activityTemplateMap.put(activityTemplate.getId(),activityTemplate);
            }
        }
        //.查询活动实体(不加时间限制)
        param.remove("startTimeLt");
        param.remove("endTimeGt");
        param.remove("actStatus");
        List<ShopActivity> shopActivityList = shopActivityService.select(param);
        Map<Long, ShopActivity> shopActivityMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(shopActivityList)) {
            for (ShopActivity shopActivity : shopActivityList) {
                shopActivityMap.put(shopActivity.getActTplId(), shopActivity);
            }
        }
        Set<Long> selectedTplIds = new HashSet<>();//所有符合条件的活动模版id:模版关系id集合与模版id集合的交集
        selectedTplIds.addAll(activityTemplateMap.keySet());
        selectedTplIds.retainAll(scopeRelTpIdSet);
        if (joinStatus == 3) {
            //未参加的,差集
            selectedTplIds.removeAll(shopActivityMap.keySet());
        } else if (joinStatus == 2) {
            //已参加的,交集
            selectedTplIds.retainAll(shopActivityMap.keySet());
        }
        //编码实现分页
        count = selectedTplIds.size();
        int index = 0;
        int end = offset + limit;
        int start = offset;
        if(!CollectionUtils.isEmpty(activityTemplateScopeRelList)){
            for(ActivityTemplateScopeRel templateScopeRel:activityTemplateScopeRelList){
                Long actTplId = templateScopeRel.getActTplId();
                if (selectedTplIds.contains(actTplId)) {
                    if (index < end && index >= start) {
                        ShopActivity shopActivity = shopActivityMap.get(actTplId);
                        ShopActivityVo shopActivityVo = new ShopActivityVo();
                        if (shopActivity != null) {
                            BeanUtils.copyProperties(shopActivity, shopActivityVo);
                            if(shopActivity.getActStatus()==ShopActivityStatusEnum.OUTLINE.getValue()){//草稿状态算作未发布
                                shopActivityVo.setIsJoin(0);
                            }else {
                                shopActivityVo.setIsJoin(1);
                            }
                            actIds.add(shopActivity.getId());
                        } else {
                            ActivityTemplate activityTemplate = activityTemplateMap.get(actTplId);
                            if(activityTemplate!=null){
                                BeanUtils.copyProperties(activityTemplate, shopActivityVo);
                                shopActivityVo.setActTplId(activityTemplate.getId());
                                shopActivityVo.setId(null);
                            }
                            shopActivityVo.setIsJoin(0);
                        }
                        shopActivityVoList.add(shopActivityVo);
                    }
                    index++;
                }
            }
        }
        //设置参与量,访问量
        if(!CollectionUtils.isEmpty(actIds)){
            Map<Long,ActBuriedDTO> actBuriedDTOMap = _getShopWechatActStatistics(actIds,shopId);
            for(ShopActivityVo shopActivityVo:shopActivityVoList){
                ActBuriedDTO actBuriedDTO = actBuriedDTOMap.get(shopActivityVo.getId());
                if(actBuriedDTO!=null){
                    shopActivityVo.setVisitCount(actBuriedDTO.getPv());
                    shopActivityVo.setPartCount(actBuriedDTO.getAppointNum());
                }
            }
        }
        //设置预览url
        if(!CollectionUtils.isEmpty(actIds)){
            Integer isFormal = 0;
            Map<Long,String> actPreviewMap = _getWechatActPreviews(actIds,isFormal);
            for (ShopActivityVo shopActivityVo:shopActivityVoList){
                String preview = actPreviewMap.get(shopActivityVo.getId());
                if(preview!=null&&!"".equals(preview)){
                    shopActivityVo.setPreviewUrl(preview);
                }
            }
        }
        //设置活动类型
        Boolean isInsurance=_setActivityType(shopActivityVoList);
        //判断门店是否可以参加(有无开通微信公众号的限制)
        _setCanJoin(shopActivityVoList,shopId,isInsurance);
        Page<ShopActivityVo> page = new DefaultPage(shopActivityVoList, pageRequest, count);
        return page;
    }

    /**
     * 判断门店是否可以参加(有无开通微信公众号的限制)<br/>
     * 包含砍价服务,组团服务的活动没开通微信公众号的门店不能参加
     * @param shopActivityVoList
     */
    private void _setCanJoin(List<ShopActivityVo> shopActivityVoList,Long shopId,Boolean isInsurance) {
        if(CollectionUtils.isEmpty(shopActivityVoList)){
            return;
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        com.tqmall.core.common.entity.Result<ShopDTO> result = weChatShopService.selectShopByUcShopId(userGlobalId);
        boolean isWechatShop = false;
        if(result.getData()!=null && ShopWechatStatusEnum.REGISTERED.getValue().equals(result.getData().getShopStatus().intValue())){
            isWechatShop = true;
        }

        boolean bonusMode = false;//未开通服务包模式
        //调用insurance查看门店配置的选项
        com.tqmall.core.common.entity.Result<List<InsuranceShopConfigDTO>> selectShopConfigResult = rpcInsuranceShopConfigService.selectShopConfigByShopId(userGlobalId.intValue());
        if(selectShopConfigResult.isSuccess()){
            List<InsuranceShopConfigDTO> insuranceShopConfigDTOList = selectShopConfigResult.getData();
            for(InsuranceShopConfigDTO insuranceShopConfigDTO : insuranceShopConfigDTOList){
                Integer cooperationMode = insuranceShopConfigDTO.getCooperationMode();
                if(cooperationMode.equals(Constants.ANXIN_INSURANCE_TWO)||cooperationMode.equals(Constants.ANXIN_INSURANCE_THRESS)){
                    bonusMode=true;//开通服务包模式
                }
            }
        }
        for (ShopActivityVo shopActivityVo : shopActivityVoList) {
            if (!isWechatShop && shopActivityVo.getWechatActivityType() != null) {
                if (DISCOUNT.getValue() == shopActivityVo.getWechatActivityType()
                        || WechatActModuleTypeEnum.GROUP_BUY.getValue() == shopActivityVo.getWechatActivityType()||(isInsurance&&!bonusMode&&shopActivityVo.getWechatActivityType()==WechatActModuleTypeEnum.DISCOUNT_COUPON.getValue())) {
                    shopActivityVo.setCanJoin(0);
                }
            }else if(isWechatShop && shopActivityVo.getWechatActivityType() != null){
                if (isInsurance&&!bonusMode&&shopActivityVo.getWechatActivityType()==WechatActModuleTypeEnum.DISCOUNT_COUPON.getValue()) {
                   // shopActivityVo.setCanJoin(2);//是微信门店但是没有开通服务包模式的参加保险券的门店是不能参加的带砍保险券活动的
                }
            }
        }
    }

    /**
     * 设置活动类别
     * @param shopActivityVoList
     */
    private Boolean _setActivityType(List<ShopActivityVo> shopActivityVoList) {
        boolean isInsurance = false;//是否含有保险券
        if(CollectionUtils.isEmpty(shopActivityVoList)){
            return isInsurance;
        }
        List<Long> actTplIdList = Lists.transform(shopActivityVoList, new Function<ShopActivityVo, Long>() {
            @Override
            public Long apply(ShopActivityVo shopActivityVo) {
                long actTplId = shopActivityVo.getActTplId() == null ? 0l : shopActivityVo.getActTplId();
                return actTplId;
            }
        });
        com.tqmall.core.common.entity.Result<List<TemplateActivityIdDTO>> result = weChatShopActService.getTplActListByIds(actTplIdList);
        log.info("[dubbo]调用ddl-wechat,查询活动模版信息,入參:{},success:{}",LogUtils.objectToString(actTplIdList),result.isSuccess());
        List<TemplateActivityIdDTO> templateActivityIdDTOList = result.getData();
        if(CollectionUtils.isEmpty(templateActivityIdDTOList)){
            return isInsurance;
        }
        Map<Long,Integer> activityTypeMap = new HashMap<>();
        for (TemplateActivityIdDTO templateActivityIdDTO : templateActivityIdDTOList) {
            Long actTplId = templateActivityIdDTO.getId();
            List<TemplateActivityPageDTO> templateActivityPageDTOList = templateActivityIdDTO.getPages();
            if (CollectionUtils.isEmpty(templateActivityPageDTOList)) {
                continue;
            }
            for (TemplateActivityPageDTO templateActivityPageDTO : templateActivityPageDTOList) {
                List<TemplateActivityPageModuleDTO> templateActivityPageModuleDTOList = templateActivityPageDTO.getModules();
                if(CollectionUtils.isEmpty(templateActivityPageModuleDTOList)) {
                    continue;
                }
                for (TemplateActivityPageModuleDTO templateActivityPageModuleDTO : templateActivityPageModuleDTOList) {
                    String moduleType = templateActivityPageModuleDTO.getModuleType();
                    WechatActModuleTypeEnum moduleTypeEnum = WechatActModuleTypeEnum.getValueByModuleType(moduleType);
                    if(moduleTypeEnum!=null){
                        activityTypeMap.put(actTplId,moduleTypeEnum.getValue());
                        if(WechatActModuleTypeEnum.DISCOUNT_COUPON.getValue()==moduleTypeEnum.getValue()){
                            List<ActivityDiscountCouponDTO> discountCouponDTOList = templateActivityPageModuleDTO.getDiscountCouponDTOList();
                            if(!CollectionUtils.isEmpty(discountCouponDTOList)){
                                for(ActivityDiscountCouponDTO discountCouponDTO:discountCouponDTOList){
                                    if(discountCouponDTO.getCouponSource()==1){//保险券
                                        isInsurance=true;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        for (ShopActivityVo shopActivityVo : shopActivityVoList) {
            shopActivityVo.setWechatActivityType(activityTypeMap.get(shopActivityVo.getActTplId()));
        }
        return isInsurance;
    }
    /**
     * 查询预览url
     */
    private Map<Long,String> _getWechatActPreviews(List<Long> actIds,Integer isFormal){
        Map<Long,String> actPreviews = new HashMap<>();
        com.tqmall.core.common.entity.Result<Map<Long,String>> result = weChatShopActService.previews(actIds, isFormal);
        log.info("[dubbo]查询预览URLS,参与量actIds:{},isFormal:{},success:{}", actIds, isFormal, result);
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())) {
            actPreviews = result.getData();
        }
        return actPreviews;
    }
    /**
     * 查询门店微信活动浏览量,参与量
     * @param actIds
     * @param shopId
     * @return
     */
    private Map<Long,ActBuriedDTO> _getShopWechatActStatistics(List<Long> actIds,Long shopId){
        Map<Long,ActBuriedDTO> actBuriedDTOMap = new HashMap<>();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if(userGlobalId==null){
            return actBuriedDTOMap;
        }
        com.tqmall.core.common.entity.Result<List<ActBuriedDTO>> result = weChatActBuriedService.shopActStatisticsByActIdAndShopId(actIds, userGlobalId);
        log.info("[dubbo]查询门店微信活动浏览量,参与量shopId:{},userGlobalId:{},actIds:{},success:{}",shopId,userGlobalId,actIds,result.isSuccess());
        if(result.isSuccess() && !CollectionUtils.isEmpty(result.getData())) {
            List<ActBuriedDTO> actBuriedDTOList = result.getData();
            for(ActBuriedDTO actBuriedDTO:actBuriedDTOList){
                actBuriedDTOMap.put(actBuriedDTO.getActId(),actBuriedDTO);
            }
        }
        return actBuriedDTOMap;
    }

    @Override
    public Result<ActivityAppointDataVo> qryActivityAppointData(Long shopId, Long actId) {
        if(shopId==null||actId==null){
            log.error("[consumer-ddlwechat-dubbo]查询微信活动的预约统计信息失败,参数为空shopId:{},actId:{}",shopId,actId);
            return Result.wrapErrorResult("-1","参数为空");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consumer-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,查询失败");
        }

        //.查询活动包含的服务
        Map<String,Object> relSearchMap = new HashMap<>();
        relSearchMap.put("shopActId",actId);
        List<ShopActivityServiceRel> shopActivityServiceRels = shopActivityServiceRelService.select(relSearchMap);
        List<Long> serviceIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopActivityServiceRels)){
            for(ShopActivityServiceRel shopActivityServiceRel:shopActivityServiceRels){
                serviceIds.add(shopActivityServiceRel.getServiceId());
            }
        }
        List<ShopServiceInfo> shopServiceInfoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(serviceIds)){
            shopServiceInfoList = shopServiceInfoService.selectByIds(serviceIds);
        }
        List<ActServiceDTO> actServiceDTOList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopServiceInfoList)){
            for(ShopServiceInfo shopServiceInfo:shopServiceInfoList){
                ActServiceDTO actServiceDTO = new ActServiceDTO();
                actServiceDTO.setServiceId(shopServiceInfo.getId().intValue());
                actServiceDTO.setServiceName(shopServiceInfo.getName());
                actServiceDTO.setServiceNum(0);
                actServiceDTOList.add(actServiceDTO);
            }
        }

        ActivityAppointDataVo activityAppointDataVo = new ActivityAppointDataVo();
        activityAppointDataVo.setActServiceDTOs(actServiceDTOList);
        int durationDay = getActDurationDay(actId);//活动进行时间
        activityAppointDataVo.setDurationDay(durationDay);

        //.查询活动内服务的预约统计数据
        com.tqmall.core.common.entity.Result<ActServiceListDTO> result = weChatActBuriedService.actAppointInfo(actId.intValue(),userGlobalId.intValue());
        log.info("[consumer-ddlwechat-dubbo]查询微信活动的预约统计信息,userGlobalId:{},actId:{},success:{}",userGlobalId,actId,result.isSuccess());
        ActServiceListDTO actServiceListDTO = result.getData();
        if(!result.isSuccess()||actServiceListDTO==null||actServiceListDTO.getActServiceDTOs()==null){
            return Result.wrapSuccessfulResult(activityAppointDataVo);
        }
        activityAppointDataVo.setAppointCount(actServiceListDTO.getAppointCount());
        List<ActServiceDTO> ddlWechatActServiceDTOList = actServiceListDTO.getActServiceDTOs();
        Map<Long,ActServiceDTO> actServiceDTOMap = new HashMap<>();
        for(ActServiceDTO actServiceDTO:ddlWechatActServiceDTOList){
            if(actServiceDTO.getServiceId()!=null){
                actServiceDTOMap.put(actServiceDTO.getServiceId().longValue(),actServiceDTO);
            }
        }
        for(ActServiceDTO actServiceDTO:actServiceDTOList){
            ActServiceDTO ddlActServiceDTO = actServiceDTOMap.get(actServiceDTO.getServiceId().longValue());
            if(ddlActServiceDTO!=null){
                actServiceDTO.setServiceNum(ddlActServiceDTO.getServiceNum());
            }
        }
        return Result.wrapSuccessfulResult(activityAppointDataVo);
    }

    @Override
    public Result<Page<ActAppointmentDTO>> qryActAppointList(Long shopId, Long actId, Long serviceId, int offset, int limit) {
        if(shopId==null||actId==null||serviceId==null||limit<1){
            log.error("[consumer-ddlwechat-dubbo]查询微信活动内服务预约的用户列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (userGlobalId == null) {
            log.error("[consumer-ddlwechat-dubbo]shopId{}查询不到userGlobalId,操作失败", shopId);
            return Result.wrapErrorResult("", "门店信息欠缺,查询失败");
        }
        Integer pageNum = offset/limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<ActAppointmentDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        com.tqmall.core.common.entity.Result<ActDetailListDTO> actDetailListDTOResult = weChatActBuriedService.actAppointInfoDetail(actId.intValue(), userGlobalId.intValue(), serviceId.intValue(), offset, limit);
        log.info("[consumer-ddlwechat-dubbo]查询微信活动的预约统计信息,serviceId:{},userGlobalId:{},actId:{},result:{}",serviceId,userGlobalId,actId,actDetailListDTOResult.isSuccess());
        if(!actDetailListDTOResult.isSuccess()){
            return Result.wrapErrorResult("-1",actDetailListDTOResult.getMessage());
        }
        ActDetailListDTO actDetailListDTO = actDetailListDTOResult.getData();
        if(actDetailListDTO==null||actDetailListDTO.getDetailNum()==null||actDetailListDTO.getActAppointmentDTOs()==null){
            return Result.wrapSuccessfulResult(page);
        }
        int count = actDetailListDTO.getDetailNum();
        List<ActAppointmentDTO> actAppointmentDTOs = actDetailListDTO.getActAppointmentDTOs();
        page = new DefaultPage(actAppointmentDTOs,pageRequest,count);
        return Result.wrapSuccessfulResult(page);
    }

    @Override
    public ActivityDetailVo getActivityDetail(Long shopId, Long actTplId) {
        if(shopId==null||actTplId==null){
            return null;
        }
        //.查询服务模版和门店关系
        Map<String,Object> relSearchMap = new HashMap<>();
        relSearchMap.put("actTplId",actTplId);
        relSearchMap.put("scopeId", shopId);
        List<ActivityTemplateScopeRel> activityTemplateScopeRelList = activityTemplateScopeRelService.select(relSearchMap);
        if(CollectionUtils.isEmpty(activityTemplateScopeRelList)){
            return null;
        }
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
        if(activityTemplate==null||activityTemplate.getActStatus()!=2){
            return null;
        }
        ActivityDetailVo activityDetailVo = new ActivityDetailVo();
        BeanUtils.copyProperties(activityTemplate, activityDetailVo);
        //.查询活动模板对应的活动实体
        Optional<ShopActivity> optional = shopActivityService.get(shopId,actTplId);
        ShopActivity shopActivity = optional.isPresent() ? optional.get() : null;
        if(shopActivity!=null){
            activityDetailVo.setShopActivityStatus(shopActivity.getActStatus());
            activityDetailVo.setActId(shopActivity.getId());
            if(shopActivity.getStartTime()!=null){
                activityDetailVo.setShopStartTimeStr(DateUtil.convertDateToYMDHMS(shopActivity.getStartTime()));
            }
            if(shopActivity.getEndTime()!=null){
                activityDetailVo.setShopEndTimeStr(DateUtil.convertDateToYMDHMS(shopActivity.getEndTime()));
            }
            activityDetailVo.setShopActivityStatus(shopActivity.getActStatus());
        } else{
            activityDetailVo.setShopActivityStatus(ShopActivityStatusEnum.NOJOIN.getValue());
        }
        if(activityTemplate.getStartTime()!=null){
            activityDetailVo.setTplStartTimeStr(DateUtil.convertDateToYMDHMS(activityTemplate.getStartTime()));
        }
        if(activityTemplate.getEndTime()!=null){
            activityDetailVo.setTplEndTimeStr(DateUtil.convertDateToYMDHMS(activityTemplate.getEndTime()));
        }
        //.查询活动模版对应的服务模版
        Map<String,Object> serviceRelSearchParam = new HashMap<>();
        serviceRelSearchParam.put("actTplId",actTplId);
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = activityTemplateServiceRelService.select(serviceRelSearchParam);
        Set<Long> serviceTplIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(activityTemplateScopeRelList)){
            for(ActivityTemplateServiceRel activityTemplateServiceRel:activityTemplateServiceRelList){
                serviceTplIds.add(activityTemplateServiceRel.getServiceTplId());
            }
        }
        List<ServiceTemplate> serviceTemplateList = null;
        if(!CollectionUtils.isEmpty(serviceTplIds)){
            Map<String,Object>  serviceTplSearchParam = new HashMap<>();
            serviceTplSearchParam.put("ids",serviceTplIds);
            serviceTplSearchParam.put("status",0);
            serviceTemplateList = serviceTemplateService.select(serviceTplSearchParam);
        }
        //.查询活动实体和服务实体对应的关系
        Map<Long, ShopServiceInfo> actDefShopServiceInfoMap = new HashMap<>();//活动模版中设置的服务Map,key:serviceTplId
        List<ShopServiceInfo> actCustomServiceInfoList = new ArrayList<>();//门店自己添加到活动中的服务List
        int selectedServiceCount =0;
        if(shopActivity!=null){
            Map<String,Object> shopServiceRelSearchParam = new HashMap<>();
            shopServiceRelSearchParam.put("shopActId",shopActivity.getId());
            List<ShopActivityServiceRel> shopActivityServiceRelList = shopActivityServiceRelService.select(shopServiceRelSearchParam);
            List<Long> serviceIds = new ArrayList<>();
            if(!CollectionUtils.isEmpty(shopActivityServiceRelList)){
                for(ShopActivityServiceRel shopActivityServiceRel:shopActivityServiceRelList){
                    serviceIds.add(shopActivityServiceRel.getServiceId());
                }
            }
            List<ShopServiceInfo> shopServiceInfoList = null;
            if(!CollectionUtils.isEmpty(serviceIds)){
                Map<String,Object> shopServiceSearchParam = new HashMap<>();
                shopServiceSearchParam.put("ids",serviceIds);
                shopServiceSearchParam.put("status",0);
                shopServiceInfoList = shopServiceInfoService.select(shopServiceSearchParam);
                shopServiceInfoService.setServiceSuitAmount(shopServiceInfoList);
                shopServiceInfoService.setSuitAmount2ServicePrice(shopServiceInfoList);
            }
            if(!CollectionUtils.isEmpty(shopServiceInfoList)){
                for(ShopServiceInfo shopServiceInfo:shopServiceInfoList){
                    if(serviceTplIds.contains(shopServiceInfo.getParentId())){
                        actDefShopServiceInfoMap.put(shopServiceInfo.getParentId(), shopServiceInfo);
                    } else {
                        actCustomServiceInfoList.add(shopServiceInfo);
                    }
                    selectedServiceCount++;
                }
            }
        }
        //.组装对象
        List<ServiceTemplateVo> serviceTemplateVoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(serviceTemplateList)){
            for(ServiceTemplate serviceTemplate:serviceTemplateList){
                ServiceTemplateVo serviceTemplateVo= new ServiceTemplateVo();
                BeanUtils.copyProperties(serviceTemplate,serviceTemplateVo);
                serviceTemplateVo.setShopServiceInfo(actDefShopServiceInfoMap.get(serviceTemplate.getId()));
                serviceTemplateVoList.add(serviceTemplateVo);
            }
        }
        activityDetailVo.setSelectedServiceCount(selectedServiceCount);
        //.将门店自己添加进活动中峰服务加上一个id=0的伪服务模版,并追加到活动模版中设置的服务list中,方便后续统一处理
        for (ShopServiceInfo shopServiceInfo : actCustomServiceInfoList) {
            ServiceTemplateVo serviceTemplateVo = new ServiceTemplateVo();
            serviceTemplateVo.setId(0L);//只有服务实例没有服务模版时默认空的服务模版,设置其id=0
            serviceTemplateVo.setShopServiceInfo(shopServiceInfo);
            serviceTemplateVoList.add(serviceTemplateVo);
        }
        activityDetailVo.setServiceTemplateVoList(serviceTemplateVoList);
        //转换成支持车主端活动模块化的对象
        _transformActivityDetailVo(activityDetailVo);
        return activityDetailVo;
    }

    /**
     * 转换成支持车主端的模块化对象<br>
     * 支持对门店自己添加到活动中服务的处理
     * @param activityDetailVo
     */
    private void _transformActivityDetailVo(ActivityDetailVo activityDetailVo) {
        if (activityDetailVo==null) {
            return;
        }
        //.活动模版处理
        _transformTplActivityDetailVo(activityDetailVo);
        if (activityDetailVo.getActId() == null) {
            //.活动id为空,表示门店没有参加过该活动,直接返回
            return;
        }
        //.门店实体下实体服务在wechat保存的配置信息
        _setShopServiceExtInfoList(activityDetailVo);
    }

    /**
     * 获取门店活动实体包含的pageList,并将云修侧保存的活动服务信息与微信侧的扩展信息进行合并
     *
     * @param activityDetailVo
     * @return
     */
    private void _setShopServiceExtInfoList(ActivityDetailVo activityDetailVo) {
        Long actId = activityDetailVo.getActId();
        //.调用ddl-wechat接口查询活动实体信息
        com.tqmall.core.common.entity.Result<List<ShopActivityPageDTO>> shopActivityPageDTOListResult = weChatShopActService.getActPageListById(actId);
        log.info("[dubbo]调用ddl-wechat查询活动实体模块信息,入參actId:{},success:{}",actId,shopActivityPageDTOListResult.isSuccess());
        if (!shopActivityPageDTOListResult.isSuccess()) {
            log.info("[dubbo]调用ddl-wechat查询活动实体模块信息出错,actId:{},resultCode:{},resultMsg", actId, shopActivityPageDTOListResult.getCode(), shopActivityPageDTOListResult.getMessage());
            throw new BizException("调用接口出错" + shopActivityPageDTOListResult.getMessage());
        }
        List<ShopActivityPageDTO> shopActivityPageDTOList = shopActivityPageDTOListResult.getData();
        if (CollectionUtils.isEmpty(shopActivityPageDTOList)) {
            return;
        }
        List<SaveWechatActivityPageVo> shopPageVoList = activityDetailVo.getPageVoList();
        Map<String, SaveWechatActivityPageVo> shopPageVoMap = Maps.uniqueIndex(shopPageVoList, new Function<SaveWechatActivityPageVo, String>() {
            @Override
            public String apply(SaveWechatActivityPageVo saveWechatActivityPageVo) {
                return saveWechatActivityPageVo.getUniqueCode();
            }
        });//key:pageUniqueCode
        for (ShopActivityPageDTO pageDTO : shopActivityPageDTOList) {
            List<ShopActivityPageModuleDTO> moduleDTOList = pageDTO.getModules();
            if (CollectionUtils.isEmpty(moduleDTOList)) {
                continue;
            }
            String pageUniqueCode = pageDTO.getUniqueCode();
            SaveWechatActivityPageVo pageVo = shopPageVoMap.get(pageUniqueCode);
            if (pageVo == null || CollectionUtils.isEmpty(pageVo.getModuleVoList())) {
                continue;
            }
            List<SaveWechatActivityModuleVo> moduleVoList = pageVo.getModuleVoList();
            Map<String, SaveWechatActivityModuleVo> moduleVoMap = Maps.uniqueIndex(moduleVoList, new Function<SaveWechatActivityModuleVo, String>() {
                @Override
                public String apply(SaveWechatActivityModuleVo saveWechatActivityModuleVo) {
                    return saveWechatActivityModuleVo.getUniqueCode();
                }
            });
            for (ShopActivityPageModuleDTO moduleDTO : moduleDTOList) {
                String moduleUniqueCode = moduleDTO.getUniqueCode();
                SaveWechatActivityModuleVo moduleVo = moduleVoMap.get(moduleUniqueCode);
                if (moduleVo == null) {
                    continue;
                }
                String moduleType = moduleDTO.getModuleType();
                WechatActModuleTypeEnum moduleTypeEnum = WechatActModuleTypeEnum.getValueByModuleType(moduleType);
                if (moduleTypeEnum == null) {
                    continue;
                }
                switch (moduleTypeEnum){
                    case SERVICE:
                        //不需要处理
                        break;
                    case DISCOUNT:
                        //服务砍价参数设置
                        transDiscountServiceDTO(moduleVo, moduleDTO);
                        break;
                    case GROUP_BUY:
                        //服务组团参数设置
                        transGroupBuyServiceDTO(moduleVo, moduleDTO);
                        break;
                    case DISCOUNT_COUPON:
                        //优惠券砍价参数设置
                        transDiscountCouponDTO(moduleVo,moduleDTO);
                        break;
                }
            }
        }
    }


    /**
     * 组团服务参数转换
     * @param moduleVo
     * @param moduleDto
     */
    private void transGroupBuyServiceDTO(SaveWechatActivityModuleVo moduleVo, ShopActivityPageModuleDTO moduleDto) {
        List<GroupBuyServiceDTO> groupBuyServiceDTOList = moduleDto.getGroupBuyServiceDTOList();
        if (CollectionUtils.isEmpty(groupBuyServiceDTOList)) {
            return;
        }
        Map<Long, GroupBuyServiceDTO> groupBuyServiceDTOMap = Maps.uniqueIndex(groupBuyServiceDTOList, new Function<GroupBuyServiceDTO, Long>() {
            @Override
            public Long apply(GroupBuyServiceDTO groupBuyServiceDTO) {
                return groupBuyServiceDTO.getServiceInfoId();
            }
        });
        List<ServiceTemplateVo> serviceTemplateVoList = moduleVo.getDetailServiceVoList();
        if (CollectionUtils.isEmpty(serviceTemplateVoList)) {
            return;
        }

        for (ServiceTemplateVo serviceTemplateVo : serviceTemplateVoList) {
            ShopServiceInfo shopServiceInfo = serviceTemplateVo.getShopServiceInfo();
            if (shopServiceInfo == null) {
                continue;
            }
            Long serviceId = shopServiceInfo.getId();
            GroupBuyServiceDTO groupBuyServiceDTO = groupBuyServiceDTOMap.get(serviceId);
            if (groupBuyServiceDTO == null) {
                continue;
            }
            serviceTemplateVo.setGroupPrice(groupBuyServiceDTO.getGroupPrice());
            serviceTemplateVo.setRealAmount(groupBuyServiceDTO.getRealAmount());
            serviceTemplateVo.setFakeAmount(groupBuyServiceDTO.getFakeAmount());
            serviceTemplateVo.setCustomerNumber(groupBuyServiceDTO.getCustomerNumber());
        }
    }

    /**
     * 砍价服务参数设置
     * @param moduleVo
     * @param moduleDto
     */
    private void transDiscountServiceDTO(SaveWechatActivityModuleVo moduleVo, ShopActivityPageModuleDTO moduleDto) {
        List<ServiceTemplateVo> serviceTemplateVoList = moduleVo.getDetailServiceVoList();
        if (CollectionUtils.isEmpty(serviceTemplateVoList)) {
            return;
        }
        List<com.tqmall.dandelion.wechat.client.dto.DiscountServiceDTO> discountServiceDTOList = moduleDto.getDiscountServiceDTOList();
        Map<Long, com.tqmall.dandelion.wechat.client.dto.DiscountServiceDTO> discountServiceDTOMap = Maps.uniqueIndex(discountServiceDTOList, new Function<com.tqmall.dandelion.wechat.client.dto.DiscountServiceDTO, Long>() {
            @Override
            public Long apply(com.tqmall.dandelion.wechat.client.dto.DiscountServiceDTO discountServiceDTO) {
                return discountServiceDTO.getServiceInfoId();
            }
        });
        for (ServiceTemplateVo serviceTemplateVo : serviceTemplateVoList) {
            ShopServiceInfo shopServiceInfo = serviceTemplateVo.getShopServiceInfo();
            if (shopServiceInfo == null) {
                continue;
            }
            Long serviceId = shopServiceInfo.getId();
            com.tqmall.dandelion.wechat.client.dto.DiscountServiceDTO discountServiceDTO = discountServiceDTOMap.get(serviceId);
            if (discountServiceDTO == null) {
                continue;
            }
            serviceTemplateVo.setFloorPrice(discountServiceDTO.getFloorPrice());
            serviceTemplateVo.setRealAmount(discountServiceDTO.getRealAmount());
            serviceTemplateVo.setFakeAmount(discountServiceDTO.getFakeAmount());
        }
    }
    /**
     * 砍券对象转换
     * @param moduleVo
     * @param moduleDto
     */
    private void transDiscountCouponDTO(SaveWechatActivityModuleVo moduleVo,ShopActivityPageModuleDTO moduleDto) {
        List<CouponTplVo> couponTplVos=moduleVo.getCouponVoList();
        if (CollectionUtils.isEmpty(couponTplVos)) {
            return;
        }
        List<ActivityDiscountCouponDTO> discountCouponDTOList = moduleDto.getDiscountCouponDTOList();
        if (CollectionUtils.isEmpty(discountCouponDTOList)) {
            return;
        }
        Map<Long, ActivityDiscountCouponDTO> activityDiscountCouponMaps = Maps.uniqueIndex(discountCouponDTOList, new Function<ActivityDiscountCouponDTO, Long>() {
            @Override
            public Long apply(ActivityDiscountCouponDTO activityDiscountCouponDTO) {
                return activityDiscountCouponDTO.getCouponTplId();
            }
        });
        for(CouponTplVo couponTplVo:couponTplVos){
            if(activityDiscountCouponMaps.size()>0) {
                ActivityDiscountCouponDTO activityDiscountCouponDTO = activityDiscountCouponMaps.get(couponTplVo.getId().longValue());
                if(activityDiscountCouponDTO!=null) {
                    couponTplVo.setReceivedCouponCount(activityDiscountCouponDTO.getReceivedCouponCount());
                    couponTplVo.setCouponTotal(activityDiscountCouponDTO.getCouponTotal());
                }
            }
        }
    }
    /**
     * 活动模版处理:将legend侧活动配置信息和微信端活动配置信息合并
     * @param activityDetailVo
     */
    private void _transformTplActivityDetailVo(ActivityDetailVo activityDetailVo) {
        Long actTplId = activityDetailVo.getId();
        com.tqmall.core.common.entity.Result<List<TemplateActivityIdDTO>> templateActivityIdDTOListResult = weChatShopActService.getTplActListByIds(Lists.newArrayList(actTplId));
        log.info("[dubbo]调用ddl-wechat查询活动模版模块信息,入參:{},success:{}",actTplId,templateActivityIdDTOListResult.isSuccess());
        if (CollectionUtils.isEmpty(templateActivityIdDTOListResult.getData())) {
            throw new BizException("查询活动模版配置信息出错");
        }
        TemplateActivityIdDTO templateActivityIdDTO = templateActivityIdDTOListResult.getData().get(0);
        List<TemplateActivityPageDTO> pages = templateActivityIdDTO.getPages();
        if(CollectionUtils.isEmpty(pages)){
            throw new BizException("数据错误");
        }
        List<ServiceTemplateVo> serviceTemplateVoList = activityDetailVo.getServiceTemplateVoList();
        List<SaveWechatActivityPageVo> pageVoList = new ArrayList<>();
        for (TemplateActivityPageDTO page : pages) {
            List<TemplateActivityPageModuleDTO> modules = page.getModules();
            if(CollectionUtils.isEmpty(modules)){
                continue;
            }
            List<SaveWechatActivityModuleVo> moduleVoList = new ArrayList<>();
            for (TemplateActivityPageModuleDTO module : modules) {
                String moduleType = module.getModuleType();
                WechatActModuleTypeEnum moduleTypeEnum = WechatActModuleTypeEnum.getValueByModuleType(moduleType);
                if (moduleTypeEnum == null) {
                    continue;
                }
                SaveWechatActivityModuleVo saveWechatActivityModuleVo = new SaveWechatActivityModuleVo();
                saveWechatActivityModuleVo.setUniqueCode(module.getUniqueCode());
                saveWechatActivityModuleVo.setModuleType(moduleType);
                saveWechatActivityModuleVo.setDetailServiceVoList(serviceTemplateVoList);
                setCouponList(module, saveWechatActivityModuleVo);
                saveWechatActivityModuleVo.setModuleIndex(module.getModuleIndex());
                moduleVoList.add(saveWechatActivityModuleVo);
            }
            SaveWechatActivityPageVo saveWechatActivityPageVo = new SaveWechatActivityPageVo();
            saveWechatActivityPageVo.setUniqueCode(page.getUniqueCode());
            saveWechatActivityPageVo.setPageIndex(page.getPageIndex());
            saveWechatActivityPageVo.setModuleVoList(moduleVoList);
            pageVoList.add(saveWechatActivityPageVo);
        }
        activityDetailVo.setPageVoList(pageVoList);
    }

    private void setCouponList(TemplateActivityPageModuleDTO module, SaveWechatActivityModuleVo saveWechatActivityModuleVo) {
        List<ActivityDiscountCouponDTO> activityDiscountCouponDTOs=module.getDiscountCouponDTOList();
        List<Integer> couponTemplateIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(activityDiscountCouponDTOs)){
            for(ActivityDiscountCouponDTO activityDiscountCouponDTO:activityDiscountCouponDTOs){
                couponTemplateIds.add(activityDiscountCouponDTO.getCouponTplId().intValue());
            }
            CouponTemplateParam templateParam = new CouponTemplateParam();
            templateParam.setCouponTemplateIds(couponTemplateIds);
            templateParam.setPageNum(1);
            templateParam.setPageSize(100);
            com.tqmall.core.common.entity.Result<PageEntityDTO<InsuranceCouponTemplateDTO>> result= rpcInsuranceCouponService.queryCouponTemplateListByIds(templateParam);
            if(result.isSuccess()&&result.getData()!=null){
                List<InsuranceCouponTemplateDTO> insuranceCouponTemplateDTOs = result.getData().getRecordList();
                List<CouponTplVo> couponTplVos = new ArrayList<>();
                if(!CollectionUtils.isEmpty(insuranceCouponTemplateDTOs)){
                    for(InsuranceCouponTemplateDTO insuranceCouponTemplateDTO:insuranceCouponTemplateDTOs){
                        CouponTplVo couponTplVo= new CouponTplVo();
                        BeanUtils.copyProperties(insuranceCouponTemplateDTO, couponTplVo);
                        couponTplVo.setCouponRuleDescriptionDelHtml(CommonUtils.delHTMLTag(insuranceCouponTemplateDTO.getCouponRuleDescription()));
                        couponTplVo.setCouponSource(1);
                        couponTplVos.add(couponTplVo);
                    }
                    saveWechatActivityModuleVo.setCouponVoList(couponTplVos);
                }
            }

        }
    }

    @Override
    public Result<ShopActivityVo> save(SaveWechatActivityVo saveWechatActivityVo, UserInfo userInfo) {
        if(userInfo==null||saveWechatActivityVo==null||saveWechatActivityVo.getActTplId()==null||userInfo.getShopId()==null
                ||saveWechatActivityVo.getStartTime()==null||saveWechatActivityVo.getEndTime()==null
                ||saveWechatActivityVo.getActStatus()==null){
            log.error("根据活动模版保存活动实体失败,参数错误");
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long shopId = userInfo.getShopId();
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if(userGlobalId==null){
            log.error("根据shopId{},查询不到userGlobalId",shopId);
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long actTplId = saveWechatActivityVo.getActTplId();
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
        if(activityTemplate==null||activityTemplate.getActStatus()==null){
            log.error("根据actTplId:{}查询活动模版为空活着无状态",actTplId);
            return Result.wrapErrorResult("-1","模版不存在,或模版已失效");
        }
        if(activityTemplate.getActStatus()!=2){
            log.error("根据actTplId:{}查询活动模版无效",actTplId);
            return Result.wrapErrorResult("-1","模版已失效");
        }
        Long actId = saveWechatActivityVo.getActId();

        //.保存门店活动实体
        ShopActivity shopActivity = null;
        ShopActivity shopActivityCopy = null;
        if(actId==null){
            //新增
            shopActivity = new ShopActivity();
            BeanUtils.copyProperties(activityTemplate,shopActivity);
            shopActivity.setId(null);
            shopActivity.setShopId(shopId);
            shopActivity.setActTplId(actTplId);
            shopActivity.setCreator(userInfo.getUserId());
            shopActivity.setModifier(userInfo.getUserId());
            shopActivity.setGmtCreate(new Date());
            shopActivity.setGmtModified(new Date());
        } else{
            shopActivity = shopActivityService.selectById(actId);
            shopActivityCopy = new ShopActivity();
            BeanUtils.copyProperties(shopActivity, shopActivityCopy);
            if(shopActivity==null){
                log.error("根据actId:{}更新门店活动实体时的活动实体不存在",actId);
                return Result.wrapErrorResult("-1","操作失败");
            }
        }
        shopActivity.setStartTime(saveWechatActivityVo.getStartTime());
        shopActivity.setEndTime(saveWechatActivityVo.getEndTime());
        shopActivity.setActStatus(saveWechatActivityVo.getActStatus());
        if(shopActivity.getId()==null){
            shopActivityService.add(shopActivity);
        } else {
            shopActivityService.update(shopActivity);
        }
        actId = shopActivity.getId();
        if (actId == null) {
            log.error("保存门店活动实体失败{}", shopActivity);
            return Result.wrapErrorResult("-1", "操作失败");
        }
        ShopActivityVo shopActivityVo = new ShopActivityVo();
        BeanUtils.copyProperties(shopActivity,shopActivityVo);

        //.保存活动实体和服务实体的关系
        Map<String,Object> delActServiceParam = new HashMap<>();
        delActServiceParam.put("shopActId",actId);
        List<ShopActivityServiceRel> shopActivityServiceRelListOld = shopActivityServiceRelService.select(delActServiceParam);
        shopActivityServiceRelService.delete(delActServiceParam);
        List<Long> serviceIds = _getServiceIds(saveWechatActivityVo);
        List<ShopActivityServiceRel> shopActivityServiceRelListNew = new ArrayList<>();
        if(!CollectionUtils.isEmpty(serviceIds)){
            for(Long serviceId:serviceIds){
                ShopActivityServiceRel shopActivityServiceRel = new ShopActivityServiceRel();
                shopActivityServiceRel.setCreator(userInfo.getUserId());
                shopActivityServiceRel.setModifier(userInfo.getUserId());
                shopActivityServiceRel.setShopActId(actId);
                shopActivityServiceRel.setServiceId(serviceId);
                shopActivityServiceRelListNew.add(shopActivityServiceRel);
            }
        }
        if(!CollectionUtils.isEmpty(shopActivityServiceRelListNew)){
            shopActivityServiceRelService.batchInsert(shopActivityServiceRelListNew);
        }
        //.更新服务的市场价,售价,和预约定金
        ShopActParam shopActParam = _transformSaveShopActParam(saveWechatActivityVo, userGlobalId, actTplId, shopActivity);
        try{
            //调ddlwechat接口
            com.tqmall.core.common.entity.Result<Boolean> ddlResult = weChatShopActService.publishShopAct(shopActParam);
            log.info("[consumer-ddlwechat-dubbo]保存微信活动入参:{},success:{}",LogUtils.objectToString(shopActParam),ddlResult.isSuccess());
            if(!ddlResult.isSuccess()){
                if("-5".equals(ddlResult.getCode())){
                    //-5表示自动配置菜单失败,但服务发布是成功的
                    shopActivityVo.setAutoMenuSuccess(0);
                    return Result.wrapSuccessfulResult(shopActivityVo);
                }
                return Result.wrapErrorResult(ddlResult.getCode(),ddlResult.getMessage());
            }
            return Result.wrapSuccessfulResult(shopActivityVo);
        } catch (Exception e){
            log.error("[dubbo]调用ddl-wechat接口发布活动失败,shopActParam:{},重置门店实体活动shopActivityCopy:{},错误信息:", LogUtils.objectToString(shopActParam),LogUtils.objectToString(shopActivityCopy),e);
            _resetShopActivity(shopActivityCopy, shopActParam.getActId(),shopActivityServiceRelListOld);
            return Result.wrapErrorResult("-1","发布失败");
        }
    }

    private void _resetShopActivity(ShopActivity shopActivityCopy, Long actId,List<ShopActivityServiceRel> shopActivityServiceRelListOld) {
        if (actId == null) {
            return;
        }
        //重置活动实体
        if (shopActivityCopy == null) {
            Map<String, Object> delparam = new HashMap<>();
            delparam.put("id", actId);
            shopActivityService.delete(delparam);
        } else {
            shopActivityService.update(shopActivityCopy);
        }
        //重置活动服务关系
        Map<String,Object> delActServiceParam = new HashMap<>();
        delActServiceParam.put("shopActId",actId);
        shopActivityServiceRelService.delete(delActServiceParam);
        if (!CollectionUtils.isEmpty(shopActivityServiceRelListOld)) {
            shopActivityServiceRelService.batchInsert(shopActivityServiceRelListOld);
        }
    }

    private ShopActParam _transformSaveShopActParam(SaveWechatActivityVo saveWechatActivityVo, Long userGlobalId, Long actTplId, ShopActivity shopActivity) {
        ShopActParam shopActParam = new ShopActParam();
        shopActParam.setShopId(userGlobalId);
        shopActParam.setActId(shopActivity.getId());
        shopActParam.setActName(shopActivity.getActName());
        shopActParam.setActTplId(actTplId);
        shopActParam.setActStatus(shopActivity.getActStatus());
        int isConfigMenu = saveWechatActivityVo.getAutoConfigMenu() == null ? 0 : saveWechatActivityVo.getAutoConfigMenu();
        shopActParam.setIsConfigMenu(isConfigMenu);
        List<ActPageModuleServiceParam> actPageModuleParamList = new ArrayList<>();
        List<SaveWechatActivityPageVo> pageVoList = saveWechatActivityVo.getPageVoList();
        if (!CollectionUtils.isEmpty(pageVoList)) {
            List<WechatActServiceVo> wechatActServiceVoList = new ArrayList<>();
            for (SaveWechatActivityPageVo saveWechatActivityPageVo : pageVoList) {
                List<SaveWechatActivityModuleVo> moduleVoList = saveWechatActivityPageVo.getModuleVoList();
                if (CollectionUtils.isEmpty(moduleVoList)) {
                    continue;
                }
                for (SaveWechatActivityModuleVo saveWechatActivityModuleVo : moduleVoList) {
                    List<WechatActServiceVo> serviceVoList = saveWechatActivityModuleVo.getServiceVoList();
                    if (!CollectionUtils.isEmpty(serviceVoList)) {
                        for (WechatActServiceVo wechatActServiceVo : serviceVoList) {
                            ActPageModuleServiceParam actPageModuleServiceParam = new ActPageModuleServiceParam();
                            actPageModuleServiceParam.setUniqueCode(saveWechatActivityModuleVo.getUniqueCode());
                            actPageModuleServiceParam.setModuleType(saveWechatActivityModuleVo.getModuleType());
                            actPageModuleServiceParam.setServiceInfoId(wechatActServiceVo.getId());
                            actPageModuleServiceParam.setFloorPrice(wechatActServiceVo.getFloorPrice());
                            actPageModuleServiceParam.setRealAmount(wechatActServiceVo.getRealAmount());
                            actPageModuleServiceParam.setFakeAmount(wechatActServiceVo.getFakeAmount());
                            actPageModuleServiceParam.setGroupPrice(wechatActServiceVo.getGroupPrice());
                            actPageModuleServiceParam.setCustomerNumber(wechatActServiceVo.getCustomerNumber());
                            actPageModuleParamList.add(actPageModuleServiceParam);
                            wechatActServiceVoList.add(wechatActServiceVo);
                        }
                    }

                    List<CouponTplVo> couponVoList = saveWechatActivityModuleVo.getCouponVoList();
                    if (!CollectionUtils.isEmpty(couponVoList)) {
                        for (CouponTplVo couponTplVo : couponVoList) {
                            ActPageModuleServiceParam actPageModuleServiceParam = new ActPageModuleServiceParam();
                            actPageModuleServiceParam.setUniqueCode(saveWechatActivityModuleVo.getUniqueCode());
                            actPageModuleServiceParam.setModuleType(saveWechatActivityModuleVo.getModuleType());
                            actPageModuleServiceParam.setCouponSource(couponTplVo.getCouponSource());
                            actPageModuleServiceParam.setCouponTplId(couponTplVo.getId().longValue());
                            actPageModuleServiceParam.setCouponTotal(couponTplVo.getCouponTotal());
                            actPageModuleParamList.add(actPageModuleServiceParam);
                        }
                    }
                }
            }
            _updateShopServiceInfo(wechatActServiceVoList);
        }
        shopActParam.setActPageModuleParamList(actPageModuleParamList);
        return shopActParam;
    }

    private void _updateShopServiceInfo(List<WechatActServiceVo> wechatActServiceVoList) {
        if (CollectionUtils.isEmpty(wechatActServiceVoList)) {
            return;
        }
        List<Long> servcieInfoIds = Lists.transform(wechatActServiceVoList, new Function<WechatActServiceVo, Long>() {
            @Override
            public Long apply(WechatActServiceVo wechatActServiceVo) {
                return wechatActServiceVo.getId();
            }
        });
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllByIds(servcieInfoIds);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return;
        }
        Map<Long,ShopServiceInfo> shopServiceInfoMap = Maps.uniqueIndex(shopServiceInfoList, new Function<ShopServiceInfo, Long>() {
            @Override
            public Long apply(ShopServiceInfo shopServiceInfo) {
                return shopServiceInfo.getId();
            }
        });
        for (WechatActServiceVo wechatActServiceVo : wechatActServiceVoList) {
            ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(wechatActServiceVo.getId());
            if(shopServiceInfo!=null){
                BeanUtils.copyProperties(wechatActServiceVo,shopServiceInfo);
                shopServiceInfoService.update(shopServiceInfo);
            }
        }
    }

    private List<Long> _getServiceIds(SaveWechatActivityVo saveWechatActivityVo) {
        if (saveWechatActivityVo==null) {
            return new ArrayList<>();
        }
        List<SaveWechatActivityPageVo> pageVoList = saveWechatActivityVo.getPageVoList();
        if (CollectionUtils.isEmpty(pageVoList)) {
            return new ArrayList<>();
        }
        List<SaveWechatActivityModuleVo> moduleVoList = new ArrayList<>();
        for (SaveWechatActivityPageVo saveWechatActivityPageVo : pageVoList) {
            if (CollectionUtils.isEmpty(saveWechatActivityPageVo.getModuleVoList())) {
                continue;
            }
            moduleVoList.addAll(saveWechatActivityPageVo.getModuleVoList());
        }
        if(CollectionUtils.isEmpty(moduleVoList)){
            return new ArrayList<>();
        }
        List<Long> serviceIdList = new ArrayList<>();
        for (SaveWechatActivityModuleVo saveWechatActivityModuleVo : moduleVoList) {
            List<WechatActServiceVo> wechatActServiceVoList = saveWechatActivityModuleVo.getServiceVoList();
            if (CollectionUtils.isEmpty(wechatActServiceVoList)) {
                continue;
            }
            for (WechatActServiceVo wechatActServiceVo : wechatActServiceVoList) {
                if (wechatActServiceVo.getId() != null) {
                    serviceIdList.add(wechatActServiceVo.getId());
                }
            }
        }
        return serviceIdList;
    }

    @Override
    public Result<String> getActivityPreviewUrl(Long shopId, Long actId,Integer isFormal) {
        com.tqmall.core.common.entity.Result<String> result = weChatShopActService.preview(actId,isFormal);
        log.info("[consumer-ddlwechat-dubbo]获取活动预览url{}",actId,LogUtils.objectToString(result));
        if(result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else{
            return Result.wrapErrorResult(result.getCode(),result.getMessage());
        }
    }

    @Override
    public JoinActivityLimitDTO getJoinActivityLimit(Long actTplId, Long shopId) {
        Assert.notNull(actTplId,"模版Id不能为空");
        Assert.notNull(shopId,"shopId不能为空");
        //若门店已经参加过,则不需判断限制条件
        Optional<ShopActivity> optional = shopActivityService.get(shopId,actTplId);
        ShopActivity shopActivity = optional.isPresent() ? optional.get() : null;
        if(shopActivity!=null && shopActivity.getActStatus()!=null
                && shopActivity.getActStatus().intValue()==ShopActivityStatusEnum.PUBLISH.getValue()){
            JoinActivityLimitDTO joinActivityLimitDTO = new JoinActivityLimitDTO();
            joinActivityLimitDTO.setCode("0");
            return joinActivityLimitDTO;
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        com.tqmall.core.common.entity.Result<JoinActivityLimitDTO> result = weChatShopActService.isFulfilLimit(actTplId, userGlobalId);
        log.info("[dubbo]调ddl-wechat接口查询参加活动限制信息,actTplId:{},userGlobalId:{},result:{}",actTplId,userGlobalId,LogUtils.objectToString(result));
        if (result.getData() == null) {
            throw new BizException("查询参加活动限制条件异常");
        }
        JoinActivityLimitDTO joinActivityLimitDTO = result.getData();
        if (!"0".equals(joinActivityLimitDTO.getCode())) {
            //门店粉丝数量不满足要求
            return joinActivityLimitDTO;
        }
        int leastAppServiceCount = joinActivityLimitDTO.getLeastAppServiceCount()==null?0:joinActivityLimitDTO.getLeastAppServiceCount();
        if(leastAppServiceCount>0){
            Map<String,Object> qryServiceinfoParam = new HashMap<>();
            qryServiceinfoParam.put("shopId",shopId);
            qryServiceinfoParam.put("flags", ServiceFlagsEnum.CZFW.getFlags());
            qryServiceinfoParam.put("appPublishStatus", ServiceInfoAppPublishStatusEnum.PUBLISHED.getCode());
            int shopAppServiceCount = shopServiceInfoService.selectCount(qryServiceinfoParam);
            if(leastAppServiceCount>shopAppServiceCount){
                joinActivityLimitDTO.setCode("-1");
                joinActivityLimitDTO.setLimitMsg("参加本次活动需要发布"+leastAppServiceCount+"个车主服务。再发布"+(leastAppServiceCount-shopAppServiceCount)+"个车主服务即可参加活动。");
                return joinActivityLimitDTO;
            }
        }
        return joinActivityLimitDTO;
    }

    @Override
    public Result<BarginAppointDataVo> qryBarginAppointData(Long shopId, Long actId) {
        if(shopId==null||actId==null){
            log.error("[consumer-ddlwechat-dubbo]查询微信活动的预约统计信息失败,参数为空shopId:{},actId:{}",shopId,actId);
            return Result.wrapErrorResult("-1","参数为空");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        //.查询活动包含的服务
        Map<String,Object> relSearchMap = new HashMap<>();
        relSearchMap.put("shopActId",actId);
        List<ShopActivityServiceRel> shopActivityServiceRels = shopActivityServiceRelService.select(relSearchMap);
        List<Long> serviceIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopActivityServiceRels)){
            for(ShopActivityServiceRel shopActivityServiceRel:shopActivityServiceRels){
                serviceIds.add(shopActivityServiceRel.getServiceId());
            }
        }
        List<ShopServiceInfo> shopServiceInfoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(serviceIds)){
            shopServiceInfoList = shopServiceInfoService.selectByIds(serviceIds);
        }
        List<ShopActDiscountDTO> shopActDiscountDTOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopServiceInfoList)){
            for(ShopServiceInfo shopServiceInfo:shopServiceInfoList){
                ShopActDiscountDTO shopActDiscountDTO = new ShopActDiscountDTO();
                shopActDiscountDTO.setServiceId(shopServiceInfo.getId().intValue());
                shopActDiscountDTO.setServiceName(shopServiceInfo.getName());
                shopActDiscountDTO.setServiceNum(0);
                shopActDiscountDTOs.add(shopActDiscountDTO);
            }
        }
        BarginAppointDataVo barginAppointDataVo = new BarginAppointDataVo();
        barginAppointDataVo.setShopActDiscountDTOs(shopActDiscountDTOs);
        int durationDay = getActDurationDay(actId);
        barginAppointDataVo.setDurationDay(durationDay);

        //.查询砍价活动内服务的预约统计数据
        com.tqmall.core.common.entity.Result<ShopActDiscountListDTO> result = weChatActBuriedService.shopActDiscountInfo(actId, userGlobalId);
        log.info("[consumer-ddlwechat-dubbo]查询微信活动的预约统计信息,userGlobalId:{},actId:{},success:{}", userGlobalId, actId, result.isSuccess());
        if (result.getData()==null){
            return Result.wrapSuccessfulResult(barginAppointDataVo);
        }
        ShopActDiscountListDTO shopActDiscountListDTO = result.getData();
        barginAppointDataVo.setPartakeNum(shopActDiscountListDTO.getPartakeNum());
        List<ShopActDiscountDTO> shopActDiscountDTOList = shopActDiscountListDTO.getShopActDiscountDTOs();
        if (CollectionUtils.isEmpty(shopActDiscountDTOList)){
            return Result.wrapSuccessfulResult(barginAppointDataVo);
        }
        Map<Integer,ShopActDiscountDTO> shopActDiscountDTOMap = new HashMap<>();
        for (ShopActDiscountDTO shopActDiscountDTO : shopActDiscountDTOList) {
            shopActDiscountDTOMap.put(shopActDiscountDTO.getServiceId(),shopActDiscountDTO);
        }
        for (ShopActDiscountDTO shopActDiscountDTO : shopActDiscountDTOs) {
            ShopActDiscountDTO shopActDiscountDTOFromMap = shopActDiscountDTOMap.get(shopActDiscountDTO.getServiceId());
            if (shopActDiscountDTOFromMap != null) {
                shopActDiscountDTO.setFromOpNum(shopActDiscountDTOFromMap.getFromOpNum());
                shopActDiscountDTO.setPartakeNum(shopActDiscountDTOFromMap.getPartakeNum());
                shopActDiscountDTO.setServiceNum(shopActDiscountDTOFromMap.getServiceNum());
            }
        }
        return Result.wrapSuccessfulResult(barginAppointDataVo);
    }

    /**
     * 获取活动进行时间
     * @param actId
     * @return
     */
    private int getActDurationDay(Long actId){
        ShopActivity shopActivity = shopActivityService.selectById(actId);
        if(shopActivity==null || shopActivity.getStartTime()==null){
            return 0;
        }
        int durationDay = DateUtil.getSpaceByCompareTwoDate(shopActivity.getStartTime(), new Date());
        return durationDay < 0 ? 0 : durationDay;
    }

    @Override
    public Result<Page<DiscountServiceDTO>> qryBarginAppointList(Long shopId, Long actId, Long serviceId, int offset, int limit) {
        if(shopId==null||actId==null||serviceId==null||limit<1){
            log.error("[consumer-ddlwechat-dubbo]查询微信砍价活动内服务预约的用户列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        Integer pageNum = offset/limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<DiscountServiceDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        com.tqmall.core.common.entity.Result<DiscountServicesDTO> result = weChatActBuriedService.discountService(actId, userGlobalId, serviceId, offset, limit);
        log.info("[consumer-ddlwechat-dubbo]查询微信砍价活动的预约统计信息,serviceId:{},userGlobalId:{},actId:{},success:{}",serviceId,userGlobalId,actId,result.isSuccess());
        if(!result.isSuccess()){
            return Result.wrapErrorResult("-1",result.getMessage());
        }
        DiscountServicesDTO discountServicesDTO = result.getData();
        if(discountServicesDTO==null||discountServicesDTO.getServiceNum()==null||discountServicesDTO.getDiscountServiceDTOs()==null){
            return Result.wrapSuccessfulResult(page);
        }
        int count = discountServicesDTO.getServiceNum();
        List<DiscountServiceDTO> discountServiceDTOs = discountServicesDTO.getDiscountServiceDTOs();
        page = new DefaultPage(discountServiceDTOs,pageRequest,count);
        return Result.wrapSuccessfulResult(page);
    }


    @Override
    public boolean whetherServiceInActivity(Long shopId, Long serviceId) {
        Map<String,Object> qryRelParam = new HashMap<>();
        qryRelParam.put("shopId",shopId);
        qryRelParam.put("serviceId",serviceId);
        List<ShopActivityServiceRel> shopActivityServiceRelList = shopActivityServiceRelService.select(qryRelParam);
        if(CollectionUtils.isEmpty(shopActivityServiceRelList)){
            return false;
        }
        Set<Long> actIds = new HashSet<>();
        for (ShopActivityServiceRel shopActivityServiceRel : shopActivityServiceRelList) {
            actIds.add(shopActivityServiceRel.getShopActId());
        }
        Map<String,Object> qryActParam = new HashMap<>();
        qryRelParam.put("shopId",shopId);
        qryRelParam.put("ids",actIds);
        List<ShopActivity> shopActivityList = shopActivityService.select(qryActParam);
        if(CollectionUtils.isEmpty(shopActivityList)){
            return false;
        }
        return true;
    }

    @Override
    public ActivityGroupBuyShareVo qryActivityGroupbugShareData(Long shopId, Long actId) {
        Map<String,Object> relSearchMap = new HashMap<>();
        relSearchMap.put("shopActId",actId);
        relSearchMap.put("shopId",shopId);
        List<ShopActivityServiceRel> shopActivityServiceRels = shopActivityServiceRelService.select(relSearchMap);
        if(CollectionUtils.isEmpty(shopActivityServiceRels)){
            return null;
        }
        Set<Long> serviceIds = new HashSet<>();
        for (ShopActivityServiceRel shopActivityServiceRel : shopActivityServiceRels) {
            serviceIds.add(shopActivityServiceRel.getServiceId());
        }
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllByIds(serviceIds);
        List<ActivityGroupBuyShareDetailDTO> activityGroupBuyShareDetailDTOList = new ArrayList<>();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            ActivityGroupBuyShareDetailDTO activityGroupBuyShareDetailDTO = new ActivityGroupBuyShareDetailDTO();
            activityGroupBuyShareDetailDTO.setServiceId(shopServiceInfo.getId());
            activityGroupBuyShareDetailDTO.setServiceName(shopServiceInfo.getName());
            activityGroupBuyShareDetailDTO.setGroupBuyNum(0);
            activityGroupBuyShareDetailDTO.setJoinNum(0);
            activityGroupBuyShareDetailDTO.setLeaderNum(0);
            activityGroupBuyShareDetailDTOList.add(activityGroupBuyShareDetailDTO);
        }
        int durationDay = getActDurationDay(actId);//活动进行时间
        ActivityGroupBuyShareVo activityGroupBuyShareVo = new ActivityGroupBuyShareVo();
        activityGroupBuyShareVo.setDurationDay(durationDay);
        activityGroupBuyShareVo.setDetail(activityGroupBuyShareDetailDTOList);

        //.查询拼团活动内服务的用户统计数据
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        com.tqmall.core.common.entity.Result<ActivityGroupBuyShareDTO> result = weChatActBuriedService.shopGroupBuyStat(userGlobalId,actId);
        log.info("[consumer-ddlwechat-dubbo]查询拼团活动内服务的用户统计数据,userGlobalId:{},actId:{},success:{}",userGlobalId,actId,result.isSuccess());
        ActivityGroupBuyShareDTO activityGroupBuyShareDTO = result.getData();
        if (activityGroupBuyShareDTO == null) {
            return activityGroupBuyShareVo;
        }
        activityGroupBuyShareVo.setTotalUser(activityGroupBuyShareDTO.getTotalUser());

        List<ActivityGroupBuyShareDetailDTO> detail = activityGroupBuyShareDTO.getDetail();
        if(CollectionUtils.isEmpty(detail)){
            return activityGroupBuyShareVo;
        }

        Map<Long,ActivityGroupBuyShareDetailDTO> detailMap = new HashMap<>();
        for (ActivityGroupBuyShareDetailDTO activityGroupBuyShareDetailDTO : detail) {
            detailMap.put(activityGroupBuyShareDetailDTO.getServiceId(),activityGroupBuyShareDetailDTO);
        }
        for (ActivityGroupBuyShareDetailDTO activityGroupBuyShareDetailDTO : activityGroupBuyShareDetailDTOList) {
            Long serviceId = activityGroupBuyShareDetailDTO.getServiceId();
            ActivityGroupBuyShareDetailDTO detailItem = detailMap.get(serviceId);
            if(detailItem==null){
                continue;
            }
            activityGroupBuyShareDetailDTO.setLeaderNum(detailItem.getLeaderNum());
            activityGroupBuyShareDetailDTO.setJoinNum(detailItem.getJoinNum());
            activityGroupBuyShareDetailDTO.setGroupBuyNum(detailItem.getGroupBuyNum());
        }
        return activityGroupBuyShareVo;
    }

    @Override
    public Page<ActivityGroupBuyUserDetailDTO> qryQroupBuyUserDetail(Long shopId, Long actId, Long serviceId, int offset, int limit) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        GroupBuyUserParam groupBuyUserParam = new GroupBuyUserParam();
        groupBuyUserParam.setShopId(userGlobalId);
        groupBuyUserParam.setActivityId(actId);
        groupBuyUserParam.setServiceId(serviceId);
        groupBuyUserParam.setOffset(offset);
        groupBuyUserParam.setLimit(limit);
        com.tqmall.core.common.entity.Result<ActivityGroupBuyUserDTO> result = weChatActBuriedService.groupBuyUserDetail(groupBuyUserParam);
        log.info("[consumer-ddlwechat-dubbo]微信拼团活动单个服务详细用户列表信息,入參:{},success:{}", LogUtils.objectToString(groupBuyUserParam), result.isSuccess());
        ActivityGroupBuyUserDTO activityGroupBuyUserDTO = result.getData();
        Integer pageNum = offset/limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<ActivityGroupBuyUserDetailDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        if (activityGroupBuyUserDTO == null) {
            return page;
        }
        int count = activityGroupBuyUserDTO.getTotal();
        List<ActivityGroupBuyUserDetailDTO> content = activityGroupBuyUserDTO.getUserDetail();
        page = new DefaultPage(content,pageRequest,count);
        return page;
    }

    @Override
    public BarginCouponDataVo qryBarginCouponData(Long shopId, Long actId) {
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        //.查询砍价活动内服务的预约统计数据
        com.tqmall.core.common.entity.Result<DiscountCouponDTO> result = weChatActBuriedService.DiscountCoupon(userGlobalId, actId);
        log.info("[consumer-ddlwechat-dubbo]查询微信活动的砍券统计信息,userGlobalId:{},actId:{},success:{}", userGlobalId, actId, result.isSuccess());
        BarginCouponDataVo barginCouponDataVo = new BarginCouponDataVo();
        int durationDay = getActDurationDay(actId);
        barginCouponDataVo.setDurationDay(durationDay);
        if (result.getData()==null){
            return barginCouponDataVo;
        }
        DiscountCouponDTO discountCouponDTO = result.getData();
        BeanUtils.copyProperties(discountCouponDTO, barginCouponDataVo);
        List<DiscountCouponInfoDTO> discountCouponInfoDTOs = barginCouponDataVo.getInfo();
        if(CollectionUtils.isEmpty(discountCouponInfoDTOs)){
            return barginCouponDataVo;
        }
        return barginCouponDataVo;
    }

    @Override
    public Page<DiscountServiceDTO> qryBarginCouponUsers(Long shopId, Long actId, Long couponTplId, int offset, int limit) {

        Long userGlobalId = shopService.getUserGlobalId(shopId);
        Integer pageNum = offset/limit;//从0开始
        PageRequest pageRequest = new PageRequest(pageNum, limit);
        Page<DiscountServiceDTO> page = new DefaultPage(new ArrayList(), pageRequest, 0);//默认没有数据的page
        GroupBuyUserParam param=new GroupBuyUserParam();
        param.setActivityId(actId);
        param.setShopId(userGlobalId);
        param.setServiceId(couponTplId);
        param.setOffset(offset);
        param.setLimit(limit);
        com.tqmall.core.common.entity.Result<DiscountCouponUserDTO> result = weChatActBuriedService.DiscountCouponUserDetail(param);
        log.info("[consumer-ddlwechat-dubbo]查询微信砍券活动的用户统计信息,serviceId:{},userGlobalId:{},actId:{},success:{}",couponTplId,userGlobalId,actId,result.isSuccess());
        DiscountCouponUserDTO discountCouponUserDTO = result.getData();
        if(discountCouponUserDTO==null||discountCouponUserDTO.getTotal()==null||discountCouponUserDTO.getInfo()==null){
            return page;
        }
        int count = discountCouponUserDTO.getTotal();
        List<DiscountCouponUserInfoDTO> discountServiceDTOs = discountCouponUserDTO.getInfo();
        page = new DefaultPage(discountServiceDTOs,pageRequest,count);
        return page;
    }

}
