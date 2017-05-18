package com.tqmall.legend.biz.settlement.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.settlement.OnlinePaymentService;
import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.entity.shop.ShopApplyStatusEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/18.
 */
@Service
@Slf4j
public class OnlinePaymentServiceImpl implements OnlinePaymentService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopApplyRecordService shopApplyRecordService;
    @Autowired
    private WeChatShopService weChatShopService;

    @Override
    public Result<String> applyOnlinePayment(Long shopId) {

        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            log.info("[微信支付申请]获取门店信息失败, [参数]shopId:{}, [结果]shop==null", shopId);
            return Result.wrapErrorResult("", "门店信息错误");
        }
        if (StringUtils.isBlank(shop.getUserGlobalId())) {
            log.info("[微信支付申请]获取门店信息有误, userGlobalId为空 [参数]shopId:{}, [结果]shop:{}", shopId, shop);
            return Result.wrapErrorResult("", "门店信息错误");
        }
        Integer shopStatus;
        try {
            shopStatus = wxShopStatus(Long.valueOf(shop.getUserGlobalId()));
        } catch (BizException biz) {
            return Result.wrapErrorResult("", biz.getMessage());
        } catch (Exception e) {
            log.error("[微信支付申请] 获取门店公众号信息失败 ucShopId={}", shop.getUserGlobalId());
            return Result.wrapErrorResult("", "系统异常，请稍后重试");
        }
        if (shopStatus == null) {
            return Result.wrapErrorResult("20028000", "您尚未开通微信公众号，请先申请开通微信公众号后再申请开通微信支付。");
        }
        if (shopStatus.intValue() != ShopWechatStatusEnum.REGISTERED.getValue()) {
            return Result.wrapErrorResult("20028001", "您的微信公众号申请正在处理中，待微信公众号开通成功后再申请微信支付。");
        }
        //校验门店是否已申请支付
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("shopId", shopId);
        List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(searchParams);
        if (!CollectionUtils.isEmpty(shopApplyRecordList)) {
            if (shopApplyRecordList.size() > 1) {
                log.info("[微信支付申请] 提交申请失败,申请记录有多条, [参数]shopId:{}, [结果]shopApplyRecordList:{}", shopId, shopApplyRecordList);
                return Result.wrapErrorResult("", "申请记录有多条");
            }
            ShopApplyRecord shopApplyRecord = shopApplyRecordList.get(0);
            Integer applyStatus = shopApplyRecord.getApplyStatus();
            //上次记录申请状态为"中断"的可以再次申请,其他状态不能再次提交申请
            if (!ShopApplyStatusEnum.ZD.getCode().equals(applyStatus)) {
                log.info("[微信支付申请] 门店微信支付已申请, shopId:{}, shopApplyRecordList:{}", shopId, shopApplyRecordList);
                return Result.wrapErrorResult("", "门店已申请");
            }
            //更新
            _saveApplyRecord(true, shop, shopApplyRecord.getId());
            return Result.wrapSuccessfulResult("申请成功");
        } else {
            //新增
            _saveApplyRecord(false, shop, null);
            return Result.wrapSuccessfulResult("申请成功");
        }
    }

    /**
     * @param isUpdate true : 更新;  false: 新增
     * @param shop
     * @param applyRecordId 若是更新,原记录id
     */
    private void _saveApplyRecord(boolean isUpdate, Shop shop, Long applyRecordId) {
        Long shopId = shop.getId();
        Long userGlobalId = Long.parseLong(shop.getUserGlobalId());
        ShopApplyRecord shopApplyRecord = new ShopApplyRecord();
        shopApplyRecord.setShopName(shop.getName());
        shopApplyRecord.setContactName(shop.getContact());
        shopApplyRecord.setContactMobile(shop.getMobile());
        shopApplyRecord.setShopTel(shop.getTel());
        //默认 申请中
        shopApplyRecord.setApplyStatus(ShopApplyStatusEnum.SQZ.getCode());
        //当前只用 微信支付
        shopApplyRecord.setApplyType(0);
        //默认申请微信支付模式为 非受理模式
        shopApplyRecord.setApplyWxMode(0);
        if (isUpdate) {
            //更新
            shopApplyRecord.setId(applyRecordId);
            shopApplyRecord.setFailReason("");
            if (shopApplyRecordService.updateById(shopApplyRecord) > 0) {
                log.info("[微信支付申请] 门店微信支付申请更新记录成功, shopId:{}; 记录id:{} ", shopId, shopApplyRecord.getId());
            }
        } else {
            //新增
            shopApplyRecord.setShopId(shopId);
            shopApplyRecord.setUserGlobalId(userGlobalId);
            if (shopApplyRecordService.insert(shopApplyRecord) > 0) {
                log.info("[微信支付申请] 门店微信支付申请新增记录成功, shopId:{}; 记录id:{} ", shopId, shopApplyRecord.getId());
            }
        }
    }

    private Integer wxShopStatus(Long ucShopId){
        Result<ShopDTO> result = weChatShopService.selectShopByUcShopId(ucShopId);
        if (result == null) {
            throw new BizException("系统异常，请稍后重试");
        }
        if (!result.isSuccess()) {
            return null;//返回错误表示门店未申请微信公众号
        }
        ShopDTO shopDTO = result.getData();
        if (shopDTO == null) {
            throw new BizException("门店公众号信息不存在");
        }
        Integer shopStatus = shopDTO.getShopStatus();
        if (shopStatus == null) {
            throw new BizException("门店公众号申请状态错误");
        }
        return shopStatus;
    }

}
