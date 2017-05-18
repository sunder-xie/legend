package com.tqmall.legend.web.insurance;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.insurance.TianInsuranceService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.ActJumpUrlEnum;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.insurance.InsuranceBillFormEntity;
import com.tqmall.legend.entity.insurance.TianInsuranceCode;
import com.tqmall.legend.entity.order.InsuranceOrderStatusEnum;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.insurance.TianServiceEnum;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 天安保险核销码信息
 * Created by lilige on 16/8/1.
 */
@Slf4j
@Controller
@RequestMapping("/insurance/tianan")
public class TianInsuranceController extends BaseController {
    @Autowired
    private TianInsuranceService tianInsuranceService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private IInsuranceBillService insuranceBillService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private ShopService shopService;

    /**
     * 根据车牌获取天安保险核销码信息
     *
     * @param carLicense
     * @return
     */
    @RequestMapping("/get_by_license")
    @ResponseBody
    public Result getCode(String carLicense) {
        carLicense = carLicense.trim();
        if (StringUtils.isBlank(carLicense)) {
            return Result.wrapErrorResult("", "车牌号不能为空");
        }
        Result<TianInsuranceCode> codeResult = tianInsuranceService.getCodeByLicense(carLicense);
        //确认门店是否报名了该服务
        if (codeResult.isSuccess()){
            TianInsuranceCode code = codeResult.getData();
            Long templateId = TianServiceEnum.getIdByName(code.getServiceItem());
            Map<String,Object> params = new HashMap<>();
            params.put("parentId",templateId);
            Long shopId = UserUtils.getShopIdForSession(request);
            params.put("shopId",shopId);
            params.put("status",0);//数据有效
            List<ShopServiceInfo> serviceInfoList = shopServiceInfoService.select(params);
            if (CollectionUtils.isEmpty(serviceInfoList)){
                return Result.wrapErrorResult("","系统查询到您的门店没有参加"+code.getServiceName()+"服务，请先报名参加");
            }
        }
        return codeResult;
    }

    /**
     * 天安保险单保存
     */
    @RequestMapping(value = "/bill/save", method = RequestMethod.POST)
    @ResponseBody
    public Result tianSave(@RequestBody List<InsuranceBillFormEntity> formEntitys) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        // order info
        List<InsuranceBill> billList = new ArrayList<>();
        if(CollectionUtils.isEmpty(formEntitys)){
            return Result.wrapErrorResult("","创建保险维修单失败,[原因]：参数错误");
        }
        for (InsuranceBillFormEntity formEntity : formEntitys) {
            billList.add(formEntity.getOrderInfo());
        }
        InsuranceBill orderInfo = billList.get(0);
        //todo 校验必填项

