package com.tqmall.legend.web.activity;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.activity.ActivityChannelService;
import com.tqmall.legend.biz.activity.ActivityJoinService;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.BannerConfigService;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.ActJumpUrlEnum;
import com.tqmall.legend.entity.activity.ActivityChannel;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.BannerConfig;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.activity.ShopActivityServiceListVO;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.insurance.InsuranceBillFormEntity;
import com.tqmall.legend.entity.order.InsuranceOrderStatusEnum;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.ServiceInfoList;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.activity.ActivityChannelSourceEnum;
import com.tqmall.legend.enums.activity.BannerPositionEnum;
import com.tqmall.legend.enums.activity.BillTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.pojo.activity.ActivityServiceTemplateVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * activity controller
 */
@Controller
@Slf4j
@RequestMapping("shop/activity")
public class ActivityController extends BaseController {

	@Autowired
	CustomerCarService customerCarService;
	@Autowired
	IInsuranceBillService insuranceBillService;
	@Autowired
	IOrderService orderService;
	@Autowired
	ShopServiceInfoService shopServiceInfoService;
	@Autowired
	ShopService shopService;
	@Autowired
	private BannerConfigService bannerConfigService;
	@Autowired
	private ActivityChannelService activityChannelService;
	@Autowired
	private ActivityTemplateService activityTemplateService;
	@Autowired
	private ShopServiceCateService shopServiceCateService;
	@Autowired
	private ActivityJoinService activityJoinService;
	@Autowired
	private IShopActivityService shopActivityService;

	/**
	 * /**
	 * to activity main page
	 *
	 * @param model
	 * @return
	 */
	@HttpRequestLog
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String main(Model model) {
		Long shopId = UserUtils.getShopIdForSession(request);
		//获取banner图列表
		List<BannerConfig> bannerConfigList = bannerConfigService.getListByPostion(BannerPositionEnum.ACTIVITY.getposition());
		model.addAttribute("bannerConfigList", bannerConfigList);
		//获取活动渠道列表(只获取云修管理后台legendm管理的渠道)
		List<ActivityChannel> activityChannelList = activityChannelService.getByChannelSource(ActivityChannelSourceEnum.LEGENDM.getValue());//0 legendm,1 mega
		model.addAttribute("activityChannelList", activityChannelList);
		model.addAttribute("moduleUrl", ModuleUrlEnum.ACTIVITY.getModuleUrl());
		model.addAttribute("actTab", "main");
		return "activity/main";
	}

	/**
	 * 根据活动模板ID获取套餐列表
	 *
	 * @param channel
	 * @return
	 */
	@RequestMapping(value = "getServiceListByChannel", method = RequestMethod.GET)
	@ResponseBody
	public Result getServiceListByChannel(Long channel) {
		Long shopId = UserUtils.getShopIdForSession(request);
		//根据渠道、门店获取套餐列表
		try {
			List<ActivityServiceTemplateVo> activityServiceTemplateVoList = activityJoinService.getServiceListByChannelAndShopId(channel, shopId);
			return Result.wrapSuccessfulResult(activityServiceTemplateVoList);
		} catch (Exception e) {
			log.error("根据活动模板ID获取套餐列表异常,{}", e);
			return Result.wrapErrorResult(LegendError.COMMON_ERROR);
		}
	}

	/**
	 * 进入活动分会场 [平安/淘汽...]
	 *
	 * @param model
	 * @param actTplId
	 * @param serviceTplId
	 * @return
	 */
	@RequestMapping(value = "meeting" , method = RequestMethod.GET)
	public String meeting(Model model, @RequestParam(value = "actTplId", required = true) Long actTplId,
						  @RequestParam(value = "serviceTplId", required = true) Long serviceTplId) {
		Long shopId = UserUtils.getShopIdForSession(request);
		ActivityTemplate activityTemplate = activityTemplateService.getById(actTplId);
		model.addAttribute("activityTemplate", activityTemplate);
		ActivityServiceTemplateVo activityServiceTemplateVo = activityJoinService.getServiceTpl(actTplId, serviceTplId, shopId);
		// 解析json格式
		Gson gson = new Gson();
		List<ServiceInfoList> serviceInfoList = gson.fromJson(activityServiceTemplateVo.getServiceInfo(),
				new TypeToken<List<ServiceInfoList>>() {
				}.getType());
		activityServiceTemplateVo.setServiceInfoList(serviceInfoList);
		model.addAttribute("activityServiceTemplateVo", activityServiceTemplateVo);
		model.addAttribute("moduleUrl", ModuleUrlEnum.ACTIVITY.getModuleUrl());
		return "activity/meeting";
	}

