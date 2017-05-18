package com.tqmall.legend.facade.shop.impl;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.agreement.ShopSignAgreementService;
import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.agreement.ShopSignAgreement;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.entity.shop.ShopApplyStatusEnum;
import com.tqmall.legend.enums.shop.ShopAgreementStatusEnum;
import com.tqmall.legend.enums.shop.ShopJoinStatusEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.shop.ShopFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/8/12.
 */
@Slf4j
@Service
public class ShopFacadeImpl implements ShopFacade {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopApplyRecordService shopApplyRecordService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private ShopSignAgreementService shopSignAgreementService;

    /**
     * 同意协议
     *
     * @param shopId
     * @param userInfo
     * @return
     */
    @Override
    public Result<Boolean> agree(Long shopId, UserInfo userInfo) {
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return Result.wrapErrorResult(LegendErrorCode.SATISFACTION_SHOP_NOT_EXIST.getCode(), "门店不存在");
        }
        Integer agreementStatus = shop.getAgreementStatus();
        if (agreementStatus.equals(ShopAgreementStatusEnum.NO.getCode())) {
            shop.setAgreementStatus(ShopAgreementStatusEnum.YES.getCode());
            try {
                Result result = shopService.updateById(shop);
                // 如果level是10、11、12中的任意一种，则插入签订协议记录
                int level = shop.getLevel().intValue();
                if (level == ShopLevelEnum.BASE.getValue() || level == ShopLevelEnum.STANDARD.getValue() || level == ShopLevelEnum.PROFESSION.getValue()) {
                    insertSignAgreement(userInfo, shop);
                }
                if (result.isSuccess()) {
                    return Result.wrapSuccessfulResult(true);
                }
            } catch (Exception e) {
                log.error("【档口版门店同意协议】：更新门店数据，出现异常", e);
            }
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), LegendErrorCode.SYSTEM_ERROR.getErrorMessage());
        }
        return Result.wrapSuccessfulResult(true);
    }

    private void insertSignAgreement(UserInfo userInfo, Shop shop) {
        ShopSignAgreement shopSignAgreement = new ShopSignAgreement();
        shopSignAgreement.setAgreementAddress("");
        shopSignAgreement.setOperateId(userInfo.getUserId());
        shopSignAgreement.setOperateName(userInfo.getName());
        shopSignAgreement.setShopId(shop.getId());
        shopSignAgreement.setUserGlobalId(StringUtils.isBlank(shop.getUserGlobalId())? 0l : Long.parseLong(shop.getUserGlobalId()));
        shopSignAgreement.setSignTime(new Date());
        shopSignAgreement.setCreator(userInfo.getUserId());
        shopSignAgreementService.add(shopSignAgreement);
    }


    @Override
    public Boolean getShopApplyStatusIsSuccess(Long shopId) {

        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return false;
        }
        Map<String, Object> recordParams = new HashMap<>();
        recordParams.put("shopId", shop.getId());
        List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(recordParams);
        if (CollectionUtils.isEmpty(shopApplyRecordList)) {
            return false;
        }
        ShopApplyRecord shopApplyRecord = shopApplyRecordList.get(0);
        if (ShopApplyStatusEnum.CSTG.getCode().equals(shopApplyRecord.getApplyStatus())) {
            log.info("[门店支付申请是否已'测试通过'] 门店支付申请已测试通过, id:{}, applyStatus:{}", shopApplyRecord.getId(), shopApplyRecord.getApplyStatus());
            return true;
        }
        return false;
    }

    @Override
    public void setShopJoinStatus(int joinStatus, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            throw new BizException("不存在id为" + shopId + "的门店");
        }
        Integer oldStatus = shop.getJoinStatus();
        if (joinStatus == oldStatus) {
            //新老值一致,不需要处理直接返回
            return;
        }
        if (ShopJoinStatusEnum.YES.getCode().equals(joinStatus)) {//加入委托
            //初始化服务
            shopServiceInfoFacade.initBpService(shopId, userInfo.getUserId());
            //刷新缓存
            cacheComponent.reload(CacheKeyConstant.SERVICE_CATEGORY);
        }
        shop.setJoinStatus(joinStatus);
        shop.setModifier(userInfo.getUserId());
        shopService.updateById(shop);
    }
}