        Long actTplId = orderInfo.getActTplId();
        if(null == actTplId){
            actTplId = ActJumpUrlEnum.TAFW.getCode().longValue();
        }
        //校验核销次数是否合理
        Result<TianInsuranceCode> codeResult = getCode(orderInfo.getCarLicense());
        if(!codeResult.isSuccess()){
            return codeResult;
        }
        TianInsuranceCode code = codeResult.getData();
        int times = formEntitys.size();
        // 剩余次数
        int leftTimes = code.getTotalCount() - code.getUsedCount();
        if (times > leftTimes) {
            return Result.wrapErrorResult("", "剩余的核销次数不够");
        }
        //车辆信息更新
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e);
            return Result.wrapErrorResult("", e.getMessage());
        }
        Result<ShopServiceInfo> result = tianInsuranceService.getShopServiceInfoByLicense(orderInfo.getCarLicense(), userInfo.getShopId());
        if (!result.isSuccess()) {
            log.error("创建保险维修单失败,[原因] 获取服务信息失败:", result.getErrorMsg());
            return result;
        }
        ShopServiceInfo shopServiceInfo = result.getData();
        try {
            for (InsuranceBill tempOrder : billList) {
                tempOrder.setCustomerCarId(customerCar.getId());
                tempOrder.setCustomerId(customerCar.getCustomerId());
                // 设置车主电话和车主姓名
                tempOrder.setCustomerName(customerCar.getCustomerName());
                tempOrder.setCustomerMobile(customerCar.getMobile());
                //关联服务
                // 服务ID
                tempOrder.setServiceId(shopServiceInfo.getId());
                // 服务名称
                tempOrder.setServiceName(shopServiceInfo.getName());
                //活动模版id
                tempOrder.setActTplId(actTplId);
                Result re = insuranceBillService.create(tempOrder, userInfo);
                if (!re.isSuccess()){
                    return re;
                }
            }
            int _usedCount = code.getUsedCount() + times;
            code.setUsedCount(_usedCount);
            tianInsuranceService.updateTianInsuranceCode(code);
        } catch (Exception e) {
            log.error("保存服务单失败",e);
            return Result.wrapErrorResult("","系统保存服务单失败");
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 修改单个服务单
     *
     * @param fromEntity
     * @return
     */
    @RequestMapping(value = "bill/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody InsuranceBillFormEntity fromEntity) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        // 校验数据
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo, 1);
        if (!re.isSuccess()) {
            return re;
        }
        //无需校验核销次数
        // 包装车辆实体并更新
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("更新保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "更新保险维修单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 设置工单状态
        orderInfo.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.SAVED.getCode()));

        //服务单更新
        try {
            Result result = insuranceBillService.update(orderInfo, userInfo);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("编辑保险维修单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "编辑保险维修单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("编辑保险维修单异常", e2);
            return Result.wrapErrorResult("", "编辑保险维修单失败!");
        }
    }

    /**
     * 天安保险单提交审核接口
     *
     * @param fromEntity
     * @return
     */
    @RequestMapping(value = "bill/submit", method = RequestMethod.POST)
    @ResponseBody
    public Result submit(@RequestBody InsuranceBillFormEntity fromEntity) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Integer flag = fromEntity.getFlag();
        // 工单信息
        InsuranceBill orderInfo = fromEntity.getOrderInfo();
        Result re = check(orderInfo, flag);
        if (!re.isSuccess()) {
            return re;
        }
        Long orderId = orderInfo.getId();

        Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(orderId);
        if (!insuranceBillOptional.isPresent()) {
            return Result.wrapErrorResult("", "无效保险单！");
        }
        InsuranceBill billDB = insuranceBillOptional.get();

        // 判断工单审核状态
        Integer status = billDB.getAuditStatus();
        if (status != null && status.intValue() == InsuranceOrderStatusEnum.AUDITING.getCode()) {
            return Result.wrapErrorResult("", "核销单正在审核中，无须重复提交审核！");
        }
        if (status != null && status.intValue() == InsuranceOrderStatusEnum.PASS.getCode()) {
            return Result.wrapErrorResult("", "核销单已审核通过，无须提交审核！");
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建保险维修单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "创建保险维修单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        // 设置工单状态
        orderInfo.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.AUDITING.getCode()));

        try {
            Result result;
            if (flag == 2) {
                result = insuranceBillService.adult(orderInfo, userInfo);;
            } else {
                result = insuranceBillService.reSubmit(orderInfo, userInfo);
            }
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("保险维修单提交失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "保险维修单提交失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("保险维修单提交异常", e2);
            return Result.wrapErrorResult("", "保险维修单提交失败!");
        }
    }

    /**
     * 校验保险单数据
     *
     * @param orderInfo
     * @param flag      0-新增 1-修改 2-提交审核
     * @return
     */
    private Result check(InsuranceBill orderInfo, int flag) {
        if (null == orderInfo) {
            return Result.wrapErrorResult("", "操作保险维修单失败!<br/>维修单不存在");
        }
        Long orderId = orderInfo.getId();
        // 无效工单
        if (flag != 0 && orderId == null) {
            return Result.wrapErrorResult("", "保险单不存在！");
        }
        // 车牌
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.isBlank(carLicense)) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        // 受损部位
        String woundPart = orderInfo.getWoundPart();
        if (StringUtils.isBlank(woundPart)) {
            return Result.wrapErrorResult("", "受损部位不能为空！");
        }
        //提交审核需要校验图片
        if (flag == 2) {
            // 车牌图片
            String imgUrl = orderInfo.getImgUrl();
            if (StringUtils.isBlank(imgUrl)) {
                return Result.wrapErrorResult("", "车牌图片未上传！");
            }

            // 受损图片
            String woundSnapshoot = orderInfo.getWoundSnapshoot();
            if (StringUtils.isBlank(woundSnapshoot)) {
                return Result.wrapErrorResult("", "车辆受损图片未上传！");
            }
            // 修理后图片
            String acceptanceSnapshoot = orderInfo.getAcceptanceSnapshoot();
            if (StringUtils.isBlank(acceptanceSnapshoot)) {
                return Result.wrapErrorResult("", "车辆修复后图片未上传！");
            }
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
     *
     * @param model
     * @param billId
     * @return
     */
    @RequestMapping(value = "join_tianan_print", method = RequestMethod.GET)
    public String pinganPrint(Model model,
                              @RequestParam(value = "billid", required = false) Long billId) {
        model.addAttribute("moduleUrl", "activity");

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
                ShopServiceCate shopServiceCate = shopServiceCateService.selectById(serviceInfo.getCategoryId());
                if (shopServiceCate != null) {
                    String categoryName = shopServiceCate.getName();
                    serviceInfo.setCategoryName((categoryName == null) ? "" : categoryName);
                }
                model.addAttribute("service", serviceInfo);
            }
        }

        if (shopId != null && shopId > 0l) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);
        }

        return "yqx/page/activity/join_tianan_print";
    }
}
