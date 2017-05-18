package com.tqmall.legend.facade.order.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.account.MemberCardService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.manager.thread.OrderInfoExtSaveThread;
import com.tqmall.legend.biz.manager.thread.ThreadManager;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.bo.CarwashOrderFormBo;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.bo.OrderDiscountFlowBo;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.customer.CardMember;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import com.tqmall.legend.facade.settlement.DebitFacade;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;
import com.tqmall.legend.object.param.settlement.PayChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixiao on 16/4/7.
 */
@Slf4j
@Service
public class CarWashFacadeImpl implements CarWashFacade {

    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderInfoExtSaveThread orderInfoExtSaveThread;
    @Autowired
    private ThreadManager threadManager;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ConfirmBillFacade confirmBillFacade;
    @Autowired
    private DebitFacade debitFacade;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private CustomerCarFacade customerCarFacade;


    /**
     * 完善洗车单客户信息
     *
     * @param customerCompletionEntity 完善客户信息表单实体
     * @param userInfo                 当前操作人
     * @return
     */
    @Override
    @Transactional
    public void perfectCustomer(CustomerCompletionFormEntity customerCompletionEntity, UserInfo userInfo) throws BusinessCheckedException {

        // 当前门店
        Long shopId = userInfo.getShopId();
        // 当前操作用户
        Long userId = userInfo.getUserId();

        // 工单实体
        Long orderId = customerCompletionEntity.getOrderId();
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            throw new BizException("工单不存在!");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        // 车辆ID
        Long customerCarId = customerCompletionEntity.getCustomerCarId();
        if (customerCarId == null) {
            customerCarId = orderInfo.getCustomerCarId();
        }
        // 车牌号
        String carLicense = customerCompletionEntity.getCarLicense();

        // 1.车型信息
        String carBrand = customerCompletionEntity.getCarBrand();
        Long carBrandId = customerCompletionEntity.getCarBrandId();
        String carSeries = customerCompletionEntity.getCarSeries();
        Long carSeriesId = customerCompletionEntity.getCarSeriesId();
        String carModel = customerCompletionEntity.getCarModel();
        Long carModelId = customerCompletionEntity.getCarModelId();
        Long carPowerId = customerCompletionEntity.getCarPowerId();
        String carPower = customerCompletionEntity.getCarPower();
        Long carYearId = customerCompletionEntity.getCarYearId();
        String carYear = customerCompletionEntity.getCarYear();
        Long carGearBoxId = customerCompletionEntity.getCarGearBoxId();
        String carGearBox = customerCompletionEntity.getCarGearBox();
        String carCompany = customerCompletionEntity.getCarCompany();
        String importInfo = customerCompletionEntity.getImportInfo();
        //车架号
        String vin = customerCompletionEntity.getVin();
        vin = (vin == null) ? "" : vin;
        //vin码校验
        if (StringUtils.isNotBlank(vin)) {
            Boolean bool = customerCarFacade.checkVinIsExist(shopId, vin, customerCompletionEntity.getCustomerCarId());
            if (bool) {
                throw new BizException("vin码已存在，请勿重复添加！");
            }
        }
        //行驶里程
        Long mileage = customerCompletionEntity.getMileage();
        if (mileage == null) {
            mileage = 0l;
        }
        // 车牌图片
        String carLicensePictureUrl = customerCompletionEntity.getCarLicensePicture();

        // 2.客户联系信息
        String contactName = customerCompletionEntity.getContactName();
        contactName = (contactName == null) ? "" : contactName;
        String contactMobile = customerCompletionEntity.getContactMobile();
        contactMobile = (contactMobile == null) ? "" : contactMobile;

        // 更新工单车辆信息，冗余字段
        orderInfo.setCustomerCarId(customerCarId);
        orderInfo.setCustomerId(customerCompletionEntity.getCustomerId());
        orderInfo.setCarLicense(carLicense);
        orderInfo.setCarBrand(carBrand);
        orderInfo.setCarBrandId(carBrandId);
        orderInfo.setCarSeries(carSeries);
        orderInfo.setCarSeriesId(carSeriesId);
        orderInfo.setCarModelsId(carModelId);
        orderInfo.setCarModels(carModel);
        orderInfo.setImportInfo(importInfo);
        orderInfo.setCarYearId(carYearId);
        orderInfo.setCarYear(carYear);
        orderInfo.setCarPowerId(carPowerId);
        orderInfo.setCarPower(carPower);
        orderInfo.setCarGearBox(carGearBox);
        orderInfo.setCarGearBoxId(carGearBoxId);
        orderInfo.setCarCompany(carCompany);
        // 车牌图片
        orderInfo.setImgUrl(carLicensePictureUrl);
        //vin
        orderInfo.setVin(vin);
        //行驶里程
        orderInfo.setMileage(mileage + "");
        // 更新工单客户联系姓名，联系电话 冗余字段
        if (!contactName.equals("")) {
            orderInfo.setCustomerName(contactName);
        }
        if (!contactMobile.equals("")) {
            orderInfo.setCustomerMobile(contactMobile);
        }
        orderInfo.setContactName(contactName);
        orderInfo.setContactMobile(contactMobile);

        // 备注
        String postscript = customerCompletionEntity.getPostscript();
        postscript = (postscript == null) ? "" : postscript;
        orderInfo.setPostscript(postscript);

        // 更新工单实体
        orderService.updateOrder(orderInfo);
        // 同步更新客户信息
        CustomerCar customerCar = BdUtil.bo2do(customerCompletionEntity, CustomerCar.class);
        customerCar.setLicense(customerCompletionEntity.getCarLicense());
        customerCar.setContact(customerCompletionEntity.getContactName());
        customerCarService.addOrUpdate(userInfo, customerCar);
        log.info("洗车工单状态流转:{} 完善客户信息成功,工单号为:{} 操作人:{}", carLicense, orderId, userId);
    }