	/**
	 * 报名参加活动
	 *
	 * @param actTplId
	 * @param serviceTplId
	 * @param shopReason
	 * @return
	 */
	@RequestMapping(value = "join_activity", method = RequestMethod.POST)
	@ResponseBody
	public Result joinActivity(@RequestParam(value = "actTplId", required = true) Long actTplId,
							   @RequestParam(value = "serviceTplId", required = true) Long serviceTplId,
							   @RequestParam(value = "servicePrice", required = false) BigDecimal servicePrice,
							   @RequestParam(value = "shopReason", required = false) String shopReason) {

		UserInfo userInfo = UserUtils.getUserInfo(request);
		try {
			log.info("报名参加引流活动，actTpId={},serviceTplId={},操作人:{}", actTplId, serviceTplId, userInfo.getAccount());
			Result result = activityJoinService.joinActivity(actTplId, serviceTplId,servicePrice, shopReason, userInfo);
			return result;
		} catch (Exception e) {
			log.error("报名参加引流活动异常,{}", e);
			return Result.wrapErrorResult(LegendErrorCode.JOIN_ACTIVITY_ERROR.getCode(), LegendErrorCode.JOIN_ACTIVITY_ERROR.getErrorMessage());
		}
	}


	/**
	 * 退出活动
	 *
	 * @param actTplId
	 * @param serviceTplId
	 * @return
	 */
	@RequestMapping(value = "exit_activity" , method = RequestMethod.POST)
	@ResponseBody
	public Result exitActivity(@RequestParam(value = "actTplId", required = true) Long actTplId,
							   @RequestParam(value = "serviceTplId", required = true) Long serviceTplId,
							   @RequestParam(value = "shopReason", required = false) String shopReason) {
		UserInfo userInfo = UserUtils.getUserInfo(request);
		try {
			log.info("退出引流活动，actTpId={},serviceTplId={},操作人:{}", actTplId, serviceTplId, userInfo.getAccount());
			Result result = activityJoinService.exitActivity(actTplId, serviceTplId, shopReason, userInfo);
			return result;
		} catch (Exception e) {
			log.error("退出引流活动异常,{}", e);
			return Result.wrapErrorResult(LegendErrorCode.EXIT_ACTIVITY_ERROR.getCode(), LegendErrorCode.EXIT_ACTIVITY_ERROR.getErrorMessage());
		}
	}


