package com.tqmall.legend.web.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.tqmall.legend.enums.setting.OrderTypeShowStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.web.common.BaseController;

/**
 * Created by lixiao on 15-1-21.
 */
@Controller
@RequestMapping("shop/order/order_type")
public class OrderTypeController extends BaseController{

    @Autowired
    private OrderTypeService orderTypeService;

    /**
     * 获取门店工单类型列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Result list(){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map map = new HashMap<>(3);
        map.put("shopId",shopId);
        map.put("showStatus", OrderTypeShowStatusEnum.SHOW.getCode());
        List<String> sorts = new ArrayList<>();
        sorts.add(" sort asc ");
        map.put("sorts",sorts);
        List<OrderType> orderTypeList = orderTypeService.selectNoCache(map);
        return Result.wrapSuccessfulResult(orderTypeList);
    }
}
