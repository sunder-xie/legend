package com.tqmall.legend.web.order;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.athena.client.maintain.MaintainService;
import com.tqmall.common.Constants;
import com.tqmall.common.UpperNumbers;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitAndRedBillDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.LicenseMatchCityService;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.insurance.InsuranceCompanyService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.IVirtualOrderService;
import com.tqmall.legend.biz.order.IVirtualOrdergoodService;
import com.tqmall.legend.biz.order.IVirtualOrderserviceService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTypeService;
import com.tqmall.legend.biz.order.log.OrderOperationLog;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.setting.vo.ShopPrintConfigVO;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.ShopPrintConfigUtil;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.insurance.InsuranceCompany;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.precheck.PrecheckDetails;
import com.tqmall.legend.entity.precheck.PrecheckItemTypeEnum;
import com.tqmall.legend.entity.precheck.PrecheckValue;
import com.tqmall.legend.entity.precheck.Prechecks;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopConfigureVO;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.enums.order.OrderProxyTypeEnum;
import com.tqmall.legend.enums.setting.PrintTemplateEnum;
import com.tqmall.legend.facade.appoint.AppointFacade;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.facade.customer.CustomerCarFacade;
import com.tqmall.legend.facade.magic.ProxyFacade;
import com.tqmall.legend.facade.magic.vo.ProxyOrderDetailVo;
import com.tqmall.legend.facade.magic.vo.ProxyServicesVo;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.facade.order.OrderInvalidFacade;
import com.tqmall.legend.facade.order.OrderPrecheckDetailsFacade;
import com.tqmall.legend.facade.order.SellGoodsFacade;
import com.tqmall.legend.facade.order.WrapperOrderFacade;
import com.tqmall.legend.facade.print.PrintFacade;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.pojo.warehouseOut.WarehouseOutDetailVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.gson.BigDecimalTypeAdapter;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.channel.ChannelDTO;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.object.result.workshop.WorkOrderDTO;
import com.tqmall.magic.service.channel.RpcChannelService;
import com.tqmall.magic.service.proxy.RpcProxyService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * order controller
 */
@Slf4j
@Controller
@RequestMapping("shop/order")
public class OrderController extends BaseController {

    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private IVirtualOrderService virtualOrderService;
    @Autowired
    private IVirtualOrdergoodService virtualOrdergoodService;
    @Autowired
    private IVirtualOrderserviceService virtualOrderserviceService;
    @Autowired
    private InsuranceCompanyService insuranceCompanyService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private LicenseMatchCityService licenseMatchCityService;
    @Autowired
    private MaintainService maintainService;
    @Autowired
    private CarWashFacade carWashFacade;
    @Autowired
    private SellGoodsFacade sellGoodsFacade;
    @Autowired
    private WrapperOrderFacade wrapperOrderFacade;
    @Autowired
    private AppointFacade appointFacade;
    @Autowired
    private OrderInvalidFacade orderInvalidFacade;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;
    @Autowired
    private RpcChannelService rpcChannelService;
    @Autowired
    private ProxyFacade proxyFacade;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private CustomerCarFacade customerCarFacade;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private PrechecksService prechecksService;
    @Autowired
    private OrderPrecheckDetailsFacade orderPrecheckDetailsFacade;
    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private ShopPrintConfigService printConfigService;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private PrintFacade printFacade;

