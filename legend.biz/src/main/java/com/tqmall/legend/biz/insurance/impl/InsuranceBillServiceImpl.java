package com.tqmall.legend.biz.insurance.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.settlement.ISettlementService;
import com.tqmall.legend.biz.shop.ServiceTemplateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.insurance.InsuranceBillDao;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.insurance.InsuranceBillSettleVO;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.shop.ServiceTemplate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.activity.BillTypeEnum;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * bill service implement
 */
@Slf4j
@Service
public class InsuranceBillServiceImpl extends BaseServiceImpl implements IInsuranceBillService {

    @Autowired
    InsuranceBillDao insuranceBillDao;
    @Autowired
    ISettlementService settlementService;
    @Autowired
    IOrderService orderService;
    @Autowired
    ShopServiceInfoService shopServiceInfoService;
    @Autowired
    CustomerCarService customerCarService;
    @Autowired
    private ServiceTemplateService serviceTemplateService;
    @Autowired
    private IShopActivityService shopActivityService;
    @Autowired
    ConfirmBillFacade confirmBillFacade;

    @Override
    public Optional<InsuranceBill> get(Long billId) {
        InsuranceBill insuranceBill = null;
        try {
            insuranceBill = insuranceBillDao.selectById(billId);
            if (insuranceBill != null && InsuranceOrderStatusEnum.getsNameByCode(insuranceBill.getAuditStatus()) != null) {
                String auditStatusName = InsuranceOrderStatusEnum.getsNameByCode(insuranceBill.getAuditStatus());
                insuranceBill.setAuditStatusName(auditStatusName);
                Long customerCarId = insuranceBill.getCustomerCarId();
                Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(customerCarId);
                if (customerCarOptional.isPresent()) {
                    CustomerCar customerCar = customerCarOptional.get();
                    Long carYearId = customerCar.getCarYearId();
                    Long carPowerId = customerCar.getCarPowerId();
                    Long carGearBoxId = customerCar.getCarGearBoxId();
                    String carYear = customerCar.getCarYear();
                    String carPower = customerCar.getCarPower();
                    String carGearBox = customerCar.getCarGearBox();
                    insuranceBill.setCarYearId(carYearId);
                    insuranceBill.setCarPowerId(carPowerId);
                    insuranceBill.setCarGearBoxId(carGearBoxId);
                    insuranceBill.setCarYear(carYear);
                    insuranceBill.setCarPower(carPower);
                    insuranceBill.setCarGearBox(carGearBox);
                }
            }
        } catch (Exception e) {
            log.error("[DB]query legend_insurance_bill exception:{}", e.toString());
            return Optional.absent();
        }

        return Optional.fromNullable(insuranceBill);
    }

    @Override
    @Transactional
    public Result save(InsuranceBill insuranceBill, UserInfo userInfo) {
        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        insuranceBill.setShopId(userInfo.getShopId());
        insuranceBill.setCreator(userInfo.getUserId());
        insuranceBill.setGmtCreate(new Date());

        int responseValue = insuranceBillDao.insert(insuranceBill);
        if (responseValue == 1) {
            return Result.wrapSuccessfulResult(insuranceBill.getId());
        }
        return Result.wrapErrorResult("", "新增保险维修单失败");
    }

    @Override
    @Transactional
    public Result update(InsuranceBill insuranceBill, UserInfo userInfo) throws BusinessCheckedException {
        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        insuranceBill.setShopId(userInfo.getShopId());
        insuranceBill.setModifier(userInfo.getUserId());
        insuranceBill.setGmtModified(new Date());

        Long primaryId = insuranceBill.getId();
        if (primaryId == null || primaryId < 0) {
            throw new BusinessCheckedException("", "无效保险维修单");
        }

        int responseValue = insuranceBillDao.updateById(insuranceBill);
        if (responseValue == 1) {
            return Result.wrapSuccessfulResult(primaryId);
        }
        return Result.wrapErrorResult("", "更新保险维修单失败");
    }

    @Override
    @Transactional
    public Result batchUpdate(Map<String, Object> params) throws BusinessCheckedException {
        int result = insuranceBillDao.updateBills(params);
        if (result > 0) {
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapErrorResult("", "批量修改失败");
    }

    @Override
    public Page<InsuranceBill> getOrderInfoPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<InsuranceBill> page = super.getPage(insuranceBillDao, pageable, searchParams);
        if (!CollectionUtils.isEmpty(page.getContent())) {
            Set<Long> ids = new HashSet<>();
            for (InsuranceBill insuranceBill : page.getContent()) {
                Long shopActId = insuranceBill.getShopActId();
                ids.add(shopActId);
            }
            searchParams.clear();
            searchParams.put("ids", ids);
            Map<Long, ShopActivity> shopActivityMap = shopActivityService.getShopActivityMap(searchParams);
            for (InsuranceBill insuranceBill : page.getContent()) {
                Long shopActId = insuranceBill.getShopActId();
                if (shopActivityMap.containsKey(shopActId)) {
                    ShopActivity shopActivity = shopActivityMap.get(shopActId);
                    Long actTplId = shopActivity.getActTplId();
                    String actName = shopActivity.getActName();
                    insuranceBill.setActTplId(actTplId);
                    insuranceBill.setActName(actName);
                }
            }
        }
        return page;
    }