	/**
	 * 活动服务页面
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "join" , method = RequestMethod.GET)
	public String join(Model model,
					   @RequestParam(value = "acttplid", required = true) Long activityTemplateId,
					   @RequestParam(value = "billid", required = false) Long billId,
					   HttpServletRequest request) {
		model.addAttribute("moduleUrl", ModuleUrlEnum.ACTIVITY.getModuleUrl());
		model.addAttribute("actTplId",activityTemplateId);

		// current login user
		UserInfo userInfo = UserUtils.getUserInfo(request);
		Long shopId = userInfo.getShopId();

		// get bill
		Optional<InsuranceBill> billOptional = insuranceBillService.get(billId);
		if (billOptional.isPresent()) {
			InsuranceBill bill = billOptional.get();
			model.addAttribute("orderInfo", bill);
			// get orderInfo
			Long orderId = bill.getOrderId();
			if (orderId != null && Long.compare(orderId, 0L) > 0) {
				Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
				if (orderInfoOptional.isPresent()) {
					model.addAttribute("order", orderInfoOptional.get());
				}
			}

			// get service
			Long serviceId = bill.getServiceId();
			Optional<ShopServiceInfo> serviceInfoOptional = shopServiceInfoService.get(serviceId);
			if (serviceInfoOptional.isPresent()) {
				model.addAttribute("service", serviceInfoOptional.get());
			}
		}

		// 跳转页面
		String targetPage = "";
		if (activityTemplateId.intValue() == ActJumpUrlEnum.PABY.getCode().intValue()) {
			// get 保养套餐列表 (1:平安保养)
			// 服务列表
			List<ShopServiceInfo> servicelist = null;
			try {
				ShopActivityServiceListVO activityServiceListVO = shopServiceInfoService.getShopActivityServiceList(shopId, 1l);
				servicelist = activityServiceListVO.getServicelist();
			} catch (BusinessCheckedException e) {
				log.error("获取保养套餐列表失败");
				servicelist = new ArrayList<ShopServiceInfo>();
			}

			model.addAttribute("servicelist", servicelist);
			model.addAttribute("actTplId",ActJumpUrlEnum.PABY.getCode());
			targetPage = "activity/join_pingan";
		} else if (activityTemplateId.intValue() == ActJumpUrlEnum.TMFW.getCode().intValue()) {
			model.addAttribute("actTplId",ActJumpUrlEnum.TMFW.getCode());
			targetPage = "activity/join_tmall";
		} else if (activityTemplateId.intValue() == ActJumpUrlEnum.PABQ.getCode().intValue()){
			model.addAttribute("actTplId",ActJumpUrlEnum.PABQ.getCode());
			targetPage = "order/insurance";
		} else if (activityTemplateId.intValue() == ActJumpUrlEnum.TAFW.getCode().intValue()){
			model.addAttribute("actTplId",ActJumpUrlEnum.TAFW.getCode());
			targetPage = "yqx/page/activity/join_tianan";
		} else {
			model.addAttribute("actTplId",ActJumpUrlEnum.PABY.getCode());
			targetPage = "activity/join_pingan";
			activityTemplateId = Long.parseLong(ActJumpUrlEnum.PABY.getCode() + "");
		}

		// 获取门店活动ID (根据门店模板)
		Optional<ShopActivity> shopActivityOptional = shopActivityService.get(shopId, activityTemplateId);
		if (shopActivityOptional.isPresent()) {
			model.addAttribute("shopActId", shopActivityOptional.get().getId());
		} else {
			model.addAttribute("shopActId", 0);
		}

		return targetPage;
	}


	/**
	 * save bill
	 *
	 * @param formEntity
	 * @return
	 */
	@RequestMapping(value = "bill/save" , method = RequestMethod.POST)
	@ResponseBody
	public Result billSave(InsuranceBillFormEntity formEntity) {
		UserInfo userInfo = UserUtils.getUserInfo(request);
		// bill info
		InsuranceBill bill = formEntity.getOrderInfo();
		if (bill == null) {
			return Result.wrapErrorResult("", "创建服务单失败!<br/>服务单不存在");
		}

		// 车牌
		String carLicense = bill.getCarLicense();
		if (StringUtils.isBlank(carLicense)) {
			return Result.wrapErrorResult("", "车牌不能为空！");
		}

		// check {平安:核销码}
		String verificationCode = bill.getVerificationCode();
		String verificationCodeNotify = "";
		String verificationCodeIsUsedNotify = "";
		String billType = bill.getBillType();
		if (billType != null &&
				billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
			verificationCodeNotify = "核销码不能为空!";
			verificationCodeIsUsedNotify = "核销码已经被使用过";
		}
		if (StringUtils.isBlank(verificationCode)) {
			// 页面判断
			return Result.wrapErrorResult("", verificationCodeNotify);
		}

		// 校验核销码是否被使用过
		boolean isUsed = insuranceBillService.checkVerificationCodeIsUsed(verificationCode);
		if (isUsed) {
			return Result.wrapErrorResult("", verificationCodeIsUsedNotify);
		}

		// 包装车辆实体
		CustomerCar customerCar = wrapperCustomerCar(bill);
		try {
			customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
		} catch (BizException e) {
			log.error("创建服务单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
			return Result.wrapErrorResult("", "创建服务单失败! <br/> 客户车辆信息不存在");
		}
		bill.setCustomerId(customerCar.getCustomerId());
		bill.setCustomerCarId(customerCar.getId());
		bill.setCustomerName(customerCar.getCustomerName());
		bill.setCustomerMobile(customerCar.getMobile());

		// 关联服务套餐
		Long serviceId = bill.getServiceId();
		if (serviceId == null || serviceId < 0) {
			log.error("创建服务单失败,[原因] 未选择服务套餐");
			return Result.wrapErrorResult("", "创建服务单失败! <br/>未选择服务套餐,或您报名的活动正在审核中");
		}

		try {
			return insuranceBillService.create(bill, userInfo);
		} catch (Exception e2) {
			log.error("创建服务单异常", e2);
			return Result.wrapErrorResult("", "创建服务单失败!");
		}
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
	 * update bill
	 *
	 * @param fromEntity
	 * @return
	 * @see InsuranceBill
	 */
	@RequestMapping(value = "bill/update" , method = RequestMethod.POST)
	@ResponseBody
	public String billUpdate(InsuranceBillFormEntity fromEntity) {

		Gson gson = new Gson();
		Result errorResult = null;

		UserInfo userInfo = UserUtils.getUserInfo(request);
		// 工单信息
		InsuranceBill bill = fromEntity.getOrderInfo();
		if (bill == null) {
			errorResult = Result.wrapErrorResult("", "编辑工单失败!<br/>保险单不存在");
			return gson.toJson(errorResult);
		}

		Long billId = bill.getId();
		// 无效工单
		if (bill == null || billId == null) {
			errorResult = Result.wrapErrorResult("", "保险单不存在！");
			return gson.toJson(errorResult);
		}

		// 车牌
		String carLicense = bill.getCarLicense();
		if (StringUtils.isBlank(carLicense)) {
			errorResult = Result.wrapErrorResult("", "车牌不能为空！");
			return gson.toJson(errorResult);
		}

		// check {平安:核销码}
		String verificationCode = bill.getVerificationCode();
		String verificationCodeNotify = "";
		String verificationCodeIsUsedNotify = "";
		String billType = bill.getBillType();
		if (billType != null &&
				billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
			verificationCodeNotify = "核销码不能为空!";
			verificationCodeIsUsedNotify = "核销码已经被使用过";
		}
		if (StringUtils.isBlank(verificationCode)) {
			errorResult = Result.wrapErrorResult("", verificationCodeNotify);
			return gson.toJson(errorResult);
		}

		// 校验核销码是否被使用过
		boolean isUsed = insuranceBillService.checkVerificationCodeIsUsed(billId, verificationCode);
		if (isUsed) {
			errorResult = Result.wrapErrorResult("", verificationCodeIsUsedNotify);
			return gson.toJson(errorResult);
		}

		// 包装车辆实体
		CustomerCar customerCar = wrapperCustomerCar(bill);
		try {
			customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
		} catch (BizException e) {
			log.error("创建服务单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
			errorResult = Result.wrapErrorResult("", "创建服务单失败! <br/> 客户车辆信息不存在");
			return gson.toJson(errorResult);
		}
		bill.setCustomerId(customerCar.getCustomerId());
		bill.setCustomerCarId(customerCar.getId());
		// 设置车主电话和车主姓名
		bill.setCustomerName(customerCar.getCustomerName());
		bill.setCustomerMobile(customerCar.getMobile());

		// 设置工单状态
		bill.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.SAVED.getCode()));

		try {
			Result result = insuranceBillService.modify(bill, userInfo);
			return gson.toJson(result);
		} catch (BusinessCheckedException e1) {
			log.error("编辑服务单失败,原因:{}", e1.toString());
			errorResult = Result.wrapErrorResult("", "编辑服务单失败!<br/>" + e1.getMessage());
			return gson.toJson(errorResult);
		} catch (RuntimeException e2) {
			log.error("编辑服务单异常", e2);
			errorResult = Result.wrapErrorResult("", "编辑服务单失败!");
			return gson.toJson(errorResult);
		}
	}

	/**
	 * 提交服务单
	 *
	 * @param fromEntity
	 * @return
	 * @see InsuranceBill
	 */
	@RequestMapping(value = "bill/submit" , method = RequestMethod.POST)
	@ResponseBody
	public String billSubmit(InsuranceBillFormEntity fromEntity) {

		Gson gson = new Gson();
		Result errorResult = null;

		UserInfo userInfo = UserUtils.getUserInfo(request);

		// 工单信息
		InsuranceBill bill = fromEntity.getOrderInfo();
		if (bill == null) {
			errorResult = Result.wrapErrorResult("", "编辑工单失败!<br/>服务单不存在");
			return gson.toJson(errorResult);
		}

		Long billId = bill.getId();
		// 无效工单
		if (bill == null || billId == null) {
			errorResult = Result.wrapErrorResult("", "服务单不存在！");
			return gson.toJson(errorResult);
		}

		Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(billId);
		if (!insuranceBillOptional.isPresent()) {
			errorResult = Result.wrapErrorResult("", "无效服务单！");
			return gson.toJson(errorResult);
		}
		InsuranceBill billDB = insuranceBillOptional.get();

		// 判断工单审核状态
		Integer status = billDB.getAuditStatus();
		if (status != null &&
				status.intValue() == InsuranceOrderStatusEnum.AUDITING.getCode()) {
			errorResult = Result.wrapErrorResult("", "服务单正在审核中，无须重复提交审核！");
			return gson.toJson(errorResult);
		}
		if (status != null &&
				status.intValue() == InsuranceOrderStatusEnum.PASS.getCode()) {
			errorResult = Result.wrapErrorResult("", "服务单已审核通过，无须提交审核！");
			return gson.toJson(errorResult);
		}

		// 车牌
		String carLicense = bill.getCarLicense();
		if (StringUtils.isBlank(carLicense)) {
			errorResult = Result.wrapErrorResult("", "车牌不能为空！");
			return gson.toJson(errorResult);
		}

		// check {平安:核销码}
		String verificationCode = bill.getVerificationCode();
		String verificationCodeNotify = "";
		String verificationCodeIsUsedNotify = "";
		String billType = bill.getBillType();
		if (billType != null &&
				billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
			verificationCodeNotify = "核销码不能为空!";
			verificationCodeIsUsedNotify = "核销码已经被使用过";
		}
		if (StringUtils.isBlank(verificationCode)) {
			errorResult = Result.wrapErrorResult("", verificationCodeNotify);
			return gson.toJson(errorResult);
		}

		// 校验核销码是否被使用过
		boolean isUsed = insuranceBillService.checkVerificationCodeIsUsed(billId, verificationCode);
		if (isUsed) {
			errorResult = Result.wrapErrorResult("", verificationCodeIsUsedNotify);
			return gson.toJson(errorResult);
		}

		// IF 平安保养 THEN 保险人\保单号\车牌图片
		if (billType != null && billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
			// 保险人
			String insured = bill.getInsured();
			if (StringUtils.isBlank(insured)) {
				errorResult = Result.wrapErrorResult("", "保险人不能为空！");
				return gson.toJson(errorResult);
			}
			// 保单号
			String insuredCode = bill.getInsuredCode();
			if (StringUtils.isBlank(insuredCode)) {
				errorResult = Result.wrapErrorResult("", "保单号不能为空！");
				return gson.toJson(errorResult);
			}
		}

		// IF 平安补漆 THEN 保险人\保单号\车牌图片\受损部位\受损图片\修复后图片
		if (billType != null && billType.equals(BillTypeEnum.PINGAN_BUQI.getCode())) {
			// 保险人
			String insured = bill.getInsured();
			if (StringUtils.isBlank(insured)) {
				errorResult = Result.wrapErrorResult("", "保险人不能为空！");
				return gson.toJson(errorResult);
			}
			// 保单号
			String insuredCode = bill.getInsuredCode();
			if (StringUtils.isBlank(insuredCode)) {
				errorResult = Result.wrapErrorResult("", "保单号不能为空！");
				return gson.toJson(errorResult);
			}
			// 受损部位
			String woundPart = bill.getWoundPart();
			if (StringUtils.isBlank(woundPart)) {
				errorResult = Result.wrapErrorResult("", "受损部位不能为空！");
				return gson.toJson(errorResult);
			}
		}

		// 包装车辆实体
		CustomerCar customerCar = wrapperCustomerCar(bill);
		try {
			customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
		} catch (BizException e) {
			log.error("提交服务单审核失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
			errorResult = Result.wrapErrorResult("", "提交服务单审核失败! <br/> 客户车辆信息不存在");
			return gson.toJson(errorResult);
		}
		bill.setCustomerId(customerCar.getCustomerId());
		bill.setCustomerCarId(customerCar.getId());
		// 设置车主电话和车主姓名
		bill.setCustomerName(customerCar.getCustomerName());
		bill.setCustomerMobile(customerCar.getMobile());

		try {
			Result result = insuranceBillService.adult(bill, userInfo);
			return gson.toJson(result);
		} catch (BusinessCheckedException e1) {
			log.error("提交服务单审核失败,原因:{}", e1.toString());
			errorResult = Result.wrapErrorResult("", "确认核销失败!<br/>" + e1.getMessage());
			return gson.toJson(errorResult);
		} catch (RuntimeException e2) {
			log.error("提交服务单审核异常", e2);
			errorResult = Result.wrapErrorResult("", "确认核销失败!");
			return gson.toJson(errorResult);
		}
	}

	/**
	 * 重新提交服务单
	 *
	 * @param fromEntity
	 * @return
	 * @see InsuranceBill
	 */
	@RequestMapping(value = "bill/resubmit", method = RequestMethod.POST)
	@ResponseBody
	public String billReSubmit(InsuranceBillFormEntity fromEntity) {

		Gson gson = new Gson();
		Result errorResult = null;

		UserInfo userInfo = UserUtils.getUserInfo(request);

		// 工单信息
		InsuranceBill bill = fromEntity.getOrderInfo();
		if (bill == null) {
			errorResult = Result.wrapErrorResult("", "编辑工单失败!<br/>服务单不存在");
			return gson.toJson(errorResult);
		}

		Long billId = bill.getId();
		// 无效工单
		if (bill == null || billId == null) {
			errorResult = Result.wrapErrorResult("", "服务单不存在！");
			return gson.toJson(errorResult);
		}

		Optional<InsuranceBill> insuranceBillOptional = insuranceBillService.get(billId);
		if (!insuranceBillOptional.isPresent()) {
			errorResult = Result.wrapErrorResult("", "无效服务单！");
			return gson.toJson(errorResult);
		}
		InsuranceBill billDB = insuranceBillOptional.get();

		// 判断工单审核状态
		Integer status = billDB.getAuditStatus();
		if (status != null &&
				status.intValue() == InsuranceOrderStatusEnum.AUDITING.getCode()) {
			errorResult = Result.wrapErrorResult("", "服务单正在审核中，无须重复提交审核！");
			return gson.toJson(errorResult);
		}
		if (status != null &&
				status.intValue() == InsuranceOrderStatusEnum.PASS.getCode()) {
			errorResult = Result.wrapErrorResult("", "服务单已审核通过，无须提交审核！");
			return gson.toJson(errorResult);
		}

		// 车牌
		String carLicense = bill.getCarLicense();
		if (StringUtils.isBlank(carLicense)) {
			errorResult = Result.wrapErrorResult("", "车牌不能为空！");
			return gson.toJson(errorResult);
		}
		// 核销码
		String verificationCode = bill.getVerificationCode();
		if (StringUtils.isBlank(verificationCode)) {
			errorResult = Result.wrapErrorResult("", "核销码不能为空！");
			return gson.toJson(errorResult);
		}

		// IF 平安保养 THEN 保险人\保单号\车牌图片
		String billType = bill.getBillType();
		if (billType != null && billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
			// 保险人
			String insured = bill.getInsured();
			if (StringUtils.isBlank(insured)) {
				errorResult = Result.wrapErrorResult("", "保险人不能为空！");
				return gson.toJson(errorResult);
			}
			// 保单号
			String insuredCode = bill.getInsuredCode();
			if (StringUtils.isBlank(insuredCode)) {
				errorResult = Result.wrapErrorResult("", "保单号不能为空！");
				return gson.toJson(errorResult);
			}

		}

		// IF 平安补漆 THEN 保险人\保单号\车牌图片\受损部位\受损图片\修复后图片
		if (billType != null && billType.equals(BillTypeEnum.PINGAN_BUQI.getCode())) {
			// 保险人
			String insured = bill.getInsured();
			if (StringUtils.isBlank(insured)) {
				errorResult = Result.wrapErrorResult("", "保险人不能为空！");
				return gson.toJson(errorResult);
			}
			// 保单号
			String insuredCode = bill.getInsuredCode();
			if (StringUtils.isBlank(insuredCode)) {
				errorResult = Result.wrapErrorResult("", "保单号不能为空！");
				return gson.toJson(errorResult);
			}

			// 受损部位
			String woundPart = bill.getWoundPart();
			if (StringUtils.isBlank(woundPart)) {
				errorResult = Result.wrapErrorResult("", "受损部位不能为空！");
				return gson.toJson(errorResult);
			}
		}

		// 包装车辆实体
		CustomerCar customerCar = wrapperCustomerCar(bill);
		try {
			customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
		} catch (BizException e) {
			log.error("提交服务单审核失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
			errorResult = Result.wrapErrorResult("", "提交服务单审核失败! <br/> 客户车辆信息不存在");
			return gson.toJson(errorResult);
		}
		bill.setCustomerId(customerCar.getCustomerId());
		bill.setCustomerCarId(customerCar.getId());
		// 设置车主电话和车主姓名
		bill.setCustomerName(customerCar.getCustomerName());
		bill.setCustomerMobile(customerCar.getMobile());

		try {
			Result result = insuranceBillService.reSubmit(bill, userInfo);
			return gson.toJson(result);
		} catch (BusinessCheckedException e1) {
			log.error("重新提交服务单失败,原因:{}", e1.toString());
			errorResult = Result.wrapErrorResult("", "确认核销失败!<br/>" + e1.getMessage());
			return gson.toJson(errorResult);
		} catch (RuntimeException e2) {
			log.error("重新提交服务单异常", e2);
			errorResult = Result.wrapErrorResult("", "确认核销失败!");
			return gson.toJson(errorResult);
		}
	}

	/**
	 * print 平安服务单
	 *
	 * @param model
	 * @param billId
	 * @return
	 */
	@RequestMapping(value = "join/pingan/print", method = RequestMethod.GET)
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

		return "activity/join_pingan_print";
	}

	@RequestMapping(value = "bill/list", method = RequestMethod.GET)
	public String billList(Model model) {
		List<ShopActivity> shopActivityList = getShopActivityAllList();
		model.addAttribute("shopActivityList", shopActivityList);
		model.addAttribute("moduleUrl", "activity");
		model.addAttribute("actTab", "bill_list");
		return "activity/join_bill_list";
	}

	/**
	 * 活动门店参加的所有活动数据（包含下线的活动）
	 *
	 * @return
	 */
	private List<ShopActivity> getShopActivityAllList() {
		Long shopId = UserUtils.getShopIdForSession(request);
		List<ShopActivity> shopActivityList = shopActivityService.getShopActivityAllList(shopId);
		return shopActivityList;
	}

	@RequestMapping(value = "shop_act/list" , method = RequestMethod.GET)
	@ResponseBody
	public Result actList() {
		List<ShopActivity> shopActivityList = getShopActivityList();
		return Result.wrapSuccessfulResult(shopActivityList);
	}

	/**
	 * 活动门店参加的活动数据
	 *
	 * @return
	 */
	private List<ShopActivity> getShopActivityList() {
		Long shopId = UserUtils.getShopIdForSession(request);
		List<ShopActivity> shopActivityList = shopActivityService.getShopActivityList(shopId);
		return shopActivityList;
	}
}

