package com.tqmall.legend.biz.activity.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.finance.model.param.OnConsignmentParam;
import com.tqmall.finance.service.consignment.FcOnConsignmentService;
import com.tqmall.legend.biz.activity.ActivityJoinService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.activity.*;
import com.tqmall.legend.dao.shop.ServiceTemplateDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoRelDao;
import com.tqmall.legend.entity.activity.*;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceInfoRel;
import com.tqmall.legend.pojo.activity.ActivityServiceTemplateVo;
import com.tqmall.holy.provider.service.crm.RpcCustomerCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixiao on 16/2/25.
 */
@Slf4j
@Service
public class ActivityJoinServiceImpl implements ActivityJoinService {

    @Autowired
    private ActivityTemplateDao activityTemplateDao;
    @Autowired
    private ActivityTemplateScopeRelDao activityTemplateScopeRelDao;
    @Autowired
    private ActivityTemplateServiceRelDao activityTemplateServiceRelDao;
    @Autowired
    private ServiceTemplateDao serviceTemplateDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopServiceInfoDao shopServiceInfoDao;
    @Autowired
    private ShopActivityDao shopActivityDao;
    @Autowired
    private ShopActivityServiceRelDao shopActivityServiceRelDao;
    @Autowired
    private ShopServiceInfoRelDao shopServiceInfoRelDao;
    @Autowired
    private ShopService shopService;
    @Resource
    private FcOnConsignmentService fcOnConsignmentService;
    @Resource
    private RpcCustomerCommonService rpcCustomerCommonService;
    @Resource
    private SmsService smsService;
    private static final String TAGINFO = "CONSIGNMENT_CUSTOMER";
    //罗丹妮号码
    private static final String CRM_CONTACT = "15267049379";
    //赵永旺手机号
    private static final String YONGWANG_MOBILE = "18368871909";