    @Override
    public Page<InsuranceBill> getActivitySettlePage(Pageable pageable, Map<String, Object> searchParams) {
        Page<InsuranceBill> page = super.getPage(insuranceBillDao, pageable, searchParams);
        if (!CollectionUtils.isEmpty(page.getContent())) {
            setSettlePrice(page.getContent());
        }
        return page;
    }

    /**
     * 设置结算价：service_template表中的settle_price字段
     */
    private void setSettlePrice(List<InsuranceBill> page) {
        Map<String, Object> searchParam = new HashMap<>();
        Set<Long> serviceIds = new HashSet<Long>();
        for (InsuranceBill bill : page) {
            serviceIds.add(bill.getServiceId());
        }
        searchParam.put("ids", serviceIds);
        List<ShopServiceInfo> serviceInfoList = shopServiceInfoService.select(searchParam);
        if (CollectionUtils.isEmpty(serviceInfoList)) {
            return;
        }
        Set<Long> parentIds = new HashSet<Long>();
        HashMap<Long, Long> serviceMap = new HashMap<>();
        for (ShopServiceInfo service : serviceInfoList) {
            parentIds.add(service.getParentId());
            serviceMap.put(service.getId(), service.getParentId());
        }
        searchParam.clear();
        searchParam.put("ids", parentIds);
        List<ServiceTemplate> parentList = serviceTemplateService.select(searchParam);
        HashMap<Long, ServiceTemplate> templateMap = new HashMap<>();
        for (ServiceTemplate template : parentList) {
            templateMap.put(template.getId(), template);
        }
        for (InsuranceBill bill : page) {
            Long serviceId = bill.getServiceId();
            if (serviceMap.containsKey(serviceId)) {
                Long parentId = serviceMap.get(serviceId);
                if (templateMap.containsKey(parentId)) {
                    ServiceTemplate template = templateMap.get(parentId);
                    bill.setSettlePrice(template.getSettlePrice());
                }
            }
        }
    }

    @Override
    public List<InsuranceBill> select(Map<String, Object> searchParams) {
        List<InsuranceBill> insuranceBillList = insuranceBillDao.select(searchParams);
        return insuranceBillList;
    }

