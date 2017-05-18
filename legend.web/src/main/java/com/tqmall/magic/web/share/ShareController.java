package com.tqmall.magic.web.share;

import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopTagRelService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.magic.vo.BPShopTagRelVo;
import com.tqmall.legend.facade.magic.vo.ShopProxyServiceVo;
import com.tqmall.legend.facade.order.CommonOrderFacade;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import com.tqmall.legend.facade.order.vo.OrderToProxyVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.partner.ShopPartnerSimParam;
import com.tqmall.magic.object.result.partner.ShopPartnerDTO;
import com.tqmall.magic.service.partner.RpcShopPartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingbao on 16/5/13.
 */
@Controller
@Slf4j
@RequestMapping("share")
public class ShareController extends BaseController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private RpcShopPartnerService rpcShopPartnerService;

    @Autowired
    private CommonOrderFacade commonOrderFacade;

    @Autowired
    private ShopTagRelService shopTagRelService;


    /**
     * 查询共享类型列表
     *
     * @return
     */
    @RequestMapping("/shareTags")
    @ResponseBody
    public Result<List<BPShopTagRelVo>> getShareTags() {
        List<BPShopTagRelVo> result = shopTagRelService.searchBPShopTagRelByName(null);
        //去除当前门店
        Long currentShopId = UserUtils.getShopIdForSession(request);
        if (!CollectionUtils.isEmpty(result)){
            for (BPShopTagRelVo tmp : result){
                if (currentShopId.equals(tmp.getShopId())){
                    result.remove(tmp);
                }
            }
        }
        return Result.wrapSuccessfulResult(result);
    }


    /**
     * 获取门店信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/shopInfo")
    @ResponseBody
    public Result getShopInfo(Long id, Long orderId) {
        log.info("[获取门店信息] Begin getShopInfo.id={},orderId={}", id, orderId);
        if (id == null || id < 0 || orderId == null || orderId < 0) {
            log.error("【获取门店信息】传入的参数错误。id={},orderId={}", id, orderId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARAM_ERROR.getCode(), LegendErrorCode.SHARE_PARAM_ERROR.getErrorMessage(id));
        }
        Shop shop = new Shop();
        try {
            shop = shopService.selectById(id);
        } catch (Exception e) {
            log.error("查询店铺失败", e);
            return Result.wrapErrorResult(LegendErrorCode.SATISFACTION_GET_INFO_ERROR.getCode(), LegendErrorCode.SATISFACTION_GET_INFO_ERROR.getErrorMessage());
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        BigDecimal rate = getPartnerRate(id);
        com.tqmall.legend.common.Result<OrderToProxyVo> result = commonOrderFacade.getProxyOrderInfo(orderId, shopId);
        ShopProxyServiceVo shopProxyServiceVo = new ShopProxyServiceVo();
        shopProxyServiceVo.setShop(shop);
        if (result.isSuccess() && result.getData() != null) {
            if (!CollectionUtils.isEmpty(result.getData().getOrderServicesList())) {
                List<OrderServicesVo> orderServicesVos = result.getData().getOrderServicesList();
                for (OrderServicesVo orderServicesVo : orderServicesVos){
                    BigDecimal proxyAmount = BigDecimal.ZERO;
                    if (orderServicesVo.getSharePrice() != null && orderServicesVo.getServiceHour() != null) {
                        proxyAmount = orderServicesVo.getSharePrice().multiply(orderServicesVo.getServiceHour()).multiply(rate);
                    }
                    orderServicesVo.setProxyAmount(proxyAmount);
                }
                shopProxyServiceVo.setOrderServicesVos(orderServicesVos);

            }
        } else {
            log.error(result.getErrorMsg());
            return Result.wrapErrorResult(LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.getCode(), LegendErrorCode.APP_ORDER_NOTEXSIT_ERROR.getErrorMessage());
        }
        log.info("[获取门店信息]End getShopInfo.shopProxyServiceVo={} ", shopProxyServiceVo);
        return Result.wrapSuccessfulResult(shopProxyServiceVo);
    }

    /**
     * 获取股东或者非股东的比率
     *
     * @param id
     * @return
     */
    private BigDecimal getPartnerRate(Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopPartnerSimParam shopPartnerSimParam = new ShopPartnerSimParam();
        shopPartnerSimParam.setShopId(id);
        shopPartnerSimParam.setPartnerId(shopId);
        Result<List<ShopPartnerDTO>> result = rpcShopPartnerService.getAllPartnerList(shopPartnerSimParam);
        if (result.isSuccess() && result.getData().size() > 0) {
            return result.getData().get(0).getRate();
        }
        Shop shop = shopService.selectById(id);
        return shop.getRate();
    }

    /**
     * 修改委托单服务
     *
     * @param id
     * @param serviceHour
     * @param sharePrice
     * @return
     */
    @RequestMapping("modifyPrice")
    @ResponseBody
    public Result modifyPrice(Long id, BigDecimal serviceHour, BigDecimal sharePrice) {
        log.info("[修改委托单服务价格]Begin modifyPrice.id={},serviceHour={},sharePrice={}", id, serviceHour, sharePrice);
        if (id == null || id < 0) {
            log.error("【修改委托单服务价格】传入的参数错误。id={}", id);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARAM_ERROR.getCode(), LegendErrorCode.SATISFACTION_GET_INFO_ERROR.getErrorMessage(id));
        }
        BigDecimal rate = getPartnerRate(id);
        if (serviceHour != null && sharePrice != null) {
            log.info("[修改委托单服务价格]End modifyPrice.");
            return Result.wrapSuccessfulResult(serviceHour.multiply(sharePrice).multiply(rate));
        }
        log.error("[修改委托单服务价格]修改委托单价格失败");
        return Result.wrapErrorResult(LegendErrorCode.MODIFY_PRICE_ERROR.getCode(), LegendErrorCode.MODIFY_PRICE_ERROR.getErrorMessage());
    }


}



