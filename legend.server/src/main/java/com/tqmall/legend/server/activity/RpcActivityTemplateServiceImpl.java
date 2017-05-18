package com.tqmall.legend.server.activity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.activity.ActivityChannelService;
import com.tqmall.legend.biz.activity.ActivityTemplateScopeRelService;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.ActivityTemplateServiceRelService;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.activity.ActivityChannel;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;
import com.tqmall.legend.entity.activity.ActivityTemplateServiceRel;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.activity.ActivityChannelSourceEnum;
import com.tqmall.legend.object.param.activity.ActivityTemplateBean;
import com.tqmall.legend.object.param.activity.ActivityTemplateParam;
import com.tqmall.legend.object.param.activity.ActivityTemplateScopeRelParam;
import com.tqmall.legend.object.result.activity.ActivityTemplateDTO;
import com.tqmall.legend.object.result.activity.ActivityTemplatePageDTO;
import com.tqmall.legend.object.result.appoint.ShopServiceDTO;
import com.tqmall.legend.object.result.service.ServiceTemplateDTO;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import com.tqmall.legend.service.activity.RpcActivityTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/5/30.
 */
@Slf4j
@Service("rpcActivityTemplateService")
public class RpcActivityTemplateServiceImpl implements RpcActivityTemplateService {
    @Autowired
    private ActivityTemplateService activityTemplateService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private ActivityTemplateServiceRelService activityTemplateServiceRelService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ActivityTemplateScopeRelService activityTemplateScopeRelService;
    @Autowired
    private ActivityChannelService activityChannelService;

