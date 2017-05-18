package com.tqmall.yunxiu.web.pub;


import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ServiceTemplateCateRelService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.insurance.InsuranceBill;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServiceTypeEnum;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.pub.order.OrderAppVo;
import com.tqmall.legend.entity.pub.order.OrderDetailVo;
import com.tqmall.legend.entity.pub.order.OrderEvaluateVo;
import com.tqmall.legend.entity.pub.order.OrderGoodsVo;
import com.tqmall.legend.entity.pub.order.OrderServicesVo;
import com.tqmall.legend.entity.pub.order.OrderVo;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * 2C-APP 工单接口controller
 */
@Controller
@RequestMapping("pub/order")
public class OrderInfoController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoController.class);

    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ShopService shopService;
    @Autowired
    ServiceTemplateCateRelService serviceTemplateCateRelService;
    @Autowired
    IOrderService orderService;
    @Autowired
    private IInsuranceBillService insuranceBillService;


    /**
     * create by jason 2015-07-09
     * <p/>
     * mobile为空获得所有工单信息,不为空获得对应的工单
     *
     * @param mobile 联系人手机号
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Result getAllOrder(@RequestParam(value = "mobile", required = true) String mobile,
                              @RequestParam(value = "licenseList[]", required = true) List<String> licenseList,
                              @RequestParam(value = "userGlobalId", required = false) String userGlobalId,
                              @RequestParam(value = "offset", required = true) Long offset,
                              @RequestParam(value = "limit", required = false) Long limit) {
        mobile = mobile.trim();
        if(!StringUtil.isMobileNO(mobile)){
            logger.info("手机号{}不正确,直接返回",mobile);
            return Result.wrapErrorResult("", "手机号不正确,获得工单列表信息为空");
        }
        if(CollectionUtils.isEmpty(licenseList)){
            logger.info("车牌号{}为空,直接返回",licenseList);
            return Result.wrapErrorResult("", "车牌号为空,获得工单列表信息为空");
        }
        Map map = new HashMap();
        map.put("contactMobile", mobile);
        map.put("carLicenses", licenseList);
        if (null == offset || offset < 0l) {
            offset = 0l;
        }
        if (null == limit || limit <= 0l) {
            limit = 5l;
        }
        map.put("limit", limit);
        map.put("offset", offset);
        if(StringUtils.isNotEmpty(userGlobalId)){
            Map<String,Object> shopParam = Maps.newHashMap();
            shopParam.put("userGlobalId",userGlobalId);
            List<Shop> shopList = shopService.select(shopParam);
            if(!CollectionUtils.isEmpty(shopList)){
                Shop shop = shopList.get(0);
                map.put("shopId",shop.getId());
            }  else{
                logger.info("根据userGlobalId:{}查询不到门店信息,预约单查询失败",userGlobalId);
                return Result.wrapErrorResult("", "获取不到门店信息,获得工单列表信息为空");
            }
        }
        logger.info("进入获得所有该手机号下所有工单信息!参数:{}", map);
        //根据手机号和车牌获得订单信息列表
        List<OrderInfo> orderInfoList = orderInfoService.selectByContactMobileAndLicense(map);

        List<OrderVo> orderVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderInfoList)) {
            //组装车主一级二级服务类别
            Map<Long, ShopServiceCate> cateMap = shopServiceCateService.dealCateInfo();

            List<String> cateNameList = new ArrayList<>();//拼接类别
            for (OrderInfo orderInfo : orderInfoList) {
                OrderVo orderVo = new OrderVo();
                Long orderId = orderInfo.getId();//订单ID
                Long shopId = orderInfo.getShopId();//店铺ID
                //店铺
                Shop shop = shopService.selectById(shopId);
                /*//从缓存中获得工单类型名称
                List<OrderType> orderTypeList = cacheComponent.getCache(CacheKeyConstant.ORDER_TYPE);
                for (OrderType orderType : orderTypeList) {
                    if (shopId.compareTo(orderType.getShopId())==0 &&
                            orderType.getId().compareTo(orderInfo.getOrderType())==0) {
                        orderInfo.setOrderTypeName(orderType.getName());
                        break;//找到之后直接跳出循环
                    }
                }*/
                // 物料
                List<OrderGoods> orderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId);
                List<OrderGoodsVo> goodsVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(orderGoodsList)) {
                    for (OrderGoods orderGoods : orderGoodsList) {
                        OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                        orderGoodsVo.setGoodsName(orderGoods.getGoodsName());
                        goodsVoList.add(orderGoodsVo);
                    }
                }

                Map<String, Object> paramsMap = new HashMap<String, Object>(2);
                paramsMap.put("orderId", orderId);
                paramsMap.put("shopId", shopId);
                // 工单服务
                List<OrderServices> orderServicesList = orderServicesService.select(paramsMap);
                List<OrderServicesVo> servicesVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(orderServicesList)) {
                    for (OrderServices orderServices : orderServicesList) {
                        OrderServicesVo orderServicesVo = new OrderServicesVo();
                        Long parentServiceId = orderServices.getParentServiceId();//服务父ID
                        Long serviceId = orderServices.getServiceId();//服务ID
                        List<Long> serviceIdList = new ArrayList<>();
                        if (null != parentServiceId && parentServiceId > 0l) {
                            serviceIdList.add(parentServiceId);
                        } else {
                            //如果这个工单服务parentServiceId=0,说明这个是基本服务要服务的一级类目ID
                            serviceIdList.add(serviceId);
                        }
                        List<ShopServiceInfo> serviceInfoList = shopServiceInfoService.selectAllByIds(serviceIdList);
                        if (!CollectionUtils.isEmpty(serviceInfoList)) {
                            ShopServiceInfo shopServiceInfo = serviceInfoList.get(0);
                            if (null != shopServiceInfo) {
                                Integer appCateId = shopServiceInfo.getAppCateId();
                                String flags = shopServiceInfo.getFlags();
                                //车主服务取车主服务类别名称
                                if ("CZFW".equals(flags)) {
                                    if (null != appCateId && appCateId > 0l) {
                                        ShopServiceCate shopServiceCate = cateMap.get(Long.valueOf(appCateId));
                                        if (null != shopServiceCate) {
                                            orderServicesVo.setCategoryName(shopServiceCate.getFirstCateName());//车主服务一级类别名称
                                            orderServicesVo.setCategoryId(shopServiceCate.getParentId());//车主服务一级类别ID
                                        }
                                    }
                                } else if ("TQFW".equals(flags)) {
                                    //淘汽服务对应的父ID
                                    Long parentId = shopServiceInfo.getParentId();
                                    if (null != parentId && parentId > 0l) {
                                        Map searchMap = new HashMap();
                                        searchMap.put("templateId", parentId);
                                        //去服务和类目对应关系表中获得数据 一个淘汽服务可能对应多个类目ID 随机取一个
                                        List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(searchMap);

                                        if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                                            Long cateId = serviceTemplateCateRelList.get(0).getCateId();
                                            if (null != cateId && cateId > 0l) {

                                                //把一级类目ID set到ShopServiceInfo中
                                                orderServicesVo.setCategoryId(cateMap.get(cateId).getParentId());
                                                //把一级类目名称 set到ShopServiceInfo中
                                                orderServicesVo.setCategoryName(cateMap.get(cateId).getFirstCateName());
                                            }
                                        }
                                    }
                                } else {
                                    orderServicesVo.setCategoryName("门店服务");//不是车主服务类别名称
                                    orderServicesVo.setCategoryId(0l);//不是车主服务类别ID
                                }
                            }
                        }

                        orderServicesVo.setServiceName(orderServices.getServiceName());
                        servicesVoList.add(orderServicesVo);
                        String cateName = orderServicesVo.getCategoryName();
                        if (null != cateName) {
                            if (!cateNameList.contains(cateName)) {
                                cateNameList.add(cateName);
                            }
                        }
                    }
                }

                StringBuffer cateSB = new StringBuffer();
                for (String s : cateNameList) {
                    cateSB.append(s).append("、");
                }
                String cateNameStr = cateSB.toString();
                String cateNameStrNew = "";
                if (StringUtil.isNotStringEmpty(cateNameStr)) {
                    cateNameStrNew = cateNameStr.substring(0, cateNameStr.length() - 1);//截取最后的顿号
                }

                orderVo.setId(orderId);//订单ID
                if (null != shop) {
                    orderVo.setUserGlobalId(shop.getUserGlobalId());//店铺ID
                    orderVo.setShopName(shop.getName());//店铺名称
                    orderVo.setShopMobile(shop.getMobile());//店铺手机
                    orderVo.setShopTel(shop.getTel());//店铺固话
                    orderVo.setShopAddress(shop.getAddress());//店铺地址
                }
                orderVo.setOrderStatus(orderInfo.getOrderStatus());//工单状态
                orderVo.setOrderTypeName(orderInfo.getOrderTypeName());//工单类型
                orderVo.setFinishTime(orderInfo.getExpectedTime());//预计完工时间
                orderVo.setGmtCreate(orderInfo.getGmtCreate());//创建时间
                //orderVo.setGmtModified(orderInfo.getGmtModified());//修改时间
                orderVo.setServiceList(servicesVoList);//服务
                orderVo.setGoodsList(goodsVoList);//物料
                orderVo.setOrderAmount(orderInfo.getOrderAmount());//实付金额
                orderVo.setPayAmount(orderInfo.getPayAmount());//实付金额
                orderVo.setPayStatus(orderInfo.getPayStatus());//支付状态，0为未支付，2为已支付
                orderVo.setCategoryName(cateNameStrNew);//服务类别
                orderVo.setCarLicense(orderInfo.getCarLicense());//车牌号

                orderVoList.add(orderVo);
            }
        }
        if (!CollectionUtils.isEmpty(orderVoList)) {
            return Result.wrapSuccessfulResult(orderVoList);
        } else {
            return Result.wrapErrorResult("", "获得工单列表信息为空");
        }
    }
    /**
     * create by jason 2015-07-09
     * <p/>
     * 获得工单详情信息
     *
     * @param orderId
     * @param mobile
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrderDetail(@RequestParam(value = "mobile", required = false) String mobile,
                                 @RequestParam(value = "orderId", required = true) Long orderId) {
        logger.info("进入获得工单详情信息接口!mobile:{},orderId:{}", mobile, orderId);
        if(StringUtils.isNoneBlank(mobile)) {
            if (!StringUtil.isMobileNO(mobile)) {
                return Result.wrapErrorResult("", "手机号不正确!");
            }
            mobile = mobile.trim();//手机号截掉空字符
        }

        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId);//订单信息
        OrderDetailVo orderDetail = new OrderDetailVo();
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            Long shopId = orderInfo.getShopId();//店铺ID
            //验证该订单是该用户的,防止跨域看到别人的订单信息
            String contactMobile = orderInfo.getContactMobile();
            if (StringUtils.isNoneBlank(mobile)&&StringUtils.equals(contactMobile, mobile)||StringUtils.isBlank(mobile)) {
                //组装车主一级二级服务类别
                Map<Long, ShopServiceCate> cateMap = shopServiceCateService.dealCateInfo();
                //店铺
                Shop shop = shopService.selectById(shopId);
                //工单类型名称
                /*List<OrderType> orderTypeList = cacheComponent.getCache(CacheKeyConstant.ORDER_TYPE);
                for (OrderType orderType : orderTypeList) {
                    if (shopId.compareTo(orderType.getShopId())==0 &&
                            orderType.getId().compareTo(orderInfo.getOrderType())==0) {
                        orderInfo.setOrderTypeName(orderType.getName());
                        break;
                    }
                }*/
                // 实开物料
                List<OrderGoods> realOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.ACTUAL);
                List<OrderGoodsVo> realGoodsList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(realOrderGoodsList)) {
                    for (OrderGoods orderGoods : realOrderGoodsList) {
                        OrderGoodsVo realOrderGoods = new OrderGoodsVo();
                        realOrderGoods.setGoodsName(orderGoods.getGoodsName());
                        realOrderGoods.setGoodsNumber(orderGoods.getGoodsNumber());
                        realOrderGoods.setGoodsPrice(orderGoods.getGoodsPrice());
                        realGoodsList.add(realOrderGoods);
                    }
                }
                // 其他物料
                List<OrderGoods> otherOrderGoodsList = orderGoodsService.queryOrderGoodList(orderId, shopId, OrderGoodTypeEnum.VIRTUAL);
                List<OrderGoodsVo> otherGoodsList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(otherOrderGoodsList)) {
                    for (OrderGoods orderGoods : otherOrderGoodsList) {
                        OrderGoodsVo otherOrderGoods = new OrderGoodsVo();
                        otherOrderGoods.setGoodsName(orderGoods.getGoodsName());
                        otherOrderGoods.setGoodsNumber(orderGoods.getGoodsNumber());
                        otherOrderGoods.setGoodsPrice(orderGoods.getGoodsPrice());
                        otherGoodsList.add(otherOrderGoods);
                    }
                }
                // 工单基本服务
                logger.info("获取工单基本服务!参数type=1,orderId:{},shopId:{}", orderId, shopId);
                List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.BASIC);
                List<OrderServicesVo> servicesVoList = new ArrayList<>();

                if (!CollectionUtils.isEmpty(orderServicesList)) {
                    for (OrderServices orderServices : orderServicesList) {
                        OrderServicesVo orderServicesVo = new OrderServicesVo();
                        //如果这个工单服务存在parentServiceId,说明这个是套餐的服务要取套餐的一级类目ID
                        Long parentServiceId = orderServices.getParentServiceId();//父ID
                        Long serviceId = orderServices.getServiceId();//服务ID
                        List<ShopServiceInfo> shopServiceInfoList;
                        List<Long> serviceIdList = new ArrayList<>();
                        if (null != parentServiceId && parentServiceId > 0l) {
                            serviceIdList.add(parentServiceId);
                            //shopServiceInfoList = shopServiceInfoService.selectAllByIds(serviceIdList);
                        } else {
                            //如果这个工单服务不存在parentServiceId,说明这个是基本服务要服务的一级类目ID
                            serviceIdList.add(serviceId);
                        }
                        shopServiceInfoList = shopServiceInfoService.selectAllByIds(serviceIdList);
                        if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                            ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                            logger.info("legend服务工单:"+shopServiceInfo);
                            int priceType = shopServiceInfo.getPriceType()==null?1:shopServiceInfo.getPriceType().intValue();
                            orderServices.setPriceType(priceType);
                            if (null != shopServiceInfo) {
                                Integer appCateId = shopServiceInfo.getAppCateId();
                                String flags = shopServiceInfo.getFlags();
                                //车主服务取车主服务类别名称
                                if ("CZFW".equals(flags)) {
                                    if (null != appCateId && appCateId > 0l) {
                                        ShopServiceCate shopServiceCate = cateMap.get(Long.valueOf(appCateId));
                                        if (null != shopServiceCate) {
                                            orderServicesVo.setCategoryName(shopServiceCate.getFirstCateName());//车主服务一级类别名称
                                            orderServicesVo.setCategoryId(shopServiceCate.getParentId());//车主服务一级类别ID
                                        }
                                    }
                                } else if ("TQFW".equals(flags)) {
                                    //淘汽服务对应的父ID
                                    Long parentId = shopServiceInfo.getParentId();
                                    if (null != parentId && parentId > 0l) {
                                        Map searchMap = new HashMap();
                                        searchMap.put("templateId", parentId);
                                        //去服务和类目对应关系表中获得数据 一个淘汽服务可能对应多个类目ID 随机取一个
                                        List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(searchMap);

                                        if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                                            Long cateId = serviceTemplateCateRelList.get(0).getCateId();
                                            if (null != cateId && cateId > 0l) {

                                                //把一级类目ID set到ShopServiceInfo中
                                                orderServicesVo.setCategoryId(cateMap.get(cateId).getParentId());
                                                //把一级类目名称 set到ShopServiceInfo中
                                                orderServicesVo.setCategoryName(cateMap.get(cateId).getFirstCateName());
                                            }
                                        }
                                    }
                                } else {
                                    orderServicesVo.setCategoryName("门店服务");//不是车主服务类别名称
                                    orderServicesVo.setCategoryId(0l);//不是车主服务类别ID
                                }
                            }
                        }
                        orderServicesVo.setServiceName(orderServices.getServiceName());//服务名称
                        orderServicesVo.setServiceHour(orderServices.getServiceHour());//服务工时
                        orderServicesVo.setServicePrice(orderServices.getServicePrice());//服务单价
                        orderServicesVo.setPriceType(orderServices.getPriceType());
                        servicesVoList.add(orderServicesVo);
                    }
                }
                // 工单附加服务
                List<OrderServices> otherServicesList = orderServicesService.queryOrderServiceList(orderId, shopId, OrderServiceTypeEnum.ANCILLARY);
                List<OrderServicesVo> otherServicesVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(otherServicesList)) {
                    for (OrderServices orderServices : otherServicesList) {
                        OrderServicesVo orderServicesVo = new OrderServicesVo();
                        orderServicesVo.setServiceName(orderServices.getServiceName());//服务名称
                        orderServicesVo.setServiceHour(orderServices.getServiceHour());//服务工时
                        orderServicesVo.setServicePrice(orderServices.getServicePrice());//服务单价
                        otherServicesVoList.add(orderServicesVo);
                    }
                }

                orderDetail.setOrderId(orderInfo.getId());//工单ID
                if (null != shop) {
                    orderDetail.setShopName(shop.getName());//店铺名称
                    orderDetail.setUserGlobalId(shop.getUserGlobalId());
                    orderDetail.setShopAddress(shop.getAddress());//店铺地址
                    orderDetail.setShopMobile(shop.getMobile());//店铺mobile
                    orderDetail.setShopTel(shop.getTel());//店铺固话
                }
                orderDetail.setOrderStatus(orderInfo.getOrderStatus());//工单状态
                orderDetail.setOrderTypeName(orderInfo.getOrderTypeName());//工单类型
                orderDetail.setGmtCreate(orderInfo.getGmtCreate());//创建时间
                orderDetail.setFinishTime(orderInfo.getExpectedTime());//预计完工时间
                orderDetail.setCompletedTime(orderInfo.getFinishTime());//完工时间
                orderDetail.setPayTime(orderInfo.getPayTime());//结算时间
                orderDetail.setGmtModified(orderInfo.getGmtModified());//修改时间
                orderDetail.setServiceList(servicesVoList);//基本服务
                orderDetail.setOtherServiceList(otherServicesVoList);//其他服务
                orderDetail.setGoodsList(realGoodsList);//实开物料
                orderDetail.setOtherGoodsList(otherGoodsList);//虚开物料
                orderDetail.setOrderAmount(orderInfo.getOrderAmount());//物料和服务和附加费用优惠后的总和
                orderDetail.setPayAmount(orderInfo.getPayAmount());//应收金额
                orderDetail.setPayStatus(orderInfo.getPayStatus());//支付状态，0为未支付，1支付部分 2为已支付
                orderDetail.setCarLicense(orderInfo.getCarLicense());//车牌
                orderDetail.setOrderTag(orderInfo.getOrderTag());//工单标签，1为普通工单，2为洗车工单，3为快修快保单，4为保险维修单
            }
        }
        if (null != orderDetail.getOrderId()) {
            logger.info("获得工单详情信息成功:{}", orderDetail);
            return Result.wrapSuccessfulResult(orderDetail);
        } else {
            return Result.wrapErrorResult("", "获得工单详情信息为空");
        }

    }

    /**
     * create by jason 2015-09-18
     * e.g. 7天之外,10天之内的工单
     */
    @RequestMapping(value = "evaluate/list", method = RequestMethod.POST)
    @ResponseBody
    public Result getEvaluateList(@RequestBody OrderAppVo orderAppVo) {

        try {
            List<String> mobileList = orderAppVo.getMobileList();
            Integer dayNumMin = orderAppVo.getDayNumMin();
            Integer dayNumMax = orderAppVo.getDayNumMax();

            if (CollectionUtils.isEmpty(mobileList)) {
                return Result.wrapErrorResult("-1", "mobileList参数不能为空!");
            } else if (null == dayNumMin) {
                return Result.wrapErrorResult("-1", "dayNumMin参数不能为空");
            } else if (dayNumMin <= 0) {
                return Result.wrapErrorResult("-1", "dayNumMin参数必须大于0");
            } else if (null != dayNumMax && dayNumMin > dayNumMax) {
                return Result.wrapErrorResult("-1", "dayNumMin不能大于dayNumMax");
            }

            List<OrderEvaluateVo> resultList = orderInfoService.selectByMobileAndDayNum(mobileList, dayNumMin, dayNumMax);

            return Result.wrapSuccessfulResult(resultList);
        } catch (Exception e) {
            logger.info("获得未评价的工单数据异常!" + e.toString());
            return Result.wrapErrorResult("", "获得未评价的工单数据异常");
        }

    }

    /*
     * 2015-11-18 柯昌强
     * 根据手机号和车牌取得已经付款的订单数量
     *
     */
    @RequestMapping(value = "orderNumList",method = RequestMethod.GET)
    @ResponseBody
    public Result getOrderNumList(@RequestParam(value = "mobile",required=true)String mobile,
                                     @RequestParam(value = "licenseList[]",required=true)List<String> licenseList) {
        mobile = mobile.trim();
        //车牌key和该车牌已完成工单数value组成的map
        Map<String, Integer> numberMap = new HashMap<>();
        Map<String, Object> searchParams = new HashMap<>();
        //支付状态集合payStatusList
        List<Integer> payStatusList = new ArrayList<>();
        //添加状态1“已支付”
        payStatusList.add(1);
        //添加状态2“挂帐”
        payStatusList.add(2);
        searchParams.put("contactMobile", mobile);
        searchParams.put("licenseList", licenseList);
        searchParams.put("payStatusList", payStatusList);
        logger.info("进入获得所有该手机号和车牌下已支付工单信息!参数:{}", searchParams);
        //根据手机号和车牌获得订单信息列表
        List<OrderInfo> orderInfoList = orderInfoService.select(searchParams);
        //通过判断orderInfoList是否空值来判断传入的车牌号和手机号是否有效
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return Result.wrapErrorResult("","传入的车牌和手机号获取不到数据");
        } else {
            for (OrderInfo order : orderInfoList) {
                //车牌号
                String carLicense = order.getCarLicense();
                //判断numberMap中是否已存在该车牌为key的元素
                if (numberMap.containsKey(carLicense)) {
                    Integer num = numberMap.get(carLicense);
                    num = num + 1;
                    numberMap.put(carLicense, num);
                } else {
                    numberMap.put(carLicense, 1);
                }
            }
            logger.info("根据手机号和车牌获取工单数量接口返回值:{}", numberMap);
            return Result.wrapSuccessfulResult(numberMap);
        }
    }

    /**
     * create by zsy 2016-01-13
     * 根据order_id获取保险维修单审核状态
     */
    @RequestMapping(value = "audit/status", method = RequestMethod.GET)
    @ResponseBody
    public Result getOrderAuditStatus(@RequestParam(value = "orderId", required = true) Long orderId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("orderId", orderId);
        List<InsuranceBill> insuranceBillList = insuranceBillService.select(searchMap);
        if (!CollectionUtils.isEmpty(insuranceBillList)) {
            InsuranceBill insuranceBill = insuranceBillList.get(0);
            Integer auditStatus = insuranceBill.getAuditStatus();
            logger.info("根据order_id:{}获取保险维修单是审核成功状态,返回数据:{}", orderId, auditStatus);
            return Result.wrapSuccessfulResult(auditStatus);
        } else {
            String errorMsg = LegendErrorCode.ORDER_NULL_EX.getErrorMessage(orderId);
            logger.info("根据order_id:{}获取保险维修单数据为空,返回数据:{}", orderId, errorMsg);
            return Result.wrapErrorResult(LegendErrorCode.ORDER_NULL_EX.getCode(), errorMsg);
        }
    }
}

