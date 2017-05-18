package com.tqmall.yunxiu.web.pub;

import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.pub.shop.ShopVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2015-07-06.
 * C-APP 获得店铺信息controller
 */
@Controller
@RequestMapping("pub/shop")
public class ShopListController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ShopListController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderInfoService orderInfoService;


    /**create by jason 2015-07-28
     * 获得所有服务过的店铺
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Result getShopList(@RequestParam(value = "mobile",required = true)String mobile) {
        if ( StringUtil.isStringEmpty(mobile) || "null".equals(mobile)) {
            return Result.wrapErrorResult("", "手机号不正确!");
        }
        Map map = new HashMap(1);
        map.put("contactMobile",mobile);
        List<OrderInfo> orderInfoList = orderInfoService.selectByContactMobileAndLicense(map);
        List<ShopVo> shopList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(orderInfoList)) {
            List shopIdList = new ArrayList();
            for (OrderInfo orderInfo : orderInfoList) {
                ShopVo shopVo = new ShopVo();
                Long shopId = orderInfo.getShopId();
                if (null != shopId) {
                    //店铺ID相同的取一次
                    if (!shopIdList.contains(shopId)) {
                        shopIdList.add(shopId);
                        //店铺
                        Shop shop = shopService.selectById(shopId);
                        if (shop != null) {
                            shopVo.setUserGlobalId(shop.getUserGlobalId());//shopId对应全局的ID
                            shopVo.setTime(orderInfo.getGmtCreate());//工单创建时间
                            shopList.add(shopVo);
                        }
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(shopList)) {
            return Result.wrapSuccessfulResult(shopList);
        } else {
            return Result.wrapErrorResult("","数据为空!");
        }
    }

    /**
     * create by jason 2015-11-03
     * 根据shopId或者userGlobalId 获取cityId
     *
     */
    @RequestMapping(value = "city")
    @ResponseBody
    public Result getCityId(@RequestParam(value = "shopId", required = false) Long shopId,
                            @RequestParam(value = "userGlobalId", required = false) String userGlobalId) {
        logger.info("根据shopId或者userGlobalId获取shop对应的cityId,shopId:{},userGlobalId:{}", shopId, userGlobalId);

        if (!StringUtils.isEmpty(userGlobalId)) {
            Map map = new HashMap(2);
            map.put("userGlobalId", userGlobalId);
            List<Shop> shopList = shopService.select(map);
            if (!CollectionUtils.isEmpty(shopList)) {
                return Result.wrapSuccessfulResult(shopList.get(0).getCity());
            } else {
                return Result.wrapErrorResult("-1", "参数输入有误,请确认参数");
            }
        } else if (null != shopId) {
            Shop shop = shopService.selectById(shopId);
            if (null != shop) {
                return Result.wrapSuccessfulResult(shop.getCity());
            } else {
                return Result.wrapErrorResult("-1", "参数输入有误,请确认参数");
            }
        }
        return Result.wrapErrorResult("-1", "参数输入有误,请确认参数");
    }

}