    /**
     * 删除洗车单图片
     *
     * @param orderId 工单ID
     * @return {"true":成功;"false":失败}
     */
    @Override
    public boolean deletePictureOfCarWash(Long orderId) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return Boolean.FALSE;
        }
        OrderInfo orderinfo = orderInfoOptional.get();
        // 图片置空
        orderinfo.setImgUrl("");
        int retValue = orderService.updateOrder(orderinfo);
        if (retValue == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


    /**
     * 包装完善洗车单表单实体
     *
     * @param orderInfo             工单实体
     * @param customerPerfectEntity 客户车辆信息
     * @return
     */
    @Override
    public CustomerCompletionFormEntity wrapperPerfectCarWashFormEntity(OrderInfo orderInfo, CustomerPerfectOfCarWashEntity customerPerfectEntity) {

        Long orderId = orderInfo.getId();
        Long shopId = orderInfo.getShopId();

        // 页面表单实体
        CustomerCompletionFormEntity formEntity = new CustomerCompletionFormEntity();

        // 工单信息
        formEntity.setOrderId(orderId);
        formEntity.setOrderSn(orderInfo.getOrderSn());
        formEntity.setGmtCreateStr(orderInfo.getGmtCreateStr());
        formEntity.setOrderAmount(orderInfo.getOrderAmount());
        formEntity.setOrderStatus(orderInfo.getOrderStatus());
        formEntity.setCreateTimeStr(orderInfo.getCreateTimeStr());
        formEntity.setInvoiceType(orderInfo.getInvoiceType());

        // 洗车服务维修工信息
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);

        if (!CollectionUtils.isEmpty(orderServicesList)) {
            OrderServices orderServices = orderServicesList.get(0);
            if (orderServices != null) {
                String workerIds = orderServices.getWorkerIds();
                if (StringUtils.isNotBlank(workerIds) && !"0".equals(workerIds)) {
                    // 通过逗号切分
                    String[] workerIdArr = workerIds.split(",");
                    Set<Long> workerIdSet = new HashSet<>();
                    if (ArrayUtils.isNotEmpty(workerIdArr)) {
                        if (workerIdArr.length > Constants.MAX_WORKER_NUMBER) {
                            workerIds = StringUtils.join(workerIdArr, ",", 0, Constants.MAX_WORKER_NUMBER);
                            orderServices.setWorkerIds(workerIds);
                            workerIdArr = ArrayUtils.subarray(workerIdArr, 0, Constants.MAX_WORKER_NUMBER);
                        }
                        for (String workerId : workerIdArr) {
                            workerId = workerId.trim();
                            if (!"0".equals(workerId) || !"".equals(workerId)) {
                                workerIdSet.add(Long.parseLong(workerId));
                            }
                        }
                        int workerIdSetSize = workerIdSet.size();
                        if (workerIdSetSize > 0) {
                            StringBuilder workerNames = new StringBuilder();
                            List<ShopManager> shopManagerLatestData = shopManagerService.selectByIdsWithDeleted(workerIdSet.toArray(new Long[workerIdSetSize]));
                            for (ShopManager shopManager : shopManagerLatestData) {
                                workerNames.append(shopManager.getName()).append(",");
                            }
                            if (workerNames.length() > 0) {
                                formEntity.setWorkerNames(workerNames.substring(0, workerNames.length() - 1));
                            }
                        }
                    }
                }
            }
        }

        // 服务顾问
        formEntity.setReceiver(orderInfo.getReceiver());
        formEntity.setReceiverName(orderInfo.getReceiverName());

        // 备注
        formEntity.setPostscript(orderInfo.getPostscript());

        // 车辆信息
        formEntity.setCustomerCarId(orderInfo.getCustomerCarId());
        formEntity.setCarLicense(orderInfo.getCarLicense());
        formEntity.setCarBrand(customerPerfectEntity.getCarBrand());
        formEntity.setCarBrandId(customerPerfectEntity.getCarBrandId());
        formEntity.setCarModel(customerPerfectEntity.getCarModel());
        formEntity.setCarModelId(customerPerfectEntity.getCarModelId());
        formEntity.setCarSeries(customerPerfectEntity.getCarSeries());
        formEntity.setCarSeriesId(customerPerfectEntity.getCarSeriesId());
        formEntity.setImportInfo(customerPerfectEntity.getImportInfo());
        formEntity.setCarYearId(customerPerfectEntity.getCarYearId());
        formEntity.setCarYear(customerPerfectEntity.getCarYear());
        formEntity.setCarPowerId(customerPerfectEntity.getCarPowerId());
        formEntity.setCarPower(customerPerfectEntity.getCarPower());
        formEntity.setCarGearBoxId(customerPerfectEntity.getCarGearBoxId());
        formEntity.setCarGearBox(customerPerfectEntity.getCarGearBox());
        formEntity.setContactName(customerPerfectEntity.getContactName());
        formEntity.setContactMobile(customerPerfectEntity.getContactMobile());
        formEntity.setCompany(customerPerfectEntity.getCompany());
        // 工单的车牌图片
        formEntity.setCarLicensePicture(orderInfo.getImgUrl());
        formEntity.setMileage(customerPerfectEntity.getMileage());
        formEntity.setVin(customerPerfectEntity.getVin());
        formEntity.setCarInfo(customerPerfectEntity.getCarInfo());

        formEntity.setPayTime(orderInfo.getPayTime());

        return formEntity;
    }


    @Override
    @Transactional
    public Result create(CarwashOrderFormEntity orderFormEntity, UserInfo userInfo) {
        // 当前门店
        Long shopId = userInfo.getShopId();
        // 当前操作人
        Long optUserId = userInfo.getUserId();

        // 页面挂账 (0:结算;1:挂账)
        int pageSign = orderFormEntity.getIsSign();
        // true: 否现金或其他支付方式
        boolean isCashOrOtherPay = Boolean.FALSE;
        // 后端计算挂账 {0:全款；1:挂账}
        int calculateSign = 0;

        // 1.获取会员信息 [[
        String carLicense = orderFormEntity.getCarLicense();
        if (!StringUtils.isBlank(carLicense)) {
            carLicense = carLicense.toUpperCase();
        }
        String ver = orderFormEntity.getVer();
        Integer refer = orderFormEntity.getRefer();
        String referStr = "";
        if (refer != null) {
            referStr = refer + "";
        }
        CustomerCar customerCar = customerCarService.selectIfNoExistInsert(userInfo, carLicense, ver, referStr);
        // ]]

        // 2.包装新增工单实体
        OrderInfo newOrder = generateOrder(orderFormEntity, shopId, optUserId, customerCar);

        // 3.包装工单服务 [[
        // 洗车服务列表
        List<OrderServices> orderServiceList = new ArrayList<OrderServices>();
        // 洗车服务JSON
        String carwashServiceJSON = orderFormEntity.getOrderServiceJson();
        // 洗车服务关联维修工
        OrderServices carwashOrderService = orderFormEntity.getCarwashService();
        // IF 自定义洗车 THEN 获取一个标准洗车服务
        Long carWashServiceId = carwashOrderService.getServiceId();
        if (carWashServiceId == null || carWashServiceId == 0) {
            // 标准洗车服务取一条
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.getBZCarWashList(shopId);
            if (CollectionUtils.isEmpty(shopServiceInfoList)) {
                return Result.wrapErrorResult("", "门店无标准洗车服务");
            }
            ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
            carwashOrderService.setServiceCatId(shopServiceInfo.getCategoryId());
            carwashOrderService.setServiceId(shopServiceInfo.getId());
            carwashOrderService.setServiceSn(shopServiceInfo.getServiceSn());
        }
        setServiceCateName(carwashOrderService);
        // 关联维修工
        carwashOrderService.setWorkerIds(orderFormEntity.getWorkerIds());
        orderServiceList.add(carwashOrderService);
        // ]]

        // 兼容老版本工单数据 计算工单的金额
        orderService.calculateOrderExpense(newOrder, orderServiceList);
        BigDecimal orderTotalAmount = newOrder.getOrderAmount();
        newOrder.setTotalAmount(orderTotalAmount);

        // 4.获取结算方式 [[

        // 实收金额
        BigDecimal paidAmount = BigDecimal.ZERO;

        Gson gson = new Gson();
        // 现金|其他方式 结算
        List<DebitBillFlowBo> payChannelList = null;
        // 会员卡结算
        CardMember cardMember = null;
        // 优惠金额
        BigDecimal memberDiscountAmount = orderFormEntity.getDiscountAmount();
        if (memberDiscountAmount == null) {
            memberDiscountAmount = BigDecimal.ZERO;
        }
        // 会员卡余额支付金额
        BigDecimal cardPaidAmount = BigDecimal.ZERO;
        // 优惠券 结算
        List<OrderDiscountFlowBo> discountCouponList = null;
        // 应付总金额 = 工单总金额-优惠金额
        BigDecimal receivableAmount = orderTotalAmount.subtract(memberDiscountAmount);
        // 预付定金,当预付定金>应付总金额时,预付定金 = 应付总金额
        BigDecimal downPayment = orderFormEntity.getDownPayment();
        boolean hasDownPayment = true;//有预付定金标识
        if (downPayment == null) {
            downPayment = BigDecimal.ZERO;
            hasDownPayment = false;
        }
        newOrder.setDownPayment(downPayment);
        // 结算方式信息 {'paymentId':,'paymentName':'','paymentValue':''}
        String paymentJson = orderFormEntity.getPaymentJson();
        if (!StringUtils.isBlank(paymentJson)) {
            try {
                payChannelList = gson.fromJson(paymentJson,
                        new TypeToken<List<DebitBillFlowBo>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                log.error("结算方式JSON转对象失败,结算方式JSON:{}", paymentJson);
                throw new BizException("结算方式JSON转对象 失败");
            }
            //本次应付金额 = 应付总金额 - 预定金
            paidAmount = receivableAmount.subtract(downPayment);
            if (paidAmount.compareTo(BigDecimal.ZERO) == -1) {
                paidAmount = BigDecimal.ZERO;
            }
            isCashOrOtherPay = Boolean.TRUE;
        }

        // 获取会员卡信息
        String cardMemberStr = orderFormEntity.getCardMemberJson();
        if (!StringUtils.isBlank(cardMemberStr)) {
            try {
                cardMember = gson.fromJson(cardMemberStr, CardMember.class);
            } catch (JsonSyntaxException e) {
                log.error("会员卡JSON转对象失败,会员卡JSON:{}", cardMemberStr);
                throw new BizException("会员卡JSON转对象 失败");
            }
        }

        // 用于结算的会员卡id和账户id
        Long memberCardIdForSettle = null;
        // 会员卡结算
        if (cardMember != null) {
            // 页面传入会员ID
            memberCardIdForSettle = cardMember.getId();
            if (memberCardIdForSettle != null && memberCardIdForSettle > 0) {
                MemberCard memberCard = memberCardService.findById(memberCardIdForSettle);
                if (memberCard != null) {
                    if (memberCard.isExpired()) {
                        log.error("会员卡已过期，会员卡id:{}", memberCardIdForSettle);
                        throw new BizException("会员卡已过期");
                    }
                    // 页面结算
                    BigDecimal balance = memberCard.getBalance();
                    if (pageSign == 0) {
                        //本次应付金额 = 应付总金额 - 预定金
                        paidAmount = receivableAmount.subtract(downPayment);
                        if (paidAmount.compareTo(BigDecimal.ZERO) == -1) {
                            paidAmount = BigDecimal.ZERO;
                        }
                        if (balance != null && balance.compareTo(paidAmount) == -1) {
                            calculateSign = 1;
                            paidAmount = balance;
                            cardPaidAmount = balance;
                        } else {
                            calculateSign = 0;
                            cardPaidAmount = paidAmount;
                        }
                    }

                    // 挂账 全额挂账结算
                    if (pageSign == 1) {
                        calculateSign = 1;
                        paidAmount = BigDecimal.ZERO;
                        cardPaidAmount = BigDecimal.ZERO;
                    }
                }
            }
        }

        // 优惠券结算
        String couponDetailJson = orderFormEntity.getCouponDetailJson();
        if (!StringUtils.isBlank(couponDetailJson)) {
            try {
                discountCouponList = gson.fromJson(couponDetailJson,
                        new TypeToken<List<OrderDiscountFlowBo>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                log.error("优惠券JSON转对象失败,优惠券JSON:{}", couponDetailJson);
                throw new BizException("优惠券JSON转对象失败");
            }
        }
        BigDecimal couponTotalDiscount = BigDecimal.ZERO;

        // 使用优惠券结算
        if (!CollectionUtils.isEmpty(discountCouponList)) {
            for (OrderDiscountFlowBo discount : discountCouponList) {
                BigDecimal discountAmount = discount.getDiscountAmount();
                discountAmount = (discountAmount == null) ? BigDecimal.ZERO : discountAmount;
                couponTotalDiscount = couponTotalDiscount.add(discountAmount);
            }

            if (couponTotalDiscount.compareTo(receivableAmount) == 1) {
                throw new BizException("'优惠券'优惠的总金额大于'洗车单金额' ");
            }
            receivableAmount = receivableAmount.subtract(couponTotalDiscount);
            if (receivableAmount.compareTo(BigDecimal.ZERO) == 1) {
                calculateSign = 1;
            }
            paidAmount = BigDecimal.ZERO;
        }
        // ]]

        // 5.保存工单和洗车服务
        newOrder.setTaxAmount(BigDecimal.ZERO);
        try {
            orderService.insertOrder(newOrder, null, orderServiceList, null, userInfo);
        } catch (Exception e) {
            log.error("[洗车单创建]保存洗车单失败,异常信息:{}", e);
            throw new BizException("洗车单结算失败");
        }
        // 新工单ID
        Long newOrderId = newOrder.getId();
        // ]]

        //  6.账单确认 [[
        ConfirmBillBo confirmBillBo = new ConfirmBillBo();
        // 收款单
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setRelId(newOrderId);
        debitBillBo.setRemark(orderFormEntity.getPostscript());
        BigDecimal orderAmount = newOrder.getOrderAmount();
        debitBillBo.setTotalAmount(orderAmount);
        debitBillBo.setReceivableAmount(receivableAmount);
        confirmBillBo.setDebitBillBo(debitBillBo);
        //用于优惠的会员卡id
        Long memberCardIdForPreferential = orderFormEntity.getMemberCardId();//会员卡id
        if (memberCardIdForPreferential != null && Long.compare(memberCardIdForPreferential, 0l) > 0) {
            String cardNumber = orderFormEntity.getCardNumber();//会员卡编号
            String cardDiscountReason = orderFormEntity.getCardDiscountReason();//会员卡优惠原因
            confirmBillBo.setMemberCardId(memberCardIdForPreferential);
            confirmBillBo.setCardNumber(cardNumber);
            confirmBillBo.setAccountId(orderFormEntity.getAccountId());
            confirmBillBo.setCardDiscountReason(cardDiscountReason);
        }
        confirmBillBo.setDiscountAmount(memberDiscountAmount);

        // 优惠信息
        if (!CollectionUtils.isEmpty(discountCouponList)) {
            for (OrderDiscountFlowBo discount : discountCouponList) {
                discount.setShopId(shopId);
                discount.setOrderId(newOrderId);
            }
            confirmBillBo.setOrderDiscountFlowBoList(discountCouponList);
        }

        Result<DebitBillDTO> confirmBillResult = null;
        try {
            confirmBillResult = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
        } catch (Exception e) {
            log.error("[账单确认]洗车单确认账单失败 工单ID:{} 原因:{}", newOrderId, e);
            throw new BizException("洗车单结算失败");
        }

        if (!confirmBillResult.isSuccess()) {
            String confirmErrorMessage = confirmBillResult.getErrorMsg();
            log.error("[账单确认]洗车单确认账单失败 工单ID:{} 原因:{}", newOrderId, confirmErrorMessage);
            throw new BizException("洗车单结算失败,原因:" + confirmErrorMessage);
        }
        // ]]

        // 7.账单收款 [[
        if ((paidAmount != null && paidAmount.compareTo(BigDecimal.ZERO) == 1) || hasDownPayment) {
            // 收款流水 IF 现金|会员余额|其他方式 THEN 记录流水
            List<DebitBillFlowBo> flowList = new ArrayList<DebitBillFlowBo>();

            //  现金|其他方式 支付
            if (!CollectionUtils.isEmpty(payChannelList)) {
                for (DebitBillFlowBo debitBillFlowBo : payChannelList) {
                    // IF 页面挂账 THEN 支付金额为0
                    debitBillFlowBo.setPayAmount((pageSign == 1) ? BigDecimal.ZERO : paidAmount);
                    flowList.add(debitBillFlowBo);
                }
            }

            // 会员卡余额支付
            if (cardMember != null && cardPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
                DebitBillFlowBo memberCardFlow = new DebitBillFlowBo();
                memberCardFlow.setPaymentId(0l);
                memberCardFlow.setPayAmount(cardPaidAmount);
                memberCardFlow.setPaymentName("会员卡");
                flowList.add(memberCardFlow);
            }
            //预付定金付款数据校验
            if (hasDownPayment) {
                newOrder.setAppointId(orderFormEntity.getAppointId());//如果预约单转工单，则设置预约单号，需要查询预约单
                debitFacade.setDownPaymentFlow(debitBillBo.getReceivableAmount(), flowList, newOrder);
            }
            DebitBillFlowSaveBo flowSaveBo = new DebitBillFlowSaveBo();
            flowSaveBo.setShopId(shopId);
            flowSaveBo.setUserId(userInfo.getUserId());
            flowSaveBo.setOrderId(newOrderId);
            flowSaveBo.setRemark(orderFormEntity.getPostscript());
            flowSaveBo.setMemberPayAmount(cardPaidAmount);
            flowSaveBo.setMemberCardId(memberCardIdForSettle);
            flowSaveBo.setFlowList(flowList);

            try {
                debitFacade.saveFlowList(flowSaveBo);
            } catch (Exception e) {
                log.error("[账单收款]洗车单账单收款失败 工单ID:{} 原因:{}", newOrderId, e);
                throw new BizException("洗车单结算失败");
            }
        }
        //]]

        // 8.附加信息处理 [[
        try {
            // 设置预约单id，用于回写预约单
            newOrder.setAppointId(orderFormEntity.getAppointId());
            orderInfoExtSaveThread.init(customerCar, newOrder, userInfo, "",
                    carwashServiceJSON, null);
            threadManager.execute(orderInfoExtSaveThread);
        } catch (Exception e) {
            log.error(e.toString());
        }
        //]]

        // 创建洗车单响应实体
        CreateCarWashResponse createCarWashResponse = new CreateCarWashResponse();
        createCarWashResponse.setOrderId(newOrderId);

        // 后端统计挂账
        BigDecimal signAmount = receivableAmount.subtract(downPayment);
        if (calculateSign == 1) {
            signAmount = signAmount.subtract(paidAmount);
            if (signAmount.compareTo(BigDecimal.ZERO) == 1) {
                createCarWashResponse.setIsSign(1);
                createCarWashResponse.setSignAmount(signAmount);
            }
        }
        // 现金,其他方式 AND 页面挂账
        if (pageSign == 1 && isCashOrOtherPay && signAmount.compareTo(BigDecimal.ZERO) == 1) {
            createCarWashResponse.setIsSign(1);
            createCarWashResponse.setSignAmount(signAmount);
        }
        //添加工单详情日志start
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(newOrderId, shopId);
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            StringBuffer orderLog = new StringBuffer();
            orderLog.append("洗车单创建成功: 工单号为:");
            orderLog.append(orderInfo.getId());
            orderLog.append(" 操作人:");
            orderLog.append(userInfo.getUserId());
            String operationLog = OrderOperationLog.getOperationLog(orderInfo, orderLog);
            log.info(operationLog);
        }
        //添加工单详情日志end
        return Result.wrapSuccessfulResult(createCarWashResponse);
    }

    /**
     * 设置服务的categoryname
     *
     * @param carwashOrderService
     */
    private void setServiceCateName(OrderServices carwashOrderService) {
        Long cateId = carwashOrderService.getServiceCatId();
        //设置cateName
        ShopServiceCate shopServiceCate = shopServiceCateService.selectById(cateId);
        if (shopServiceCate != null) {
            carwashOrderService.setServiceCatName(shopServiceCate.getName());
        }
    }


    /**
     * wrapper new order
     *
     * @param orderFormEntity 洗车单表单实体
     * @param shopId          当前门店
     * @param optUserId       当前操作人
     * @param customerCar     客户信息
     * @return
     */
    private OrderInfo generateOrder(CarwashOrderFormEntity orderFormEntity,
                                    Long shopId,
                                    Long optUserId,
                                    CustomerCar customerCar) {
        OrderInfo newOrder = new OrderInfo();
        // 开单门店
        newOrder.setShopId(shopId);
        // 开单时间
        String createTimeStr = orderFormEntity.getCreateTimeStr();
        Date createTime = null;
        if (StringUtils.isEmpty(createTimeStr)) {
            createTime = new Date();
        } else {
            createTime = DateUtil.convertStringToDate(createTimeStr, "yyyy-MM-dd HH:mm");
        }
        newOrder.setCreateTime(createTime);
        // 工单标记为“洗车单”
        newOrder.setOrderTag(OrderCategoryEnum.CARWASH.getCode());
        // 工单来源
        newOrder.setRefer(orderFormEntity.getRefer());
        // 版本号
        newOrder.setVer(orderFormEntity.getVer());
        // 车牌图片(APP使用)
        if (!StringUtils.isBlank(orderFormEntity.getImgUrl())) {
            newOrder.setImgUrl(orderFormEntity.getImgUrl());
        }
        // 耦合会员信息到工单
        newOrder.setCarLicense(customerCar.getLicense());
        newOrder.setCustomerCarId(customerCar.getId());
        newOrder.setCustomerId(customerCar.getCustomerId());
        newOrder.setCarBrand(customerCar.getCarBrand());
        newOrder.setCarBrandId(customerCar.getCarBrandId());
        newOrder.setCarSeries(customerCar.getCarSeries());
        newOrder.setCarSeriesId(customerCar.getCarSeriesId());
        newOrder.setCarModelsId(customerCar.getCarModelId());
        newOrder.setCarModels(customerCar.getCarModel());
        newOrder.setCarCompany(customerCar.getCarCompany());
        newOrder.setImportInfo(customerCar.getImportInfo());
        newOrder.setCarAlias(customerCar.getByName());
        newOrder.setContactName(customerCar.getContact());
        newOrder.setContactMobile(customerCar.getContactMobile());
        newOrder.setCustomerMobile(customerCar.getMobile());
        newOrder.setCustomerName(customerCar.getCustomerName());
        // 客户单位
        String company = orderFormEntity.getCompany();
        newOrder.setCompany((company == null) ? " " : company);
        // 行驶里程
        newOrder.setMileage(customerCar.getMileage() + "");
        // TODO 洗车单的业务类型
        newOrder.setOrderType(0l);
        // 服务顾问ID
        newOrder.setReceiver(orderFormEntity.getReceiver());
        // 服务顾问名称
        newOrder.setReceiverName(orderFormEntity.getReceiverName());
        // 设置备注
        newOrder.setPostscript(orderFormEntity.getPostscript());
        // 完工时间
        newOrder.setFinishTime(new Date());
        // 创建人
        newOrder.setCreator(optUserId);
        // 创建时间
        newOrder.setGmtCreate(new Date());
        return newOrder;
    }


    @Override
    @Transactional
    public Result createForApp(@NotNull CarwashOrderFormBo orderFormBo, UserInfo userInfo) {

        // 当前门店
        Long shopId = userInfo.getShopId();
        // 当前操作人
        Long optUserId = userInfo.getUserId();

        // 页面挂账 (0:结算;1:挂账)
        int pageSign = orderFormBo.getIsSign();
        // 后端计算挂账 {0:全款；1:挂账}
        int calculateSign = 0;

        // 1.获取会员信息 [[
        String carLicense = orderFormBo.getCarLicense();
        if (!StringUtils.isBlank(carLicense)) {
            carLicense = carLicense.toUpperCase();
        }
        String ver = orderFormBo.getVer();
        Integer refer = orderFormBo.getRefer();
        String referStr = "";
        if (refer != null) {
            referStr = refer + "";
        }
        CustomerCar customerCar = customerCarService.selectIfNoExistInsert(userInfo, carLicense, ver, referStr);
        // ]]

        // 2.包装新增工单实体
        OrderInfo newOrder = generateOrder(orderFormBo, shopId, optUserId, customerCar);

        // 3.包装工单服务 [[
        // 洗车服务
        Long orderServiceId = orderFormBo.getOrderServiceId();
        BigDecimal servicePrice = orderFormBo.getServicePrice();
        OrderServices carwashOrderService = new OrderServices();
        // IF 自定义洗车 THEN 获取一个标准洗车服务
        if (orderServiceId == null || orderServiceId == 0) {
            // 标准洗车服务取一条
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.getBZCarWashList(shopId);
            if (CollectionUtils.isEmpty(shopServiceInfoList)) {
                return Result.wrapErrorResult("", "门店无标准洗车服务");
            }
            ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
            carwashOrderService.setServiceId(shopServiceInfo.getId());
            carwashOrderService.setServiceSn(shopServiceInfo.getServiceSn());
            carwashOrderService.setType(shopServiceInfo.getType());
            carwashOrderService.setServiceName(shopServiceInfo.getName());
            carwashOrderService.setServiceCatId(shopServiceInfo.getCategoryId());
            //设置serviceCatName
            setServiceCateName(carwashOrderService);
            carwashOrderService.setServicePrice(servicePrice);
            // 1工时 折扣0
            carwashOrderService.setServiceHour(BigDecimal.ONE);
            carwashOrderService.setServiceAmount(servicePrice);
            carwashOrderService.setDiscount(BigDecimal.ZERO);
            carwashOrderService.setSoldPrice(servicePrice);
            carwashOrderService.setSoldAmount(servicePrice);
        } else {
            Optional<ShopServiceInfo> serviceInfoOptional = shopServiceInfoService.get(orderServiceId);
            if (!serviceInfoOptional.isPresent()) {
                return Result.wrapErrorResult("", "你选择洗车服务不存在!");
            }
            ShopServiceInfo shopServiceInfo = serviceInfoOptional.get();
            carwashOrderService.setServiceId(shopServiceInfo.getId());
            carwashOrderService.setServiceSn(shopServiceInfo.getServiceSn());
            carwashOrderService.setType(shopServiceInfo.getType());
            carwashOrderService.setServiceName(shopServiceInfo.getName());
            carwashOrderService.setServiceCatId(shopServiceInfo.getCategoryId());
            carwashOrderService.setServiceCatName(shopServiceInfo.getCategoryName());
            carwashOrderService.setServicePrice(servicePrice);
            // 1工时 折扣0
            carwashOrderService.setServiceHour(BigDecimal.ONE);
            carwashOrderService.setServiceAmount(servicePrice);
            carwashOrderService.setDiscount(BigDecimal.ZERO);
            carwashOrderService.setSoldPrice(servicePrice);
            carwashOrderService.setSoldAmount(servicePrice);
        }

        // 关联维修工
        carwashOrderService.setWorkerIds(orderFormBo.getWorkerIds());
        List<OrderServices> orderServiceList = new ArrayList<OrderServices>(1);
        orderServiceList.add(carwashOrderService);
        // ]]

        // 兼容老版本工单数据 计算工单的金额
        orderService.calculateOrderExpense(newOrder, orderServiceList);
        BigDecimal orderTotalAmount = newOrder.getOrderAmount();
        newOrder.setTotalAmount(orderTotalAmount);

        // 4.获取结算方式 [[
        // 实收金额
        BigDecimal paidAmount = BigDecimal.ZERO;

        // 现金|其他方式 结算
        PayChannel payChannel = null;
        // 会员卡结算
        MemberCard memberCard = null;
        // 会员优惠折扣金额
        BigDecimal memberDiscountAmount = orderFormBo.getDiscountAmount();
        if (memberDiscountAmount == null) {
            memberDiscountAmount = BigDecimal.ZERO;
        }
        // 应付总金额 = 工单总金额-优惠金额
        BigDecimal receivableAmount = orderTotalAmount.subtract(memberDiscountAmount);
        // 会员余额支付金额
        BigDecimal cardPaidAmount = BigDecimal.ZERO;
        // 会员优惠券列表
        List<OrderDiscountFlowBo> discountCouponList = null;

        // 1.(现金/其他方式)结算
        payChannel = orderFormBo.getPayChannel();
        if (payChannel != null) {
            // 结算
            if (pageSign == 0) {
                paidAmount = receivableAmount;
            }
            // 全额挂账
            if (pageSign == 1) {
                paidAmount = BigDecimal.ZERO;
            }
        }

        // 2.会员卡余额结算
        Long cardMemberId = orderFormBo.getCardMemberId();
        if (cardMemberId != null && cardMemberId > 0l) {
            memberCard = memberCardService.getMemberCardById(shopId, cardMemberId);
            // 应付总金额 = 工单总金额-会员优惠金额
            BigDecimal balance = memberCard.getBalance();
            // 结算
            if (pageSign == 0) {
                if (balance != null && balance.compareTo(receivableAmount) == -1) {
                    calculateSign = 1;
                    paidAmount = balance;
                    cardPaidAmount = balance;
                } else {
                    calculateSign = 0;
                    paidAmount = receivableAmount;
                    cardPaidAmount = receivableAmount;
                }
            }

            // 全额挂账
            if (pageSign == 1) {
                calculateSign = 1;
                paidAmount = BigDecimal.ZERO;
                cardPaidAmount = BigDecimal.ZERO;
            }
        }

        // 3.优惠券结算
        BigDecimal couponTotalDiscount = BigDecimal.ZERO;
        discountCouponList = orderFormBo.getOrderDiscountFlowParamList();
        if (!CollectionUtils.isEmpty(discountCouponList)) {
            for (OrderDiscountFlowBo discountFlowBo : discountCouponList) {
                BigDecimal discountAmount = discountFlowBo.getDiscountAmount();
                discountAmount = (discountAmount == null) ? BigDecimal.ZERO : discountAmount;
                // 累加总折扣
                couponTotalDiscount = couponTotalDiscount.add(discountAmount);
            }

            if (couponTotalDiscount.compareTo(orderTotalAmount) == 1) {
                return Result.wrapErrorResult("", "'优惠券'优惠的总金额大于'洗车单金额' ");
            }

            // 核算 应收金额 = 工单总金额 - 优惠券总折扣金额
            receivableAmount = receivableAmount.subtract(couponTotalDiscount);
            if (receivableAmount.compareTo(BigDecimal.ZERO) == 1) {
                calculateSign = 1;
            }
            // 实收金额=0
            paidAmount = BigDecimal.ZERO;
        }

        // 5.保存工单和洗车服务
        newOrder.setTaxAmount(BigDecimal.ZERO);
        try {
            orderService.insertOrder(newOrder, null, orderServiceList, null, userInfo);
        } catch (Exception e) {
            log.error("[洗车单创建]保存洗车单失败,异常信息:{}", e);
            throw new RuntimeException("保存工单基本信息失败");
        }
        // 新工单ID
        Long newOrderId = newOrder.getId();
        log.info("[洗车单创建]保存工单基本信息成功");
        // ]]

        //  6.账单确认 [[
        ConfirmBillBo confirmBillBo = new ConfirmBillBo();
        // 收款单
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setRelId(newOrderId);
        debitBillBo.setRemark(orderFormBo.getPostscript());
        BigDecimal orderAmount = newOrder.getOrderAmount();
        debitBillBo.setTotalAmount(orderAmount);
        debitBillBo.setReceivableAmount(receivableAmount);
        confirmBillBo.setDebitBillBo(debitBillBo);

        // 会员卡结算优惠
        BigDecimal discountAmount = orderFormBo.getDiscountAmount();
        boolean useMemberCard = orderFormBo.isUseMemberCard();//是否使用会员卡
        if (useMemberCard) {
            Long useMemberCardId = orderFormBo.getUseMemberCardId();
            MemberCard memberCardForSettle = null;
            if (useMemberCardId != null && Long.compare(useMemberCardId, 0l) == 1) {
                memberCardForSettle = memberCardService.getMemberCardById(shopId, useMemberCardId);
            }
            if (memberCardForSettle != null) {
                confirmBillBo.setCardNumber(memberCardForSettle.getCardNumber());
                confirmBillBo.setAccountId(memberCardForSettle.getAccountId());
            }
            confirmBillBo.setMemberCardId(useMemberCardId);
        }
        confirmBillBo.setCardDiscountReason(orderFormBo.getCardDiscountReason());
        confirmBillBo.setDiscountAmount(discountAmount);

        // 优惠券关联工单ID
        if (!CollectionUtils.isEmpty(discountCouponList)) {
            confirmBillBo.setOrderDiscountFlowBoList(discountCouponList);
        }

        Result<DebitBillDTO> confirmBillResult = null;
        try {
            confirmBillResult = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
        } catch (Exception e) {
            log.error("[账单确认]洗车单确认账单失败 工单ID:{} 原因", newOrderId, e);
            throw new BizException("洗车单结算失败");
        }

        if (!confirmBillResult.isSuccess()) {
            String confirmErrorMessage = confirmBillResult.getErrorMsg();
            log.error("[账单确认]洗车单确认账单失败 工单ID:{} 原因:{}", newOrderId, confirmErrorMessage);
            throw new BizException("洗车单结算失败,原因:" + confirmErrorMessage);
        }
        log.info("[洗车单创建]账单确认成功");
        // ]]

        // 7.账单收款 [[
        if (paidAmount != null && paidAmount.compareTo(BigDecimal.ZERO) == 1) {
            // 收款流水
            List<DebitBillFlowBo> flowList = new ArrayList<DebitBillFlowBo>();

            //  现金|其他方式 支付
            if (payChannel != null) {
                DebitBillFlowBo billFlow = new DebitBillFlowBo();
                billFlow.setPaymentId(payChannel.getChannelId());
                billFlow.setPaymentName(payChannel.getChannelName());
                billFlow.setPayAmount(paidAmount);
                flowList.add(billFlow);
            }

            // 会员卡余额支付
            if (memberCard != null && cardPaidAmount.compareTo(BigDecimal.ZERO) == 1) {
                DebitBillFlowBo cardBalanceBillFlow = new DebitBillFlowBo();
                cardBalanceBillFlow.setPaymentId(0l);
                cardBalanceBillFlow.setPayAmount(cardPaidAmount);
                cardBalanceBillFlow.setPaymentName("会员卡");
                flowList.add(cardBalanceBillFlow);
            }

            DebitBillFlowSaveBo flowSaveBo = new DebitBillFlowSaveBo();
            flowSaveBo.setShopId(shopId);
            flowSaveBo.setUserId(userInfo.getUserId());
            flowSaveBo.setOrderId(newOrderId);
            flowSaveBo.setRemark(orderFormBo.getPostscript());
            flowSaveBo.setMemberPayAmount(cardPaidAmount);
            flowSaveBo.setFlowList(flowList);
            flowSaveBo.setMemberCardId(cardMemberId);
            try {
                debitFacade.saveFlowList(flowSaveBo);
            } catch (Exception e) {
                log.error("[账单收款]洗车单账单收款失败 工单ID:{} 原因:{}", newOrderId, e);
                throw new RuntimeException("洗车单结算失败");
            }
        }
        //添加工单详情日志start
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(newOrderId, shopId);
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            StringBuffer orderLog = new StringBuffer();
            orderLog.append("app洗车单创建成功: 工单号为:");
            orderLog.append(orderInfo.getId());
            orderLog.append(" 操作人:");
            orderLog.append(userInfo.getUserId());
            String operationLog = OrderOperationLog.getOperationLog(orderInfo, orderLog);
            log.info(operationLog);
        }
        //添加工单详情日志end
        //]]

        // 8.附加信息处理 [[
        try {
            orderInfoExtSaveThread.init(customerCar, newOrder, userInfo, "",
                    carwashOrderService.toString(), null);
            threadManager.execute(orderInfoExtSaveThread);
        } catch (Exception e) {
            log.error("异步保存工单无关紧要信息,异常信息:{}", e);
        }
        //]]

        // 包装响应实体 [[
        CreateCarWashResponse createCarWashResponse = new CreateCarWashResponse();
        createCarWashResponse.setOrderId(newOrderId);
        if (calculateSign == 1) {
            createCarWashResponse.setIsSign(1);
            createCarWashResponse.setSignAmount(receivableAmount.subtract(paidAmount));
        } else {
            createCarWashResponse.setIsSign(0);
            createCarWashResponse.setSignAmount(BigDecimal.ZERO);
        }
        // ]]

        return Result.wrapSuccessfulResult(createCarWashResponse);
    }


    /**
     * wrapper new order
     *
     * @param orderFormEntity 洗车单表单实体
     * @param shopId          当前门店
     * @param optUserId       当前操作人
     * @param customerCar     客户信息
     * @return
     */
    private OrderInfo generateOrder(CarwashOrderFormBo orderFormEntity,
                                    Long shopId,
                                    Long optUserId,
                                    CustomerCar customerCar) {
        OrderInfo newOrder = new OrderInfo();
        // 开单门店
        newOrder.setShopId(shopId);
        // 开单时间
        String createTimeStr = orderFormEntity.getCreateTimeStr();
        Date createTime = null;
        if (StringUtils.isEmpty(createTimeStr)) {
            createTime = new Date();
        } else {
            createTime = DateUtil.convertStringToDate(createTimeStr, "yyyy-MM-dd HH:mm");
        }
        newOrder.setCreateTime(createTime);
        // 工单标记为“洗车单”
        newOrder.setOrderTag(OrderCategoryEnum.CARWASH.getCode());
        // 工单来源
        newOrder.setRefer(orderFormEntity.getRefer());
        // 版本号
        newOrder.setVer(orderFormEntity.getVer());
        // 车牌图片(APP使用)
        if (!StringUtils.isBlank(orderFormEntity.getImgUrl())) {
            newOrder.setImgUrl(orderFormEntity.getImgUrl());
        }
        // 耦合会员信息到工单
        newOrder.setCarLicense(customerCar.getLicense());
        newOrder.setCustomerCarId(customerCar.getId());
        newOrder.setCustomerId(customerCar.getCustomerId());
        newOrder.setCarBrand(customerCar.getCarBrand());
        newOrder.setCarBrandId(customerCar.getCarBrandId());
        newOrder.setCarSeries(customerCar.getCarSeries());
        newOrder.setCarSeriesId(customerCar.getCarSeriesId());
        newOrder.setCarModelsId(customerCar.getCarModelId());
        newOrder.setCarModels(customerCar.getCarModel());
        newOrder.setCarCompany(customerCar.getCarCompany());
        newOrder.setImportInfo(customerCar.getImportInfo());
        newOrder.setCarAlias(customerCar.getByName());
        newOrder.setContactName(customerCar.getContact());
        newOrder.setContactMobile(customerCar.getContactMobile());
        newOrder.setCustomerMobile(customerCar.getMobile());
        newOrder.setCustomerName(customerCar.getCustomerName());
        // 客户单位
        String company = orderFormEntity.getCompany();
        newOrder.setCompany((company == null) ? " " : company);
        // 行驶里程
        newOrder.setMileage(customerCar.getMileage() + "");
        // TODO 洗车单的业务类型
        newOrder.setOrderType(0l);
        // 服务顾问ID
        newOrder.setReceiver(orderFormEntity.getReceiver());
        // 服务顾问名称
        newOrder.setReceiverName(orderFormEntity.getReceiverName());
        // 设置备注
        newOrder.setPostscript(orderFormEntity.getPostscript());
        // 完工时间
        newOrder.setFinishTime(new Date());
        // 创建人
        newOrder.setCreator(optUserId);
        // 创建时间
        newOrder.setGmtCreate(new Date());
        return newOrder;
    }
}
