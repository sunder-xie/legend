package com.tqmall.legend.server.appoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointAppVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.object.param.appoint.AppAppointParam;
import com.tqmall.legend.object.param.appoint.AppointSearchParams;
import com.tqmall.legend.object.result.appoint.AppointResultDTO;
import com.tqmall.legend.object.result.appoint.AppointServiceDTO;
import com.tqmall.legend.object.result.appoint.ShopServiceDTO;
import com.tqmall.legend.service.appoint.RpcAppAppointService;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsy on 16/1/12.
 */
@Slf4j
@Service("rpcAppAppointService")
public class RpcAppAppointServiceImpl implements RpcAppAppointService {
    @Autowired
    private ShopService shopService;
    @Autowired
    private AppointService appointService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ActivityTemplateService activityTemplateService;

    /**
     * 根据活动id校验活动是否有效
     *
     * @param source
     * @param actTplId
     * @return
     */
    @Override
    public Result<Boolean> check(String source, Integer actTplId) {
        log.info("【dubbo】：校验活动是否有效，来源：{}，活动id：{}", source, actTplId);
        Result result;
        String logInfo = "【dubbo】：校验活动是否有效，返回数据{}";
        if (StringUtils.isBlank(source)) {
            result = LegendErrorCode.DUBBO_SOURCE_NULL_EX.newClsResult(Boolean.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        if (actTplId == null || actTplId <= 0) {
            result = LegendErrorCode.SERVICETPLID_NULL_ERROR.newClsResult(Boolean.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId.longValue());
        if (activityTemplate == null || !activityTemplate.getActStatus().equals(2)) {
            result = LegendErrorCode.ACTIVITY_NOT_EXIST.newClsResult(Boolean.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        Date startTime = activityTemplate.getStartTime();
        Date endTime = activityTemplate.getEndTime();
        try {
            int start = DateUtil.daysBetween(startTime, new Date());
            int end = DateUtil.daysBetween(endTime, new Date());
            if (start >= 0 && end <= 0) {
                result = Result.wrapSuccessfulResult(true);
                log.info(logInfo, LogUtils.objectToString(result));
                return result;
            } else {
                result = Result.wrapSuccessfulResult(false);
                log.info(logInfo, LogUtils.objectToString(result));
                return result;
            }
        } catch (ParseException e) {
            log.error("【dubbo】:判断活动是否有效时间转换异常", e);
        }
        result = LegendErrorCode.DATE_FORMAT_ERROR.newClsResult(Boolean.class);
        log.info(logInfo, LogUtils.objectToString(result));
        return result;
    }

    /**
     * 根据模板服务id获取参加报名的门店
     *
     * @return
     */
    @Override
    public Result<AppointServiceDTO> getServiceList(AppointSearchParams appointSearchParams) {
        Result result;
        String logInfo = "【dubbo】：根据模板服务id获取参加报名的门店，返回数据{}";
        if (appointSearchParams == null) {
            result = LegendErrorCode.DUBBO_PARAM_NULL_EX.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        String source = appointSearchParams.getSource();
        Integer serviceTplId = appointSearchParams.getServiceTplId();
        Integer city = appointSearchParams.getCity();
        Integer priceSort = appointSearchParams.getPriceSort();
        log.info("【dubbo】：根据模板服务id获取参加报名的门店，来源：{}，参数对象：{}", source, appointSearchParams);
        if (StringUtils.isBlank(source)) {
            result = LegendErrorCode.DUBBO_SOURCE_NULL_EX.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        if (serviceTplId == null || serviceTplId <= 0) {
            result = LegendErrorCode.SERVICETPLID_NULL_ERROR.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        //查询有效的服务
        Map<String, Object> serviceMap = Maps.newHashMap();
        serviceMap.put("parentId", serviceTplId);
        serviceMap.put("status", 0);
        List<String> sorts = Lists.newArrayList();
        if (priceSort == 1) {
            sorts.add("service_price asc");//升序
        } else {
            sorts.add("service_price desc");//降序
        }
        serviceMap.put("sorts", sorts);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(serviceMap);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            result = LegendErrorCode.SHOP_JOIN_ERROR.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        //查询正式门店
        Map<String, Object> shopMap = Maps.newHashMap();
        Set<Long> shopIds = Sets.newHashSet();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            Long shopId = shopServiceInfo.getShopId();
            shopIds.add(shopId);
        }
        shopMap.put("ids", shopIds);
        shopMap.put("shopStatus", 1);
        shopMap.put("city", city);
        List<Shop> shopList = shopService.select(shopMap);
        if (CollectionUtils.isEmpty(shopList)) {
            result = LegendErrorCode.SHOP_JOIN_ERROR.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        Map<Long, Shop> shopListMap = Maps.newHashMap();
        for (Shop shop : shopList) {
            Long id = shop.getId();
            shopListMap.put(id, shop);
        }
        //返回数据
        AppointServiceDTO appointServiceDTO = new AppointServiceDTO();
        List<ShopServiceDTO> shopServiceDTOList = Lists.newArrayList();
        boolean flags = false;
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            Long shopId = shopServiceInfo.getShopId();
            if (shopListMap.containsKey(shopId)) {
                flags = true;
                Shop shop = shopListMap.get(shopId);
                ShopServiceDTO shopServiceDTO = new ShopServiceDTO();
                shopServiceDTO.setShopId(shopId);
                BeanUtils.copyProperties(shop, shopServiceDTO);
                shopServiceDTO.setServicePrice(shopServiceInfo.getServicePrice());
                shopServiceDTO.setPriceType(shopServiceInfo.getPriceType().intValue());
                shopServiceDTO.setServiceId(shopServiceInfo.getId());
                shopServiceDTO.setServiceTplId(shopServiceInfo.getParentId());
                shopServiceDTOList.add(shopServiceDTO);
                //设置数据
                appointServiceDTO.setName(shopServiceInfo.getName());
                appointServiceDTO.setImgUrl(shopServiceInfo.getImgUrl());
                appointServiceDTO.setServiceNote(shopServiceInfo.getServiceNote());
                appointServiceDTO.setShopServiceDTOList(shopServiceDTOList);
            }
        }
        if (flags) {
            result = Result.wrapSuccessfulResult(appointServiceDTO);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        } else {
            result = LegendErrorCode.SHOP_JOIN_ERROR.newClsResult(AppointServiceDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
    }

    /**
     * app端活动页面下预约单
     *
     * @param appAppointParam
     * @return
     */
    @Override
    public Result<AppointResultDTO> insertAppAppoint(AppAppointParam appAppointParam) {
        Result result;
        String logInfo = "【dubbo:app端下预约单】：返回数据{}";
        if (appAppointParam == null) {
            result = LegendErrorCode.DUBBO_PARAM_NULL_EX.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        String source = appAppointParam.getSource();
        log.info("【dubbo:app端下预约单】：来源：{}，接收参数{}", source, appAppointParam);
        if (StringUtils.isBlank(source)) {
            result = LegendErrorCode.DUBBO_SOURCE_NULL_EX.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        Long shopId = appAppointParam.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            String userGlobalId = appAppointParam.getUserGlobalId();
            //正式门店userGlobalId大于0
            if (StringUtils.isNotBlank(userGlobalId)) {
                Map<String, Object> searchMap = Maps.newHashMap();
                searchMap.put("userGlobalId", userGlobalId);
                List<Shop> shopList = shopService.select(searchMap);
                if (!CollectionUtils.isEmpty(shopList)) {
                    shop = shopList.get(0);
                }
            }
        }
        if (shop == null) {
            result = LegendErrorCode.SHOP_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        Long serviceId = appAppointParam.getServiceId();
        if (serviceId != null && serviceId < 0) {
            result = LegendErrorCode.APPOINT_SERVICEID_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        if (serviceId == null) {
            Long templateId = appAppointParam.getTemplateId();
            if (templateId == null || templateId <= 0) {
                result = LegendErrorCode.SERVICETPLID_NULL_ERROR.newClsResult(AppointResultDTO.class);
                log.info(logInfo, LogUtils.objectToString(result));
                return result;
            }
            Map<String, Object> serviceMap = Maps.newHashMap();
            serviceMap.put("parentId", templateId);
            serviceMap.put("shopId", shop.getId());
            serviceMap.put("status", 0);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(serviceMap);
            if (CollectionUtils.isEmpty(shopServiceInfoList)) {
                result = LegendErrorCode.SERVICE_NOT_EXSIT.newClsResult(AppointResultDTO.class);
                log.info(logInfo, LogUtils.objectToString(result));
                return result;
            }
            ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
            appAppointParam.setServiceId(shopServiceInfo.getId());
        }
        Date appointTime = appAppointParam.getAppointTime();
        if (appointTime == null) {
            result = LegendErrorCode.APPOINTDATE_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        Long channel = appAppointParam.getChannel();
        if (channel == null || channel.compareTo(0l) <= 0) {
            result = LegendErrorCode.APPOINT_CHANNEL_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        String mobile = appAppointParam.getMobile();
        if (!StringUtil.isMobileNO(mobile)) {
            result = LegendErrorCode.MOBILE_NO_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
        try {
            AppointAppVo appointAppVo = new AppointAppVo();
            BeanUtils.copyProperties(appAppointParam, appointAppVo);
            appointAppVo.setUserGlobalId(shop.getUserGlobalId());
            com.tqmall.legend.common.Result resultTemp = appointService.addAppointApp(appointAppVo);
            log.info("【dubbo:app端下预约单】：调到通用添加预约单接口，返回数据{}", LogUtils.objectToString(resultTemp));
            if (resultTemp.isSuccess()) {
                Long appointId = (Long) resultTemp.getData();
                //查询预约单信息
                Appoint appoint = appointService.selectById(appointId);
                AppointResultDTO appointResultDTO = new AppointResultDTO();
                BeanUtils.copyProperties(appoint, appointResultDTO);
                appointResultDTO.setAppointAddress(shop.getAddress());
                result = Result.wrapSuccessfulResult(appointResultDTO);
                log.info(logInfo, LogUtils.objectToString(result));
                return result;
            }
            result = LegendErrorCode.INSERT_APPOINT_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        } catch (BeansException e) {
            log.error("【dubbo:app端下预约单】：出现异常", e);
            result = LegendErrorCode.SAVE_APPOINT_ERROR.newClsResult(AppointResultDTO.class);
            log.info(logInfo, LogUtils.objectToString(result));
            return result;
        }
    }

    /**
     * @param appointSn 预约单编号
     * @return 如果原来数据就是支付成功的不需要更新直接返回success
     */
    @Override
    public com.tqmall.core.common.entity.Result<Boolean> updatePayStatus(String appointSn, Long shopId) {
        log.info("[dubbo]预约单支付成功状态更新appointSn:{},shopId:{}", appointSn, shopId);
        Map<String, Object> param = new HashMap<>();
        param.put("appointSn", appointSn);
        param.put("shopId", shopId);
        List<Appoint> appointList = appointService.select(param);
        if (CollectionUtils.isEmpty(appointList)) {
            log.error("[更新预约单状态错误]:预约单编号为 {} 的预约单不存在", appointSn);
            return com.tqmall.core.common.entity.Result.wrapErrorResult("", "编号为:" + appointSn + " 的预约单不存在");
        }
        Appoint appoint = appointList.get(0);
        if (1 == appoint.getPayStatus()) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(true);
        }
        appoint.setPayStatus(1);
        appointService.updateById(appoint);
        //.预约单支付成功后的消息处理
        appointService.sendAppointMsg(appoint);
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(true);
    }
}