    /**
     * 根据模板活动id获取活动详情
     *
     * @param source
     * @param actTplId
     * @return
     */
    @Override
    public Result<ActivityTemplateDTO> getActTplInfoByActTplId(String source, Long actTplId) {
        log.info("【dubbo:根据活动id获取活动详情】：来源：{},活动id：{}", source,actTplId);
        Result checkParamsResult = checkParams(source, actTplId);
        if(!checkParamsResult.isSuccess()){
            log.info("【dubbo:根据活动id获取活动详情】：返回值：{}", JSONUtil.object2Json(checkParamsResult));
            return checkParamsResult;
        }
        if (checkParamsResult.getData() == null) {
            log.info("【dubbo:根据活动id获取活动详情】：返回值：{}", JSONUtil.object2Json(checkParamsResult));
            return Result.wrapSuccessfulResult(null);
        }
        ActivityTemplate activityTemplate = (ActivityTemplate)checkParamsResult.getData();
        ActivityTemplateDTO activityTemplateDTO = new ActivityTemplateDTO();
        try {
            BeanUtils.copyProperties(activityTemplate, activityTemplateDTO);
            log.info("【dubbo:根据活动id获取活动详情】：返回值：{}", JSONUtil.object2Json(activityTemplateDTO));
            return Result.wrapSuccessfulResult(activityTemplateDTO);
        } catch (BeansException e) {
            log.error("【dubbo:根据活动id获取活动详情】：对象拷贝异常",e);
        }
        String errorMsg = LegendErrorCode.SYSTEM_ERROR.getErrorMessage();
        log.info("【dubbo:根据活动id获取活动详情】：返回值：{}", errorMsg);
        return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), errorMsg);
    }

    /**
     * 根据模板活动id获取模板服务list
     *
     * @param source
     * @param actTplId
     * @return
     */
    @Override
    public Result<List<ServiceTemplateDTO>> getServiceTplListByActTplId(String source, Long actTplId) {
        log.info("【dubbo:根据活动id获取活动套餐列表】：来源：{},活动id：{}", source,actTplId);
        Result checkParamsResult = checkParams(source, actTplId);
        if(!checkParamsResult.isSuccess()){
            log.info("【dubbo:根据活动id获取活动套餐列表】：返回值：{}", JSONUtil.object2Json(checkParamsResult));
            return checkParamsResult;
        }
        List<ServiceTemplateDTO> serviceTemplateDTOList = Lists.newArrayList();
        //获取模板套餐list
        List<ServiceTemplate> serviceTemplateList = getServiceTemplateList(actTplId);
        if(!CollectionUtils.isEmpty(serviceTemplateList)){
            try {
                for(ServiceTemplate serviceTemplate : serviceTemplateList){
                    ServiceTemplateDTO serviceTemplateDTO = new ServiceTemplateDTO();
                    BeanUtils.copyProperties(serviceTemplate,serviceTemplateDTO);
                    serviceTemplateDTOList.add(serviceTemplateDTO);
                }
            } catch (BeansException e) {
                log.error("【dubbo:根据活动id获取活动套餐列表】：对象拷贝异常",e);
            }
        }
        log.info("【dubbo:根据活动id获取活动套餐列表】：返回值：{}", serviceTemplateDTOList);
        return Result.wrapSuccessfulResult(serviceTemplateDTOList);
    }

    /**
     * 参数校验
     * @param source
     * @param actTplId
     * @return
     */
    private Result checkParams(String source, Long actTplId){
        if(StringUtils.isBlank(source)){
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(),LegendErrorCode.DUBBO_SOURCE_NULL_EX.getErrorMessage());
        }
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
        if("6".equals(source)){//6微信活动模版可以直接查出来
            return Result.wrapSuccessfulResult(activityTemplate);
        }
        if(activityTemplate == null){
            return Result.wrapErrorResult(LegendErrorCode.ACTIVITY_NOT_EXIST.getCode(),LegendErrorCode.ACTIVITY_NOT_EXIST.getErrorMessage());
        }
        if(activityTemplate.getActStatus() != 2){
            return Result.wrapErrorResult(LegendErrorCode.ACTIVITY_NOT_EXIST.getCode(),LegendErrorCode.ACTIVITY_NOT_EXIST.getErrorMessage());
        }
        long startTime = activityTemplate.getStartTime().getTime();
        long endTime = activityTemplate.getEndTime().getTime();
        long nowTime = new Date().getTime();
        if(startTime > nowTime  || nowTime > endTime){
            return Result.wrapErrorResult(LegendErrorCode.ACTIVITY_FAIL.getCode(),LegendErrorCode.ACTIVITY_FAIL.getErrorMessage());
        }
        return Result.wrapSuccessfulResult(activityTemplate);
    }

    private Result checkParamsWithoutTime(String source, Long actTplId){
        if(StringUtils.isBlank(source)){
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(),LegendErrorCode.DUBBO_SOURCE_NULL_EX.getErrorMessage());
        }
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
        if(activityTemplate == null){
            return Result.wrapErrorResult(LegendErrorCode.ACTIVITY_NOT_EXIST.getCode(),LegendErrorCode.ACTIVITY_NOT_EXIST.getErrorMessage());
        }
        if(activityTemplate.getActStatus() != 2){
            return Result.wrapErrorResult(LegendErrorCode.ACTIVITY_NOT_EXIST.getCode(),LegendErrorCode.ACTIVITY_NOT_EXIST.getErrorMessage());
        }
        return Result.wrapSuccessfulResult(activityTemplate);
    }

    /**
     * 根据模板活动id、uc的shopId获取门店参加报名的服务列表
     * @param source
     * @param actTplId
     * @param ucShopId
     * @return
     */
    @Override
    public Result<List<ShopServiceInfoDTO>> getShopServiceInfo(String source, Long actTplId, Integer ucShopId) {
        log.info("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：来源：{},活动id：{},userGlobalId：{}", source,actTplId,ucShopId);
        Result checkParamsResult = checkParams(source, actTplId);
        if(!checkParamsResult.isSuccess()){
            log.info("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：返回值：{}", JSONUtil.object2Json(checkParamsResult));
            return checkParamsResult;
        }
        List<ShopServiceInfoDTO> shopServiceInfoDTOList = Lists.newArrayList();//返回对象list
        //获取模板套餐list
        List<ServiceTemplate> serviceTemplateList = getServiceTemplateList(actTplId);
        if(!CollectionUtils.isEmpty(serviceTemplateList)){
            String errorMsg;
            List<Long> parentIds = Lists.newArrayList();
            for(ServiceTemplate serviceTemplate : serviceTemplateList){
                parentIds.add(serviceTemplate.getId());
            }
            if(ucShopId == null){
                errorMsg = "ucShopId不能为空";
                log.info("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：返回值：{}", errorMsg);
                return Result.wrapErrorResult(LegendErrorCode.SHOP_ERROR.getCode(),errorMsg);
            }
            Map<String,Object> searchMap = Maps.newHashMap();
            searchMap.put("userGlobalId", ucShopId);
            List<Shop> shopList = shopService.select(searchMap);
            if(CollectionUtils.isEmpty(shopList)){
                errorMsg = LegendErrorCode.SHOP_ERROR.getErrorMessage();
                log.info("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：返回值：{}", errorMsg);
                return Result.wrapErrorResult(LegendErrorCode.SHOP_ERROR.getCode(),errorMsg);
            }
            Shop shop = shopList.get(0);
            Long shopId = shop.getId();
            Map<String,Object> serviceSearchMap = Maps.newHashMap();
            serviceSearchMap.put("shopId",shopId);
            serviceSearchMap.put("status",0);
            serviceSearchMap.put("parentIds",parentIds);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(serviceSearchMap);
            if(!CollectionUtils.isEmpty(serviceTemplateList)){
                try {
                    for(ShopServiceInfo shopServiceInfo : shopServiceInfoList){
                        ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
                        shopServiceInfoDTO.setSort(shopServiceInfo.getSort().intValue());
                        shopServiceInfoDTO.setPriceType(shopServiceInfo.getPriceType().intValue());
                        shopServiceInfoDTO.setCateId(shopServiceInfo.getCategoryId());
                        shopServiceInfoDTO.setStatus(shopServiceInfo.getStatus().intValue());
                        BeanUtils.copyProperties(shopServiceInfo,shopServiceInfoDTO);
                        shopServiceInfoDTOList.add(shopServiceInfoDTO);
                    }
                } catch (BeansException e) {
                    log.error("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：对象转换异常",e);
                }
            }
        }
        log.info("【dubbo:根据模板活动id、uc的shopId获取门店参加报名的服务列表】：返回值：{}", shopServiceInfoDTOList);
        return  Result.wrapSuccessfulResult(shopServiceInfoDTOList);
    }

    @Override
    public Result<List<ShopServiceDTO>> getShopByActTplId(String source, Long actTplId, Integer city) {
        log.info("【dubbo:根据活动id获取参加报名的门店列表】：来源：{},活动id：{}", source,actTplId);
        Result checkParamsResult = checkParamsWithoutTime(source, actTplId);
        if(!checkParamsResult.isSuccess()){
            log.info("【dubbo:根据活动id获取参加报名的门店列表】：返回值：{}", JSONUtil.object2Json(checkParamsResult));
            return checkParamsResult;
        }
        List<ShopServiceDTO> shopServiceDTOList = Lists.newArrayList();
        //查询活动关联的服务
        Map<String,Object> searchServiceRelMap = Maps.newHashMap();
        searchServiceRelMap.put("actTplId",actTplId);
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = activityTemplateServiceRelService.select(searchServiceRelMap);
        if(!CollectionUtils.isEmpty(activityTemplateServiceRelList)){
            //查询门店服务
            List<Long> parentIds = Lists.newArrayList();
            for(ActivityTemplateServiceRel activityTemplateServiceRel : activityTemplateServiceRelList){
                Long patentId = activityTemplateServiceRel.getServiceTplId();
                parentIds.add(patentId);
            }
            Map<String,Object> serviceSearchMap = Maps.newHashMap();
            serviceSearchMap.put("status",0);
            serviceSearchMap.put("parentIds",parentIds);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(serviceSearchMap);
            if(!CollectionUtils.isEmpty(shopServiceInfoList)){
                List<Long> shopIds = Lists.newArrayList();
                for(ShopServiceInfo shopServiceInfo : shopServiceInfoList){
                    Long shopId = shopServiceInfo.getShopId();
                    shopIds.add(shopId);
                }
                //正式门店
                Map<String,Object> searchMap = Maps.newHashMap();
                searchMap.put("shopStatus",1);
                searchMap.put("city", city);
                searchMap.put("shopIds", shopIds);
                List<Shop> shopList = shopService.select(searchMap);
                try {
                    for(Shop shop : shopList){
                        ShopServiceDTO shopServiceDTO = new ShopServiceDTO();
                        BeanUtils.copyProperties(shop,shopServiceDTO);
                        shopServiceDTO.setShopId(shop.getId());
                        shopServiceDTOList.add(shopServiceDTO);
                    }
                } catch (BeansException e) {
                    log.info("【dubbo:根据活动id获取参加报名的门店列表】：对象拷贝异常",e);
                }
            }
        }
        log.info("【dubbo:根据活动id获取参加报名的门店列表】：返回值：{}", shopServiceDTOList);
        return Result.wrapSuccessfulResult(shopServiceDTOList);
    }

    /**
     * 获取模板套餐list
     * @param actTplId
     * @return
     */
    private List<ServiceTemplate> getServiceTemplateList(Long actTplId) {
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("actTplId",actTplId);
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = activityTemplateServiceRelService.select(searchMap);
        List<ServiceTemplate> serviceTemplateList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(activityTemplateServiceRelList)){
            List<Long> serviceTplIds = Lists.newArrayList();
            for(ActivityTemplateServiceRel activityTemplateServiceRel : activityTemplateServiceRelList){
                Long serviceTplId = activityTemplateServiceRel.getServiceTplId();
                serviceTplIds.add(serviceTplId);
            }
            Map<String,Object> serviceTplSearchMap = Maps.newHashMap();
            serviceTplSearchMap.put("ids",serviceTplIds);
            serviceTplSearchMap.put("status",0);//有效
            serviceTemplateList = serviceTemplateService.select(serviceTplSearchMap);
        }
        return serviceTemplateList;
    }

    @Override
    public Result<ActivityTemplateBean> saveActivityTemplate(ActivityTemplateBean activityTemplateBean) {
        if(activityTemplateBean==null){
            log.error("[dubbo]保存活动模版失败,参数为空",activityTemplateBean);
            return Result.wrapErrorResult("-1","参数为空");
        }
        ActivityTemplate activityTemplate = new ActivityTemplate();
        BeanUtils.copyProperties(activityTemplateBean,activityTemplate);
        int efftiveCount = 0;
        if(activityTemplateBean.getId()!=null){
            //更新
            efftiveCount = activityTemplateService.update(activityTemplate);
        } else{
            efftiveCount = activityTemplateService.add(activityTemplate);
        }
        if(efftiveCount<1){
            return Result.wrapErrorResult("-1","操作失败");
        }else{
            ActivityTemplateBean retActivityTemplateBean = new ActivityTemplateBean();
            BeanUtils.copyProperties(activityTemplate,retActivityTemplateBean);
            return Result.wrapSuccessfulResult(retActivityTemplateBean);
        }
    }

    @Override
    public Result<String> delActivityTemplate(ActivityTemplateBean activityTemplateBean) {
        if(activityTemplateBean==null ||activityTemplateBean.getId()==null){
            log.error("[dubbo]删除活动模版,参数错误{}",activityTemplateBean);
            return Result.wrapErrorResult("-1","参数错误");
        }
        Long activityTemplateId = activityTemplateBean.getId();
        //.参数校验(只能删除mega渠道上的活动模版)
        ActivityTemplate activityTemplate = activityTemplateService.getById(activityTemplateId);
        if(activityTemplate==null){
            return Result.wrapErrorResult("-1","没有对应的活动模版");
        }
        if(activityTemplate.getChannel()==null){
            return Result.wrapErrorResult("-1","待删除的模版没有渠道编号");
        }
        Long channelId = activityTemplate.getChannel();
        ActivityChannel activityChannel = activityChannelService.selectById(channelId);
        if(activityChannel==null){
            return Result.wrapErrorResult("-1","待删除的模版却少渠道信息");
        }
        if(activityChannel.getChannelSource()==null||activityChannel.getChannelSource().longValue()!=ActivityChannelSourceEnum.MEGA.getValue()){
            //不能删除非mega系统来源渠道上的活动模版
            return Result.wrapErrorResult("-1","无权删除此模版");
        }

        //.删除模板
        Result<String> result = null;
        if(activityTemplateService.deleteById(activityTemplateId)==0){
            result = Result.wrapErrorResult("-1","删除失败");
        }
        result = Result.wrapSuccessfulResult("操作成功");
        log.info("[dubbo']删除活动模版,{}", LogUtils.funToString(activityTemplateBean,result));
        return result;
    }

    @Override
    public Result<ActivityTemplatePageDTO> qryActivityTemplatePage(ActivityTemplateParam param) {
        if(param==null){
            log.error("[dubbo]查询活动模版列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        ActivityTemplatePageDTO activityTemplatePageDTO = new ActivityTemplatePageDTO();
        int offset = 0;
        int limit = 10;
        List<ActivityTemplateDTO> content = new ArrayList<>();
        if(param.getOffset()!=null){
            offset = param.getOffset();
        }
        if(param.getLimit()!=null){
            limit = param.getLimit();
        }
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("offset",offset);
        searchMap.put("limit",limit);
        if(!CollectionUtils.isEmpty(param.getSorts())){
            searchMap.put("sorts",param.getSorts());
        }
        if(param.getId()!=null){
            searchMap.put("id",param.getId());
        }
        if(!CollectionUtils.isEmpty(param.getIds())){
            searchMap.put("ids",param.getIds());
        }
        if(StringUtils.isNotEmpty(param.getActName())){
            searchMap.put("actNameLike",param.getActName());
        }
        searchMap.put("actName",param.getActNameExact());
        if(param.getStartTime()!=null){
            searchMap.put("startTimeGt",param.getStartTime());
        }
        if(param.getEndTime()!=null){
            searchMap.put("endTimeLt",param.getEndTime());
        }
        if(param.getActStatus()!=null){
            searchMap.put("actStatus",param.getActStatus());
        }
        if(param.getChannel()!=null){
            searchMap.put("channel",param.getChannel());
        }
        Integer total = activityTemplateService.selectCount(searchMap);
        List<ActivityTemplate> activityTemplateList = activityTemplateService.select(searchMap);
        if(!CollectionUtils.isEmpty(activityTemplateList)){
            for(ActivityTemplate activityTemplate:activityTemplateList){
                ActivityTemplateDTO activityTemplateDTO = new ActivityTemplateDTO();
                BeanUtils.copyProperties(activityTemplate,activityTemplateDTO);
                content.add(activityTemplateDTO);
            }
        }
        activityTemplatePageDTO.setTotal(total);
        activityTemplatePageDTO.setContent(content);
        log.info("[dubbo']查询活动模版列表,{}", LogUtils.funToString(param,activityTemplatePageDTO));
        return Result.wrapSuccessfulResult(activityTemplatePageDTO);
    }

    @Override
    public Result<String> addActivityTemplateScopeRel(ActivityTemplateScopeRelParam param) {
        if(param==null){
            log.error("[dubbo]设置活动模板范围失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        Long actTplId = param.getActTplId();
        if (actTplId==null) {
            log.error("[dubbo]设置活动模板范围失败,模版id为空{}",param);
            return Result.wrapErrorResult("-1","模版id为空");
        }
        List<Long> userGlobalIds = param.getUserGlobalIds();
        List<Long> cityIds = param.getCityIds();
        List<ActivityTemplateScopeRel> activityTemplateScopeRelList = new ArrayList<>();
        List<Long> scopeIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userGlobalIds)) {
            Map<String,Object> shopSearchMap = new HashMap<>();
            shopSearchMap.put("userGlobalIds",userGlobalIds);
            List<Shop> shopList = shopService.select(shopSearchMap);
            if (!CollectionUtils.isEmpty(shopList)) {
                for (Shop shop:shopList) {
                    scopeIds.add(shop.getId());
                    ActivityTemplateScopeRel activityTemplateScopeRel = new ActivityTemplateScopeRel();
                    activityTemplateScopeRel.setActTplId(actTplId);
                    activityTemplateScopeRel.setScopeId(shop.getId());
                    activityTemplateScopeRelList.add(activityTemplateScopeRel);
                }
            }

        }
        if (!CollectionUtils.isEmpty(cityIds)) {
            for (Long cityId:cityIds) {
                scopeIds.add(cityId);
                ActivityTemplateScopeRel activityTemplateScopeRel = new ActivityTemplateScopeRel();
                activityTemplateScopeRel.setActTplId(actTplId);
                activityTemplateScopeRel.setScopeId(cityId);
                activityTemplateScopeRelList.add(activityTemplateScopeRel);
            }
        }
        //删除已经存在的范围关系
        if(!CollectionUtils.isEmpty(scopeIds)){
            Map<String,Object> deleteParam =  new HashMap<>();
            deleteParam.put("actTplId",actTplId);
            deleteParam.put("scopeIds",scopeIds);
            activityTemplateScopeRelService.delete(deleteParam);
        }
        if (!CollectionUtils.isEmpty(activityTemplateScopeRelList)) {
            int count = activityTemplateScopeRelService.batchInsert(activityTemplateScopeRelList);
            log.info("[dubbo]设置活动模板范围{}",LogUtils.funToString(activityTemplateScopeRelList,count));
        }
        return Result.wrapSuccessfulResult("操作成功");
    }

    @Override
    public Result<String> saveActivityTemplateServiceRel(Long actTplId, List<Long> serviceTplIds) {
        if(actTplId==null||CollectionUtils.isEmpty(serviceTplIds)){
            log.error("[dubbo]保存服务模版和服务模版关系失败,有参数为空actTplId:{},serviceTplIds:{}",actTplId,serviceTplIds);
            return Result.wrapErrorResult("-1","参数为空");
        }
        //.删除原有的关系
        Map<String,Object> param = new HashMap<>();
        param.put("actTplId",actTplId);
        activityTemplateServiceRelService.delete(param);
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = new ArrayList<>();
        for(Long serviceTpl:serviceTplIds){
            ActivityTemplateServiceRel activityTemplateServiceRel = new ActivityTemplateServiceRel();
            activityTemplateServiceRel.setActTplId(actTplId);
            activityTemplateServiceRel.setServiceTplId(serviceTpl);
            activityTemplateServiceRelList.add(activityTemplateServiceRel);
        }
        int count = 0;
        //.新增关系
        if(!CollectionUtils.isEmpty(activityTemplateServiceRelList)){
            count = activityTemplateServiceRelService.batchInsert(activityTemplateServiceRelList);
            log.info("[dubbo]保存服务模版和服务模版关系",LogUtils.funToString(activityTemplateServiceRelList,count));
        }
        if(count>0){
            return Result.wrapSuccessfulResult("成功");
        } else{
            return Result.wrapErrorResult("-1","保存失败");
        }
    }
}
