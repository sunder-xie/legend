package com.tqmall.legend.web.order;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p/>
 * 工单状态流转controller
 */
@Controller
@RequestMapping("shop/order/order_track")
@Slf4j
public class OrderTrackController extends BaseController {

    @Autowired
    private OrderTrackService orderTrackService;


    /**
     * 工单派工
     *
     * @param orderSn
     * @param orderId
     * @return
     */
    @RequestMapping("tasking")
    @ResponseBody
    public Result tasking(String orderSn, Long orderId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Result result = orderTrackService.tasking(orderId, orderSn, "FPDD", userInfo);
            log.info("工单派工，插入orderTrack,order_sn={},orderStatus=FPDD", orderSn);
            return result;
        } catch (Exception e) {
            log.error("工单派工失败,order_sn={},orderStatus=FPDD，错误信息：{}", orderSn, e);
            return Result.wrapErrorResult("", "工单派工失败");
        }
    }

    /**
     * 工单完工
     *
     * @param orderSn
     * @param orderId
     * @return
     */
    @RequestMapping("finish")
    @ResponseBody
    public Result finish(String orderSn, Long orderId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Result result = orderTrackService.finish(orderId, orderSn, "DDWC", userInfo);
            log.info("工单完工，插入orderTrack,order_sn=" + orderSn + ",orderStatus=DDWC");
            return result;
        } catch (BizException e) {
            log.error("工单完工失败，异常信息", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e) {
            log.error("工单完工失败,order_sn={},orderStatus=DDWC，错误信息：{}", orderSn, e);
            return Result.wrapErrorResult("", "工单完工失败");
        }
    }


    /**
     * 工单报价
     *
     * @param orderSn
     * @param orderId
     * @return
     */
    @RequestMapping("pricing")
    @ResponseBody
    public Result pricing(String orderSn, Long orderId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Result result = orderTrackService.pricing(orderId, orderSn, "DDBJ", userInfo);
            log.info("工单报价,order_sn={},orderStatus=DDBJ", orderSn);
            return result;
        } catch (Exception e) {
            log.error("工单报价，插入orderTrack,order_sn={},orderStatus=DDBJ.错误信息：{}", orderSn, e);
            return Result.wrapErrorResult("", "工单报价失败");
        }
    }
}
