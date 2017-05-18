package com.tqmall.legend.web.order;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.vo.OrderGoodsVo;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderFormEntityBo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import com.tqmall.legend.service.service.RpcShopServiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 16/4/16.
 */
@Controller
@RequestMapping("shop/order")
@Slf4j
public class SpeedilyController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RpcShopServiceInfoService rpcShopServiceInfoService;

    /**
     * 有以下几种情况:
     * 1,新建/编辑快修快保单页面
     * 2,从预约单跳转过来
     * 3,从预检单跳转过来
     * 4,从新建客户页面跳转过来
     * 5,从客户详情跳转过来
     * 6,从客户详情复制工单跳转过来
     *
     * @param model
     * @param orderId 编辑需要传入的字段
     * @param appointId 从预约单跳转过来需要传入的字段
     * @param customerCarId 从预检单或客户详情页跳转过来需要传入的字段
     * @param copyOrderId 从客户详情页复制跳转过来需要传入的字段
     * @return
     */
    @HttpRequestLog
    @RequestMapping(value = "speedily", method = RequestMethod.GET)
    public String speedily(Model model,
                           @RequestParam(value = "orderId", required = false) Long orderId,
                           @RequestParam(value = "appointId", required = false) Long appointId,
                           @RequestParam(value = "customerCarId", required = false) Long customerCarId,
                           @RequestParam(value = "copyOrderId", required = false) Long copyOrderId,
                           HttpServletRequest request) {
        model.addAttribute("moduleUrl", "reception");

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // Log trac
        logTrac(SiteUrls.SPEEDILY, userId, null);

        // 获取热门快修快保服务列表
        com.tqmall.core.common.entity.Result<List<ShopServiceInfoDTO>> hotServiceResult = rpcShopServiceInfoService.getHotService(shopId);
        if(hotServiceResult.isSuccess()){
            model.addAttribute("shopServiceInfoList",hotServiceResult.getData());
        }
        if (orderId != null) { // 编辑
            // IF 校验跨门店 THEN 返回空页面
            Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
            if (!orderInfoOptional.isPresent()) {
                return "yqx/page/order/speedily";
            }
            try {
                wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, userInfo.getShopId());
            } catch (BusinessCheckedException e) {
                log.error("快修快保页封装失败，原因:{}", e.toString());
                model.addAttribute("isexist", 0);
                return "yqx/page/order/speedily";
            }
        } else if (appointId != null) { // 从预约单跳转过来
            appointToSpeedily(model, shopId, appointId);
        } else if (customerCarId != null) { // 从预检单或新建客户或客户详情跳转过来
            wrapperModelByCarId(model, customerCarId);
        } else if (copyOrderId != null) { // 从客户详情复制工单跳转过来
            // IF 校验跨门店 THEN 返回空页面
            Optional<OrderInfo> orderInfoOptional = orderService.getOrder(copyOrderId, shopId);
            if (!orderInfoOptional.isPresent()) {
                return "yqx/page/order/speedily";
            }
            try {
                OrderInfo orderInfo = wrapperOrderFacade.wrapperModelOfEditedPage(copyOrderId, model, userInfo.getShopId());
                orderInfo.setId(null);
                orderInfo.setCreateTime(null);
                orderInfo.setDownPayment(null);
                model.addAttribute("orderInfo", orderInfo);
            } catch (BusinessCheckedException e) {
                log.error("快修快保页封装失败，原因:{}", e.toString());
                model.addAttribute("isexist", 0);
                return "yqx/page/order/speedily";
            }
        } else { // 新建
            //按门店所在城市设置门店头两位车牌号
            String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
            model.addAttribute("license", license);
        }
        return "yqx/page/order/speedily";
    }

    /**
     * 从预约单跳转到新建快修快保单
     * @param model
     * @param shopId
     * @param appointId
     */
    private void appointToSpeedily(Model model, Long shopId, Long appointId) {
        AppointDetailFacVo appointDetail = appointFacade.getAppointDetail(appointId, shopId);
        AppointVo appointVo = appointDetail.getAppointVo();
        if (appointVo != null) {
            OrderInfo orderInfo = wrapperModelByCarId(model, appointVo.getCustomerCarId());
            // 服务费用
            BigDecimal totalServiceAmount = appointVo.getTotalServiceAmount();
            if (totalServiceAmount == null) {
                totalServiceAmount = BigDecimal.ZERO;
            }
            orderInfo.setServiceAmount(totalServiceAmount);
            // 服务优惠
            orderInfo.setServiceDiscount(BigDecimal.ZERO);

            // 配件费用
            BigDecimal totalGoodsAmount = appointVo.getTotalGoodsAmount();
            if (totalGoodsAmount == null) {
                totalGoodsAmount = BigDecimal.ZERO;
            }
            orderInfo.setGoodsAmount(totalGoodsAmount);
            // 配件优惠
            orderInfo.setGoodsDiscount(BigDecimal.ZERO);

            // 工单总计
            orderInfo.setOrderAmount(totalServiceAmount.add(totalGoodsAmount));
            orderInfo.setAppointId(appointId);
            orderInfo.setDownPayment(appointVo.getDownPayment());
            model.addAttribute("orderInfo", orderInfo);
        }

        List<AppointServiceVo> appointServiceVoList = appointDetail.getAppointServiceVoList();
        if (!CollectionUtils.isEmpty(appointServiceVoList)) {
            // 封装 OrderServicesVo
            List<OrderServicesVo> orderServicesVoList = new ArrayList<>(appointServiceVoList.size());
            for (AppointServiceVo appointServiceVo : appointServiceVoList) {
                OrderServicesVo orderServicesVo = new OrderServicesVo();
                ShopServiceInfo shopServiceInfo = appointServiceVo.getShopServiceInfo();
                if (shopServiceInfo != null) {
                    orderServicesVo.setServiceId(shopServiceInfo.getId());
                    orderServicesVo.setServiceName(shopServiceInfo.getName());
                    orderServicesVo.setParentServiceId(appointServiceVo.getParentServiceId());
                    orderServicesVo.setServiceSn(shopServiceInfo.getServiceSn());
                    orderServicesVo.setServiceCatId(shopServiceInfo.getCategoryId());
                    orderServicesVo.setServiceCatName(appointServiceVo.getCategoryName());
                    orderServicesVo.setServiceHour(BigDecimal.ONE);
                    //价格和优惠从预约单取
                    orderServicesVo.setServicePrice(appointServiceVo.getServicePrice());
                    orderServicesVo.setServiceAmount(appointServiceVo.getServicePrice());
                    orderServicesVo.setDiscount(appointServiceVo.getDiscountAmount());

                    orderServicesVo.setType(1);
                    orderServicesVoList.add(orderServicesVo);
                }
            }
            model.addAttribute("basicOrderService", orderServicesVoList);
        }

        List<Goods> goodsList = appointDetail.getGoodsList();
        if (!CollectionUtils.isEmpty(goodsList)) {
            // 封装 OrderGoodsVo
            List<OrderGoodsVo> orderGoodsVoList = new ArrayList<>(goodsList.size());
            List<Long> goodsIdList = new ArrayList<>();
            for (Goods goods : goodsList) {
                goodsIdList.add(goods.getId());

                OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                orderGoodsVo.setGoodsFormat(goods.getFormat());
                orderGoodsVo.setGoodsId(goods.getId());
                orderGoodsVo.setGoodsSn(goods.getGoodsSn());
                if (goods.getGoodsType() != null) {
                    orderGoodsVo.setGoodsType(goods.getGoodsType().longValue());
                } else {
                    orderGoodsVo.setGoodsType(0l);
                }
                orderGoodsVo.setGoodsName(goods.getName());
                orderGoodsVo.setGoodsPrice(goods.getPrice());
                Long goodsNum = goods.getGoodsNum();
                BigDecimal goodsNumber = BigDecimal.ONE;
                if (goodsNum != null) {
                    goodsNumber = BigDecimal.valueOf(goodsNum);
                }
                orderGoodsVo.setGoodsNumber(goodsNumber);
                BigDecimal goodsAmount = goods.getGoodsAmount();
                if (goodsAmount == null) {
                    goodsAmount = goods.getPrice().multiply(goodsNumber);
                }
                orderGoodsVo.setGoodsAmount(goodsAmount);
                orderGoodsVo.setMeasureUnit(goods.getMeasureUnit());
                orderGoodsVo.setInventoryPrice(goods.getInventoryPrice());
                orderGoodsVo.setDiscount(BigDecimal.ZERO);
                orderGoodsVoList.add(orderGoodsVo);
            }

            // 批量查询goods
            List<Goods> goodses = goodsService.selectByIds(goodsIdList.toArray(new Long[goodsIdList.size()]));
            // list 转 map
            Map<Long, Goods> goodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(goodses)) {
                for (Goods goods : goodses) {
                    goodsMap.put(goods.getId(), goods);
                }
            }
            // 库存
            for (OrderGoodsVo orderGoodsVo : orderGoodsVoList) {
                Long goodsId = orderGoodsVo.getGoodsId();
                Goods goods = goodsMap.get(goodsId);
                if (goods != null) {
                    orderGoodsVo.setStock(goods.getStock());
                } else {
                    orderGoodsVo.setStock(BigDecimal.ZERO);
                }
            }

            model.addAttribute("orderGoodsList", orderGoodsVoList);
        }
    }

    /**
     * 根据车id包装Model
     * @param model
     * @param customerCarId
     */
    private OrderInfo wrapperModelByCarId(Model model, Long customerCarId) {
        OrderInfo orderInfo = new OrderInfo();
        CustomerCar customerCar = customerCarService.selectById(customerCarId);
        if (customerCar != null) {
            //客户车辆信息
            model.addAttribute("customerCar", customerCar);

            orderInfo.setVin(customerCar.getVin());
            orderInfo.setCustomerCarId(customerCarId);
            orderInfo.setCarLicense(customerCar.getLicense());
            orderInfo.setCarGearBox(customerCar.getCarGearBox());
            orderInfo.setCarGearBoxId(customerCar.getCarGearBoxId());
            orderInfo.setCarBrandId(customerCar.getCarBrandId());
            orderInfo.setCarBrand(customerCar.getCarBrand());
            orderInfo.setCarSeriesId(customerCar.getCarSeriesId());
            orderInfo.setCarSeries(customerCar.getCarSeries());
            orderInfo.setCarModelsId(customerCar.getCarModelId());
            orderInfo.setCarModels(customerCar.getCarModel());
            orderInfo.setImportInfo(customerCar.getImportInfo());
            orderInfo.setCarYearId(customerCar.getCarYearId());
            orderInfo.setCarYear(customerCar.getCarYear());
            orderInfo.setCarPowerId(customerCar.getCarPowerId());
            orderInfo.setCarPower(customerCar.getCarPower());
            orderInfo.setUpkeepMileage(customerCar.getUpkeepMileage());
            orderInfo.setMileage(customerCar.getMileage() == null? "0":customerCar.getMileage().toString());
            Customer customer = customerService.selectById(customerCar.getCustomerId());
            if (customer != null) {
                String contactName = customer.getContact();
                if(StringUtils.isBlank(contactName)){
                    contactName = customer.getCustomerName();
                }
                orderInfo.setContactName(contactName);
                String contactMobile = customer.getContactMobile();
                if(StringUtils.isBlank(contactMobile)){
                    contactMobile = customer.getMobile();
                }
                orderInfo.setContactMobile(contactMobile);
                model.addAttribute("customer", customer);
            }
            model.addAttribute("orderInfo", orderInfo);
        }
        return orderInfo;
    }

    /**
     * save speedily's order
     *
     * @param orderFormEntity
     * @return
     * @see 
     */
    @RequestMapping(value = "speedily/save", method = RequestMethod.POST)
    @ResponseBody
    public Result speedilySave(OrderFormEntityBo orderFormEntity,
                               HttpServletRequest request) {

        // TODO optimize by interceptor
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac(SiteUrls.SPEEDILYSAVE, userId, orderFormEntity);

        // order info
        OrderInfo orderInfo = orderFormEntity.getOrderInfo();
        // 设置工单类别 {'3':'快修快保单'}
        orderInfo.setOrderTag(OrderCategoryEnum.SPEEDILY.getCode());

        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        if (!StringUtil.checkCarLicense(carLicense)) {
            return Result.wrapErrorResult("", "车牌格式不正确！");
        }

        String contactMobile = orderInfo.getContactMobile();
        if (!StringUtils.isBlank(contactMobile)) {
            if (!StringUtil.isMobileNO(contactMobile)) {
                return Result.wrapErrorResult("", "联系人电话格式不正确！");
            }
        }

        // TODO 快修快保单的业务类型
        orderInfo.setOrderType(0l);

        // 包装车辆实体
        CustomerCar customerCar = wrapperOrderFacade.wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建快修快保工单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "创建快修快保工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());
        //校验预付定金是否有效
        BigDecimal downPayment = orderInfo.getDownPayment();
        if(downPayment != null && downPayment.compareTo(BigDecimal.ZERO) == 1){
            Long appointId = orderInfo.getAppointId();
            boolean checkDownPaymentIsValid = appointFacade.checkDownPaymentIsValid(appointId,downPayment);
            if(!checkDownPaymentIsValid){
                return Result.wrapErrorResult("", "预付定金有误，请检查预约单的预付定金是否正确");
            }
        }
        // 保存工单信息
        try {
            Result result = orderService.save(orderFormEntity, userInfo);
            Object orderId = (result != null) ? result.getData() : "";
            log.info("工单状态流转:{} 创建快修快保工单,工单号为:{} 操作人:{}", carLicense, orderId, userId);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("创建快修快保工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "创建快修快保工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("创建快修快保工单异常", e2);
            return Result.wrapErrorResult("", "创建快修快保工单失败!");
        }
    }


    /**
     * update speedily's order
     *
     * @param orderFormEntity
     * @return
     * @see 
     */
    @RequestMapping(value = "speedily/update", method = RequestMethod.POST)
    @ResponseBody
    public Result speedilyUpdate(OrderFormEntityBo orderFormEntity,
                                 HttpServletRequest request) {

        // TODO optimize by interceptor
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac(SiteUrls.SPEEDILYUPDATE, userId, orderFormEntity);

        // order info
        OrderInfo orderInfo = orderFormEntity.getOrderInfo();

        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        if (!StringUtil.checkCarLicense(carLicense)) {
            return Result.wrapErrorResult("", "车牌格式不正确！");
        }

        String contactMobile = orderInfo.getContactMobile();
        if (!StringUtils.isBlank(contactMobile)) {
            if (!StringUtil.isMobileNO(contactMobile)) {
                return Result.wrapErrorResult("", "联系人电话格式不正确！");
            }
        }

        // 开单日期
        String createTimeStr = orderInfo.getCreateTimeStr();
        if (StringUtils.isEmpty(createTimeStr)) {
            return Result.wrapErrorResult("", "开单日期不能为空！");
        }

        // 包装车辆实体
        CustomerCar customerCar = wrapperOrderFacade.wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("更新快修快保工单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "更新快修快保工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());

        // 客户单位为空==null 置“ ”处理
        String company = orderInfo.getCompany();
        orderInfo.setCompany((company == null) ? " " : company);

        // 保存工单信息
        try {
            Result result = orderService.update(orderFormEntity, userInfo, false);
            Object orderId = (result != null) ? result.getData() : "";
            log.info("工单状态流转:{} 更新快修快保工单,工单号为:{} 操作人:{}", carLicense, orderId, userId);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("更新快修快保工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "更新快修快保工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("更新快修快保工单异常", e2);
            return Result.wrapErrorResult("", "更新快修快保工单失败!");
        }
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
}
