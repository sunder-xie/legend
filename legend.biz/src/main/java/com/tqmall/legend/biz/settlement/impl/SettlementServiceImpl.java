package com.tqmall.legend.biz.settlement.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.bo.dandelion.TaoqiBaseCouponParam;
import com.tqmall.legend.biz.bo.dandelion.TaoqiCouponParam;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.remote.dandelion.RemoteCouponHttp;
import com.tqmall.legend.biz.settlement.ISettlementService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.Shop;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Settlement Service implement
 */
@Service
public class SettlementServiceImpl implements ISettlementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SettlementServiceImpl.class);

    @Autowired
    private RemoteCouponHttp remoteCouponHttp;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderServicesService orderServicesService;

    /**
     * 校验淘汽优惠券
     *
     * @param orderInfo
     * @param taoqiCouponSn
     * @return
     */
    @Override
    public Result checkTaoqiCoupon(OrderInfo orderInfo, String taoqiCouponSn) {
        List<String> itemIds = new ArrayList<String>();
        if (StringUtils.isNotBlank(taoqiCouponSn)) {
            TaoqiBaseCouponParam baseCouponParam = new TaoqiBaseCouponParam();
            baseCouponParam.setCouponCode(taoqiCouponSn);
            baseCouponParam.setLicense(orderInfo.getCarLicense());
            baseCouponParam.setMobile(orderInfo.getContactMobile());
            itemIds = orderServicesService.getTaoqiFirstCateIds(orderInfo.getId());
            if (CollectionUtils.isEmpty(itemIds)) {
                itemIds = new ArrayList<>();
                itemIds.add("0");
            }
            baseCouponParam.setItemIds(itemIds);
            Map<String, Object> resultMap = remoteCouponHttp.couponCheck(baseCouponParam);
            if (CollectionUtils.isEmpty(resultMap)) {
                return Result.wrapErrorResult("", "获取淘汽优惠券失败");
            }
            Object successObj = resultMap.get("success");
            if (successObj == null) {
                LOGGER.error("结算失败！调用dandelion结算优惠券校验接口返回报文有误，接口返回值：" + resultMap);
                return Result.wrapErrorResult("", "获取淘汽优惠券失败");
            }

            boolean success = Boolean.parseBoolean(successObj + "");
            if (!success) {
                LOGGER.error("结算失败！调用dandelion结算优惠券校验接口失败，接口返回值：" + resultMap);
                String errorMsg = (String) resultMap.get("errorMsg");
                if (!StringUtils.isBlank(errorMsg)) {
                    return Result.wrapErrorResult("", errorMsg);
                }
                return Result.wrapErrorResult("", "输入的淘汽代金券无效");
            }
        }
        return Result.wrapSuccessfulResult(itemIds);
    }

    /**
     * 告知车主端优惠券已经使用
     *
     * @param taoqiCouponSn
     * @param orderInfo
     * @param itemIds
     * @return
     */
    @Override
    public Result taoqiCouponPush2CAPP(String taoqiCouponSn, OrderInfo orderInfo, List<String> itemIds) {
        Long shopId = orderInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return Result.wrapErrorResult("", "门店不存在");
        }
        TaoqiCouponParam taoqiCouponParam = new TaoqiCouponParam();
        taoqiCouponParam.setMobile(orderInfo.getContactMobile());
        taoqiCouponParam.setLicense(orderInfo.getCarLicense());
        taoqiCouponParam.setCouponCode(taoqiCouponSn);
        taoqiCouponParam.setSaId(orderInfo.getReceiver() + "");
        taoqiCouponParam.setSaName(orderInfo.getReceiverName());
        taoqiCouponParam.setSaPhone("");
        taoqiCouponParam.setSettleTime(DateUtil.convertDateToYMDHMS(new Date()));
        taoqiCouponParam.setShopId(shopId);
        taoqiCouponParam.setShopPhone(shop.getTel());
        taoqiCouponParam.setWorkOrderId(orderInfo.getId());
        taoqiCouponParam.setItemIds(itemIds);

        // 推送信息实体
        LOGGER.info("2CAPP更新淘汽代金券信息,淘汽代金券实体：{}", taoqiCouponParam.toString());
        Map resultMap = remoteCouponHttp.couponSettle(taoqiCouponParam);
        if (resultMap == null) {
            LOGGER.error("2CAPP更新淘汽代金券信息失败");
        } else if (resultMap.containsKey("success")) {
            Object resultSuccess = resultMap.get("success");
            boolean success = Boolean.parseBoolean(resultSuccess + "");
            if (!success) {
                LOGGER.error("2CAPP更新淘汽代金券信息失败");
            }
        }
        return Result.wrapSuccessfulResult(true);
    }

}
