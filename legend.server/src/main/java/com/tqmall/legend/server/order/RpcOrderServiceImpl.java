package com.tqmall.legend.server.order;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.order.*;
import com.tqmall.legend.biz.shop.ServiceTemplateCateRelService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.order.OrderInvalidFacade;
import com.tqmall.legend.object.param.order.QryOrderParam;
import com.tqmall.legend.object.result.goods.WechatOrdGoodsDTO;
import com.tqmall.legend.object.result.order.WechatOrderDTO;
import com.tqmall.legend.object.result.service.WechatOrdServiceDTO;
import com.tqmall.legend.service.order.RpcOrderService;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 远程工单服务
 * Created by lixiao on 16/2/29.
 */
@Slf4j
@Service ("rpcOrderService")
public class RpcOrderServiceImpl implements RpcOrderService {

    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderInvalidFacade orderInvalidFacade;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    ServiceTemplateCateRelService serviceTemplateCateRelService;
    @Autowired
    OrderTypeService orderTypeService;

    @Override
    public Result<List<Long>> getServiceTplIdsByOrderId(String source, Long orderId) {
        log.info("【dubbo】RpcOrderServiceImpl.getServiceTplIdsByOrderId：根据工单id获取服务模板ids,传参对象{},来源{}", orderId, source);
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("orderId", orderId);
        List<OrderServices> orderServicesList = orderServicesService.select(searchMap);
        List<Long> serviceIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                serviceIds.add(orderServices.getServiceId());
            }
        }
        Set<Long> serviceTplIdsSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(serviceIds)) {
            Map<String, Object> serviceMap = new HashMap<>();
            serviceMap.put("ids", serviceIds);
            List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(serviceMap);
            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
                    serviceTplIdsSet.add(shopServiceInfo.getParentId());
                }
            }
        }
        List<Long> serviceTplIds = new ArrayList<>();
        serviceTplIds.addAll(serviceTplIdsSet);
        log.info("[dubbo]RpcOrderServiceImpl.getServiceTplIdsByOrderId：工单ID为{},serviceTplIds为{}", orderId, new Gson().toJson(serviceTplIds));
        return Result.wrapSuccessfulResult(serviceTplIds);
    }

    @Override
    public com.tqmall.core.common.entity.Result<List<WechatOrderDTO>> qryOrderList(QryOrderParam qryOrderParam) {
        log.info("[dubbo]工单列表查询,查询条件:{}", LogUtils.objectToString(qryOrderParam));
        Assert.notNull(qryOrderParam, "查询条件不能为空");
        String mobile = qryOrderParam.getMobile();
        if (!StringUtil.isMobileNO(mobile)) {
            log.info("手机号{}不正确,直接返回", mobile);
            return com.tqmall.core.common.entity.Result.wrapErrorResult("-1", "手机号不正确,查询工单列表失败");
        }
        Long shopId = null;
        Shop shop = null;
        Long userGlobalId = qryOrderParam.getUserGlobalId();
        List<WechatOrderDTO> wechatOrderDTOList = new ArrayList<>();

        //.根据手机号查询客户信息
        Map<String, Object> qryCustMap = new HashMap();
        qryCustMap.put("mobile", mobile);
        if (userGlobalId != null) {
            //有门店限制
            shop = shopService.getShopByUserGlobalId(userGlobalId);
            if (shop == null) {
                log.info("userGlobalId{}查询不到门店", userGlobalId);
                return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(wechatOrderDTOList);
            }
            shopId = shop.getId();
        }
        qryCustMap.put("shopId", shopId);
        List<Customer> customerList = customerService.select(qryCustMap);

        if (CollectionUtils.isEmpty(customerList)) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(wechatOrderDTOList);
        }
        Set<Long> customerIds = new HashSet<>();
        for (Customer customer : customerList) {
            customerIds.add(customer.getId());
        }

        //.查询工单
        int limit = qryOrderParam.getLimit() == null ? 5 : qryOrderParam.getLimit();
        long offset = qryOrderParam.getOffset() == null ? 0 : qryOrderParam.getOffset();
        Map<String, Object> qryOrderMap = new HashMap<>();
        qryOrderMap.put("customerIds", customerIds);
        qryOrderMap.put("licenseList", qryOrderParam.getLicenseList());
        qryOrderMap.put("shopId", shopId);
        qryOrderMap.put("limit", limit);
        qryOrderMap.put("offset", offset);
        qryOrderMap.put("sorts", new String[]{"create_time desc"});
        List<OrderInfo> orderInfoList = orderInfoService.select(qryOrderMap);
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(wechatOrderDTOList);
        }

        //.查询参数收集
        Set<Long> shopIds = new HashSet<>();
        Set<Long> orderIds = new HashSet<>();
        for (OrderInfo orderInfo : orderInfoList) {
            shopIds.add(orderInfo.getShopId());
            orderIds.add(orderInfo.getId());
        }

        //.获取门店信息
        Map<Long, Shop> shopMap = _getOrderShopInfo(shop, shopIds);//key:shopId

        //.获取物料信息
        Map<Long, List<WechatOrdGoodsDTO>> ordGoodsListMap = _getOrderGoods(orderIds);

        //.获取工单服务信息
        Map<Long, List<WechatOrdServiceDTO>> ordServiceListMap = _getOrderServices(orderIds);

        //.获取工单类型
        ImmutableMap<Long, OrderType> orderTypeMap = _getOrderType(orderInfoList);

        //.组装数据
        for (OrderInfo orderInfo : orderInfoList) {
            WechatOrderDTO wechatOrderDTO = new WechatOrderDTO();
            Long orderId = orderInfo.getId();
            String orderStatusClientName = OrderStatusEnum.getorderStatusClientNameByKey(orderInfo.getOrderStatus());//订单状态名称
            //设置工单信息
            wechatOrderDTO.setId(orderId);
            wechatOrderDTO.setOrderStatus(orderInfo.getOrderStatus());//工单状态
            wechatOrderDTO.setOrderStatusClientName(orderStatusClientName);//订单状态名称
            OrderType orderType = orderTypeMap.get(orderInfo.getOrderType());
            if (orderType != null) {
                wechatOrderDTO.setOrderTypeName(orderType.getName());//工单类型
            }
            wechatOrderDTO.setFinishTime(orderInfo.getExpectedTime());//预计完工时间
            wechatOrderDTO.setGmtCreate(orderInfo.getGmtCreate());//创建时间
            wechatOrderDTO.setOrderAmount(orderInfo.getOrderAmount());//实付金额
            wechatOrderDTO.setPayAmount(orderInfo.getPayAmount());//实付金额
            wechatOrderDTO.setPayStatus(orderInfo.getPayStatus());//支付状态，0为未支付，2为已支付
            wechatOrderDTO.setCarLicense(orderInfo.getCarLicense());//车牌号
            //.设置门店信息
            Shop shopInfo = shopMap.get(orderInfo.getShopId());
            if (shopInfo != null) {
                wechatOrderDTO.setUserGlobalId(shopInfo.getUserGlobalId());//店铺ID
                wechatOrderDTO.setShopName(shopInfo.getName());//店铺名称
                wechatOrderDTO.setShopMobile(shopInfo.getMobile());//店铺手机
                wechatOrderDTO.setShopTel(shopInfo.getTel());//店铺固话
                wechatOrderDTO.setShopAddress(shopInfo.getAddress());//店铺地址
            }
            //.设置物料信息
            List<WechatOrdGoodsDTO> ordGoodsList = ordGoodsListMap.get(orderId);
            if (CollectionUtils.isEmpty(ordGoodsList)) {
                wechatOrderDTO.setGoodsList(new ArrayList<WechatOrdGoodsDTO>());
            } else {
                wechatOrderDTO.setGoodsList(ordGoodsList);
            }
            //.设置服务信息
            List<WechatOrdServiceDTO> ordServiceList = ordServiceListMap.get(orderId);
            if (CollectionUtils.isEmpty(ordServiceList)) {
                wechatOrderDTO.setServiceList(new ArrayList<WechatOrdServiceDTO>());
            } else {
                wechatOrderDTO.setServiceList(ordServiceList);
                //设置工单包含的服务列表名称"、"连接
                StringBuffer categoryNameSBf = new StringBuffer();
                Set<String> categoryNameSet = new HashSet<>();
                for (WechatOrdServiceDTO wechatOrdServiceDTO : ordServiceList) {
                    if (StringUtils.isNotBlank(wechatOrdServiceDTO.getCategoryName())) {
                        categoryNameSet.add(wechatOrdServiceDTO.getCategoryName());
                    }
                }
                for (String s : categoryNameSet) {
                    categoryNameSBf.append(s + "、");
                }
                //去掉最后一个顿号
                if (categoryNameSBf.length() > 0) {
                    String categoryNameStr = categoryNameSBf.substring(0, categoryNameSBf.length() - 1);
                    wechatOrderDTO.setCategoryName(categoryNameStr);//服务类别
                }
            }
            wechatOrderDTOList.add(wechatOrderDTO);
        }
        return com.tqmall.core.common.entity.Result.wrapSuccessfulResult(wechatOrderDTOList);
    }

    /**
     * 获取工单类型信息
     * @param orderInfoList
     * @return
     */
    private ImmutableMap<Long, OrderType> _getOrderType(List<OrderInfo> orderInfoList) {
        Set<Long> orderTypeIds = new HashSet<>();
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderType() != null) {
                orderTypeIds.add(orderInfo.getOrderType());
            }
        }
        Map<String, Object> qryOrderTypeMap = new HashMap<>();
        qryOrderTypeMap.put("ids", orderTypeIds);
        List<OrderType> orderTypeList = orderTypeService.selectNoCache(qryOrderTypeMap);
        ImmutableMap<Long, OrderType> orderTypeMap = ImmutableMap.copyOf(new HashMap<Long, OrderType>());//key:orderId
        if (!CollectionUtils.isEmpty(orderTypeList)) {
            orderTypeMap = Maps.uniqueIndex(orderTypeList, new Function<OrderType, Long>() {
                @Override
                public Long apply(OrderType orderType) {
                    return orderType.getId();
                }
            });
        }
        return orderTypeMap;
    }

    /**
     * 获取工单关联的服务列表
     * @param orderIds
     * @return
     */
    private Map<Long, List<WechatOrdServiceDTO>> _getOrderServices(Set<Long> orderIds) {
        //服务类目信息
        Map<Long, ShopServiceCate> cateMap = shopServiceCateService.dealCateInfo();//key:cateId
        Map<String, Object> qryOrderServiceMap = new HashMap<>();
        qryOrderServiceMap.put("orderIds", orderIds);
        List<OrderServices> orderServicesList = orderServicesService.select(qryOrderServiceMap);
        List<ShopServiceInfo> serviceInfoList = new ArrayList<>();
        Set<Long> serviceIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                Long parentServiceId = orderServices.getParentServiceId();//服务父ID
                Long serviceId = orderServices.getServiceId();//服务ID
                if (null != parentServiceId && parentServiceId > 0l) {
                    serviceIds.add(parentServiceId);
                } else {
                    //如果这个工单服务parentServiceId=0,说明这个是基本服务要服务的一级类目ID
                    serviceIds.add(serviceId);
                }
            }
        }
        if (!CollectionUtils.isEmpty(serviceIds)) {
            serviceInfoList = shopServiceInfoService.selectAllByIds(new ArrayList<>(serviceIds));
        }
        Set<Long> serviceTemplateIds = new HashSet<>();
        ImmutableMap<Long, ShopServiceInfo> shopServiceInfoMap = ImmutableMap.copyOf(new HashMap<Long, ShopServiceInfo>());//key:serviceId
        if (!CollectionUtils.isEmpty(serviceInfoList)) {
            shopServiceInfoMap = Maps.uniqueIndex(serviceInfoList, new Function<ShopServiceInfo, Long>() {
                @Override
                public Long apply(ShopServiceInfo shopServiceInfo) {
                    return shopServiceInfo.getId();
                }
            });
            for (ShopServiceInfo shopServiceInfo : serviceInfoList) {
                //"TQFW"才需要处理服务模版与服务类别关系
                if ("TQFW".equals(shopServiceInfo.getFlags()) && shopServiceInfo.getParentId() != null
                        && shopServiceInfo.getParentId() > 0) {
                    serviceTemplateIds.add(shopServiceInfo.getParentId());
                }
            }
        }
        //.服务模版类别关联信息
        Map<Long, ServiceTemplateCateRel> serviceTemplateCateRelMap =new HashMap();//key:serviceTemplateId
        if (!CollectionUtils.isEmpty(serviceTemplateIds)) {
            Map<String, Object> qryServiceTemplateCateRelMap = new HashMap<>();
            qryServiceTemplateCateRelMap.put("templateIds", serviceTemplateIds);
            List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelService.select(qryServiceTemplateCateRelMap);
            if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                for (ServiceTemplateCateRel serviceTemplateCateRel : serviceTemplateCateRelList) {
                    serviceTemplateCateRelMap.put(serviceTemplateCateRel.getTemplateId(), serviceTemplateCateRel);
                }
            }
        }
        //工单服务根据工单id合并同类项
        Map<Long, List<WechatOrdServiceDTO>> ordServiceListMap = new HashMap<>();//工单关联的服务列表 key:orderId
        if (!CollectionUtils.isEmpty(orderServicesList)) {
            for (OrderServices orderServices : orderServicesList) {
                List<WechatOrdServiceDTO> wechatOrdServiceList = ordServiceListMap.get(orderServices.getOrderId());
                if (wechatOrdServiceList == null) {
                    wechatOrdServiceList = new ArrayList<>();
                }
                WechatOrdServiceDTO wechatOrdServiceDTO = new WechatOrdServiceDTO();
                Long serviceId = orderServices.getServiceId();
                ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(serviceId);
                wechatOrdServiceDTO.setServiceId(serviceId);
                wechatOrdServiceDTO.setServiceName(orderServices.getServiceName());
                wechatOrdServiceDTO.setServicePrice(orderServices.getServicePrice());
                wechatOrdServiceDTO.setServiceHour(orderServices.getServiceHour());
                if (shopServiceInfo != null) {
                    Integer appCateId = shopServiceInfo.getAppCateId();
                    String flags = shopServiceInfo.getFlags();
                    //车主服务取车主服务类别名称
                    if ("CZFW".equals(flags)) {
                        if (null != appCateId && appCateId > 0l) {
                            ShopServiceCate shopServiceCate = cateMap.get(Long.valueOf(appCateId));
                            if (null != shopServiceCate) {
                                wechatOrdServiceDTO.setCategoryName(shopServiceCate.getFirstCateName());//车主服务一级类别名称
                                wechatOrdServiceDTO.setCategoryId(shopServiceCate.getParentId());//车主服务一级类别ID
                            }
                        }
                    } else if ("TQFW".equals(flags)) {
                        //淘汽服务对应的父ID
                        Long parentId = shopServiceInfo.getParentId();
                        //去服务模版和类目对应关系表中获得数据
                        ServiceTemplateCateRel serviceTemplateCateRel = serviceTemplateCateRelMap.get(parentId);
                        if (serviceTemplateCateRel != null) {
                            Long cateId = serviceTemplateCateRel.getCateId();
                            if (cateId != null && cateId > 0) {
                                ShopServiceCate shopServiceCate = cateMap.get(cateId);
                                if (shopServiceCate != null) {
                                    //设置一级类目
                                    wechatOrdServiceDTO.setCategoryId(shopServiceCate.getParentId());
                                    wechatOrdServiceDTO.setCategoryName(shopServiceCate.getFirstCateName());
                                }
                            }
                        }
                    } else {
                        wechatOrdServiceDTO.setCategoryName("门店服务");//不是车主服务类别名称
                        wechatOrdServiceDTO.setCategoryId(0l);//不是车主服务类别ID
                    }
                }
                wechatOrdServiceList.add(wechatOrdServiceDTO);
                ordServiceListMap.put(orderServices.getOrderId(), wechatOrdServiceList);
            }
        }
        return ordServiceListMap;
    }

    /**
     * 获取工单的物料列表
     * @param orderIds
     * @return
     */
    private Map<Long, List<WechatOrdGoodsDTO>> _getOrderGoods(Set<Long> orderIds) {
        List<OrderGoods> orderGoodsList = orderGoodsService.selectByOrderIds(orderIds.toArray(new Long[orderIds.size()]));
        Map<Long, List<WechatOrdGoodsDTO>> ordGoodsListMap = new HashMap<>();//工单关联的物料列表 key:orderId
        //物料信息跟进工单id合并同类项
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            for (OrderGoods orderGoods : orderGoodsList) {
                List<WechatOrdGoodsDTO> wechatOrdGoodsList = ordGoodsListMap.get(orderGoods.getOrderId());
                if (wechatOrdGoodsList == null) {
                    wechatOrdGoodsList = new ArrayList<>();
                }
                WechatOrdGoodsDTO wechatOrdGoodsDTO = new WechatOrdGoodsDTO();
                wechatOrdGoodsDTO.setGoodsId(orderGoods.getGoodsId());
                wechatOrdGoodsDTO.setGoodsName(orderGoods.getGoodsName());
                wechatOrdGoodsDTO.setGoodsNumber(orderGoods.getGoodsNumberInt());
                wechatOrdGoodsDTO.setGoodsPrice(orderGoods.getGoodsPrice());
                wechatOrdGoodsList.add(wechatOrdGoodsDTO);
                ordGoodsListMap.put(orderGoods.getOrderId(), wechatOrdGoodsList);
            }
        }
        return ordGoodsListMap;
    }

    /**
     * 获取工单关联的门店信息,若工单是根据门店查询的,直接返回当前的门店信息shop,否则根据shopIds查询门店
     * @param shop    查询工单时有门店信息时此值不为空,直接包装成map返回即可
     * @param shopIds
     * @return
     */
    private Map<Long, Shop> _getOrderShopInfo(Shop shop, Set<Long> shopIds) {
        Map<Long, Shop> shopMap = new HashMap<>();
        if (shop != null) {
            shopMap.put(shop.getId(), shop);
        } else {
            Map<String, Object> qryShopMap = new HashMap<>();
            qryShopMap.put("shopIds", shopIds);
            List<Shop> shopList = shopService.select(qryShopMap);
            if (!CollectionUtils.isEmpty(shopList)) {
                shopMap = Maps.uniqueIndex(shopList, new Function<Shop, Long>() {
                    @Override
                    public Long apply(Shop shop) {
                        return shop.getId();
                    }
                });
            }
        }
        return shopMap;
    }
}