    @Override
    @Transactional
    public Result submit(InsuranceBill insuranceBill, UserInfo userInfo) throws BusinessCheckedException, RuntimeException {

        Long shopId = userInfo.getShopId();
        Long optUserId = userInfo.getUserId();
        String optUserName = userInfo.getName();

        // 1.generate order
        OrderInfo order = new OrderInfo();

        // 标记"4:保险维修单"
        order.setOrderTag(OrderCategoryEnum.INSURANCE.getCode());
        // 同步车牌照图片到工单
        order.setImgUrl(insuranceBill.getImgUrl());

        // TODO 冗余客户信息到工单
        String carLicense = insuranceBill.getCarLicense();
        customerCarService.copyCustomerProperty(userInfo, order, carLicense);

        // wrapper [[ 保险维修服务列表 目前仅一项服务
        Long insuranceServiceId = insuranceBill.getServiceId();
        Optional<ShopServiceInfo> serviceOptional = shopServiceInfoService.get(insuranceServiceId);
        if (!serviceOptional.isPresent()) {
            log.error("保险单提交失败，保险维修服务不存在，服务ID:{}", insuranceServiceId);
            throw new RuntimeException("保险单提交失败，保险维修服务不存在");
        }
        ShopServiceInfo serviceInfo = serviceOptional.get();

        List<OrderServices> orderServiceList = new ArrayList<OrderServices>(1);
        // 选购服务(工时费:0 工时:1 优惠金额:0)
        OrderServices insuranceService = new OrderServices();
        insuranceService.setServiceId(serviceInfo.getId());
        insuranceService.setServiceSn(serviceInfo.getServiceSn());
        insuranceService.setServiceName(serviceInfo.getName());
        // 基础服务类型
        insuranceService.setType(Integer.valueOf(OrderServiceTypeEnum.BASIC.getCode()));
        // 服务类别
        insuranceService.setServiceCatId(serviceInfo.getCategoryId());
        // 服务类别名称
        insuranceService.setServiceCatName(serviceInfo.getCategoryName());
        insuranceService.setFlags(serviceInfo.getFlags());
        // 工时费
        BigDecimal servicePrice = BigDecimal.ZERO;
        // 工时费
        insuranceService.setServicePrice(servicePrice);
        // 工时:1
        insuranceService.setServiceHour(BigDecimal.ONE);
        // 服务总费用
        insuranceService.setServiceAmount(servicePrice);
        // 优惠金额
        insuranceService.setDiscount(BigDecimal.ZERO);
        insuranceService.setSoldPrice(servicePrice);
        insuranceService.setSoldAmount(servicePrice);
        orderServiceList.add(insuranceService);
        // ]]

        // 服务价格(servicePrice) = 工单总金额(totalAmount)
        BigDecimal totalAmount = servicePrice;

        // 核算工单金额
        orderService.calculateOrderExpense(order, orderServiceList);

        // 核算工单优惠(兼容普通工单)
        // 折扣
        BigDecimal preDiscountRate = BigDecimal.ONE;
        order.setPreDiscountRate(preDiscountRate);
        // 费用
        BigDecimal preTaxAmount = BigDecimal.ZERO;
        order.setPreTaxAmount(preTaxAmount);
        // 优惠
        BigDecimal prePreferentiaAmount = BigDecimal.ZERO;
        order.setPrePreferentiaAmount(prePreferentiaAmount);
        // 代金券
        BigDecimal preCouponAmount = BigDecimal.ZERO;
        order.setPreCouponAmount(preCouponAmount);
        // 实际应收金额 =工单总金额 * 折扣 + 费用 - 优惠 - 代金券
        BigDecimal preTotalAmount = totalAmount.multiply(preDiscountRate)
                .add(preTaxAmount)
                .subtract(prePreferentiaAmount)
                .subtract(preCouponAmount);
        order.setPreTotalAmount(preTotalAmount);

        // TODO mybatis底层处理 设置操作信息
        order.setCreator(optUserId);
        order.setModifier(optUserId);
        order.setOperatorName(optUserName);
        order.setShopId(shopId);

        // TODO 工单类型
        order.setOrderType(0l);
        // 设置服务顾问(当前操作人)
        order.setReceiver(optUserId);
        order.setReceiverName(optUserName);
        // 工单状态为“已结算”
        order.setOrderStatus(OrderStatusEnum.DDYFK.getKey());
        // 支付状态"已支付"
        order.setPayStatus(PayStatusEnum.PAYED.getCode());
        // 完工时间
        order.setFinishTime(new Date());
        // 开单时间
        order.setCreateTime(new Date());

        // 保存工单基本信息
        OrderInfo newOrder = orderService.save(order, orderServiceList, userInfo);
        log.info("[业务流水-保险维修单]生成工单 成功");

        // 2.update insurance bill
        Long newOrderId = newOrder.getId();
        insuranceBill.setOrderId(newOrderId);
        this.update(insuranceBill, userInfo);
        log.info("[业务流水-保险维修单]更新保险单基础数据 成功");

        // 3. 账单确认
        ConfirmBillBo confirmBillBo = new ConfirmBillBo();
        // 收款单
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setRelId(newOrderId);
        debitBillBo.setTotalAmount(totalAmount);
        confirmBillBo.setDebitBillBo(debitBillBo);
        Result<DebitBillDTO> confirmBillResult = null;
        try {
            confirmBillResult = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
        } catch (Exception e) {
            log.error("[账单确认]引流活动账单确认失败 工单ID:{} 原因:{}", newOrderId, e);
            throw new RuntimeException("服务单提交审核失败");
        }
        if (!confirmBillResult.isSuccess()) {
            throw new RuntimeException("服务单提交审核失败");
        }
        log.info("[业务流水-保险维修单]工单结算成功,工单ID为：{} 操作人：{}", newOrderId, optUserId);

        return Result.wrapSuccessfulResult(newOrderId);
    }


