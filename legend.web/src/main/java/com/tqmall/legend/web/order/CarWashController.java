package com.tqmall.legend.web.order;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.customer.AppointService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.order.CarwashOrderFormEntity;
import com.tqmall.legend.entity.order.CustomerCompletionFormEntity;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.order.OrderSourceChannelEnum;
import com.tqmall.legend.entity.order.CreateCarWashResponse;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.service.ServiceCateTagEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.shop.ShopConfigureFacade;
import com.tqmall.legend.object.result.settlement.PaymentDTO;
import com.tqmall.legend.service.settlement.RpcSettlementService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.service.vo.ShopServiceInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xin on 16/4/14.
 */
@Controller
@RequestMapping("shop/order")
@Slf4j
public class CarWashController extends BaseController{

    @Autowired
    private IOrderService orderService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private CarWashFacade carWashFacade;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AppointService appointService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private RpcSettlementService rpcSettlementService;
    @Autowired
    private ShopConfigureFacade shopConfigureFacade;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return ModuleUrlEnum.RECEPTION.getModuleUrl();
    }


    /**
     * 有以下几种情况:
     * 1,新建洗车单页面
     * 2,从新建客户页面跳转过来
     * 3,从客户详情跳转过来
     *
     * @param model
     * @param request
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "carwash", method = RequestMethod.GET)
    public String carWash(Model model,
                          @RequestParam(value = "customerCarId", required = false) Long customerCarId,
                          @RequestParam(value = "appointId", required = false) Long appointId,
                          HttpServletRequest request) {
        // current user
        Long userId = UserUtils.getUserIdForSession(request);
        // current shop
        Long shopId = UserUtils.getShopIdForSession(request);

        // Log trac
        logTrac(SiteUrls.CARWASH, userId, null);

        // 取最近一次自定义的洗车服获务
        model.addAttribute("orderServices", orderService.getLastCarwash(shopId));
        // {1:非第一次 ;0:第一次}
        int isFirst = 1;
        // 门店是否第一次开洗车单
        int orderNum = orderService.statisticsOrderNumber(shopId, OrderCategoryEnum.CARWASH);
        if (orderNum == 0) {
            isFirst = 0;
        }

        // 是否初始化洗车服务
        model.addAttribute("isFirst", isFirst);
        // 获取洗车标准服务列表
        model.addAttribute("shopServiceList", shopServiceInfoService.getBZCarWashList(shopId));
        // 支付方式列表
        com.tqmall.core.common.entity.Result<List<PaymentDTO>> paymentListResult = rpcSettlementService.getPaymentList(shopId);
        if(paymentListResult.isSuccess()){
            model.addAttribute("paymentList", paymentListResult.getData());
        }
        if (customerCarId != null) { // 从客户详情跳转过来
            // 查询车牌
            CustomerCar customerCar = customerCarService.selectById(customerCarId);
            String license = "";
            String company = "";
            if (customerCar != null) {
                license = customerCar.getLicense();
                // 查询
                Customer customer = customerService.selectById(customerCar.getCustomerId());
                if (customer != null) {
                    company = customer.getCompany();
                }
            }
            model.addAttribute("license", license);
            model.addAttribute("company", company);
            //查询预约单信息，如果存在，写保存洗车单时需要回写预约单
            if(appointId != null){
                Appoint appoint = appointService.selectById(appointId);
                if(appoint != null){
                    model.addAttribute("appointId",appointId);
                    model.addAttribute("downPayment",appoint.getDownPayment());
                }
            }
        } else {
            //按门店所在城市设置门店头两位车牌号
            String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
            model.addAttribute("license", license);
        }
        //设置是否使用他人账户
        String confValue = shopConfigureFacade.getConfValue(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT, shopId);
        if (StringUtils.isNotBlank(confValue)) {
            model.addAttribute("USE_GUEST_ACCOUNT", confValue);
        }
        return "yqx/page/order/carwash";
    }

    /**
     * 洗车单完善页面
     *
     * @param orderId 工单ID
     * @param model
     * @return
     */
    @RequestMapping(value = "carwash-perfect", method = RequestMethod.GET)
    public String carWashPerfect(Model model, @RequestParam("orderId") Long orderId, HttpServletRequest request) {

        // current user and shop
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac(SiteUrls.CARWASHPERFECT, userId, null);

        // check orderId valid
        if (orderId == null || orderId < 0) {
            // 工单不存在提示信息
            return "common/error";
        }

        // get orderInfo
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        // IF 校验跨门店 THEN 返回空页面
        if (!orderInfoOptional.isPresent()) {
            return "common/error";
        }

        OrderInfo orderInfo = orderInfoOptional.get();
        Integer orderTag = orderInfo.getOrderTag();
        // check是否洗车单
        if (!(orderTag != null && orderTag.intValue() == OrderCategoryEnum.CARWASH.getCode())) {
            // 不是洗车单提示信息
            return "common/error";
        }
        Long customerCarId = orderInfo.getCustomerCarId();

        // 获取客户车辆信息
        Result<CustomerPerfectOfCarWashEntity> result = customerCarService.selectCustomerCar(shopId, customerCarId);
        if (!result.isSuccess()) {
            return "common/error";
        }
        CustomerPerfectOfCarWashEntity customerPerfectEntity = result.getData();

        // wrapper完善洗车表单实体
        CustomerCompletionFormEntity formEntity = carWashFacade.wrapperPerfectCarWashFormEntity(orderInfo, customerPerfectEntity);

        model.addAttribute("formEntity", formEntity);

        return "yqx/page/order/carwash-perfect";
    }

    /**
     * 保存洗车单
     *
     * @param orderFormEntity 洗车单表单 实体
     * @return
     * @see CarwashOrderFormEntity
     */
    @RequestMapping(value = "carwash/save", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<CreateCarWashResponse> saveCarwashOrder(@RequestBody final CarwashOrderFormEntity orderFormEntity) {
        return new ApiTemplate<CreateCarWashResponse>() {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long userId = userInfo.getUserId();

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(orderFormEntity, "参数为空");
                // 车牌号码
                String carLicense = orderFormEntity.getCarLicense();
                Assert.notNull(carLicense, "车牌不能为空！");
                Assert.isTrue(StringUtil.checkCarLicense(carLicense), "车牌格式不正确！");
                // 服务顾问
                Long receiver = orderFormEntity.getReceiver();
                if (receiver == null || receiver == 0) {
                    throw new IllegalArgumentException("服务顾问不能为空！");
                }
                // 洗车工
                String workerIds = orderFormEntity.getWorkerIds();
                Assert.notNull(workerIds, "洗车工不能为空！");
                String[] workerIdArr = workerIds.split(",");
                if (ArrayUtils.isEmpty(workerIdArr)) {
                    throw new IllegalArgumentException("洗车工不能为空！");
                } else if (workerIdArr.length > Constants.MAX_WORKER_NUMBER) {
                    throw new IllegalArgumentException("洗车工不能超过" + Constants.MAX_WORKER_NUMBER + "个人！");
                }
                // 校验洗车服务
                String carwashServiceJSON = orderFormEntity.getOrderServiceJson();
                Assert.notNull(carwashServiceJSON, "未选择'收款金额'！");
                OrderServices carwashService;
                try {
                    carwashService = new Gson().fromJson(carwashServiceJSON,
                            new TypeToken<OrderServices>() {
                            }.getType());
                } catch (JsonSyntaxException e) {
                    log.error("洗车服务JSON转对象异常,洗车服务JSON：{}", carwashServiceJSON);
                    throw new IllegalArgumentException("未选择'收款金额'！");
                }
                BigDecimal servicePrice = carwashService.getServicePrice();
                Assert.notNull(servicePrice, "'收款金额'为空");
                if (servicePrice.compareTo(BigDecimal.ZERO) == -1) {
                    throw new IllegalArgumentException("'收款金额'小于0元");
                }
                orderFormEntity.setCarwashService(carwashService);
                // 设置工单渠道来源
                orderFormEntity.setRefer(OrderSourceChannelEnum.WEB.getCode());
                //校验预付定金是否有效
                BigDecimal downPayment = orderFormEntity.getDownPayment();
                if(downPayment != null && downPayment.compareTo(BigDecimal.ZERO) == 1){
                    Long appointId = orderFormEntity.getAppointId();
                    boolean checkDownPaymentIsValid = appointFacade.checkDownPaymentIsValid(appointId,downPayment);
                    Assert.isTrue(checkDownPaymentIsValid,"预付定金有误，请检查预约单的预付定金是否正确");
                }
            }

            @Override
            protected CreateCarWashResponse process() throws BizException {
                logTrac(SiteUrls.CARWASHSAVE, userId, orderFormEntity);
                Result<CreateCarWashResponse> createCarWashResponseResult = carWashFacade.create(orderFormEntity, userInfo);
                if (createCarWashResponseResult != null && createCarWashResponseResult.isSuccess()) {
                    CreateCarWashResponse createCarWashResponse = createCarWashResponseResult.getData();
                    log.info("洗车工单状态流转:{} 创建洗车工单， 返回信息为：{}，操作人：{}", orderFormEntity.getCarLicense(), createCarWashResponse, userId);
                    return createCarWashResponse;
                }
                throw new BizException("创建洗车工单失败!");
            }
        }.execute();
    }

    /**
     * 完善洗车单客户信息
     *
     * @param customerCompletionEntity
     * @return
     * @see CustomerCompletionFormEntity
     */
    @RequestMapping(value = "carwashperfect/save", method = RequestMethod.POST)
    @ResponseBody
    public Result perfectCustomerSave(CustomerCompletionFormEntity customerCompletionEntity, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac(SiteUrls.CARWASHPERFECTSAVE, userId, customerCompletionEntity);

        // 客户信息
        Long orderId = customerCompletionEntity.getOrderId();
        String carLicense = customerCompletionEntity.getCarLicense();
        //为了解决洗车专用车牌，完善客户信息时 车牌号在数据库已存在
        CustomerCar customerCar = customerCarService.selectByLicenseAndShopId(customerCompletionEntity.getCarLicense(), customerCompletionEntity.getShopId());
        if (customerCar != null) {
            customerCompletionEntity.setCustomerCarId(customerCar.getId());
            customerCompletionEntity.setCustomerId(customerCar.getCustomerId());
        }
        try {
            carWashFacade.perfectCustomer(customerCompletionEntity, userInfo);
            log.info("洗车工单状态流转:{} 完善洗车单客户信息,工单号为:{} 操作人:{}", carLicense, orderId, userId);
            return Result.wrapSuccessfulResult("完善洗车单客户信息成功");
        } catch (BizException e) {
            log.error("完善洗车单客户信息失败,原因:{}", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e) {
            log.error("完善洗车单客户信息异常", e);
            return Result.wrapErrorResult("", "系统异常，请稍后再试");
        }
    }

    /**
     * 删除洗车单图片
     *
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "carwashperfect/delpic", method = RequestMethod.GET)
    @ResponseBody
    public Result carWashPerfectDeletePicture(@RequestParam(value = "orderId", required = true) Long orderId) {
        boolean isSuccess;
        try {
            isSuccess = carWashFacade.deletePictureOfCarWash(orderId);
        } catch (Exception e) {
            log.error("删除洗车单图片异常，异常信息:{}", e.toString());
            isSuccess = Boolean.FALSE;
        }

        if (!isSuccess) {
            return Result.wrapErrorResult("", "删除洗车单图片失败");
        }

        return Result.wrapSuccessfulResult("");
    }

    /**
     * log trac
     * 业务场景:xx|操作人:xx|操作实体:xx
     *
     * @param scene
     * @param userId
     */
    private void logTrac(String scene, Long userId, Object optObj) {
        StringBuffer logSB = new StringBuffer("业务场景:");
        logSB.append(SiteUrlNameEnum.getUrlName(scene));
        logSB.append(" | ");
        logSB.append("操作人:");
        logSB.append(userId);
        logSB.append(" | ");
        logSB.append("操作实体:");
        logSB.append((optObj == null) ? "" : optObj.toString());
        log.info(logSB.toString());
    }

    /**
     * 获取洗车服务列表
     * @param nameLike
     * @return
     */
    @RequestMapping(value = "carwash/get-service", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ShopServiceInfoVo>> getServiceList(@RequestParam(value = "nameLike",required = false)String nameLike) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoFacade.getShopServiceInfoByCateTag(shopId, ServiceCateTagEnum.XC.getCode(), nameLike);
        List<ShopServiceInfoVo> shopServiceInfoVoList = Lists.newArrayList();
        for(ShopServiceInfo shopServiceInfo : shopServiceInfoList){
            ShopServiceInfoVo shopServiceInfoVo = new ShopServiceInfoVo();
            BeanUtils.copyProperties(shopServiceInfo,shopServiceInfoVo);
            shopServiceInfoVoList.add(shopServiceInfoVo);
        }
        return Result.wrapSuccessfulResult(shopServiceInfoVoList);
    }
}
