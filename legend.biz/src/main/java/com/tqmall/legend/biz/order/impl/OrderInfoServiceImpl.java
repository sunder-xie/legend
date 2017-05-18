package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitAndRedBillDTO;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.billcenter.client.enums.DebitTypeEnum;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.warehouse.WarehouseOutService;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.order.OrderServicesDao;
import com.tqmall.legend.dao.shop.ServiceTemplateCateRelDao;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.shop.ShopServiceInfoDao;
import com.tqmall.legend.entity.order.*;
import com.tqmall.legend.entity.pub.order.OrderEvaluateVo;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by lixiao on 14-10-28.
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends BaseServiceImpl implements OrderInfoService {

    public static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderServicesDao orderServicesDao;
    @Autowired
    private ShopServiceInfoDao shopServiceInfoDao;
    @Autowired
    private ServiceTemplateCateRelDao serviceTemplateCateRelDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private WarehouseOutService warehouseOutService;
    @Autowired
    private RpcDebitBillService rpcDebitBillService;

    @Override
    public List<OrderInfo> select(Map<String, Object> searchMap) {
        return orderInfoDao.select(searchMap);
    }

    @Override
    public OrderInfo selectById(Long id, Long shopId) {
        if (id == null || shopId == null || id <= 0) {
            return null;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("shopId", shopId);
        List<OrderInfo> orderInfoList = orderInfoDao.select(param);
        if (CollectionUtils.isEmpty(orderInfoList)) {
            return null;
        }
        return orderInfoList.get(0);
    }

    @Override
    public OrderInfo selectById(Long id) {
        return orderInfoDao.selectById(id);
    }

    @Override
    public List<OrderInfo> selectByContactMobileAndLicense(Map<String, Object> searchMap) {
        return orderInfoDao.selectByContactMobileAndLicense(searchMap);
    }

    @Override
    public Integer selectCount(Map<String, Object> searchMap) {
        return orderInfoDao.selectCount(searchMap);
    }

    @Override
    public Page<OrderInfo> getOrderInfoPage(Pageable pageable, Map<String, Object> searchParams) {
        Page<OrderInfo> page = super.getPage(orderInfoDao, pageable, searchParams);
        return page;
    }

    /**
     * create by jason 2015-09-18
     * 获取dayNumMin到dayNumMax的结算过的工单
     *
     * @param mobileList 工单ID
     * @param dayNumMin  天数
     * @param dayNumMax  天数
     */
    @Override
    public List<OrderEvaluateVo> selectByMobileAndDayNum(List<String> mobileList, Integer dayNumMin, Integer
            dayNumMax) {

        LOGGER.info("mobileList:{},dayNumMin:{},dayNumMax:{}", mobileList, dayNumMin, dayNumMax);

        List<OrderEvaluateVo> resultList = null;
        try {
            //endTime = 当前时间-dayNumMin天
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dayNumMin);
            Date endTime = calendar.getTime();

            Map map = new HashMap();
            map.put("contactMobiles", mobileList);//联系人手机号list
            map.put("orderStatus", "DDYFK");//DDYFK 工单已付款
            map.put("payEndTime", endTime);//结算时间<=startTime
            if (null != dayNumMax) {
                //startTime = 当前时间-dayNumMax天
                calendar.setTime(now);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dayNumMax);
                Date startTime = calendar.getTime();
                map.put("payStartTime", startTime);//结算时间>=startTime
            }

            //获得工单数据
            List<OrderInfo> orderInfoList = orderInfoDao.select(map);
            //返回List对象
            resultList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(orderInfoList)) {
                //组装车主一级二级服务类别
                Map<Long, ShopServiceCate> cateMap = shopServiceCateService.dealCateInfo();

                for (OrderInfo orderInfo : orderInfoList) {
                    //工单数据对象
                    OrderEvaluateVo orderEvaluateVo = new OrderEvaluateVo();

                    Long orderId = orderInfo.getId();
                    map.clear();
                    map.put("orderId", orderId);
                    map.put("type", 1);//type=1的服务
                    //获得工单对应的服务
                    List<OrderServices> orderServicesList = orderServicesDao.select(map);
                    //存放服务一级ID和名称
                    List<ServiceCateVo> serviceCateVoList = new ArrayList<>();

                    if (!CollectionUtils.isEmpty(orderServicesList)) {
                        for (OrderServices orderServices : orderServicesList) {
                            //服务类别对象
                            ServiceCateVo serviceCateVo = new ServiceCateVo();
                            //服务父ID
                            Long parentServiceId = orderServices.getParentServiceId();
                            //服务ID
                            Long serviceId = orderServices.getServiceId();
                            List<ShopServiceInfo> shopServiceInfoList;
                            Long[] serviceIds = new Long[1];
                            if (null != parentServiceId && parentServiceId > 0l) {
                                serviceIds[0] = parentServiceId;
                            } else {
                                //如果这个工单服务不存在parentServiceId,说明这个是基本服务要服务的一级类目ID
                                serviceIds[0] = serviceId;
                            }
                            shopServiceInfoList = shopServiceInfoDao.selectAllByIds(serviceIds);
                            if (!CollectionUtils.isEmpty(shopServiceInfoList)) {
                                ShopServiceInfo shopServiceInfo = shopServiceInfoList.get(0);
                                if (null != shopServiceInfo) {
                                    Integer appCateId = shopServiceInfo.getAppCateId();
                                    String flags = shopServiceInfo.getFlags();
                                    //车主服务取车主服务类别名称
                                    if ("CZFW".equals(flags)) {
                                        if (null != appCateId && appCateId > 0l) {
                                            ShopServiceCate shopServiceCate = cateMap.get(Long.valueOf(appCateId));
                                            if (null != shopServiceCate) {
                                                serviceCateVo.setId(shopServiceCate.getParentId());//车主服务一级类别ID
                                                serviceCateVo.setName(shopServiceCate.getFirstCateName());//车主服务一级类别名称
                                            }
                                        }
                                    } else if ("TQFW".equals(flags)) {
                                        //淘汽服务对应的父ID
                                        Long parentId = shopServiceInfo.getParentId();
                                        if (null != parentId && parentId > 0l) {
                                            Map searchMap = new HashMap();
                                            searchMap.put("templateId", parentId);
                                            //去服务和类目对应关系表中获得数据 一个淘汽服务可能对应多个类目ID 随机取一个
                                            List<ServiceTemplateCateRel> serviceTemplateCateRelList = serviceTemplateCateRelDao.select(searchMap);

                                            if (!CollectionUtils.isEmpty(serviceTemplateCateRelList)) {
                                                Long cateId = serviceTemplateCateRelList.get(0).getCateId();
                                                if (null != cateId && cateId > 0l) {

                                                    //把一级类目ID set到ShopServiceInfo中
                                                    serviceCateVo.setId(cateMap.get(cateId).getParentId());
                                                    //把一级类目名称 set到ShopServiceInfo中
                                                    serviceCateVo.setName(cateMap.get(cateId).getFirstCateName());
                                                }
                                            }
                                        }
                                    } else {
                                        serviceCateVo.setId(0l);//不是车主服务类别ID
                                        serviceCateVo.setName("门店服务");//不是车主服务类别名称
                                    }
                                }
                            }
                            serviceCateVoList.add(serviceCateVo);
                        }
                    }
                    orderEvaluateVo.setOrderId(orderId);//工单ID
                    orderEvaluateVo.setOrderTag(orderInfo.getOrderTag());//工单类别
                    orderEvaluateVo.setLicense(orderInfo.getCarLicense());//车牌
                    Shop shop = shopDao.selectById(orderInfo.getShopId());
                    if (null != shop) {
                        orderEvaluateVo.setUserGlobalId(shop.getUserGlobalId());
                    }
                    orderEvaluateVo.setList(serviceCateVoList);//一级服务类别List

                    resultList.add(orderEvaluateVo);
                }

            }
        } catch (Exception e) {
            LOGGER.info("获取7天之外的结算过的工单数据异常!" + e.toString());
        }
        return resultList;
    }

    @Override
    public List<Long> selectReceiverByShopId(Long shopId) {
        return orderInfoDao.selectReceiverByShopId(shopId);
    }

    /**
     * 从 billcenter 获取工单收款金额
     *
     * @param shopId
     * @param orderList
     */
    public List<OrderInfo> getOrderDebitAmountByBillcenter(Long shopId, List<OrderInfo> orderList) {
        if (CollectionUtils.isEmpty(orderList)) {
            return orderList;
        }
        HashSet<Long> orderIdsSet = new HashSet<>();
        for (OrderInfo orderInfo : orderList) {
            if (orderInfo.getOrderStatus().equals(OrderStatusEnum.DDYFK.getKey())) {
                orderIdsSet.add(orderInfo.getId());
            }
        }
        Result<DebitAndRedBillDTO> result = rpcDebitBillService.findBillListByRelIds(shopId, DebitTypeEnum.ORDER.getId(), orderIdsSet, false);
        if (result.isSuccess()) {
            DebitAndRedBillDTO debitAndRedBillDTO = result.getData();
            if (debitAndRedBillDTO == null) {
                return orderList;
            }
            List<DebitBillDTO> debitBillDTOList = debitAndRedBillDTO.getDebitBillDTOList();
            Map<Long, DebitBillDTO> debitBillMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(debitBillDTOList)) {
                for (DebitBillDTO debitBillDTO : debitBillDTOList) {
                    debitBillMap.put(debitBillDTO.getRelId(), debitBillDTO);
                }
            }
            for (OrderInfo orderInfo : orderList) {
                if (debitBillMap.containsKey(orderInfo.getId())) {
                    DebitBillDTO debitBillDTO = debitBillMap.get(orderInfo.getId());
                    //应收金额
                    orderInfo.setPayAmount(debitBillDTO.getReceivableAmount());
                    //实收金额
                    orderInfo.setPayedAmount(debitBillDTO.getPaidAmount());
                    //挂账金额
                    orderInfo.setSignAmount(debitBillDTO.getSignAmount());
                    //坏账金额
                    orderInfo.setBadAmount(debitBillDTO.getBadAmount());
                }
            }
        }
        return orderList;
    }

    @Override
    public List<OrderInfo> selectByIdsAndShopId(Long shopId, List<Long> idList) {
        return orderInfoDao.selectByIdsAndShopId(idList, shopId);
    }
    

    /**
     * 更新工单回访状态为已回访
     *
     * @param shopId
     * @param orderId
     * @return
     */
    @Override
    public int updateOrderVisit(Long shopId, Long orderId) {
        return orderInfoDao.updateOrderVisit(shopId, orderId);
    }

    @Override
    public Map<Long, OrderInfo> selectMapByIds(Collection<Long> orderIds) {
        HashMap<Long, OrderInfo> orderInfoMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(orderIds)) {
            return orderInfoMap;
        }
        Long[] orderIdArray = orderIds.toArray(new Long[orderIds.size()]);
        List<OrderInfo> orderInfos = orderInfoDao.selectByIds(orderIdArray);
        for (OrderInfo orderInfo : orderInfos) {
            orderInfoMap.put(orderInfo.getId(), orderInfo);
        }
        return orderInfoMap;
    }

    /**
     * 查询未回访工单
     *
     * @param shopId
     * @param confirmTimeGt
     * @param confirmTimeLt
     * @return
     */
    @Override
    public List<OrderInfo> findUnVisitList(Long shopId, Date confirmTimeGt, Date confirmTimeLt) {
        return orderInfoDao.findUnVisitList(shopId, confirmTimeGt, confirmTimeLt);
    }

    /**
     * 更新工单
     *
     * @param orderInfo
     * @return
     */
    @Override
    public int updateById(OrderInfo orderInfo) {
        return orderInfoDao.updateById(orderInfo);
    }
}
