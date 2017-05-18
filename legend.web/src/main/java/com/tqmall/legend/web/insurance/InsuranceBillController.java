package com.tqmall.legend.web.insurance;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.insurance.InsuranceBillFormEntity;
import com.tqmall.legend.entity.order.InsuranceOrderStatusEnum;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * insurance bill controller
 */
@Slf4j
@Controller
@RequestMapping("insurance/bill")
public class InsuranceBillController extends BaseController {
    
    @Autowired
    IInsuranceBillService insuranceBillService;
    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    ShopServiceInfoService shopServiceInfoService;
    @Autowired
    ShopService shopService;
    @Autowired
    IOrderService orderService;

    /**
     * to insurance‘s edit page
     *
     * @param model
     * @param billId
     * @return
     */
    @RequestMapping(value = "edit" , method = RequestMethod.GET)
    public String edit(Model model,
                       @RequestParam(value = "billid", required = false) Long billId) {
        model.addAttribute("moduleUrl", "insurance");

        UserInfo userInfo = UserUtils.getUserInfo(request);

        // get bill
        Optional<InsuranceBill> billOptional = insuranceBillService.get(billId);
        if (billOptional.isPresent()) {
            model.addAttribute("orderInfo", billOptional.get());
            // get orderInfo
            Long orderId = billOptional.get().getOrderId();
            if(orderId != null && Long.compare(orderId, 0L) > 0){
                Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
                if(orderInfoOptional.isPresent()){
                    model.addAttribute("order", orderInfoOptional.get());
                }
            }
        }
        return "order/insurance";
    }

    /**
     * save insurance's bill
     *
     * @param fromEntity
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(InsuranceBillFormEntity fromEntity) {

        // TODO optimize by interceptor
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        // order info
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo);
        if (!re.isSuccess()){
            return re;
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e);
            return Result.wrapErrorResult("", "创建保险维修单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        orderInfo.setCustomerId(customerCar.getCustomerId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 关联平安洗车服务(目前仅有一条保险维修服务)
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.getServiceByFlag("PABQ", shopId);
        if (CollectionUtils.isEmpty(shopServiceInfoList)) {
            log.error("创建保险维修单失败,[原因] 获取保险服务失败");
            return Result.wrapErrorResult("", "创建保险维修单失败! <br/> 获取保险服务失败");
        }
        ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
        // 服务ID
        orderInfo.setServiceId(shopServiceInfo.getId());
        // 服务名称
        orderInfo.setServiceName(shopServiceInfo.getName());

        try {
            return insuranceBillService.save(orderInfo, userInfo);
        } catch (BusinessCheckedException e1) {
            log.error("创建保险维修单失败,原因:{}", e1);
            return Result.wrapErrorResult("", "创建保险维修单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("创建保险维修单异常", e2);
            return Result.wrapErrorResult("", "创建保险维修单失败!");
        }
    }

    private Result check(InsuranceBill orderInfo){
        if (orderInfo == null) {
            return Result.wrapErrorResult("", "操作保险维修单失败!<br/>维修单不存在");
        }
        // 车牌
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.isBlank(carLicense)) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        // 保险人
        String insured = orderInfo.getInsured();
        if (StringUtils.isBlank(insured)) {
            return Result.wrapErrorResult("", "保险人不能为空！");
        }
        // 保单号
        String insuredCode = orderInfo.getInsuredCode();
        if (StringUtils.isBlank(insuredCode)) {
            return Result.wrapErrorResult("", "保单号不能为空！");
        }
        // 受损部位
        String woundPart = orderInfo.getWoundPart();
        if (StringUtils.isBlank(woundPart)) {
            return Result.wrapErrorResult("", "受损部位不能为空！");
        }
        // 核销号
        String verificationCode = orderInfo.getVerificationCode();
        if (StringUtils.isBlank(verificationCode)) {
            return Result.wrapErrorResult("", "核销号不能为空！");
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * wrapper customercar
     *
     * @param orderInfo 工单实体
     * @return
     */
    private CustomerCar wrapperCustomerCar(InsuranceBill orderInfo) {
        CustomerCar customerCar = new CustomerCar();
        // 设置车牌
        customerCar.setLicense(orderInfo.getCarLicense());
        // 设置车型
        customerCar.setCarBrand(orderInfo.getCarBrand());
        customerCar.setCarBrandId(orderInfo.getCarBrandId());
        customerCar.setCarSeries(orderInfo.getCarSeries());
        customerCar.setCarSeriesId(orderInfo.getCarSeriesId());
        customerCar.setCarModel(orderInfo.getCarModels());
        customerCar.setCarModelId(orderInfo.getCarModelsId());
        customerCar.setCarYear(orderInfo.getCarYear());
        customerCar.setCarYearId(orderInfo.getCarYearId());
        customerCar.setCarPower(orderInfo.getCarPower());
        customerCar.setCarPowerId(orderInfo.getCarPowerId());
        customerCar.setCarGearBox(orderInfo.getCarGearBox());
        customerCar.setCarGearBoxId(orderInfo.getCarGearBoxId());
        // 设置联系人
        customerCar.setCustomerName(orderInfo.getCustomerName());
        customerCar.setMobile(orderInfo.getCustomerMobile());
        // 设置行驶里程
        if (!StringUtils.isBlank(orderInfo.getMileage())) {
            Long mileage = Long.parseLong(orderInfo.getMileage());
            customerCar.setMileage(mileage);
        }
        // 设置vin
        customerCar.setVin(orderInfo.getVin());
        // 客户单位
        customerCar.setCompany(orderInfo.getCompany());

        return customerCar;
    }

