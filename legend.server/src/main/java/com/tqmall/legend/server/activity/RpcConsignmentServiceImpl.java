package com.tqmall.legend.server.activity;


import com.tqmall.common.Constants;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.activity.ActivityTemplateScopeRelService;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ActivityTemplateScopeRel;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.service.activity.RpcConsignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by sven on 16/5/10.
 */
@Slf4j
@Service("rpcConsignmentService")
public class RpcConsignmentServiceImpl implements RpcConsignmentService {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ActivityTemplateService activityTemplateService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private ActivityTemplateScopeRelService activityTemplateScopeRelService;


    @Override
    public Result<String> getAgreements(Long ucShopId, String source) {
        Result result;
        if (ucShopId == null) {
            log.error("【dubbo】：请求系统:{},寄售活动获取寄售协议,门店ID不存在", source);
            return translate(LegendErrorCode.SHOP_ERROR.newResult(""));
        }
        log.info("【dubbo】：请求系统:{},寄售活动获取寄售协议,入参门店ID:{}", source, ucShopId);
        Map<String, Object> param = new HashMap<>();
        param.put("userGlobalId", ucShopId);
        List<Shop> list = shopService.select(param);
        if (CollectionUtils.isEmpty(list)) {
            log.error("【dubbo】：寄售活动获取寄售协议,门店:{}信息不存在", ucShopId);
            return translate(LegendErrorCode.SHOP_ERROR.newResult(""));
        }
        param.clear();
        param.put("flags", Constants.JSFW);
        param.put("shopId", list.get(0).getId());
        List<ShopServiceInfo> shopInfoList = shopServiceInfoService.select(param);
        if (CollectionUtils.isEmpty(shopInfoList) || shopInfoList.get(0).getParentId() <= 0) {
            log.error("【dubbo】：请寄售活动获取寄售协议,无法获取到寄售协议编号");
            return translate(LegendErrorCode.GET_PARENT_ID_FAIL.newDataResult(""));
        }
        ServiceTemplate serviceTemplate = serviceTemplateService.selectById(shopInfoList.get(0).getParentId());
        if (serviceTemplate == null) {
            log.error("【dubbo】：寄售活动获取寄售协议,寄售协议信息不存在");
            return translate(LegendErrorCode.GET_AGREEMENTS_FAIL.newDataResult(""));
        }
        result = Result.wrapSuccessfulResult(serviceTemplate.getAgreement());
        log.info("【dubbo】：请求系统:{},寄售活动获取协议,返回参数信息: code:{},data:{},message:{}", source, result.getCode(),result.getData(),result.getMessage());
        return result;
    }

    @Override
    @Transactional
    public Result setFundQuota(Long ucShopId, Integer quota, String source) {
        if (ucShopId == null) {
            log.error("【dubbo】：请求系统:{},寄售活动获取共建基金金额,门店不存在", source);
            return translate(LegendErrorCode.SHOP_ERROR.newResult(""));
        }
        log.info("【dubbo】：请求系统:{},寄售活动获取共建基金金额start,入参门店ID:{},金额:{}", source, ucShopId, quota);
        Map<String, Object> param = new HashMap<>();
        param.put("userGlobalId", ucShopId);
        List<Shop> list = shopService.select(param);
        if (CollectionUtils.isEmpty(list)) {
            log.error("【dubbo】：寄售活动获取共建基金金额,门店:{}信息不存在", ucShopId);
            return translate(LegendErrorCode.SHOP_ERROR.newResult(""));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("byName", quota);
        List<ActivityTemplate> templateList = activityTemplateService.select(map);
        if (CollectionUtils.isEmpty(templateList)) {
            log.error("【dubbo】：寄售活动获取共建基金金额,{}额度信息不存在", quota);
            return translate(LegendErrorCode.GET_QUOTA_INFO_FAIL.newDataResult(""));
        }
        Long actTplId = templateList.get(0).getId();
        ActivityTemplateScopeRel rel = new ActivityTemplateScopeRel();
        rel.setActTplId(actTplId);
        rel.setScopeId(list.get(0).getId());
        boolean ExistFlag = existQuotaFromCrm(rel);
        if (ExistFlag) {
            log.error("【dubbo】：寄售活动获取共建基金金额,{}额度信息已存在", quota);
            return translate(LegendErrorCode.SET_QUOTA_INFO_FAIL.newDataResult(""));
        }
        log.info("寄售活动插入记录至activity_template_scope_rel");
        activityTemplateScopeRelService.insert(rel);
        log.info("【dubbo】：请求系统:{},寄售活动获取共建基金金额成功", source);
        return Result.wrapSuccessfulResult("");
    }

    public Result translate(com.tqmall.zenith.errorcode.support.Result result) {
        return Result.wrapErrorResult(result.getCode(), result.getMessage());
    }
//校验crm传入的该基金数据是不是已经存在
    private boolean existQuotaFromCrm(ActivityTemplateScopeRel activityTemplateScopeRel) {
        Map<String, Object> map = new HashMap<>();
        map.put("actTplId", activityTemplateScopeRel.getActTplId());
        map.put("scopeId", activityTemplateScopeRel.getScopeId());
        int size = activityTemplateScopeRelService.selectCount(map);
        if (size > 0) {
            return true;
        }
        return false;
    }
}