    @Override
    public List<ActivityServiceTemplateVo> getServiceListByChannelAndShopId(Long channel, Long shopId) {
        Shop shop = shopDao.selectById(shopId);
        List<ActivityTemplate> activityTemplateList = activityTemplateDao.getValidActivityList(channel);
        List<ActivityTemplate> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(activityTemplateList)) {
            for (ActivityTemplate activityTemplate : activityTemplateList) {
                if (activityTemplate.getActScope().equals(0)) {
                    //针对所有门店
                    newList.add(activityTemplate);
                } else if (activityTemplate.getActScope().equals(1)) {
                    //针对指定门店
                    Map searchMap = Maps.newHashMap();
                    searchMap.put("actTplId", activityTemplate.getId());
                    searchMap.put("scopeId", shopId);
                    List<ActivityTemplateScopeRel> relList = activityTemplateScopeRelDao.select(searchMap);
                    if (!CollectionUtils.isEmpty(relList)) {
                        newList.add(activityTemplate);
                    }
                } else if (activityTemplate.getActScope().equals(2)) {
                    //针对指定市
                    Map searchMap = Maps.newHashMap();
                    searchMap.put("actTplId", activityTemplate.getId());
                    searchMap.put("scopeId", shop.getCity());
                    List<ActivityTemplateScopeRel> relList = activityTemplateScopeRelDao.select(searchMap);
                    if (!CollectionUtils.isEmpty(relList)) {
                        newList.add(activityTemplate);
                    }
                }
            }
        }
        List<Long> actTplIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(newList)) {
            return null;
        }
        //活动模板Ids列表
        for (ActivityTemplate activityTemplate : newList) {
            actTplIdList.add(activityTemplate.getId());
        }
        //获取活动套餐关系表
        List<ActivityTemplateServiceRel> activityTemplateServiceRelList = activityTemplateServiceRelDao.selectByActTplIds(actTplIdList);
        if (CollectionUtils.isEmpty(activityTemplateServiceRelList)) {
            return null;
        }
        //获取套餐模板列表
        List<ActivityServiceTemplateVo> list = new ArrayList<>();
        for (ActivityTemplateServiceRel activityTemplateServiceRel : activityTemplateServiceRelList) {
            ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(activityTemplateServiceRel.getServiceTplId());
            if (serviceTemplate != null) {

                // 查询服务模板是否在该门店的区域内
                Map<String, Object> relMap = new HashMap<>();
                relMap.put("templateId", serviceTemplate.getId());
                List<ShopServiceInfoRel> shopServiceInfoRelList = shopServiceInfoRelDao.select(relMap);
                Map<Long, ShopServiceInfoRel> checkMap = new HashMap<>();
                for (ShopServiceInfoRel shopServiceInfoRel : shopServiceInfoRelList) {
                    checkMap.put(shopServiceInfoRel.getCity(), shopServiceInfoRel);
                }
                if ( !checkMap.isEmpty() && ( checkMap.containsKey(shop.getCity()) || checkMap.containsKey(0L))) {
                    ActivityServiceTemplateVo activityServiceTemplateVo = new ActivityServiceTemplateVo();
                    BeanUtils.copyProperties(serviceTemplate, activityServiceTemplateVo);
                    activityServiceTemplateVo.setActTplId(activityTemplateServiceRel.getActTplId());
                    activityServiceTemplateVo.setServiceTplId(activityTemplateServiceRel.getServiceTplId());

                    Map serviceMap = Maps.newHashMap();
                    serviceMap.put("parentId", activityServiceTemplateVo.getServiceTplId());
                    List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.selectAllStatus(serviceMap);

                    Integer joinFlag = 0;
                    if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                            if (shopServiceInfo.getShopId().equals(shopId)) {
                                if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(0)) {
                                    //活动无效且待审核
                                    joinFlag = 1;
                                    break;
                                }
                                if (shopServiceInfo.getStatus().equals(0l) && shopServiceInfo.getAuditStatus().equals(1)) {
                                    //活动有效且审核通过
                                    joinFlag = 2;
                                    break;
                                }
                                if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(2)) {
                                    //活动无效且审核不通过
                                    joinFlag = 3;
                                    activityServiceTemplateVo.setAuditReason(shopServiceInfo.getAuditReason());
                                    break;
                                }
                                if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(3)) {
                                    //活动无效且退出活动
                                    joinFlag = 0;
                                    break;
                                }

                            }
                        }
                    }
                    activityServiceTemplateVo.setJoinFlag(joinFlag);
                    Integer joinNum = activityServiceTemplateVo.getVirtualJoinNum() + shopServiceInfoList.size();
                    activityServiceTemplateVo.setJoinNum(joinNum);
                    list.add(activityServiceTemplateVo);
                }
            }
        }
        return list;
    }

    @Override
    public ActivityServiceTemplateVo getServiceTpl(Long actTplId, Long serviceTplId, Long shopId) {
        Shop shop = shopDao.selectById(shopId);
        ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(serviceTplId);
        if (serviceTemplate != null) {
            // 查询服务模板是否在该门店的区域内
            Map<String, Object> relMap = new HashMap<>();
            relMap.put("templateId", serviceTemplate.getId());
            List<ShopServiceInfoRel> shopServiceInfoRelList = shopServiceInfoRelDao.select(relMap);
            Map<Long, ShopServiceInfoRel> checkMap = new HashMap<>();
            for (ShopServiceInfoRel shopServiceInfoRel : shopServiceInfoRelList) {
                checkMap.put(shopServiceInfoRel.getCity(), shopServiceInfoRel);
            }
            if (checkMap.containsKey(shop.getCity()) || checkMap.containsKey(0L)) {
                ActivityServiceTemplateVo activityServiceTemplateVo = new ActivityServiceTemplateVo();
                BeanUtils.copyProperties(serviceTemplate, activityServiceTemplateVo);
                activityServiceTemplateVo.setActTplId(actTplId);
                activityServiceTemplateVo.setServiceTplId(serviceTplId);

                Map serviceMap = Maps.newHashMap();
                serviceMap.put("parentId", activityServiceTemplateVo.getServiceTplId());
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.selectAllStatus(serviceMap);

                Integer joinFlag = 0;
                if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                    for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                        if (shopServiceInfo.getShopId().equals(shopId)) {
                            activityServiceTemplateVo.setShopServiceInfo(shopServiceInfo);
                            if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(0)) {
                                //活动无效且待审核
                                joinFlag = 1;
                                break;
                            }
                            if (shopServiceInfo.getStatus().equals(0l) && shopServiceInfo.getAuditStatus().equals(1)) {
                                //活动有效且审核通过
                                joinFlag = 2;
                                break;
                            }
                            if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(2)) {
                                //活动无效且审核不通过
                                joinFlag = 3;
                                activityServiceTemplateVo.setAuditReason(shopServiceInfo.getAuditReason());
                                break;
                            }
                            if (shopServiceInfo.getStatus().equals(-1l) && shopServiceInfo.getAuditStatus().equals(3)) {
                                //活动无效且退出活动
                                joinFlag = 0;
                                break;
                            }
                        }
                    }
                }
                activityServiceTemplateVo.setJoinFlag(joinFlag);
                Integer joinNum = activityServiceTemplateVo.getVirtualJoinNum() + shopServiceInfoList.size();
                activityServiceTemplateVo.setJoinNum(joinNum);
                return activityServiceTemplateVo;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Result joinActivity(Long actTplId, Long serviceTplId, BigDecimal servicePrice, String shopReason, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();

        ActivityTemplate activityTemplate = activityTemplateDao.selectById(actTplId);
        if (activityTemplate == null) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(serviceTplId);
        if (serviceTemplate == null) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        //活动模板->活动
        Long shopActId = 0l;
        Map<String, Object> actMap = Maps.newHashMap();
        actMap.put("actTplId", actTplId);
        actMap.put("shopId", shopId);
        List<ShopActivity> shopActivityList = shopActivityDao.select(actMap);
        if (CollectionUtils.isEmpty(shopActivityList)) {
            ShopActivity shopActivity = new ShopActivity();
            BeanUtils.copyProperties(activityTemplate, shopActivity);
            shopActivity.setActTplId(activityTemplate.getId());
            shopActivity.setShopId(shopId);
            shopActivity.setCreator(userInfo.getUserId());
            shopActivity.setModifier(userInfo.getUserId());
            shopActivityDao.insert(shopActivity);
            shopActId = shopActivity.getId();
        } else {
            ShopActivity shopActivity = shopActivityList.get(0);
            shopActId = shopActivity.getId();
        }
        //套餐模板->模板
        Long serviceId = 0l;
        Map<String, Object> serviceMap = Maps.newHashMap();
        serviceMap.put("parentId", serviceTplId);
        serviceMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.selectAllStatus(serviceMap);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            ShopServiceInfo shopServiceInfo = new ShopServiceInfo();
            BeanUtils.copyProperties(serviceTemplate, shopServiceInfo);
            shopServiceInfo.setType(1);//常规服务
            shopServiceInfo.setCategoryId(serviceTemplate.getCateId());
            shopServiceInfo.setParentId(serviceTplId);
            shopServiceInfo.setShopId(shopId);
            shopServiceInfo.setCreator(userInfo.getUserId());
            shopServiceInfo.setModifier(userInfo.getUserId());

            if (activityTemplate.getIsNeedAudit().equals(0)) {
                //不需要审核
                shopServiceInfo.setAuditStatus(1);
                shopServiceInfo.setAuditPassTime(new Date());
                shopServiceInfo.setStatus(0l);
            } else if (activityTemplate.getIsNeedAudit().equals(1)) {
                //需要审核
                shopServiceInfo.setAuditStatus(0);
                shopServiceInfo.setStatus(-1l);
            }
            if (shopReason != null) {
                shopServiceInfo.setShopReason(shopReason);
            }
            if (servicePrice != null) {
                shopServiceInfo.setServicePrice(servicePrice);
            }
            shopServiceInfoDao.insert(shopServiceInfo);
            serviceId = shopServiceInfo.getId();
        } else {
            ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
            if (!shopServiceInfo.getStatus().equals(0l)) {
                if (activityTemplate.getIsNeedAudit().equals(0)) {
                    //不需要审核
                    shopServiceInfo.setAuditStatus(1);
                    shopServiceInfo.setAuditPassTime(new Date());
                    shopServiceInfo.setStatus(0l);
                } else if (activityTemplate.getIsNeedAudit().equals(1)) {
                    //需要审核
                    shopServiceInfo.setAuditStatus(0);
                    shopServiceInfo.setStatus(-1l);
                }
                shopServiceInfo.setModifier(userInfo.getUserId());
                if (shopReason != null) {
                    shopServiceInfo.setShopReason(shopReason);
                }
                if (servicePrice != null) {
                    shopServiceInfo.setServicePrice(servicePrice);
                }
                shopServiceInfoDao.updateById(shopServiceInfo);
            }
            serviceId = shopServiceInfo.getId();
        }
        //建立活动、服务之间的关系
        Map<String, Object> relMap = Maps.newHashMap();
        relMap.put("shopActId", shopActId);
        relMap.put("serviceId", serviceId);
        List<ShopActivityServiceRel> shopActivityServiceRelList = shopActivityServiceRelDao.select(relMap);
        if (CollectionUtils.isEmpty(shopActivityServiceRelList)) {
            ShopActivityServiceRel shopActivityServiceRel = new ShopActivityServiceRel();
            shopActivityServiceRel.setServiceId(serviceId);
            shopActivityServiceRel.setShopActId(shopActId);
            shopActivityServiceRelDao.insert(shopActivityServiceRel);
        }

        if (Constants.JSFW.equals(serviceTemplate.getFlags())) {

            //寄售活动将额度传给账务中心
            try {
                Boolean flag = sendQuotaToFinance(userInfo, activityTemplate);
                if (!flag) {
                    throw new RuntimeException("寄售活动账务中心保存额度失败");
                }
            } catch (RuntimeException e) {
                log.error("寄售活动账务中心接口异常,错误:{}", e);
                throw new RuntimeException("寄售活动账务中心接口异常");
            }
            //寄售活动给CRM标识用户参加活动
            makeTagToCrm(userInfo);

        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 退出活动
     *
     * @param actTplId
     * @param serviceTplId
     * @param shopReason
     * @param userInfo
     * @return
     */
    @Override
    @Transactional
    public Result exitActivity(Long actTplId, Long serviceTplId, String shopReason, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();

        ActivityTemplate activityTemplate = activityTemplateDao.selectById(actTplId);
        if (activityTemplate == null) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        ServiceTemplate serviceTemplate = serviceTemplateDao.selectById(serviceTplId);
        if (serviceTemplate == null) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        Map<String, Object> serviceMap = Maps.newHashMap();
        serviceMap.put("parentId", serviceTplId);
        serviceMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoDao.selectAllStatus(serviceMap);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
        shopServiceInfo.setStatus(-1l);
        shopServiceInfo.setAuditStatus(3);
        if (shopReason != null) {
            shopServiceInfo.setShopReason(shopReason);
        }
        shopServiceInfoDao.updateById(shopServiceInfo);
        //若为寄售活动
        if (Constants.JSFW.equals(serviceTemplate.getFlags())) {
            Shop shop = selectShop(userInfo);
            String ucShopId = shop.getUserGlobalId();
            exitConsignment(userInfo.getUserId(), ucShopId);
        }
        //若为天安保险,需要发送短信给赵永旺
        if (ActJumpUrlEnum.TAFW.getCode().longValue()== activityTemplate.getId()){
            SmsBase smsBase = new SmsBase();
            smsBase.setAction(Constants.LEGEND_MARKETING_SMS_TPL);
            smsBase.setMobile(YONGWANG_MOBILE);
            Map<String , Object> param = new HashMap<>();
            StringBuffer content = new StringBuffer();
            Shop shop = shopService.selectById(shopId);
            content.append("有店铺正在退出天安活动:店铺ID为[").append(shopId).append("],").append("店铺名称为:[").append(shop.getName()).append("],").append("联系手机号:[").append(shop.getMobile()).append("],").append("退出理由为:[").append(shopReason).append("]");
            param.put("content",content);
            smsBase.setData(param);
            smsService.sendMsg(smsBase,"天安活动店铺退出活动");
        }
        return Result.wrapSuccessfulResult(true);
    }

    //寄售活动将额度传给账务中心
    private Boolean sendQuotaToFinance(UserInfo userInfo, ActivityTemplate activityTemplate) throws RuntimeException {
        Long amount = Long.decode(activityTemplate.getByName());
        OnConsignmentParam consignmentParam = new OnConsignmentParam();
        String ucShopId = selectShop(userInfo).getUserGlobalId();
        consignmentParam.setShopId(Integer.parseInt(ucShopId));
        consignmentParam.setAmount(BigDecimal.valueOf(amount));
        consignmentParam.setUid(userInfo.getUserId().intValue());
        log.info("[Dubbo]: 调用更新账务中心寄售活动额度接口,入参:{}", LogUtils.objectToString(consignmentParam));
        com.tqmall.core.common.entity.Result result = fcOnConsignmentService.saveOnConsignment(Constants.CUST_SOURCE, consignmentParam);
        log.info("[Dubbo]: 调用更新账务中心寄售活动额度接口,返回:{}", result.isSuccess());
        return result.isSuccess();
    }

    //店铺参加活动,crm打标
    private void makeTagToCrm(UserInfo userInfo) {
        Shop shop = selectShop(userInfo);
        Long UcShopId = Long.valueOf(shop.getUserGlobalId());
        log.info("[Dubbo]: 调用CRM寄售活动打标接口user_global_id:{}", UcShopId);
        try {
            com.tqmall.core.common.entity.Result result = rpcCustomerCommonService.markTagInfo(UcShopId, TAGINFO, Constants.CUST_SOURCE);
            log.info("[Dubbo]: 调用CRM打标接口寄售活动额度接口返回:{}", result.isSuccess());
            if (!result.isSuccess()) {
                sendMessageToCrm(shop);
            }
        } catch (Exception e) {
            log.error("[Dubbo]: 调用CRM打标接口寄售活动额度接口错误:{}", e);
            sendMessageToCrm(shop);
        }

    }


    //通过shopId 获取shop
    private Shop selectShop(UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        return shop;
    }

    //发送通知短信
    private void sendMessageToCrm(Shop shop) {
        SmsBase smsBase = new SmsBase();
        smsBase.setAction(Constants.LEGEND_MARKETING_SMS_TPL);
        smsBase.setMobile(CRM_CONTACT);
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("content", "【通 知】手机号码: " + shop.getMobile() + " ,参加寄售活动打标失败,请手动处理");
        smsBase.setData(smsMap);
        smsService.sendMsg(smsBase, "CRM打标失败通知短信");
    }

    //退出活动
    private void exitConsignment(Long uid, String ucShopId) {
        if (StringUtils.isBlank(ucShopId) || "0".equals(ucShopId)) {
            throw new BizException("门店信息有误,寄售活动退出失败");
        }
        Integer shopId = Integer.parseInt(ucShopId);
        OnConsignmentParam param = new OnConsignmentParam();
        param.setShopId(shopId);
        param.setUid(uid.intValue());
        log.info("[Dubbo]: 调用账务中心退出活动接口,入参:{}", LogUtils.objectToString(param));
        com.tqmall.core.common.entity.Result result = fcOnConsignmentService.cleanAmount(Constants.CUST_SOURCE, param);
        if (!result.isSuccess()) {
            log.error("[Dubbo]: 调用账务中心退出活动接口失败:{}", LogUtils.objectToString(result));
            throw new BizException("寄售活动退出失败");
        }
    }
}
