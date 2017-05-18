package com.tqmall.legend.web.setting;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.facade.setting.OrderTypeFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/11/7.
 * 工单业务类型设置
 */
@Slf4j
@Controller
@RequestMapping("shop/setting/order-type")
public class OrderTypeSettingController extends BaseController {

    @Autowired
    private OrderTypeFacade orderTypeFacade;

    /**
     * 工单业务类型页面
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String index(Model model) {
        //查询门店已存在的业务类型
        Long shopId = UserUtils.getShopIdForSession(request);
        List<OrderType> orderTypeList = orderTypeFacade.getOrderTypeByShopId(shopId);
        model.addAttribute("orderTypeList", orderTypeList);
        return "yqx/page/setting/order-type";
    }

    /**
     * 更新工单类型
     *
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody OrderType orderType) {
        if (orderType == null || StringUtils.isBlank(orderType.getName())) {
            return Result.wrapErrorResult("", "业务类型为空");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Long userId = UserUtils.getUserIdForSession(request);
        if(orderType.getId() == null){
            //新增，需要校验是否存在
            String name = orderType.getName();
            OrderType checkOrderType = orderTypeFacade.getOrderTypeNoCacheByShopIdAndName(shopId, orderType.getName());
            if (checkOrderType != null) {
                return Result.wrapErrorResult("", "业务类型【" + name + "】已存在，无效添加");
            }
            orderType.setShopId(shopId);
            orderType.setCreator(userId);
        }
        orderTypeFacade.updateOrderType(orderType, userId);
        return Result.wrapSuccessfulResult(true);
    }
}