    @Override
    @Transactional
    public Result reSubmit(InsuranceBill bill, UserInfo userInfo) throws BusinessCheckedException {
        // 保险单ID
        Long insuranceId = bill.getId();
        // 车牌
        String carLicense = bill.getCarLicense();

        // 置为审核状态
        bill.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.AUDITING.getCode()));

        // update insurance bill
        Result updateBillResult = this.update(bill, userInfo);
        if (!updateBillResult.isSuccess()) {
            log.error("重新提交保险单失败，原因:更新保险单基本信息失败，保险单ID:{}", insuranceId);
            throw new RuntimeException("更新保险单基本信息失败");
        }

        // syn update refer orderinfo
        Long referOrderId = bill.getOrderId();
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(referOrderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("保险单重新提交失败，原因:关联工单不存在，工单ID:{} 保险单ID:{}", referOrderId, insuranceId);
            throw new RuntimeException("保险单关联工单不存在");
        }
        OrderInfo referOrderInfo = orderInfoOptional.get();

        // copy customer's property to bill
        customerCarService.copyCustomerProperty(userInfo, referOrderInfo, carLicense);

        // update refer bill
        int updateOrderSign = orderService.updateOrder(referOrderInfo);
        if (updateOrderSign == 0) {
            log.error("保险单重新提交失败，原因:更新关联工单基本信息失败，工单ID:{}", referOrderId);
            throw new RuntimeException("更新关联工单基本信息失败");
        }
        return Result.wrapSuccessfulResult(bill);
    }


    @Override
    @Transactional
    public Result create(InsuranceBill bill, UserInfo userInfo) {

        // check property
        Long serviceId = bill.getServiceId();
        Optional<ShopServiceInfo> serviceInfoOptional = shopServiceInfoService.get(serviceId);
        if (!serviceInfoOptional.isPresent()) {
            log.error("创建服务单失败,原因:套餐服务不存在,服务ID:{}", serviceId);
            throw new RuntimeException("您选择服务套餐不存在!");
        }
        ShopServiceInfo serviceInfo = serviceInfoOptional.get();
        String serviceName = serviceInfo.getName();

        // 1.generate order
        OrderInfo newOrder = generateOrder(bill, userInfo);

        // 2.create insurance bill
        Long newOrderId = newOrder.getId();
        bill.setOrderId(newOrderId);
        bill.setServiceName(serviceName);

        // 创建服务单
        // TODO 设置公共信息(创建者、创建时间、更新者、更新时间)
        bill.setShopId(userInfo.getShopId());
        bill.setCreator(userInfo.getUserId());
        bill.setGmtCreate(new Date());
        // 关联新建工单
        bill.setOrderId(newOrderId);
        insuranceBillDao.insert(bill);
        log.info("[业务流水-客户到店服务]生成服务单 成功");

        return Result.wrapSuccessfulResult(bill);
    }


    @Override
    @Transactional
    public Result adult(InsuranceBill bill, UserInfo userInfo) throws BusinessCheckedException {

        // current operate message
        Long shopId = userInfo.getShopId();
        Long optUserId = userInfo.getUserId();

        // 1.服务单'待审核'
        Long billId = bill.getId();
        Optional<InsuranceBill> billOptional = this.get(billId);
        if (!billOptional.isPresent()) {
            log.error("服务单提交审核失败,原因:服务单不存在,服务单编号{}", billId);
            throw new BusinessCheckedException("", "服务单不存在");
        }
        // 审核中
        bill.setAuditStatus(Integer.valueOf(InsuranceOrderStatusEnum.AUDITING.getCode()));
        this.update(bill, userInfo);
        // 工单实体
        Long orderId = billOptional.get().getOrderId();
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            log.error("服务单提交审核失败,原因:工单不存在,工单编号{}", orderId);
            throw new BusinessCheckedException("", "工单不存在");
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        // 总金额
        BigDecimal orderAmount = orderInfo.getOrderAmount();

        // 2. 账单确认
        ConfirmBillBo confirmBillBo = new ConfirmBillBo();
        // 收款单
        DebitBillBo debitBillBo = new DebitBillBo();
        debitBillBo.setRelId(orderId);
        debitBillBo.setTotalAmount(orderAmount);
        confirmBillBo.setDebitBillBo(debitBillBo);

        Result<DebitBillDTO> confirmBillResult = null;
        try {
            confirmBillResult = confirmBillFacade.confirmBill(confirmBillBo, userInfo);
        } catch (Exception e) {
            log.error("[账单确认]引流活动账单确认失败 工单ID:{} 原因:{}", orderId, e);
            throw new RuntimeException("服务单提交审核失败");
        }

        if (!confirmBillResult.isSuccess()) {
            throw new RuntimeException("服务单提交审核失败");
        }
        log.info("[业务流水-客户到店服务]服务单提交审核,工单ID为：{} 操作人：{}", orderId, optUserId);
        return Result.wrapSuccessfulResult(bill);
    }


    @Override
    @Transactional
    public Result<InsuranceBill> modify(InsuranceBill bill, UserInfo userInfo) throws BusinessCheckedException {
        // 1.获取服务单
        Long primaryId = bill.getId();
        if (primaryId == null || primaryId < 0) {
            throw new BusinessCheckedException("", "无效服务单");
        }

        Optional<InsuranceBill> billOptional = this.get(primaryId);
        if (!billOptional.isPresent()) {
            throw new BusinessCheckedException("", "无效服务单");
        }

        // 2.更新服务单关联的工单(先删除工单-重新创建工单)
        Long orderId = billOptional.get().getOrderId();
        orderService.delete(orderId);
        OrderInfo newOrder = generateOrder(bill, userInfo);

        // 3.重新关联工单
        bill.setOrderId(newOrder.getId());
        bill.setShopId(userInfo.getShopId());
        bill.setModifier(userInfo.getUserId());
        bill.setGmtModified(new Date());
        insuranceBillDao.updateById(bill);

        return Result.wrapSuccessfulResult(bill);
    }

    @Override
    @Transactional
    public void create(UserInfo userinfo, OrderInfo orderInfo, String couponCode, InsuranceOrderStatusEnum insuranceOrderStatusEnum) {
        Long shopId = orderInfo.getShopId();
        // new bill
        InsuranceBill bill = new InsuranceBill();
        bill.setShopId(orderInfo.getShopId());
        // refer order
        Long orderId = orderInfo.getId();
        bill.setOrderId(orderId);
        // refer customerCar
        customerCarService.copyCustomerProperty(userinfo, bill, orderInfo.getCarLicense());

        // 服务ID,服务名称
        Optional<ShopServiceInfo> serviceInfoOptional = shopServiceInfoService.getCouponActivityService(couponCode, shopId);
        if (serviceInfoOptional.isPresent()) {
            ShopServiceInfo serviceInfo = serviceInfoOptional.get();
            bill.setServiceId(serviceInfo.getId());
            bill.setServiceName(serviceInfo.getName());
            bill.setShopActId(serviceInfo.getShopActivityId());
        } else {
            bill.setServiceId(0l);
            bill.setServiceName("");
            bill.setShopActId(0l);
        }

        if (insuranceOrderStatusEnum == null) {
            // 审核状态:审核中
            bill.setAuditStatus(InsuranceOrderStatusEnum.AUDITING.getCode());
        } else {
            bill.setAuditStatus(insuranceOrderStatusEnum.getCode());
        }
        //如果审核通过，设置审核通过时间
        if (bill.getAuditStatus().equals(InsuranceOrderStatusEnum.PASS.getCode())) {
            bill.setAuditPassTime(new Date());
        }
        // 对应核销码为 '优惠券码'
        bill.setVerificationCode(couponCode);

        // save bill
        this.save(bill, userinfo);
        Long billId = bill.getId();
        log.info("使用淘汽优惠券的工单->生成服务单成功,服务单编号:{} 工单编号:{}", billId, orderId);
    }

    /**
     * 获取门店引流活动收入汇总账单
     *
     * @return
     */
    @Override
    public Result getInsuranceBillSettleList(Map<String, Object> searchParams) {
        if (!searchParams.containsKey("auditPassStartTime") || !searchParams.containsKey("auditPassEndTime")) {
            return Result.wrapErrorResult(LegendErrorCode.PARAMS_ERROR.getCode(), "请输入审核通过时间范围");
        }
        if (!searchParams.containsKey("shopId")) {
            return Result.wrapErrorResult(LegendErrorCode.PARAMS_ERROR.getCode(), "门店不存在,请重新登录");
        }
        //设置查询时间
        StringBuffer auditPassTimeStr = new StringBuffer();// 审核通过查询时间
        String auditPassStartTime = (String) searchParams.get("auditPassStartTime");
        String auditPassEndTime = (String) searchParams.get("auditPassEndTime");
        auditPassTimeStr.append(DateUtil.convertDate(DateUtil.convertStringToDateYMD(auditPassStartTime)));
        auditPassTimeStr.append("-");
        auditPassTimeStr.append(DateUtil.convertDate(DateUtil.convertStringToDateYMD(auditPassEndTime)));
        searchParams.put("auditPassStartTime", auditPassStartTime + " 00:00:00");
        searchParams.put("auditPassEndTime", auditPassEndTime + " 23:59:59");
        Long shopId = (Long) searchParams.get("shopId");
        //查询门店参加的活动
        List<ShopActivity> shopActivityList = shopActivityService.getShopActivityAllList(shopId);
        List<ShopActivity> resultShopActList = new ArrayList<>();
        if (searchParams.containsKey("shopActId")) {
            Long shopActId = Long.parseLong((String) searchParams.get("shopActId"));
            for (ShopActivity shopActivity : shopActivityList) {
                Long temp = shopActivity.getId();
                if (temp.equals(shopActId)) {
                    resultShopActList.add(shopActivity);
                }
            }
            if (CollectionUtils.isEmpty(resultShopActList)) {
                return Result.wrapErrorResult(LegendErrorCode.DATA_EMPTY_ERROR.getCode(), "数据为空");
            }
        } else {
            resultShopActList = shopActivityList;
        }
        if (!CollectionUtils.isEmpty(shopActivityList)) {
            //查询门店建的服务单
            List<InsuranceBill> insuranceBillList = insuranceBillDao.select(searchParams);
            List<InsuranceBillSettleVO> insuranceBillSettleVOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(insuranceBillList)) {
                //根据serviceId获取模板服务数据
                Map<Long, ServiceTemplate> serviceTemplateMap = new HashMap<>();
                //查询门店服务
                Set<Long> setIds = new HashSet<>();
                for (InsuranceBill insuranceBill : insuranceBillList) {
                    Long serviceId = insuranceBill.getServiceId();
                    setIds.add(serviceId);
                }
                searchParams.clear();
                searchParams.put("shopId", shopId);
                searchParams.put("ids", setIds);
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(searchParams);
                if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                    setIds.clear();
                    for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                        Long parentId = shopServiceInfo.getParentId();
                        setIds.add(parentId);
                    }
                    searchParams.clear();
                    searchParams.put("shopId", shopId);
                    searchParams.put("ids", setIds);
                    //查询模板服务
                    List<ServiceTemplate> serviceTemplateList = serviceTemplateService.select(searchParams);
                    //设置门店服务对应的模板服务
                    if (!CollectionUtils.isEmpty(serviceTemplateList)) {
                        Map<Long, ServiceTemplate> templateMap = new HashMap<>();
                        for (ServiceTemplate serviceTemplate : serviceTemplateList) {
                            templateMap.put(serviceTemplate.getId(), serviceTemplate);
                        }
                        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                            Long parentId = shopServiceInfo.getParentId();
                            Long serviceId = shopServiceInfo.getId();
                            if (templateMap.containsKey(parentId)) {
                                ServiceTemplate serviceTemplate = templateMap.get(parentId);
                                serviceTemplateMap.put(serviceId, serviceTemplate);
                            }
                        }
                    }
                }

                //组装数据,key为门店活动id(包括门店自有的活动和平台的活动)

                for (ShopActivity shopActivity : resultShopActList) {
                    Long id = shopActivity.getId();
                    Long actTplId = shopActivity.getActTplId();
                    String actName = shopActivity.getActName();//活动类型名称
                    Integer serviceNumTemp = 0; //活动服务单数
                    BigDecimal totalServiceAmount = BigDecimal.ZERO; // 活动服务售总卖价
                    BigDecimal totalSettleAmount = BigDecimal.ZERO;// 活动服务总收入
                    boolean shopConfirm = false;//是否需要对账

                    InsuranceBillSettleVO insuranceBillSettleVO = new InsuranceBillSettleVO();
                    insuranceBillSettleVO.setAuditPassTimeStr(auditPassTimeStr.toString()); //审核通过时间
                    insuranceBillSettleVO.setActTplId(actTplId);
                    insuranceBillSettleVO.setActName(actName);
                    //TODO 待优化
                    for (InsuranceBill insuranceBill : insuranceBillList) {
                        Long shopActId = insuranceBill.getShopActId();
                        Long serviceId = insuranceBill.getServiceId();
                        Integer shopConfirmStatus = insuranceBill.getShopConfirmStatus();
                        if (id.equals(shopActId)) {
                            if (shopConfirmStatus.equals(0)) {
                                //需要对账
                                shopConfirm = true;
                            }
                            //活动单据+1
                            serviceNumTemp++;
                            if (serviceTemplateMap.containsKey(serviceId)) {
                                ServiceTemplate serviceTemplate = serviceTemplateMap.get(serviceId);
                                BigDecimal servicePrice = serviceTemplate.getServicePrice();
                                BigDecimal settlePrice = serviceTemplate.getSettlePrice();
                                totalServiceAmount = totalServiceAmount.add(servicePrice);
                                totalSettleAmount = totalSettleAmount.add(settlePrice);
                            }
                        }
                    }
                    insuranceBillSettleVO.setTotalServiceAmount(totalServiceAmount);
                    insuranceBillSettleVO.setTotalSettleAmount(totalSettleAmount);
                    insuranceBillSettleVO.setNeedSettle(shopConfirm);
                    insuranceBillSettleVO.setServiceNum(serviceNumTemp);
                    insuranceBillSettleVOList.add(insuranceBillSettleVO);
                }
                //统计合计金额数据
                InsuranceBillSettleVO statisticsVo = new InsuranceBillSettleVO();
                BigDecimal totalServicePrice = BigDecimal.ZERO;
                BigDecimal totalSettlePrice = BigDecimal.ZERO;
                for (InsuranceBillSettleVO insuranceBillSettleVO : insuranceBillSettleVOList) {
                    totalServicePrice = totalServicePrice.add(insuranceBillSettleVO.getTotalServiceAmount());
                    totalSettlePrice = totalSettlePrice.add(insuranceBillSettleVO.getTotalSettleAmount());
                }
                statisticsVo.setTotalServiceAmount(totalServicePrice);
                statisticsVo.setTotalSettleAmount(totalSettlePrice);
                insuranceBillSettleVOList.add(statisticsVo);
            } else {
                //设置默认值
                for (ShopActivity shopActivity : resultShopActList) {
                    Long actTplId = shopActivity.getActTplId();
                    String actName = shopActivity.getActName();//活动类型名称
                    Integer serviceNumTemp = 0; //活动服务单数
                    BigDecimal totalServiceAmount = BigDecimal.ZERO; // 活动服务售总卖价
                    BigDecimal totalSettleAmount = BigDecimal.ZERO;// 活动服务总收入
                    InsuranceBillSettleVO insuranceBillSettleVO = new InsuranceBillSettleVO();
                    insuranceBillSettleVO.setAuditPassTimeStr(auditPassTimeStr.toString()); //审核通过时间
                    insuranceBillSettleVO.setActTplId(actTplId);
                    insuranceBillSettleVO.setActName(actName);
                    insuranceBillSettleVO.setTotalServiceAmount(totalServiceAmount);
                    insuranceBillSettleVO.setTotalSettleAmount(totalSettleAmount);
                    insuranceBillSettleVO.setServiceNum(serviceNumTemp);
                    insuranceBillSettleVOList.add(insuranceBillSettleVO);
                }
                //统计合计金额数据
                InsuranceBillSettleVO statisticsVo = new InsuranceBillSettleVO();
                BigDecimal totalServicePrice = BigDecimal.ZERO;
                BigDecimal totalSettlePrice = BigDecimal.ZERO;
                statisticsVo.setTotalServiceAmount(totalServicePrice);
                statisticsVo.setTotalSettleAmount(totalSettlePrice);
                insuranceBillSettleVOList.add(statisticsVo);
            }
            return Result.wrapSuccessfulResult(insuranceBillSettleVOList);
        }
        return Result.wrapErrorResult(LegendErrorCode.DATA_EMPTY_ERROR.getCode(), "数据为空");
    }

    @Override
    public boolean checkVerificationCodeIsUsed(String verificationCode) {
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("verificationCode", verificationCode);
        List<InsuranceBill> insuranceBillList = insuranceBillDao.select(paramMap);
        return (insuranceBillList != null && insuranceBillList.size() > 0) ? true : false;
    }

    @Override
    public boolean checkVerificationCodeIsUsed(Long billId, String verificationCode) {
        Map<String, Object> paramMap = new HashMap<String, Object>(1);
        paramMap.put("verificationCode", verificationCode);
        List<InsuranceBill> insuranceBillList = insuranceBillDao.select(paramMap);

        // 未使用: 自己使用 and 从来没用过
        if (insuranceBillList != null && insuranceBillList.size() > 0) {
            if (insuranceBillList.size() == 1 &&
                    insuranceBillList.get(0).getId().equals(billId)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Result deleteBillByOrderId(Long orderId, UserInfo userInfo) {
        String userName = "";
        if (userInfo != null) {
            userName = userInfo.getName();
        }
        log.info("【根据工单id删除保险单】操作人：{}，工单：{}", userName, orderId);
        //校验工单是否存在
        try {
            Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
            if (orderInfoOptional.isPresent()) {
                OrderInfo orderInfo = orderInfoOptional.get();
                Long shopId = orderInfo.getShopId();
                Map<String, Object> searchMap = Maps.newHashMap();
                searchMap.put("orderId", orderId);
                searchMap.put("shopId",shopId);
                List<InsuranceBill> insuranceBillList = insuranceBillDao.select(searchMap);
                if(!CollectionUtils.isEmpty(insuranceBillList)){
                    List<Long> ids = Lists.newArrayList();
                    for(InsuranceBill insuranceBill : insuranceBillList){
                        Long id = insuranceBill.getId();
                        ids.add(id);
                    }
                    insuranceBillDao.deleteByIds(ids.toArray());
                }
            }else{
                log.info("【根据工单id删除保险单】操作人：{}，工单：{},对应工单不存在", userName, orderId);
            }
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("【根据工单id删除保险单】操作人：{}，工单：{},出现异常", userName, orderId,e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(),"报销单删除失败");
        }
    }

    /**
     * generate bill
     *
     * @param bill
     * @param userInfo
     * @return OrderInfo
     */
    public OrderInfo generateOrder(InsuranceBill bill, UserInfo userInfo) {

        // 当前操作人信息
        Long optUserId = userInfo.getUserId();
        String optUserName = userInfo.getName();
        Long shopId = userInfo.getShopId();

        // 1.generate order
        OrderInfo order = new OrderInfo();

        // TODO 工单类别
        order.setOrderTag(OrderCategoryEnum.INSURANCE.getCode());
        // 同步车牌照图片到工单
        String carLicensePicture = bill.getImgUrl();
        if (!StringUtils.isEmpty(carLicensePicture)) {
            order.setImgUrl(bill.getImgUrl());
        }

        // 冗余客户信息到工单
        String carLicense = bill.getCarLicense();
        customerCarService.copyCustomerProperty(userInfo, order, carLicense);

        // wrapper [[ 包装服务套餐
        Long insuranceServiceId = bill.getServiceId();
        Optional<ShopServiceInfo> serviceOptional = shopServiceInfoService.get(insuranceServiceId);
        if (!serviceOptional.isPresent()) {
            log.error("服务单创建失败，服务套餐不存在，服务ID:{}", insuranceServiceId);
            throw new RuntimeException("服务单创建失败，服务套餐不存在");
        }
        ShopServiceInfo shopServiceInfo = serviceOptional.get();
        BigDecimal shopServicePrice = shopServiceInfo.getServicePrice();
        shopServicePrice = (shopServicePrice == null) ? BigDecimal.ZERO : shopServicePrice;

        List<OrderServices> orderServiceList = new ArrayList<OrderServices>(1);
        // 选购服务(工时费:0 工时:1 优惠金额:0)
        OrderServices billService = new OrderServices();
        billService.setServiceId(shopServiceInfo.getId());
        billService.setServiceSn(shopServiceInfo.getServiceSn());
        billService.setServiceName(shopServiceInfo.getName());
        // 基础服务类型
        billService.setType(Integer.valueOf(OrderServiceTypeEnum.BASIC.getCode()));
        // 服务类别
        billService.setServiceCatId(shopServiceInfo.getCategoryId());
        // 服务类别名称
        billService.setServiceCatName(shopServiceInfo.getCategoryName());
        billService.setFlags(shopServiceInfo.getFlags());
        // 工时费
        BigDecimal servicePrice = shopServicePrice;
        BigDecimal serviceHour = BigDecimal.ONE;
        billService.setServicePrice(servicePrice);
        // 工时:1
        billService.setServiceHour(BigDecimal.ONE);
        // 服务总费用 servicePrice * serviceHour
        billService.setServiceAmount(servicePrice.multiply(serviceHour));
        // 优惠金额
        // IF 平安保养 THEN 工单金额为O 套餐服务金额 用 套餐服务折扣金额 抵消
        String billType = bill.getBillType();
        if (billType != null && billType.equals(BillTypeEnum.PINGAN_BAOYANG.getCode())) {
            billService.setDiscount(servicePrice);
        } else {
            billService.setDiscount(BigDecimal.ZERO);
        }
        billService.setSoldPrice(servicePrice);
        billService.setSoldAmount(servicePrice);
        orderServiceList.add(billService);
        // ]]

        // 核算工单金额
        orderService.calculateOrderExpense(order, orderServiceList);

        // 核算工单优惠(兼容普通工单)
        // 折扣
        BigDecimal preDiscountRate = BigDecimal.ONE;
        order.setPreDiscountRate(preDiscountRate);
        // 费用
        BigDecimal preTaxAmount = BigDecimal.ZERO;
        order.setPreTaxAmount(preTaxAmount);
        // 优惠
        BigDecimal prePreferentiaAmount = BigDecimal.ZERO;
        order.setPrePreferentiaAmount(prePreferentiaAmount);
        // 代金券
        BigDecimal preCouponAmount = BigDecimal.ZERO;
        order.setPreCouponAmount(preCouponAmount);
        // 工单实际应收总金额
        BigDecimal totalAmount = order.getOrderAmount();

        // 实际应收金额 =工单总金额 * 折扣 + 费用 - 优惠 - 代金券
        BigDecimal preTotalAmount = totalAmount.multiply(preDiscountRate)
                .add(preTaxAmount)
                .subtract(prePreferentiaAmount)
                .subtract(preCouponAmount);
        order.setPreTotalAmount(preTotalAmount);

        // TODO mybatis底层处理 设置操作信息
        order.setCreator(optUserId);
        order.setModifier(optUserId);
        order.setOperatorName(optUserName);
        order.setShopId(shopId);

        // TODO 工单类型
        order.setOrderType(0l);
        // 设置服务顾问(当前操作人)
        order.setReceiver(optUserId);
        order.setReceiverName(optUserName);
        // 工单状态为“已完工”
        order.setOrderStatus(OrderStatusEnum.DDWC.getKey());
        // 支付状态"未支付"
        order.setPayStatus(PayStatusEnum.UNPAY.getCode());
        // 完工时间
        order.setFinishTime(new Date());
        // 工单创建时间
        order.setCreateTime(new Date());

        // 保存工单基本信息
        OrderInfo newOrder = orderService.save(order, orderServiceList, userInfo);
        log.info("[业务流水-客户到店服务]生成工单 成功");

        return newOrder;
    }

}
