package com.tqmall.manage.web.init;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.pvg.IHRMService;
import com.tqmall.legend.biz.settlement.PaymentService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.settlement.Payment;
import com.tqmall.legend.enums.setting.OrderTypeShowStatusEnum;
import com.tqmall.legend.facade.setting.OrderTypeFacade;
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

import java.util.List;

/**
 * Created by zsy on 13/11/15.
 */
@Controller
@RequestMapping("manage/init")
public class ManageInitOrgController {
    @Autowired
    private OrderTypeFacade orderTypeFacade;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IHRMService ihrmService;
    @Autowired
    private CacheComponent<List<Payment>> cacheComponent;

    Logger logger = LoggerFactory.getLogger(ManageInitOrgController.class);

    /**
     * 添加业务类型
     *
     * @param shopId
     * @return
     */
    @RequestMapping(value = "insert_order_type", method = RequestMethod.GET)
    @ResponseBody
    public Result insertOrderType(@RequestParam(value = "shopId", required = false) Long shopId) {
        try {
            if (shopId == null) {
                return Result.wrapErrorResult("", "shopId不能为空");
            }
            orderTypeFacade.initOrderTypeByShopId(shopId);
            cacheComponent.reload(CacheKeyConstant.ORDER_TYPE);
            logger.info("【管理后台调用添加业务类型接口】：url：/manage/init/insert_order_type?shopId={}",shopId);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            logger.error("【管理后台调用添加业务类型接口】：url：/manage/init/insert_order_type?shopId={}；出现异常{}", e);
        }
        return Result.wrapErrorResult("500", "【管理后台调用添加业务类型接口】：系统异常");
    }

    /**
     * 添加支付方式
     *
     * @param paymentListStr
     * @return
     */
    @RequestMapping(value = "insert_payment", method = RequestMethod.GET)
    @ResponseBody
    public Result insertPayment(@RequestParam(value = "paymentListStr", required = false) String paymentListStr) {
        try {
            if (StringUtils.isBlank(paymentListStr)) {
                return Result.wrapErrorResult("", "需要添加结算方式数据为空");
            }
            List<Payment> paymentList = new Gson().fromJson(paymentListStr,
                    new TypeToken<List<Payment>>(){}.getType());
            if(!CollectionUtils.isEmpty(paymentList)){
                Payment payment =  paymentList.get(0);
                Long shopId = payment.getShopId();
                UserInfo userInfo = getUserInfo(shopId);
                logger.info("【管理后台调用添加支付方式接口】：url：/manage/init/insert_payment?paymentListStr={}",paymentListStr);
                Result result = paymentService.batchInsertPayment(paymentList, userInfo);
                cacheComponent.reload(CacheKeyConstant.PAYMENT);
                return result;
            }else{
                return Result.wrapErrorResult("", "需要添加结算方式数据为空");
            }
        } catch (Exception e) {
            logger.error("【管理后台调用添加支付方式接口】：url：/manage/init/insert_payment?shopId={}；出现异常{}", e);
        }
        return Result.wrapErrorResult("500", "【管理后台调用添加支付方式接口】：系统异常");
    }

    /**
     * 添加人员岗位
     *
     * @param shopId
     * @param type
     * @return
     */
    @RequestMapping(value = "insert_org_tree", method = RequestMethod.GET)
    @ResponseBody
    public Result insertOrgTree(@RequestParam(value = "shopId", required = false) Long shopId, @RequestParam(value = "type", required = false) Integer type) {
        try {

            if (shopId == null) {
                return Result.wrapErrorResult("", "shopId不能为空");
            }
            if (type == null) {
                type = 4;
            }
            UserInfo userInfo = getUserInfo(shopId);
            Result result = ihrmService.insertOrgTreeFromTemplate(type, userInfo);
            logger.info("【管理后台调用添加人员岗位接口】：url：/manage/init/insert_org_tree?shopId={}&type={},返回结果{}",shopId,type, JSONUtil.object2Json(result));
            return result;
        } catch (Exception e) {
            logger.error("【管理后台调用添加人员岗位接口】：url：/manage/init/insert_org_tree?shopId={}&type={}；出现异常{}", e);
        }
        return Result.wrapErrorResult("500", "【管理后台调用添加人员岗位接口】：系统异常");
    }

    /**
     * 获取门店用户
     *
     * @param shopId
     * @return
     */
    private UserInfo getUserInfo(Long shopId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(shopId);
        userInfo.setUserId(0L);
        return userInfo;
    }
}