    /**
     * update insurance's bill
     *
     * @param fromEntity
     * @return
     * @see InsuranceBill
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public String update(InsuranceBillFormEntity fromEntity) {

        Gson gson = new Gson();
        Result errorResult = null;

        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 工单信息
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo);
        if (!re.isSuccess()){
            return gson.toJson(re);
        }
        Long orderId = orderInfo.getId();
        // 无效工单
        if (orderId == null) {
            errorResult = Result.wrapErrorResult("", "保险单不存在！");
            return gson.toJson(errorResult);
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e);
            errorResult = Result.wrapErrorResult("", "创建保险维修单失败! <br/> 客户车辆信息不存在");
            return gson.toJson(errorResult);
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 设置工单状态
        orderInfo.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.SAVED.getCode()));

        try {
            Result result = insuranceBillService.update(orderInfo, userInfo);
            return gson.toJson(result);
        } catch (BusinessCheckedException e1) {
            log.error("编辑保险维修单失败,原因:{}", e1);
            errorResult = Result.wrapErrorResult("", "编辑保险维修单失败!<br/>" + e1.getMessage());
            return gson.toJson(errorResult);
        } catch (RuntimeException e2) {
            log.error("编辑保险维修单异常", e2);
            errorResult = Result.wrapErrorResult("", "编辑保险维修单失败!");
            return gson.toJson(errorResult);
        }
    }

    /**
     * submit insurance's bill
     *
     * @param fromEntity
     * @return
     * @see InsuranceBill
     */
    @RequestMapping(value = "submit" , method = RequestMethod.POST)
    @ResponseBody
    public String submit(InsuranceBillFormEntity fromEntity) {

        Gson gson = new Gson();
        Result errorResult = null;

        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 工单信息
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo);
        if (!re.isSuccess()){
            return gson.toJson(re);
        }

        Long orderId = orderInfo.getId();
        // 无效工单
        if (orderId == null) {
            errorResult = Result.wrapErrorResult("", "保险单不存在！");
            return gson.toJson(errorResult);
        }

        Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(orderId);
        if (!insuranceBillOptional.isPresent()) {
            errorResult = Result.wrapErrorResult("", "无效保险单！");
            return gson.toJson(errorResult);
        }
        InsuranceBill billDB = insuranceBillOptional.get();