    /**
     * 查看工单详情
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String orderDetail(Model model, @RequestParam("orderId") Long orderId, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        List<ShopPrintConfig> shopPrintConfigs = printConfigService.getShopOpenConfig(request);
        if (!CollectionUtils.isEmpty(shopPrintConfigs)){
            model.addAttribute("openPrintConfig",shopPrintConfigs);
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        if (orderId == null || orderId < 0) {
            // 工单不存在提示信息
            log.error("工单不存在!");
            return "common/error";
        }

        // 根据工单类型跳转到对应的工单详情页
        Optional<OrderInfo> orderOptional = orderService.getOrder(orderId, shopId);
        // IF 校验跨门店 THEN 返回空页面
        if (!orderOptional.isPresent()) {
            return "yqx/page/order/order-list";
        }
        ShopConfigure shopConfigure = shopConfigureService.getPrintVersion(shopId);
        String version = shopConfigure.getConfValue();
        model.addAttribute("shopPrintVersion",version);

        OrderInfo orderInfo = orderOptional.get();
        Integer orderTag = orderInfo.getOrderTag();

        //设置debitBill流水
        Set<Long> relIds = new HashSet<>();
        relIds.add(orderId);
        com.tqmall.core.common.entity.Result<DebitAndRedBillDTO> billResult = null;
        try {
            billResult = rpcDebitBillService.findBillListByRelIds(shopId, DebitTypeEnum.ORDER.getId(), relIds, true);
            log.info("查询工单对应的收款单调用账单中心dubbo接口, 参数: shopId: {}, debitTypeId: {}, relIds: {}, hasRed: {},返回:{}", shopId, DebitTypeEnum.ORDER.getId(), LogUtils.objectToString(relIds), true, billResult != null ? billResult.isSuccess() : false);
        } catch (Exception e) {
            log.error("查询工单对应的收款单调用账单中心dubbo接口异常, {}", e);
            return "common/error";
        }
        if (billResult != null && billResult.isSuccess()) {
            DebitAndRedBillDTO resultData = billResult.getData();
            if (resultData != null) {
                List<DebitBillDTO> debitBillDTOList = resultData.getDebitBillDTOList();
                if (!CollectionUtils.isEmpty(debitBillDTOList)) {
                    model.addAttribute("debitBill", debitBillDTOList.get(0));
                }
                // 冲红单
                List<DebitBillDTO> redBillDTOList = resultData.getRedBillDTOList();
                if (!CollectionUtils.isEmpty(redBillDTOList)) {
                    model.addAttribute("redBill", redBillDTOList.get(0));
                }
            }
        }
        if (orderTag != null) {
            if (orderTag.intValue() == OrderCategoryEnum.CARWASH.getCode()) {
                // 洗车单详情

                Long customerCarId = orderInfo.getCustomerCarId();
                // 获取客户车辆信息
                Result<CustomerPerfectOfCarWashEntity> result = customerCarService.selectCustomerCar(shopId, customerCarId);
                if (!result.isSuccess()) {
                    return "common/error";
                }
                CustomerPerfectOfCarWashEntity customerPerfectEntity = result.getData();
                // wrapper完善洗车表单实体
                CustomerCompletionFormEntity formEntity = carWashFacade.wrapperPerfectCarWashFormEntity(orderInfo, customerPerfectEntity);
                formEntity.setPayStatus(orderInfo.getPayStatus());
                model.addAttribute("formEntity", formEntity);
                model.addAttribute("orderInfo", orderInfo);

                return "yqx/page/order/carwash-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.SPEEDILY.getCode() || orderTag.intValue() == OrderCategoryEnum.INSURANCE.getCode()) {
                // 快修快保单/引流活动详情

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, shopId);
                } catch (BusinessCheckedException e) {
                    log.error("快修快保页封装失败，原因:{}", e.toString());
                    return "common/error";
                }
                // exist virtual order
                model.addAttribute("virtualOrderId", virtualOrderService.isExistVirtualOrder(orderId));

                return "yqx/page/order/speedily-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.COMMON.getCode()) {
                // 综合维修单详情

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, shopId);
                } catch (BusinessCheckedException e) {
                    log.error("工单编辑失败，原因:{}", e.toString());
                    model.addAttribute("isexist", 0);
                    return "common/error";
                }

                // exist virtual order
                model.addAttribute("virtualOrderId", virtualOrderService.isExistVirtualOrder(orderId));
                /**
                 * 共享中心代码 start
                 */
                try {
                    //工单是委托状态才查询委托单
                    Integer proxyType = orderInfo.getProxyType();
                    if (OrderProxyTypeEnum.WT.getCode().equals(proxyType)) {
                        ProxyParam proxyParam = new ProxyParam();
                        proxyParam.setSource(Constants.CUST_SOURCE);
                        proxyParam.setOrderId(orderId);//委托单工单id
                        com.tqmall.core.common.entity.Result<List<ProxyDTO>> proxyResult = rpcProxyService.showProxyList(proxyParam);
                        if (proxyResult.isSuccess()) {
                            model.addAttribute("proxyList", proxyResult.getData());
                        }
                    } else if (OrderProxyTypeEnum.ST.getCode().equals(proxyType)) {
                        ProxyParam proxyParam = new ProxyParam();
                        proxyParam.setSource(Constants.CUST_SOURCE);
                        proxyParam.setProxyId(orderId);//受托单工单id
                        com.tqmall.core.common.entity.Result<List<ProxyDTO>> proxyResult = rpcProxyService.showProxyList(proxyParam);
                        if (proxyResult.isSuccess()) {
                            model.addAttribute("proxyedList", proxyResult.getData());
                        }

                    }
                } catch (Exception e) {
                    log.error("【dubbo】调用magic获取委托单列表，传参为orderId:{},shopId:{}", orderId, shopId);
                }
                //如果是体系的门店，则跳转至新详情页
                if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
                    return "yqx/page/order/magic-common-detail";
                }
                /**
                 * 共享中心代码 end
                 */
                return "yqx/page/order/common-detail";
            } else if (orderTag.intValue() == OrderCategoryEnum.SELLGOODS.getCode()) {
                // 销售单详情

                try {
                    wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, shopId);
                } catch (BusinessCheckedException e) {
                    log.error("销售单页封装失败，原因:{}", e.toString());
                    return "common/error";
                }

                return "yqx/page/order/sell-good-detail";
            } else {
                log.error("未知的工单类型!");
                return "common/error";
            }
        }

        return "yqx/page/order/order-list";
    }

    /**
     * TODO refactor code dirty
     * <p/>
     * to add page
     *
     * @param customerCarId 客户车辆ID 从车况登记，进入工单
     * @param copyOrderId   快速复制创建工单
     * @param appointId     预约单转工单
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "common-add" , method = RequestMethod.GET)
    public String add(@RequestParam(value = "cache", required = false) Integer cache, @RequestParam(value = "customerCarId", required = false) Long customerCarId, @RequestParam(value = "copyOrderId", required = false) Long copyOrderId, @RequestParam(value = "appointId", required = false) Long appointId, @RequestParam(value = "proxyId", required = false) Long proxyId, @RequestParam(value = "precheckId", required = false) Long precheckId, Model model, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());

        // TODO 拦截器处理
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        logTrac("common-add", userId, null);

        //工单信息
        OrderInfo orderInfo = new OrderInfo();
        //客户车辆
        CustomerCar customerCar = null;
        // 附加服务
        List<OrderServices> additionalServices = new ArrayList<OrderServices>();
        // 选购的物料
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        // 选购的基本服务
        List<OrderServices> basicOrderService = new ArrayList<OrderServices>();

        //  车况登记,进入工单
        if (customerCarId != null && customerCarId > 0) {

            Map carMap = new HashMap(2);
            carMap.put("id", customerCarId);
            carMap.put("shopId", shopId);
            List<CustomerCar> customerCarList = customerCarService.select(carMap);
            if (!CollectionUtils.isEmpty(customerCarList)) {
                customerCar = customerCarList.get(0);
                model.addAttribute("customerCar", customerCar);
                Customer customer = customerService.selectById(customerCar.getCustomerId());
                orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
            }
        }
        // 车牌头两位
        getLicenseModel(model, shopId);

        // case3: 快速复制创建工单
        if (copyOrderId != null && copyOrderId > 0l) {
            try {
                orderInfo = wrapperOrderFacade.wrapperModelOfEditedPage(copyOrderId, model, userInfo.getShopId());
                orderInfo.setId(null);
                orderInfo.setCreateTime(null);
                orderInfo.setProxyType(null);
                orderInfo.setDownPayment(null);
                model.addAttribute("orderInfo", orderInfo);
                model.addAttribute("copyOrderId", copyOrderId);
            } catch (BusinessCheckedException e) {
                log.error("综合维修单页面封装失败，原因:", e);
                model.addAttribute("isexist", 0);
            }
            if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
                //设置预检信息
                wrapperOrderFacade.wrapperModelOfEditedPageAboutPrecheck(copyOrderId, model);
                return "yqx/page/order/magic-common-add";
            }
            return "yqx/page/order/common-add";
        }

        // case4: 预约单转工单 包装数据
        else if (appointId != null && appointId > 0l) {

            AppointDetailFacVo appointDetail = appointFacade.getAppointDetail(appointId, shopId);
            AppointVo appointVo = appointDetail.getAppointVo();
            // 获取客户信息
            Long appointVoCustomerCarId = appointVo.getCustomerCarId();
            // 获取车辆信息
            Optional<CustomerCar> customerCarOptional = customerCarService.getCustomerCar(appointVoCustomerCarId);
            if (customerCarOptional.isPresent()) {
                customerCar = customerCarOptional.get();
                model.addAttribute("customerCar", customerCar);
                Customer customer = customerService.selectById(customerCar.getCustomerId());
                orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
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
            }

            // 包装基本服务列表
            List<AppointServiceVo> appointServiceVoList = appointDetail.getAppointServiceVoList();
            if (!CollectionUtils.isEmpty(appointServiceVoList)) {
                // 封装 OrderServicesVo
                OrderServices orderServices = null;
                for (AppointServiceVo appointServiceVo : appointServiceVoList) {
                    orderServices = new OrderServices();
                    ShopServiceInfo shopServiceInfo = appointServiceVo.getShopServiceInfo();
                    if (shopServiceInfo != null) {
                        orderServices.setServiceId(shopServiceInfo.getId());
                        orderServices.setServiceName(shopServiceInfo.getName());
                        orderServices.setParentServiceId(appointServiceVo.getParentServiceId());
                        orderServices.setServiceSn(shopServiceInfo.getServiceSn());
                        orderServices.setServiceCatId(shopServiceInfo.getCategoryId());
                        orderServices.setServiceCatName(appointServiceVo.getCategoryName());
                        // 工时:1
                        orderServices.setServiceHour(BigDecimal.ONE);
                        //价格和优惠从预约单取
                        orderServices.setServicePrice(appointServiceVo.getServicePrice());
                        orderServices.setDiscount(appointServiceVo.getDiscountAmount());
                        orderServices.setServiceAmount(appointServiceVo.getServicePrice());
                        orderServices.setType(1);
                        basicOrderService.add(orderServices);
                    }
                }
            }

            List<Goods> goodsList = appointDetail.getGoodsList();
            if (!CollectionUtils.isEmpty(goodsList)) {
                OrderGoods orderGoods = null;
                List<Long> goodsIdList = new ArrayList<>();
                for (Goods goods : goodsList) {
                    goodsIdList.add(goods.getId());
                    orderGoods = new OrderGoods();
                    orderGoods.setGoodsFormat(goods.getFormat());
                    orderGoods.setGoodsId(goods.getId());
                    orderGoods.setGoodsSn(goods.getGoodsSn());
                    if (goods.getGoodsType() != null) {
                        orderGoods.setGoodsType(goods.getGoodsType().longValue());
                    } else {
                        orderGoods.setGoodsType(0l);
                    }
                    orderGoods.setGoodsName(goods.getName());
                    // 数量:1
                    orderGoods.setGoodsName(goods.getName());
                    orderGoods.setGoodsPrice(goods.getPrice());
                    Long goodsNum = goods.getGoodsNum();
                    BigDecimal goodsNumber = BigDecimal.ONE;
                    if (goodsNum != null) {
                        goodsNumber = BigDecimal.valueOf(goodsNum);
                    }
                    orderGoods.setGoodsNumber(goodsNumber);
                    BigDecimal goodsAmount = goods.getGoodsAmount();
                    if (goodsAmount == null) {
                        goodsAmount = goods.getPrice().multiply(goodsNumber);
                    }
                    orderGoods.setGoodsAmount(goodsAmount);
                    orderGoods.setMeasureUnit(goods.getMeasureUnit());
                    orderGoods.setInventoryPrice(goods.getInventoryPrice());
                    orderGoods.setDiscount(BigDecimal.ZERO);
                    orderGoodsList.add(orderGoods);
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
                for (OrderGoods orderGoods1 : orderGoodsList) {
                    Long goodsId = orderGoods1.getGoodsId();
                    Goods goods = goodsMap.get(goodsId);
                    if (goods != null) {
                        orderGoods1.setStock(goods.getStock());
                    } else {
                        orderGoods1.setStock(BigDecimal.ZERO);
                    }
                }
            }
        } else if (proxyId != null) {
            //case5: 委托单转工单,受托方才能转工单
            Result<ProxyOrderDetailVo> proxyOrderDetailVoResult = proxyFacade.getProxyOrderDetail(shopId, proxyId);
            if (proxyOrderDetailVoResult.isSuccess()) {
                ProxyOrderDetailVo proxyOrderDetailVo = proxyOrderDetailVoResult.getData();
                String proxyStatus = proxyOrderDetailVo.getProxyStatus();
                //门店id是受托方id，且委托单是已接单状态,未开过工单，才能转工单
                if (shopId.equals(proxyOrderDetailVo.getProxyShopId()) && ProxyStatusEnum.YJD.getCode().equals(proxyStatus) && proxyOrderDetailVo.getProxyId().equals(0l)) {
                    //判断是否存在客户，没有则需新建
                    String carLicense = proxyOrderDetailVo.getCarLicense();
                    String contactName = proxyOrderDetailVo.getContactName();
                    String contactMobile = proxyOrderDetailVo.getContactMobile();
                    Long carId = proxyOrderDetailVo.getCustomerCarId();//委托方车辆id
                    //同步车辆
                    CustomerCar customerC = customerCarFacade.getProxyCustomerCar(shopId, carId, carLicense, contactName, contactMobile, userInfo);
                    model.addAttribute("customerCar", customerC);
                    Customer customer = customerService.selectById(customerC.getCustomerId());
                    orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerC, customer);
                    model.addAttribute("proxyOrderDetailVo", proxyOrderDetailVo);
                    model.addAttribute("proxyId", proxyId);
                    //钣喷二期：同步委托方工单对应的预检信息
                    orderPrecheckDetailsFacade.setOrderPrecheckDetailsModelByOrderId(model, proxyOrderDetailVo.getOrderId());
                    //钣喷三期：如果存在委托服务，则默认匹配钣喷中心的服务
                    List<ProxyServicesVo> proxyServicesVoList = proxyOrderDetailVo.getProxyServicesVoList();
                    if (!CollectionUtils.isEmpty(proxyServicesVoList)) {
                        for (ProxyServicesVo proxyServicesVo : proxyServicesVoList) {
                            OrderServices orderServices = shopServiceInfoFacade.getOrderServicesByProxyServices(shopId, proxyServicesVo);
                            if (orderServices != null) {
                                basicOrderService.add(orderServices);
                            }
                        }
                    }
                }
            }
        } else if (precheckId != null) {
            //case5: 预检单外观检查信息带入工单
            //获取预检单信息
            Prechecks prechecks = prechecksService.selectById(precheckId);
            if (prechecks != null) {
                model.addAttribute("prechecks", prechecks);
                //获取预检单外观检查详情
                Map<String, Object> params = new HashMap<>();
                params.put("precheckId", precheckId);
                params.put("precheckItemType", PrecheckItemTypeEnum.OUTLINECODE.getIndex());
                params.put("shopId", shopId);
                List<PrecheckDetails> precheckDetailsList = prechecksService.getListedPrecheckDetails(params);
                for (PrecheckDetails tmp : precheckDetailsList) {
                    tmp.setFtlId(com.tqmall.legend.common.Constants.ftlItemMapping.get(tmp.getPrecheckItemId()));
                }
                model.addAttribute("precheckDetailsList", precheckDetailsList);
                model.addAttribute("precheckId", precheckId);
            }
        }

        // 门店服务
        if (copyOrderId == null) {
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.queryShopServiceList(shopId, ShopServiceTypeEnum.OTHER, Constants.GLF);
            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                additionalServices.add(wrapperOrderServiceInfo(shopServiceInfoList.get(0)));
            }
        }
        //新增页面将orderInfo的id 设置为空
        orderInfo.setId(null);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("basicOrderService", basicOrderService);
        model.addAttribute("additionalServices", additionalServices);
        model.addAttribute("orderGoodsList", orderGoodsList);
        if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
            //设置预检项目信息
            List<PrecheckValue> precheckValueList = prechecksService.getItemValuesByType(PrecheckItemTypeEnum.OUTLINECODE.getIndex());
            model.addAttribute("outlineValues", precheckValueList);
            return "yqx/page/order/magic-common-add";
        }
        return "yqx/page/order/common-add";
    }

    /**
     * 按门店所在城市设置门店车牌号前缀
     *
     * @param model
     * @param shopId
     */
    private void getLicenseModel(Model model, Long shopId) {
        String license = licenseMatchCityService.getLicenseCityByShopId(shopId);
        model.addAttribute("license", license);
    }


    /**
     * 包装orderServices订单服务实体
     *
     * @param shopServiceInfo
     * @return OrderServices
     */
    private OrderServices wrapperOrderServiceInfo(ShopServiceInfo shopServiceInfo) {
        OrderServices fee = new OrderServices();
        fee.setServicePrice(BigDecimal.ZERO);
        fee.setServiceHour(BigDecimal.ONE);
        fee.setServiceId(shopServiceInfo.getId());
        fee.setServiceName(shopServiceInfo.getName());
        fee.setServiceSn(shopServiceInfo.getServiceSn());
        fee.setServiceAmount(shopServiceInfo.getServiceAmount());
        fee.setType(2);
        fee.setFlags(shopServiceInfo.getFlags());
        fee.setDiscount(BigDecimal.ZERO);
        fee.setManageRate(shopServiceInfo.getServicePrice());
        return fee;
    }


    /**
     * save orderInfo
     *
     * @param orderFormEntityBo
     * @return
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "common-save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(OrderFormEntityBo orderFormEntityBo) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac("common-save", userId, orderFormEntityBo);

        // order info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        if (orderInfo == null) {
            return Result.wrapErrorResult("", "创建工单失败!<br/>工单不存在");
        }
        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        // 服务顾问
        Long receiver = orderInfo.getReceiver();
        if (receiver == null || receiver <= 0) {
            return Result.wrapErrorResult("", "服务顾问不能为空！");
        }

        CustomerCar customerCar = wrapperOrderFacade.wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建工单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "创建工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());
        //校验预付定金是否有效
        BigDecimal downPayment = orderInfo.getDownPayment();
        if (downPayment != null && downPayment.compareTo(BigDecimal.ZERO) == 1) {
            Long appointId = orderInfo.getAppointId();
            boolean checkDownPaymentIsValid = appointFacade.checkDownPaymentIsValid(appointId, downPayment);
            if (!checkDownPaymentIsValid) {
                return Result.wrapErrorResult("", "预付定金有误，请检查预约单的预付定金是否正确");
            }
        }
        try {
            Result result = orderService.save(orderFormEntityBo, userInfo);
            Object orderId = (result != null) ? result.getData() : "";
            log.info("工单状态流转:{} 创建工单,工单号为:{} 操作人:{}", carLicense, orderId, userId);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("创建工单失败,原因:{}", e1.toString());
            // TODO 物料不存在时：页面给错误的物料行变背景色
            return Result.wrapErrorResult("", "创建工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("创建工单异常", e2);
            return Result.wrapErrorResult("", "创建工单失败!");
        }
    }


    /**
     * to edit page
     *
     * @param orderId 工单编号
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "common-edit", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "orderId") Long orderId, Model model, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        // log trac
        logTrac("common-edit", userId, null);

        // IF 校验跨门店 THEN 返回空页面
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
                //设置预检信息
                wrapperOrderFacade.wrapperModelOfEditedPageAboutPrecheck(orderId, model);
                return "yqx/page/order/magic-common-add";
            }
            return "yqx/page/order/common-add";
        }

        // wrapper model
        try {
            wrapperOrderFacade.wrapperModelOfEditedPage(orderId, model, userInfo.getShopId());
        } catch (BusinessCheckedException e) {
            log.error("工单编辑失败，原因:{}", e.toString());
            model.addAttribute("isexist", 0);
            if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
                return "yqx/page/order/magic-common-add";
            }
            return "yqx/page/order/common-add";
        }

        // exist virtual order
        model.addAttribute("virtualOrderId", virtualOrderService.isExistVirtualOrder(orderId));

        if (shopFunFacade.isUseNewPrecheck(request, shopId)) {
            //如果是受托工单，则查询委托单信息
            if (!wrapperModelOfProxy(orderId, model, userInfo, shopId, orderInfoOptional)) {
                return "common/error";
            }
            //设置预检信息
            wrapperOrderFacade.wrapperModelOfEditedPageAboutPrecheck(orderId, model);
            return "yqx/page/order/magic-common-add";
        }
        return "yqx/page/order/common-add";
    }

    private boolean wrapperModelOfProxy(@RequestParam(value = "orderId") Long orderId, Model model, UserInfo userInfo, Long shopId, Optional<OrderInfo> orderInfoOptional) {
        OrderInfo orderInfo = orderInfoOptional.get();
        if (OrderProxyTypeEnum.ST.getCode().equals(orderInfo.getProxyType())) {
            log.info("【受托工单编辑】：获取委托单信息，门店id：{}，工单id：{}", shopId, orderId);
            com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = null;
            try {
                proxyDTOResult = rpcProxyService.getProxyInfoByShopIdAndProxyOrderId(shopId, orderId);
            } catch (Exception e) {
                log.error("【受托工单编辑】：获取委托单信息，出现异常", e);
                return false;
            }
            log.info("【受托工单编辑】：获取委托单信息，返回值：{}", JSONUtil.object2Json(proxyDTOResult));
            if (proxyDTOResult == null || !proxyDTOResult.isSuccess()) {
                return false;
            }
            ProxyDTO proxyDTO = proxyDTOResult.getData();
            if (proxyDTO == null) {
                return false;
            }
            Long proxyId = proxyDTO.getId();
            log.info("【受托工单编辑】：获取委托单服务等信息，受托方门店id：{}，委托单id：{}", shopId, proxyId);
            Result<ProxyOrderDetailVo> proxyOrderDetailVoResult = null;
            try {
                proxyOrderDetailVoResult = proxyFacade.getProxyOrderDetail(shopId, proxyId);
            } catch (Exception e) {
                log.info("【受托工单编辑】：获取委托单服务等信息，出现异常", e);
                return false;
            }
            if (proxyOrderDetailVoResult == null || !proxyOrderDetailVoResult.isSuccess()) {
                return false;
            }
            ProxyOrderDetailVo proxyOrderDetailVo = proxyOrderDetailVoResult.getData();
            model.addAttribute("proxyOrderDetailVo", proxyOrderDetailVo);
        }
        return true;
    }


    /**
     * update orderInfo
     *
     * @param orderFormEntityBo
     * @return
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "common-update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(OrderFormEntityBo orderFormEntityBo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // LOG TRAC
        logTrac("common-update", userId, orderFormEntityBo);

        // 工单信息
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        if (orderInfo == null) {
            return Result.wrapErrorResult("", "编辑工单失败!<br/>工单不存在");
        }

        Long orderId = orderInfo.getId();
        // 无效工单
        if (orderId == null) {
            return Result.wrapErrorResult("", "工单不存在！");
        }

        // 无效车牌号
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空");
        }
        // 服务顾问为空
        Long receiver = orderInfo.getReceiver();
        if (receiver == null || receiver <= 0) {
            return Result.wrapErrorResult("", "服务顾问不能为空");
        }

        // 校验工单状态
        String orderStatus = orderInfo.getOrderStatus();
        OrderStatusEnum statusEnumOptional = null;
        String operateVal = "";
        if (StringUtils.isNotEmpty(orderStatus)) {
            statusEnumOptional = OrderStatusEnum.getOrderStatusEnum(orderStatus);
            if (statusEnumOptional == null) {
                return Result.wrapErrorResult("", "非法的工单状态");
            }
            operateVal = statusEnumOptional.getValue();
        }
        // 包装车辆实体
        CustomerCar customerCar = wrapperOrderFacade.wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("更新工单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "更新工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        // 设置车主电话和车主姓名
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        try {
            boolean isUseNewPrecheck = shopFunFacade.isUseNewPrecheck(request, userInfo.getShopId());
            orderService.update(orderFormEntityBo, userInfo, isUseNewPrecheck);

            // 状态流水
            log.info("工单状态流转:{} {} 工单号为:{} 操作人:{}", carLicense, operateVal, orderId, userId);

            return Result.wrapSuccessfulResult(orderId);
        } catch (BusinessCheckedException e1) {
            log.error("编辑工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "编辑工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("编辑工单异常", e2);
            return Result.wrapErrorResult("", "编辑工单失败!");
        }
    }


    /**
     * calculate order expense
     * <p/>
     * 访问比较频繁
     * TODO refacor
     *
     * @param orderGoodsJson    工单.物料JSON
     * @param orderServicesJson 工单.服务JSON
     * @return
     */
    @RequestMapping(value = "calc_price", method = RequestMethod.POST)
    @ResponseBody
    public Result calculateOrderExpense(@RequestParam("orderGoods") String orderGoodsJson, @RequestParam("orderServices") String orderServicesJson) {
        // 转换JSON工具
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(BigDecimal.class, new BigDecimalTypeAdapter());
        Gson gson = builder.create();

        // 物料列表
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        if (!StringUtils.isBlank(orderGoodsJson)) {
            try {
                orderGoodsList = gson.fromJson(orderGoodsJson, new TypeToken<List<OrderGoods>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                log.error("计算工单费用异常！物料JSON转对象失败：{}", orderGoodsJson);
                return Result.wrapErrorResult("", "计算工单费用失败！");
            }
        }

        // 服务列表
        List<OrderServices> orderServicesList = new ArrayList<OrderServices>();
        if (!StringUtils.isBlank(orderServicesJson)) {
            try {
                orderServicesList = gson.fromJson(orderServicesJson, new TypeToken<List<OrderServices>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                log.error("计算工单费用异常！服务JSON转对象失败：{}", orderServicesJson);
                return Result.wrapErrorResult("", "计算工单费用失败！");
            }
        }

        // 计算工单费用
        try {
            OrderExpenseEntity orderExpense = orderService.calculateOrderExpense(orderGoodsList, orderServicesList);
            return Result.wrapSuccessfulResult(orderExpense);
        } catch (Exception e) {
            log.error("计算工单费用异常:{}", e);
            return Result.wrapErrorResult("", "计算工单费用失败！");
        }
    }


    /**
     * 工单打印 简化版
     *
     * @param model
     * @param orderId 工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "simple-order-print", method = RequestMethod.GET)
    public String orderSimplePrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, HttpServletRequest request) {
        boolean bool = wrapperModelOfOrderPrint(model, orderId);
        if (bool) {
            String printLog = getPrintLog(request, "简化版派工单打印");
            log.info(printLog);
            return "yqx/page/order/simple-order-print";
        }
        return "redirect:/shop/order/order-list";
    }

    /**
     * 获取打印日志
     *
     * @param request
     * @return
     */
    private String getPrintLog(HttpServletRequest request, String header) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        String printLog = "";
        if (shop != null) {
            printLog = OrderOperationLog.getOrderPrintLog(header, shop);
        }
        return printLog;
    }

    /**
     * 工单打印
     *
     * @param model
     * @param orderId 工单ID
     * @param request
     * @return
     */
    @RequestMapping(value = "order-print", method = RequestMethod.GET)
    public String orderPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, HttpServletRequest request) {
        Boolean bool = wrapperModelOfOrderPrint(model, orderId);
        if (bool) {
            String printLog = getPrintLog(request, "派工单打印");
            log.info(printLog);
            return "yqx/page/order/order-print";
        } else {
            //工单不存在跳转至列表页
            return "redirect:/shop/order/order-list";
        }
    }

    /**
     *  新版打印
     * @param model
     * @param orderId
     * @return
     */
    @RequestMapping("shop-order-print")
    public String shopPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId) {
        Long startTime = System.currentTimeMillis();
        Long shopId = UserUtils.getShopIdForSession(request);
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.ORDER.getCode(),request);
        if (null == printConfig){
            //没有可供打印的单据
            return "redirect:/shop/order/order-list";
        }
        ShopPrintConfigVO printConfigVO = new ShopPrintConfigVO();
        try {
            BeanUtils.copyProperties(printConfigVO,printConfig);
        } catch (Exception e){
            log.error("[打印转换vo错误]",e);
        }
        Boolean bool = wrapperShopOrderPrint(printConfigVO, model, orderId);

        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        if (bool) {
            String printLog = getPrintLog(request,"派工单打印");
            log.info(printLog);
            log.info("[新版工单打印耗时:]{}",System.currentTimeMillis() - startTime);
            return "yqx/page/order/print/order-print";
        } else {
            //工单不存在跳转至列表页
            return "redirect:/shop/order/order-list";
        }
    }

    /**
     * 综合维修工单打印-待外观检查
     *
     * @param model
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "common-order-print", method = RequestMethod.GET)
    public String commonOrderPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, @RequestParam(value = "type") Integer type) {
        Boolean bool = wrapperModelOfOrderPrint(model, orderId);
        if (bool) {
            //获取施工单信息
            Long shopId = UserUtils.getShopIdForSession(request);
            try {
                log.error("【dubbo:根据工单查询施工单信息】orderId:{},shopId:{}", orderId, shopId);
                com.tqmall.core.common.entity.Result<WorkOrderDTO> workOrderDTOResult = rpcWorkOrderService.getWorkOrderInfoByOrderIdAndShopId(shopId, orderId);
                if (workOrderDTOResult.isSuccess()) {
                    WorkOrderDTO workOrderDTO = workOrderDTOResult.getData();
                    if (workOrderDTO != null) {
                        model.addAttribute("workOrder", workOrderDTO);
                    }
                }
            } catch (Exception e) {
                log.error("【dubbo:根据工单查询施工单信息】出现异常", e);
            }
            // 如果type为1，则打印简化版工单，否则打印正常版全量工单
            if (null != type && type == 1) {
                String printLog = getPrintLog(request, "简化版综合维修单打印");
                log.info(printLog);
                return "yqx/page/order/simple-common-order-print";
            } else {
                String printLog = getPrintLog(request, "综合维修单打印");
                log.info(printLog);
                //设置以及信息
                orderPrecheckDetailsFacade.setOrderPrecheckDetailsModelByOrderId(model, orderId);
                return "yqx/page/order/common-order-print";
            }
        } else {
            //工单不存在跳转至列表页
            return "redirect:/shop/order/order-list";
        }
    }

    /**
     * 判断工单.物料是否全部出库
     *
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "isGoodsAllOut", method = RequestMethod.GET)
    @ResponseBody
    public Result isGoodsAllOut(Long orderId) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        // 当前门店ID
        Long shopId = userInfo.getShopId();
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        boolean flag = true;
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                BigDecimal outNumber = warehouseOutService.countOutGoods(orderGoods.getGoodsId(), shopId, orderId, orderGoods.getId());
                if (outNumber.intValue() < orderGoods.getGoodsNumber().intValue()) {
                    flag = false;
                    break;
                }
            }
        }

        if (!flag) {
            return Result.wrapErrorResult("", "该工单存在物料未全部出库，请联系仓库确认,操作失败！");
        }

        return Result.wrapSuccessfulResult(true);
    }


    /**
     * getRealOrderGoods
     *
     * @param orderId     工单ID
     * @param checkFinish
     * @return
     */
    @RequestMapping(value = "getRealOrderGoodsListByOrderId", method = RequestMethod.GET)
    @ResponseBody
    public Result getRealOrderGoodsListByOrderId(Long orderId, @RequestParam(value = "checkFinish", required = false) boolean checkFinish) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        // 门店类别
        Integer level = userInfo.getLevel();
        // 当前门店
        Long shopId = userInfo.getShopId();

        //直营店
        if (checkFinish && level == Constants.SHOP_DIRECT_LEVEL) {
            List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
            // 判断这个订单有没有指派维修工
            if (!CollectionUtils.isEmpty(orderServicesList)) {
                for (OrderServices orderServices : orderServicesList) {
                    String workerIds = orderServices.getWorkerIds();
                    if (StringUtils.isBlank(workerIds) || "0".equals(workerIds)) {
                        return Result.wrapErrorResult("", "存在服务项目未指派维修工，完工失败");
                    }
                }
            }
        }

        RealWarehouseGoodsVo realWarehouseGoodsVo = new RealWarehouseGoodsVo();

        BigDecimal totalGoodsAmount = BigDecimal.ZERO;
        BigDecimal totalWarehouseOutAmount = BigDecimal.ZERO;
        BigDecimal totalWarehouseInventoryAmount = BigDecimal.ZERO;

        // 实开物料
        List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
        int right = 0;
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                WarehouseOutDetailVO warehouseOutDetailVO = warehouseOutService.getWarehouseDetailForGoods(orderGoods.getGoodsId(), userInfo.getShopId(), orderId, orderGoods.getId());
                if (warehouseOutDetailVO != null) {
                    orderGoods.setWarehouseOutPrice(warehouseOutDetailVO.getSalePrice());
                    orderGoods.setOutNumber(warehouseOutDetailVO.getGoodsCount());
                    orderGoods.setWarehouseOutAmount(warehouseOutDetailVO.getSaleAmount());
                    orderGoods.setWarehouseInventoryAmount(warehouseOutDetailVO.getInventoryAmount());
                    totalWarehouseOutAmount = totalWarehouseOutAmount.add(orderGoods.getWarehouseOutAmount());
                    totalWarehouseInventoryAmount = totalWarehouseInventoryAmount.add(orderGoods.getWarehouseInventoryAmount());
                    if (orderGoods.getOutNumber().compareTo(orderGoods.getGoodsNumber()) == 0) {
                        right++;
                    }
                }
                totalGoodsAmount = totalGoodsAmount.add(orderGoods.getGoodsAmount());
            }
            realWarehouseGoodsVo.setOrderGoodsList(orderGoodsList);
            if (right < orderGoodsList.size()) {
                realWarehouseGoodsVo.setCode(false);
            }
        } else {
            realWarehouseGoodsVo.setCode(true);
        }

        realWarehouseGoodsVo.setTotalGoodsAmount(totalGoodsAmount);
        realWarehouseGoodsVo.setTotalWarehouseOutAmount(totalWarehouseOutAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        realWarehouseGoodsVo.setTotalWarehouseInventoryAmount(totalWarehouseInventoryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        if (totalGoodsAmount.compareTo(totalWarehouseOutAmount) != 0) {
            realWarehouseGoodsVo.setCode(false);
        }
        return Result.wrapSuccessfulResult(realWarehouseGoodsVo);
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
     * 新版试车单.打印
     *
     * @param model
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "trialrun-print", method = RequestMethod.GET)
    public String tryCarPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, Integer printTemplate) {
        Long startTime = System.currentTimeMillis();
        //获取打印设置
        ShopPrintConfig printConfig = printConfigService.checkOpenStatus(PrintTemplateEnum.TRAILRUN.getCode(),request);
        if (null == printConfig){
            //没有可供打印的单据
            return "redirect:/shop/order/order-list";
        }
        long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop", shop);

        //编辑工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("currentDate", DateUtil.convertDateToYMD(new Date()));
            model.addAttribute("fontStyle",printConfig.getFontStyle());
            log.info("门店ID:" + shopId + ",打印试车单,单号:" + orderInfo.getOrderSn());
            String printLog = getPrintLog(request, "试车单打印");
            log.info(printLog);

            //打印自定义设置
            printFacade.printSelfConfig(model, shopId);

            log.info("[新版试车单打印耗时:]{}",System.currentTimeMillis() - startTime);
            return "yqx/page/order/print/car-test";
        } else {
            return "redirect:/shop/order/order-list";
        }
    }



    /**
     * save  virtual orderInfo
     *
     * @param orderFormEntityBo
     * @return
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "virtual/save", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualSave(OrderFormEntityBo orderFormEntityBo) {

        // TODO optimize by interceptor
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo == null) {
            return Result.wrapErrorResult("", "用户登录失效，请登录后再尝试");
        }
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac("virtual/save", userId, orderFormEntityBo);

        // order info
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        if (orderInfo == null) {
            return Result.wrapErrorResult("", "创建工单失败!<br/>工单不存在");
        }
        // 行驶里程
        String mileage = orderInfo.getMileage();
        if (mileage == null) {
            return Result.wrapErrorResult("", "行驶里程不能为空！");
        }
        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空！");
        }
        // 服务顾问
        Long receiver = orderInfo.getReceiver();
        if (receiver == null || receiver <= 0) {
            return Result.wrapErrorResult("", "服务顾问不能为空！");
        }

        try {
            // TODO 保存虚拟工单实体
            return orderService.virtualSave(orderFormEntityBo, userInfo);
        } catch (BusinessCheckedException e1) {
            log.error("创建子单失败,原因:{}", e1.toString());
            // TODO 物料不存在时：页面给错误的物料行变背景色
            return Result.wrapErrorResult("", "创建子单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("创建子单异常", e2);
            return Result.wrapErrorResult("", "创建子单失败!");
        }
    }


    /**
     * to virtualOrder's edit page
     *
     * @param parentId 工单编号
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "virtualorder-edit", method = RequestMethod.GET)
    public String virtualEdit(@RequestParam(value = "parentId") Long parentId, Model model, HttpServletRequest request) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        // log trac
        logTrac("virtualorder-edit", userId, null);
        try {
            Long virtualOrderId = virtualOrderService.isExistVirtualOrder(parentId);
            if (virtualOrderId == null) {
                wrapperOrderFacade.wrapperModelOfEditedPage(parentId, model, userInfo.getShopId());
            } else {
                model.addAttribute("virtualOrderId", virtualOrderId);
                wrapperModelOfVirtualOrderEditedPage(virtualOrderId, model, userInfo.getShopId());
            }
        } catch (BusinessCheckedException e) {
            log.error("虚拟工单编辑失败，原因:{}", e.toString());
            model.addAttribute("isexist", 0);
            return "yqx/page/order/virtualorder-edit";
        }
        return "yqx/page/order/virtualorder-edit";
    }


    /**
     * update virtualOrder
     *
     * @param orderFormEntityBo
     * @return
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "virtual/update", method = RequestMethod.POST)
    @ResponseBody
    public String virtualUpdate(OrderFormEntityBo orderFormEntityBo) {

        Gson gson = new Gson();
        Result errorResult = null;

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // LOG TRAC
        logTrac("virtual/update", userId, orderFormEntityBo);

        // 工单信息
        OrderInfo orderInfo = orderFormEntityBo.getOrderInfo();
        if (orderInfo == null) {
            errorResult = Result.wrapErrorResult("", "编辑工单失败!<br/>工单不存在");
            return gson.toJson(errorResult);
        }

        Long orderId = orderInfo.getId();
        // 无效工单
        if (orderId == null) {
            errorResult = Result.wrapErrorResult("", "工单不存在！");
            return gson.toJson(errorResult);
        }

        // 无效车牌号
        if (StringUtils.trimToEmpty(orderInfo.getCarLicense()).equals("")) {
            errorResult = Result.wrapErrorResult("", "车牌不能为空");
            return gson.toJson(errorResult);
        }

        // 服务顾问为空
        Long receiver = orderInfo.getReceiver();
        if (receiver == null || receiver <= 0) {
            errorResult = Result.wrapErrorResult("", "服务顾问不能为空");
            return gson.toJson(errorResult);
        }

        // 校验工单状态
        String orderStatus = orderInfo.getOrderStatus();
        if (StringUtils.isNotEmpty(orderStatus)) {
            OrderStatusEnum statusEnumOptional = OrderStatusEnum.getOrderStatusEnum(orderStatus);
            if (statusEnumOptional == null) {
                errorResult = Result.wrapErrorResult("", "非法的工单状态");
                return gson.toJson(errorResult);
            }
        }

        try {
            //  TODO 保存虚拟工单
            Result result = orderService.virtualUpdate(orderFormEntityBo, userInfo);
            return gson.toJson(result);
        } catch (BusinessCheckedException e1) {
            log.error("编辑工单失败,原因:{}", e1.toString());
            // TODO 物料不存在时：页面给错误的物料行变背景色
            errorResult = Result.wrapErrorResult("", "编辑工单失败!<br/>" + e1.getMessage());
            return gson.toJson(errorResult);
        } catch (RuntimeException e2) {
            log.error("编辑工单异常", e2);
            errorResult = Result.wrapErrorResult("", "编辑工单失败!");
            return gson.toJson(errorResult);
        }
    }


    /**
     * wrapper model of VirtualOrder edited page
     *
     * @param orderId
     * @param model
     * @param shopId
     */

    private void wrapperModelOfVirtualOrderEditedPage(Long orderId, Model model, Long shopId) throws BusinessCheckedException {
        Optional<VirtualOrder> virtualOrderOptional = virtualOrderService.getOrderById(orderId);
        if (!virtualOrderOptional.isPresent()) {
            throw new BusinessCheckedException("", "虚拟工单不存在,编号：" + orderId);
        }

        VirtualOrder virtualOrder = virtualOrderOptional.get();
        //设置业务类型
        Optional<OrderType> orderTypeObj = orderTypeService.getOrderType(virtualOrder.getOrderType());
        if (orderTypeObj.isPresent()) {
            virtualOrder.setOrderTypeName(orderTypeObj.get().getName());
        }
        // 工单信息
        model.addAttribute("orderInfo", virtualOrder);

        // 实开物料
        model.addAttribute("orderGoodsList", virtualOrdergoodService.queryOrderGoods(orderId, shopId, 0L));

        // 虚开物料
        model.addAttribute("otherOrderGoods", virtualOrdergoodService.queryOrderGoods(orderId, shopId, 1l));

        // 工单基本服务
        model.addAttribute("basicOrderService", virtualOrderserviceService.queryOrderServices(orderId, 1, shopId));


        // 工单附属服务
        List<VirtualOrderservice> orderServicesList2 = virtualOrderserviceService.queryOrderServices(orderId, 2, shopId);

        // 管理费
        if (!CollectionUtils.isEmpty(orderServicesList2)) {
            for (VirtualOrderservice orderServices : orderServicesList2) {
                if (orderServices.getFlags() != null && orderServices.getFlags().contains(Constants.GLF)) {
                    List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.queryShopServiceList(shopId, ShopServiceTypeEnum.OTHER, Constants.GLF);
                    if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                        ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                        BigDecimal price = shopServiceInfo.getServicePrice();
                        orderServices.setManageRate(price);
                    }
                }
            }
        }
        model.addAttribute("additionalServices", orderServicesList2);

    }


    /**
     * delete virtual's order
     *
     * @param orderId
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "virtual/delete", method = RequestMethod.GET)
    @ResponseBody
    public String virtualDelete(@RequestParam(value = "orderId") Long orderId, Model model, HttpServletRequest request) {

        model.addAttribute("moduleUrl", "order");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        // log trac
        logTrac("virtual/delete", userId, null);

        Gson gson = new Gson();
        Result result = null;

        try {
            orderService.virtualDelete(orderId, userInfo);
            result = Result.wrapSuccessfulResult(orderId);
            return gson.toJson(result);
        } catch (BusinessCheckedException e1) {
            log.error("删除虚拟子工单失败,原因:{}", e1.toString());
            result = Result.wrapErrorResult("", "删除虚拟子失败!<br/>" + e1.getMessage());
            return gson.toJson(result);
        } catch (RuntimeException e2) {
            log.error("删除虚拟子工单异常", e2);
            result = Result.wrapErrorResult("", "删除虚拟子工单失败!");
            return gson.toJson(result);
        }
    }


    /**
     * 子工单打印
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "virtualorder-print", method = RequestMethod.GET)
    public String virtualPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        // 工单信息
        Optional<VirtualOrder> orderInfoOptional = virtualOrderService.getOrderById(orderId);

        // 结算提示信息
        if (shopId != null && shopId > 0) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);

            // 获取门店配置
            Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.SETTLEPRINT, shopId);
            String shopConfigureContent = "";
            if (shopConfigureOptional.isPresent()) {
                ShopConfigure shopConfigure = shopConfigureOptional.get();
                shopConfigureContent = shopConfigure.getConfValue();
            }
            ShopConfigureVO configureVO = new ShopConfigureVO();
            if (StringUtils.isBlank(shopConfigureContent)) {
                configureVO.setSettleComment("1、本公司的检验不承担用户提供配件的维修和已建议的维修故障而拒修的质量责任；<br />" + "2、本维修工单内价格供参考，服务费用以实际结算为准。");
            } else {
                configureVO.setSettleComment(shopConfigureContent);
            }

            model.addAttribute("conf", configureVO);
        }


        if (orderInfoOptional.isPresent()) {
            VirtualOrder virtualOrder = orderInfoOptional.get();
            Long insuranceCompanyId = virtualOrder.getInsuranceCompanyId();
            List<InsuranceCompany> insuranceCompanyList = insuranceCompanyService.select(null);
            for (InsuranceCompany insuranceCompany : insuranceCompanyList) {
                if (insuranceCompany.getId().equals(insuranceCompanyId)) {
                    virtualOrder.setInsuranceCompanyName(insuranceCompany.getName());
                }
            }

            //工单物料
            List<VirtualOrdergood> VirtualOrderGoodList = virtualOrdergoodService.queryOrderGoods(virtualOrder.getId(), shopId);
            OrderType type = orderTypeService.selectById(virtualOrder.getOrderType());
            if (null != type && !StringUtils.isBlank(type.getName())) {
                virtualOrder.setOrderTypeName(type.getName());
            }
            model.addAttribute("preOrderAmount", 0);
            model.addAttribute("preOrderAmountUpper", 0);
            model.addAttribute("totalAmountUpper", 0);
            model.addAttribute("orderAmountUpper", UpperNumbers.toChinese(virtualOrder.getOrderAmount() + ""));
            //工单信息
            model.addAttribute("orderInfo", virtualOrder);

            //客户应付工时费合计orderInfo.serviceAmount-orderInfo.serviceDiscount
            model.addAttribute("serviceAmount", virtualOrder.getServiceAmount().subtract(virtualOrder.getServiceDiscount()));
            //客户应付配件费合计orderInfo.goodsAmount-orderInfo.goodsDiscount
            model.addAttribute("goodesAmount", virtualOrder.getGoodsAmount().subtract(virtualOrder.getGoodsDiscount()));
            //客户应付附加费合计
            model.addAttribute("feeAmount", virtualOrder.getFeeAmount().subtract(virtualOrder.getFeeDiscount()));

            Map map = new HashMap(2);
            map.put("shopId", shopId);
            map.put("id", virtualOrder.getCustomerCarId());
            List<CustomerCar> customerCarList = customerCarService.select(map);
            if (!CollectionUtils.isEmpty(customerCarList)) {
                CustomerCar customerCar = customerCarList.get(0);
                Customer customer = customerService.selectById(customerCar.getCustomerId());
                if (customer != null) {
                    customerCar.setCustomerAddr(customer.getCustomerAddr());
                }
                model.addAttribute("customerCar", customerCar);
            }

            model.addAttribute("orderGoodsList", VirtualOrderGoodList);
            //工单服务
            List<VirtualOrderservice> orderServicesList = virtualOrderserviceService.queryOrderServices(orderId, shopId);
            List<VirtualOrderservice> orderServicesList1 = new ArrayList<>();
            List<VirtualOrderservice> orderServicesList2 = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderServicesList)) {
                for (VirtualOrderservice orderServices : orderServicesList) {
                    int serviceType = orderServices.getType();
                    String flags = orderServices.getFlags();
                    if (serviceType == 1) {
                        orderServicesList1.add(orderServices);
                    }

                    if (serviceType == 2) {
                        if (flags != null && flags.contains("GLF")) {
                            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.queryShopServiceList(shopId, ShopServiceTypeEnum.OTHER, Constants.GLF);
                            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                                ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                                BigDecimal price = shopServiceInfo.getServicePrice();
                                BigDecimal glfAmount = (virtualOrder.getGoodsAmount().subtract(virtualOrder.getGoodsDiscount())).multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
                                model.addAttribute("glfTotalAmount", glfAmount);
                                model.addAttribute("glfAmount", glfAmount.subtract(orderServices.getDiscount()));
                                orderServices.setServicePrice(glfAmount);
                                orderServices.setServiceAmount(orderServices.getServicePrice());
                            }
                        }
                        orderServicesList2.add(orderServices);
                    }
                }
            }
            BigDecimal glfAmount = (BigDecimal) model.asMap().get("glfTotalAmount");
            if (glfAmount != null) {
                model.addAttribute("orderFreeAmount", virtualOrder.getFeeAmount().subtract(glfAmount));
            }

            model.addAttribute("orderServicesList1", orderServicesList1);
            model.addAttribute("orderServicesList2", orderServicesList2);
            model.addAttribute("currentDate", DateUtil.convertDateToYMDHM(new Date()));
        }

        String printLog = getPrintLog(request, "子单打印");
        log.info(printLog);

        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        //获取打印新老版本配置
        ShopConfigure shopConfigure = shopConfigureService.getPrintVersion(shopId);
        if (null != shopConfigure && "new".equals(shopConfigure.getConfValue())){
            return "yqx/page/order/print/virtualorder-print";
        }
        return "yqx/page/order/virtualorder-print";
    }


    /**
     * calculate virtual order price
     *
     * @param orderGoodsStr    工单.物料JSON
     * @param orderServicesStr 工单.服务JSON
     * @return
     */
    @RequestMapping(value = "virtual/calculate", method = RequestMethod.POST)
    @ResponseBody
    public Result virtualCalcPrice(@RequestParam("orderGoods") String orderGoodsStr, @RequestParam("orderServices") String orderServicesStr) {
        List<Map<String, Object>> orderGoodsMapList;
        List<Map<String, Object>> orderServiceMapList;

        ObjectMapper mapper;
        try {
            mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            orderGoodsMapList = mapper.readValue(orderGoodsStr, List.class);
            orderServiceMapList = mapper.readValue(orderServicesStr, List.class);
            return orderService.virtualCalcPrice(orderGoodsMapList, orderServiceMapList);

        } catch (Exception e) {
            log.error("计算工单价格异常:{}", e.getMessage());
            return Result.wrapErrorResult("", "计算工单价格异常");
        }

    }


    /**
     * 获取工单基本信息
     *
     * @param orderId
     * @return
     */
    @RequestMapping("get_order_json")
    @ResponseBody
    public Result<OrderInfo> getOrderJson(Long orderId) {
        try {
            Long shopId = UserUtils.getShopIdForSession(request);
            Optional<OrderInfo> orderInfoOpt = orderService.getOrder(orderId, shopId);
            if (orderInfoOpt.isPresent()) {
                return Result.wrapSuccessfulResult(orderInfoOpt.get());
            } else {
                return Result.wrapErrorResult("", "工单不存在");
            }
        } catch (Exception e) {
            log.error("根据orderId获取工单基本信息异常,{}", e);
            return Result.wrapErrorResult("", "根据orderId获取工单基本信息异常");
        }
    }

    /**
     * 查询下次保养里程
     *
     * @param carModelId
     * @param mileage
     * @return
     */
    @RequestMapping(value = "get_upkeep_mileage", method = RequestMethod.GET)
    @ResponseBody
    public Result<Integer> getUpkeepMileage(@RequestParam(value = "carId", required = false) Long carId, Integer carModelId, Integer mileage) {
        CustomerCar customerCar = customerCarService.selectById(carId);
        if (customerCar != null) {
            String upkeepMileage = customerCar.getUpkeepMileage();
            if (!StringUtils.isBlank(upkeepMileage)) {
                int upkeepMileageInt = Integer.parseInt(upkeepMileage);
                if (upkeepMileageInt - mileage > 0) {
                    return Result.wrapSuccessfulResult(upkeepMileageInt);
                }
            }
        }
        com.tqmall.core.common.entity.Result<Integer> result = null;
        try {
            // dubbo 调用远程接口
            result = maintainService.nextMaintainMileage(carModelId, mileage);
        } catch (Exception e) {
            log.error("根据车型和行驶里程查询下次保养里程异常,{}", e);
            return Result.wrapErrorResult("", "根据车型和行驶里程查询下次保养里程异常");
        }
        if (result != null) {
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult(result.getData());
            } else {
                return Result.wrapErrorResult("", result.getMessage());
            }
        }
        return Result.wrapErrorResult("", "根据车型和行驶里程查询下次保养里程为空");
    }


    /**
     * 进入物料销售单页面
     *
     * @param customerCarId 客户车辆ID
     * @param orderId       工单ID
     */
    @RequestMapping(value = "sell-good", method = RequestMethod.GET)
    public String sellGood(Model model, @RequestParam(value = "customerCarId", required = false) Long customerCarId, @RequestParam(value = "orderId", required = false) Long orderId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        // current loginuser
        UserInfo userInfo = UserUtils.getUserInfo(request);
        // wrapper formEntity
        if(customerCarId != null || orderId != null){
            model.addAttribute("formEntity", sellGoodsFacade.wrapperFormBody(customerCarId, orderId, userInfo));
        }
        getLicenseModel(model, userInfo.getShopId());
        return "yqx/page/order/sell-good";
    }

    /**
     * 进入物料销售单页面
     *
     * @param copyOrderId 工单ID
     */
    @RequestMapping(value = "sell-good-copy", method = RequestMethod.GET)
    public String sellGoodCopy(Model model, @RequestParam(value = "copyOrderId", required = false) Long copyOrderId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        // current loginuser
        UserInfo userInfo = UserUtils.getUserInfo(request);
        // wrapper formEntity
        model.addAttribute("formEntity", sellGoodsFacade.wrapperFormBody(copyOrderId, userInfo));

        return "yqx/page/order/sell-good";
    }


    /**
     * 保存物料销售单
     *
     * @param orderFormEntity 表单实体
     * @return Result
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "sell-good/save", method = RequestMethod.POST)
    @ResponseBody
    public Result sellGoodSave(OrderFormEntityBo orderFormEntity) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();

        // Log trac
        logTrac("sell-good/save", userId, orderFormEntity);

        // order info
        OrderInfo orderInfo = orderFormEntity.getOrderInfo();
        // 设置工单类别 {'5':'物料销售单'}
        orderInfo.setOrderTag(OrderCategoryEnum.SELLGOODS.getCode());
        // TODO 物料销售单的业务类型
        orderInfo.setOrderType(0l);

        // TODO 确认客户信息 包装车辆实体
        if (StringUtils.isBlank(orderInfo.getCarLicense())) {
            Long increment = jedisClient.incrHash("SELL_DEFAULT_LICENSE", "" + userInfo.getShopId(), 1);
            orderInfo.setCarLicense(Constants.SELL_DEFAULT_LICENSE + increment);
        }

        CustomerCar customerCar = wrapperOrderFacade.wrapperCustomerCar(orderInfo);
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("创建物料销售工单失败,[原因] 获取客户车辆信息异常，异常信息:{}", e.toString());
            return Result.wrapErrorResult("", "创建物料销售工单失败! <br/> 客户车辆信息不存在");
        }
        orderInfo.setCustomerCarId(customerCar.getId());
        orderInfo.setCustomerName(customerCar.getCustomerName());
        orderInfo.setCustomerMobile(customerCar.getMobile());

        try {
            Result result = orderService.save(orderFormEntity, userInfo);
            Object orderId = (result != null) ? result.getData() : "";
            log.info("工单状态流转:创建物料销售工单,工单号为:{} 操作人:{}", orderId, userId);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("创建物料销售工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "创建物料销售工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("创建物料销售工单异常", e2);
            return Result.wrapErrorResult("", "创建物料销售工单异常!");
        }
    }


    /**
     * TODO 更新物料销售单
     *
     * @param orderFormEntity 保单实体
     * @return Result
     * @see OrderFormEntityBo
     */
    @RequestMapping(value = "sell-good/update", method = RequestMethod.POST)
    @ResponseBody
    public Result sellGoodUpdate(OrderFormEntityBo orderFormEntity) {

        UserInfo userInfo = UserUtils.getUserInfo(request);

        Long userId = userInfo.getUserId();

        // order info
        OrderInfo orderInfo = orderFormEntity.getOrderInfo();

        // 车牌号码
        String carLicense = orderInfo.getCarLicense();
        if (StringUtils.trimToEmpty(carLicense).equals("")) {
            return Result.wrapErrorResult("", "车牌不能为空！");
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
            log.info("工单状态流转:{} 更新物料销售工单,工单号为:{} 操作人:{}", carLicense, orderId, userId);
            return result;
        } catch (BusinessCheckedException e1) {
            log.error("更新物料销售工单失败,原因:{}", e1.toString());
            return Result.wrapErrorResult("", "更新物料销售工单失败!<br/>" + e1.getMessage());
        } catch (RuntimeException e2) {
            log.error("更新物料销售工单异常", e2);
            return Result.wrapErrorResult("", "更新物料销售工单失败!");
        }
    }

    /**
     * 销售单打印
     *
     * @param model
     * @param orderId 工单ID
     * @return
     */
    @RequestMapping(value = "sell-good-print", method = RequestMethod.GET)
    public String sellGoodPrint(Model model, @RequestParam(value = "orderId", required = true) Long orderId) {
        Boolean bool = wrapperModelOfOrderPrint(model, orderId);
        if (!bool){
            //工单不存在跳转至列表页
            return "redirect:/shop/order/order-list";
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopConfigure shopConfigure = shopConfigureService.getPrintVersion(shopId);
        String version = shopConfigure.getConfValue();
        String printLog = getPrintLog(request, version + "销售单打印");
        log.info(printLog);

        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        if ("new".equals(version)){
            return "yqx/page/order/print/sale-print";
        }
        return "yqx/page/order/sell-good-print";
    }


    /**
     * 分页查询客户历史选购的物料
     *
     * @param customercarid 客户ID
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "historygoods", method = RequestMethod.GET)
    @ResponseBody
    public Object customerHistoryGoodList(@RequestParam(value = "customercarid", required = true) Long customercarid, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        // 获取客户历史选购物料
        Page<OrderGoods> page = null;
        try {
            page = orderGoodsService.getHistoryGoodList(pageable, customercarid, shopId);
        } catch (Exception e) {
            log.error("分页查询客户历史选购的物料 失败,异常信息:{}", e);
            return Result.wrapErrorResult("-1", "分页查询客户历史选购的物料失败");
        }

        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 更新工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 工单状态
     * @return
     */
    @RequestMapping(value = "updateorderstatus", method = RequestMethod.GET)
    @ResponseBody
    public Result updateOrderStatus(@RequestParam(value = "orderId", required = true) Long orderId, @RequestParam(value = "orderStatus", required = true) String orderStatus) {
        try {
            int isSuccess = orderService.updateOrderStatus(orderId, OrderStatusEnum.getOrderStatusEnum(orderStatus));
            if (isSuccess == 1) {
                return Result.wrapSuccessfulResult(orderId);
            } else {
                return Result.wrapErrorResult("", "更新工单状态失败");
            }
        } catch (Exception e) {
            log.error("更新工单状态失败,工单ID:{} 异常信息:{}", orderId, e);
            return Result.wrapErrorResult("", "更新工单状态失败");
        }
    }

    /**
     * 工单删除
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();

        if (orderId == null || orderId < 0) {
            // 工单不存在提示信息
            log.error("工单不存在!");
            return Result.wrapErrorResult("", "工单不存在!");
        }

        Optional<OrderInfo> orderOptional = orderService.getOrder(orderId, shopId);
        if (!orderOptional.isPresent()) {
            return Result.wrapErrorResult("", "工单不存在!");
        }

        OrderInfo orderInfo = orderOptional.get();
        if (!OrderStatusEnum.WXDD.getKey().equals(orderInfo.getOrderStatus())) {
            return Result.wrapErrorResult("", "工单只有在无效后才可以删除,请先进行无效操作!");
        }

        try {
            int isSuccess = orderService.delete(orderId);
            if (isSuccess == 1) {
                log.info("工单状态流转: 删除工单,工单号为:{} 操作人:{}", orderId, userId);
                return Result.wrapSuccessfulResult(orderId);
            } else {
                return Result.wrapErrorResult("", "工单删除失败!");
            }
        } catch (Exception e) {
            log.error("删除工单失败,工单ID:{} 异常信息:{}", orderId, e);
            return Result.wrapErrorResult("", "删除工单失败");
        }
    }

    /**
     * 工单无效
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping(value = "invalid", method = RequestMethod.POST)
    @ResponseBody
    public Result orderInvaild(@RequestParam("orderId") Long orderId, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();

        if (orderId == null || orderId < 0) {
            // 工单不存在提示信息
            log.error("工单不存在!");
            return Result.wrapErrorResult("", "工单不存在!");
        }

        Optional<OrderInfo> orderOptional = orderService.getOrder(orderId, shopId);
        if (!orderOptional.isPresent()) {
            return Result.wrapErrorResult("", "工单不存在!");
        }

        OrderInfo orderInfo = orderOptional.get();
        if (OrderStatusEnum.WXDD.getKey().equals(orderInfo.getOrderStatus())) {
            return Result.wrapErrorResult("", "工单已经无效,无需重复无效!");
        }
        try {
            Result result = orderInvalidFacade.invalid(orderInfo, userInfo);
            if (result.isSuccess()) {
                log.info("工单无效成功: 工单号为:{} 操作人:{}", orderInfo.getId(), userInfo.getUserId());
            }
            return result;
        } catch (Exception e) {
            log.error("工单无效失败 工单ID:{} 异常信息:{}", orderId, e);
            return Result.wrapErrorResult("500", "工单无效失败");
        }
    }

    @RequestMapping(value = "get-channel")
    @ResponseBody
    public Result getChannel() {
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            com.tqmall.core.common.entity.Result<List<ChannelDTO>> channelResult = rpcChannelService.getAllShopChannel(shopId, null);
            if (channelResult.isSuccess()) {
                return Result.wrapSuccessfulResult(channelResult.getData());
            }
        } catch (Exception e) {
            log.error("【dubbo：获取渠道商列表】：出现异常", e);
        }
        return Result.wrapErrorResult(LegendErrorCode.GET_CHANNEL_EX.getCode(), LegendErrorCode.GET_CHANNEL_EX.getErrorMessage());
    }


    private boolean wrapperModelOfOrderPrint(Model model, Long orderId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId != null && shopId > 0) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);

            // 获取门店配置
            Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.ORDERPRINT, shopId);
            String shopConfigureContent = "";
            if (shopConfigureOptional.isPresent()) {
                ShopConfigure shopConfigure = shopConfigureOptional.get();
                shopConfigureContent = shopConfigure.getConfValue();
            }
            ShopConfigureVO configureVO = new ShopConfigureVO();
            if (StringUtils.isBlank(shopConfigureContent)) {
                configureVO.setOrderComment("根据汽车维修部门规定：小修或日常维修保修期：三天或者500公里");
            } else {
                configureVO.setSettleComment(shopConfigureContent);
            }

            model.addAttribute("conf", configureVO);
        }


        //编辑工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            if (orderInfo.getOrderType() != null && orderInfo.getOrderType() > 0) {
                OrderType orderType = orderTypeService.selectById(orderInfo.getOrderType());
                if (orderType != null) {
                    orderInfo.setOrderTypeName(orderType.getName());
                }
            }

            //工单信息
            String oilMeter = orderInfo.getOilMeter();
            orderInfo.setOilMeter((oilMeter == null) ? "" : oilMeter);


            Map<String, Object> carMap = Maps.newHashMap();
            carMap.put("shopId", shopId);
            carMap.put("id", orderInfo.getCustomerCarId());
            List<CustomerCar> customerCarList = customerCarService.select(carMap);
            CustomerCar customerCar = null;
            Customer customer = null;
            if (!CollectionUtils.isEmpty(customerCarList)) {
                customerCar = customerCarList.get(0);
                customer = customerService.selectById(customerCar.getCustomerId());
                if (customer != null) {
                    customerCar.setCustomerAddr(customer.getCustomerAddr());
                    customerCar.setCompany(customer.getCompany());
                }

                model.addAttribute("customerCar", customerCar);
            }
            orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
            model.addAttribute("orderInfo", orderInfo);


            //工单物料
            Map orderGoodsMap = new HashMap<>();
            orderGoodsMap.put("orderId", orderId);
            orderGoodsMap.put("shopId", shopId);
            List<OrderGoods> orderGoodsList = orderGoodsService.select(orderGoodsMap);
            model.addAttribute("orderGoodsList", orderGoodsList);
            //工单服务
            Map orderServicesMap1 = new HashMap<>();
            orderServicesMap1.put("orderId", orderId);
            orderServicesMap1.put("shopId", shopId);
            List<OrderServices> orderServicesList = orderServicesService.select(orderServicesMap1);
            List<OrderServices> orderServicesList1 = new ArrayList<>();
            List<OrderServices> orderServicesList2 = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderServicesList)) {
                for (OrderServices orderServices : orderServicesList) {
                    if (orderServices.getType() == 1) {
                        orderServicesList1.add(orderServices);
                    }
                    if (orderServices.getType() == 2) {
                        orderServicesList2.add(orderServices);
                    }
                }
            }
            // 关联维修工名称
            List<OrderServicesVo> orderServicesVos = wrapperOrderFacade.orderServiceListReferWorderName(orderServicesList1);
            model.addAttribute("orderServicesList1", orderServicesVos);
            model.addAttribute("orderServicesList2", orderServicesList2);

            log.info("门店ID:" + userInfo.getShopId() + "账户:" + userInfo.getAccount() + "打印工单,单号：" + orderInfo.getOrderSn());

            return true;
        } else {
            //工单不存在跳转至列表页
            return false;
        }
    }
    private boolean wrapperShopOrderPrint(ShopPrintConfigVO printConfigVO, Model model, Long orderId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId != null && shopId > 0) {
            Shop shop = shopService.selectById(shopId);
            model.addAttribute("shop", shop);
        }
        //编辑工单
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);
        if (!orderInfoOptional.isPresent()) {
            return false;
        }
        OrderInfo orderInfo = orderInfoOptional.get();
        if (orderInfo.getOrderType() != null && orderInfo.getOrderType() > 0) {
            OrderType orderType = orderTypeService.selectById(orderInfo.getOrderType());
            if (orderType != null) {
                orderInfo.setOrderTypeName(orderType.getName());
            }
        }

        //工单信息
        String oilMeter = orderInfo.getOilMeter();
        orderInfo.setOilMeter((oilMeter == null) ? "" : oilMeter);


        Map<String, Object> carMap = Maps.newHashMap();
        carMap.put("shopId", shopId);
        carMap.put("id", orderInfo.getCustomerCarId());
        List<CustomerCar> customerCarList = customerCarService.select(carMap);
        CustomerCar customerCar = null;
        Customer customer = null;
        if (!CollectionUtils.isEmpty(customerCarList)) {
            customerCar = customerCarList.get(0);
            customer = customerService.selectById(customerCar.getCustomerId());
            if (customer != null) {
                customerCar.setCustomerAddr(customer.getCustomerAddr());
                customerCar.setCompany(customer.getCompany());
            }

            model.addAttribute("customerCar", customerCar);
        }
        orderInfo = wrapperOrderFacade.wrapperOrderInfo(orderInfo, customerCar, customer);
        ShopPrintConfigUtil.wrapperField(printConfigVO.getConfigFieldVO(),orderInfo);
        model.addAttribute("printConfigVO", printConfigVO);
        model.addAttribute("orderInfo", orderInfo);


        //工单物料
        Map orderGoodsMap = new HashMap<>();
        orderGoodsMap.put("orderId", orderId);
        orderGoodsMap.put("shopId", shopId);
        List<OrderGoods> orderGoodsList = orderGoodsService.select(orderGoodsMap);
        model.addAttribute("orderGoodsList", orderGoodsList);
        //工单服务
        Map orderServicesMap1 = new HashMap<>();
        orderServicesMap1.put("orderId", orderId);
        orderServicesMap1.put("shopId", shopId);
        List<OrderServices> orderServicesList = orderServicesService.select(orderServicesMap1);
        List<OrderServices> orderServicesList1 = new ArrayList<>();
        List<OrderServices> orderServicesList2 = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                if (orderServices.getType() == 1) {
                    orderServicesList1.add(orderServices);
                }
                if (orderServices.getType() == 2) {
                    orderServicesList2.add(orderServices);
                }
            }
        }
        // 关联维修工名称
        List<OrderServicesVo> orderServicesVos = wrapperOrderFacade.orderServiceListReferWorderName(orderServicesList1);
        // 服务项目
        model.addAttribute("orderServicesList1", orderServicesVos);
        // 附加项目
        model.addAttribute("orderServicesList2", orderServicesList2);

        log.info("门店ID:" + userInfo.getShopId() + "账户:" + userInfo.getAccount() + "打印工单,单号：" + orderInfo.getOrderSn());

        return true;
    }
}
