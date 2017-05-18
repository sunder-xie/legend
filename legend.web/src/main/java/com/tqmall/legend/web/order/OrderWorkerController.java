package com.tqmall.legend.web.order;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.order.OrderServicesFacade;
import com.tqmall.legend.facade.order.vo.OrderWorkerVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zsy on 16/4/14.
 * 车间看板
 */
@Slf4j
@Controller
@RequestMapping("shop/order/work")
public class OrderWorkerController extends BaseController{
    @Autowired
    private OrderServicesFacade orderServicesFacade;

    /**
     * 获取工单列表
     *
     * @return
     */
    @RequestMapping
    @ResponseBody
    public Result getOrderWorkList(@RequestParam(value = "size",required = false)Integer size){
        if(size == null){
            size = 6;//默认6条
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        List<OrderWorkerVo> orderWorkerVoList = orderServicesFacade.getOrderWorkerList(shopId,size);
        if(!CollectionUtils.isEmpty(orderWorkerVoList)){
            return Result.wrapSuccessfulResult(orderWorkerVoList);
        }else{
            return Result.wrapErrorResult(LegendErrorCode.ORDER_LIST_EX.getCode(), "未查到工单");
        }
    }
}