        // 判断工单审核状态
        Integer status = billDB.getAuditStatus();
        if (status != null &&
                status.intValue() == InsuranceOrderStatusEnum.AUDITING.getCode()) {
            errorResult = Result.wrapErrorResult("", "保险单正在审核中，无须重复提交审核！");
            return gson.toJson(errorResult);
        }
        if (status != null &&
                status.intValue() == InsuranceOrderStatusEnum.PASS.getCode()) {
            errorResult = Result.wrapErrorResult("", "保险单已审核通过，无须提交审核！");
            return gson.toJson(errorResult);
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e);
            errorResult = Result.wrapErrorResult("", "创建保险维修单失败! <br/> 客户车辆信息不存在");
            return gson.toJson(errorResult);
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 设置工单状态
        orderInfo.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.AUDITING.getCode()));

        try {
            Result result = insuranceBillService.submit(orderInfo, userInfo);
            return gson.toJson(result);
        } catch (BusinessCheckedException e1) {
            log.error("保险维修单提交失败,原因:{}", e1);
            errorResult = Result.wrapErrorResult("", "保险维修单提交失败!<br/>" + e1.getMessage());
            return gson.toJson(errorResult);
        } catch (RuntimeException e2) {
            log.error("保险维修单提交异常", e2);
            errorResult = Result.wrapErrorResult("", "保险维修单提交失败!");
            return gson.toJson(errorResult);
        }
    }

    /**
     * re-submit insurance's bill
     *
     * @param fromEntity
     * @return
     * @see InsuranceBill
     */
    @RequestMapping(value = "resubmit", method = RequestMethod.POST)
    @ResponseBody
    public String reSubmit(InsuranceBillFormEntity fromEntity) {

        Gson gson = new Gson();
        Result errorResult = null;

        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 工单信息
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo);
        if (!re.isSuccess()){
            return gson.toJson(re);
        }

        Long orderId = orderInfo.getId();
        // 无效工单
        if (orderInfo == null || orderId == null) {
            errorResult = Result.wrapErrorResult("", "保险单不存在！");
            return gson.toJson(errorResult);
        }

        Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(orderId);
        if (!insuranceBillOptional.isPresent()) {
            errorResult = Result.wrapErrorResult("", "无效保险单！");
            return gson.toJson(errorResult);
        }
        InsuranceBill billDB = insuranceBillOptional.get();

        // 判断工单审核状态
        Integer status = billDB.getAuditStatus();
        if (status != null &&
                status.intValue() == InsuranceOrderStatusEnum.AUDITING.getCode()) {
            errorResult = Result.wrapErrorResult("", "保险单正在审核中，无须重复提交审核！");
            return gson.toJson(errorResult);
        }
        if (status != null &&
                status.intValue() == InsuranceOrderStatusEnum.PASS.getCode()) {
            errorResult = Result.wrapErrorResult("", "保险单已审核通过，无须提交审核！");
            return gson.toJson(errorResult);
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("重新提交保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e);
            errorResult = Result.wrapErrorResult("", "重新提交保险维修单失败! <br/> 客户车辆信息不存在");
            return gson.toJson(errorResult);
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 设置工单状态
        orderInfo.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.AUDITING.getCode()));

        try {
            Result result = insuranceBillService.reSubmit(orderInfo, userInfo);
            return gson.toJson(result);
        } catch (BusinessCheckedException e1) {
            log.error("重新提交保险维修单失败,原因:{}", e1);
            errorResult = Result.wrapErrorResult("", "重新提交保险维修单失败!<br/>" + e1.getMessage());
            return gson.toJson(errorResult);
        } catch (RuntimeException e2) {
            log.error("重新提交保险维修单异常", e2);
            errorResult = Result.wrapErrorResult("", "重新提交保险维修单失败!");
            return gson.toJson(errorResult);
        }
    }

    /**
     * to insurance list page
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "page" , method = RequestMethod.GET)
    public String page(Model model) {
        model.addAttribute("moduleUrl", "insurance");
        model.addAttribute("menu", "insurance");
        return "order/insuranceList";
    }

    /**
     * get list json
     *
     * @return
     */
    @RequestMapping(value = "list" , method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request, HttpServletResponse response) {

        // searchForm 条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        if (searchParams.containsKey("startTime")) {
            searchParams.put("startTime", searchParams.get("startTime") + " 00:00:00");
        }
        if (searchParams.containsKey("endTime")) {
            searchParams.put("endTime", searchParams.get("endTime") + " 23:59:59");
        }

        try {
            DefaultPage<InsuranceBill> page = (DefaultPage<InsuranceBill>) insuranceBillService.getOrderInfoPage(
                    pageable, searchParams);
            page.setPageUri(request.getRequestURI());
            page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
            return Result.wrapSuccessfulResult(page);
        } catch (Exception e) {
            log.error("[DB] query InsuranceBill data failure,exception:{}", e);
            return Result.wrapErrorResult("", "");
        }
    }

    /**
     * to insurance print page
     *
     * @param model
     * @param billId
     * @return
     */
    @RequestMapping(value = "print", method = RequestMethod.GET)
    public String print(Model model,
                        @RequestParam(value = "billid", required = false) Long billId) {
        model.addAttribute("moduleUrl", "insurance");
        Long shopId = null;
        Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(billId);
        if (insuranceBillOptional.isPresent()) {
            InsuranceBill bill = insuranceBillOptional.get();
            model.addAttribute("bill", bill);
            Long serviceId = bill.getServiceId();
            shopId = bill.getShopId();
            // 获取服务信息
            ShopServiceInfo serviceInfo = shopServiceInfoService.selectById(serviceId, shopId);
            if (serviceInfo != null) {
                model.addAttribute("service", serviceInfo);
            }
        }

        if (shopId != null && shopId > 0l) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);
        }

        return "order/insurance_print";
    }

    /**
     * delete picture
     *
     * @param billId      工单ID
     * @param pictureName 图片名称
     *                    {'imgUrl':车牌,
     *                    'woundSnapshoot':'受损图片',
     *                    'acceptanceSnapshoot':'修复后图片'}
     * @param model
     * @return
     */
    @RequestMapping(value = "deletepic", method = RequestMethod.GET)
    @ResponseBody
    public Result deletePicture(Model model,
                                @RequestParam(value = "billid", required = true) Long billId,
                                @RequestParam(value = "picname", required = true) String pictureName) {
        model.addAttribute("moduleUrl", "insurance");

        UserInfo userInfo = UserUtils.getUserInfo(request);

        // get bill
        Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(billId);
        if (!insuranceBillOptional.isPresent()) {
            return Result.wrapErrorResult("", "保险单不存在，删除图片失败！");
        }
        InsuranceBill insuranceBill = insuranceBillOptional.get();

        if (pictureName.equals("imgUrl")) {
            insuranceBill.setImgUrl("");
        }
        if (pictureName.equals("woundSnapshoot")) {
            insuranceBill.setWoundSnapshoot("");
        }
        if (pictureName.equals("acceptanceSnapshoot")) {
            insuranceBill.setAcceptanceSnapshoot("");
        }

        Result result = null;
        try {
            result = insuranceBillService.update(insuranceBill, userInfo);
        } catch (BusinessCheckedException e1) {
            log.error("删除图片失败,原因:{}", e1);
            return Result.wrapErrorResult("", "删除图片失败<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("删除图片异常", e2);
            return Result.wrapErrorResult("", "删除图片失败");
        }

        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult("");
        }

        return Result.wrapErrorResult("", "删除图片失败");
    }
}
