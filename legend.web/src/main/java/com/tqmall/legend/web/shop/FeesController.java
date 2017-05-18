package com.tqmall.legend.web.shop;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;

/**
 * Created by lixiao on 14-11-16.
 */
@Controller
@RequestMapping("shop/fees")
public class FeesController {

    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    Logger logger = LoggerFactory.getLogger(FeesController.class);

    /**
     * 综合维修单新增费用使用
     *
     * @param shopServiceInfo
     * @param request
     * @return
     */
    @RequestMapping(value = "updateInOrder", method = RequestMethod.POST)
    @ResponseBody
    public Result updateInOrder(@BeanParam ShopServiceInfo shopServiceInfo,
                                HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (shopServiceInfo != null) {

            if (shopServiceInfo.getId() != null && shopServiceInfo.getId() > 0) {
                ShopServiceInfo shopServiceInfo1 = shopServiceInfoService
                        .selectById(shopServiceInfo.getId());
                if (shopServiceInfo1 != null) {
                    logger.info("修改费用" + shopServiceInfo.getName());
                    shopServiceInfo.setModifier(userInfo.getUserId());
                    Result result = shopServiceInfoService.update(
                            shopServiceInfo, userInfo);
                    return Result.wrapSuccessfulResult(result.getData());
                } else {
                    logger.error("费用" + shopServiceInfo.getName() + "失败");
                    return Result.wrapErrorResult("", "操作失败");
                }
            } else {
                logger.info("添加费用" + shopServiceInfo.getName());
                shopServiceInfo.setShopId(userInfo.getShopId());
                shopServiceInfo.setCreator(userInfo.getUserId());
                shopServiceInfo.setModifier(userInfo.getUserId());
                shopServiceInfo.setParentId(0L);
                shopServiceInfo.setSuiteNum(0L);
                shopServiceInfo.setSuiteGoodsNum(0L);
                shopServiceInfo.setTqmallServiceItemId(0L);
                shopServiceInfo.setType(2);
                return shopServiceInfoService.addInOrder(shopServiceInfo, userInfo);

            }

        } else {
            logger.error("费用失败");
            return Result.wrapErrorResult("", "操作失败");
        }

    }

}
