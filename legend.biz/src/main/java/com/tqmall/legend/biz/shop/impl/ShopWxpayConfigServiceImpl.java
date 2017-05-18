package com.tqmall.legend.biz.shop.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopWxpayConfigService;
import com.tqmall.legend.dao.shop.ShopApplyRecordDao;
import com.tqmall.legend.dao.shop.ShopWxpayConfigDao;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.entity.shop.ShopWxpayConfig;
import com.tqmall.legend.object.param.shop.ShopWxpayConfigSaveParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/17.
 */
@Service
@Slf4j
public class ShopWxpayConfigServiceImpl implements ShopWxpayConfigService {

    @Autowired
    private ShopWxpayConfigDao shopWxpayConfigDao;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopApplyRecordDao shopApplyRecordDao;

    @Override
    public List<ShopWxpayConfig> select(Map<String, Object> searchParams) {
        return shopWxpayConfigDao.select(searchParams);
    }

    @Override
    public Integer insert(ShopWxpayConfig shopWxpayConfig) {
        return shopWxpayConfigDao.insert(shopWxpayConfig);
    }

    @Override
    public Integer updateById(ShopWxpayConfig shopWxpayConfig) {
        return shopWxpayConfigDao.updateById(shopWxpayConfig);
    }


    @Override
    @Transactional
    public Result<String> saveWxpayConfig(ShopWxpayConfigSaveParam shopWxpayConfigSaveParam, Long shopId, Long applyRecordId) {

        //更新门店申请记录 - 申请状态
        ShopApplyRecord shopApplyRecordUpt = new ShopApplyRecord();
        shopApplyRecordUpt.setId(applyRecordId);
        //CRM 那边userGlobalId有可能会改变
        shopApplyRecordUpt.setUserGlobalId(shopWxpayConfigSaveParam.getUcShopId());
        shopApplyRecordUpt.setApplyStatus(shopWxpayConfigSaveParam.getApplyStatus());
        shopApplyRecordUpt.setApplyWxMode(shopWxpayConfigSaveParam.getPayMode());
        shopApplyRecordUpt.setApplyAccount(shopWxpayConfigSaveParam.getMchId());
        if (shopApplyRecordDao.updateById(shopApplyRecordUpt) > 0) {
            ShopWxpayConfig shopWxpayConfig = new ShopWxpayConfig();
            shopWxpayConfig.setUserGlobalId(shopWxpayConfigSaveParam.getUcShopId());
            shopWxpayConfig.setAppId(shopWxpayConfigSaveParam.getAppId());
            shopWxpayConfig.setAppSecret(shopWxpayConfigSaveParam.getAppSecret());
            shopWxpayConfig.setApiKey(shopWxpayConfigSaveParam.getApiKey());
            shopWxpayConfig.setMchId(shopWxpayConfigSaveParam.getMchId());
            shopWxpayConfig.setPayMode(shopWxpayConfigSaveParam.getPayMode());

            try {
                //更新
                if (null != shopWxpayConfigSaveParam.getWxpayConfigId() && shopWxpayConfigSaveParam.getWxpayConfigId() > 0) {
                    shopWxpayConfig.setId(shopWxpayConfigSaveParam.getWxpayConfigId());
                    shopWxpayConfigDao.updateById(shopWxpayConfig);
                    return Result.wrapSuccessfulResult("更新成功");
                }
                //新增
                if (null == shopWxpayConfigSaveParam.getWxpayConfigId()) {
                    shopWxpayConfig.setShopId(shopId);
                    shopWxpayConfig.setApplyRecordId(applyRecordId);
                    shopWxpayConfigDao.insert(shopWxpayConfig);
                    return Result.wrapSuccessfulResult("新增成功");
                }
            } catch (Exception e) {
                log.error("[门店支付配置保存] 更新或新增配置失败, e:", e);
                throw new BizException("保存配置失败");
            }
        }
        return Result.wrapErrorResult("", "保存支付配置失败");
    }


}
