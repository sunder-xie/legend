package com.tqmall.legend.facade.magic.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.facade.magic.ProxyFacade;
import com.tqmall.legend.facade.magic.vo.ProxyOrderDetailVo;
import com.tqmall.legend.facade.magic.vo.ProxyOrderInfoVo;
import com.tqmall.legend.facade.magic.vo.ProxyServicesVo;
import com.tqmall.legend.facade.order.OrderInvalidFacade;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import com.tqmall.legend.facade.settlement.ConfirmBillFacade;
import com.tqmall.magic.object.param.proxy.ProxyInfo;
import com.tqmall.magic.object.param.proxy.ProxyPageParam;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.object.result.proxyServices.ProxyServicesDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shulin on 16/5/11.
 */
@Service
@Slf4j
public class ProxyFacadeImpl implements ProxyFacade {
    private static final String DUBBO = "【调用DUBBO接口-ProxyFacadeImpl】";
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderInvalidFacade orderInvalidFacade;
    @Autowired
    private ConfirmBillFacade confirmBillFacade;

    @Override
    public com.tqmall.zenith.errorcode.support.Result addProxyOrder(ProxyInfo proxyInfo) {
        com.tqmall.core.common.entity.Result resultTemp = null;

        com.tqmall.zenith.errorcode.support.Result result = new com.tqmall.zenith.errorcode.support.Result();
        try {
            resultTemp = rpcProxyService.addProxy(proxyInfo);
            return result.wrapSuccessfulResult(resultTemp.getData());
        } catch (Exception e) {
            log.error("添加委托单失败", e);
            return LegendErrorCode.ADD_PROXY_ORDER_ERROR.newResult();
        }
    }

    @Override
    public com.tqmall.zenith.errorcode.support.Result updateProxyOrder(ProxyInfo proxyInfo) {
        com.tqmall.core.common.entity.Result resultTemp = null;
        com.tqmall.zenith.errorcode.support.Result result = new com.tqmall.zenith.errorcode.support.Result();
        try {
            resultTemp = rpcProxyService.updateProxy(proxyInfo);
            return result.wrapSuccessfulResult(resultTemp.getData());
        } catch (Exception e) {
            log.error("更新委托单服务失败", e);
            return LegendErrorCode.UPDATE_PROXY_SERVICES_ERROR.newResult();
        }
    }

    @Override
    public Result acceptProxyOrder(Long proxyOrderId, String receiveName, Long receiver, String remark) {
        if (null == proxyOrderId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{}接委托单操作 开始，委托单ID={}", DUBBO, proxyOrderId);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.acceptProxyOrder(proxyOrderId, receiveName, receiver, remark);
            log.info("{}接委托单操作 结束", DUBBO);
            if (resultTemp.isSuccess()) {
                return Result.wrapSuccessfulResult("");
            } else {
                return Result.wrapErrorResult(resultTemp.getCode(), resultTemp.getMessage());
            }
        } catch (Exception e) {
            log.error("接受委托单操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.ACCEPT_PROXY_ORDER_ERROR.getCode(), LegendErrorCode.ACCEPT_PROXY_ORDER_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result backCar(Long proxyOrderId, UserInfo userInfo) {
        if (null == proxyOrderId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{}交车操作 开始，委托单ID={}", DUBBO, proxyOrderId);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.backCar(proxyOrderId);
            log.info("{}交车操作 结束", DUBBO);
            if (resultTemp.isSuccess()) {

                //交完车之后需要对应的工单已挂账
                Result result = confirmBillFacade.shareConfirmBill(proxyOrderId, userInfo);
                return result;
            } else {
                return Result.wrapErrorResult(resultTemp.getCode(), resultTemp.getMessage());
            }
        } catch (Exception e) {
            log.error("交车操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.BACK_CAR_ERROR.getCode(), LegendErrorCode.BACK_CAR_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result clearProxyOrder(Long proxyOrderId) {
        if (null == proxyOrderId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{} 委托单结清操作 开始，委托单ID={}", DUBBO, proxyOrderId);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.clearProxy(proxyOrderId);
            log.info("{} 委托单结清操作 结束", DUBBO);
            if (resultTemp.isSuccess()) {
                return Result.wrapSuccessfulResult("");
            } else {
                return Result.wrapErrorResult(resultTemp.getCode(), resultTemp.getMessage());
            }
        } catch (Exception e) {
            log.error("结清委托单操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.CLEAR_PROXY_ORDER_ERROR.getCode(), LegendErrorCode.CLEAR_PROXY_ORDER_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result clearAllProxyOrder(Long[] ids) {
        return null;
    }


    @Override
    public Result cancelProxy(Long proxyOrderId) {
        if (null == proxyOrderId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{} 委托单取消操作 开始，委托单ID={}", DUBBO, proxyOrderId);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.cancelProxyOrder(proxyOrderId);
            log.info("{} 委托单取消操作 结束", DUBBO);
            if (resultTemp.isSuccess()) {
                //此时委托单已取消委托了

                Long workId = (Long) resultTemp.getData();
                log.info("{} 委托单取消成功！开始取消工单操作，工单ID={}", DUBBO, workId);
                if (0 == workId) {
                    log.error("{} 委托单（{}）没有转工单，取消成功！", DUBBO, proxyOrderId);
                    return Result.wrapSuccessfulResult("委托单取消成功");
                }
                log.info("{} 委托单取消操作，返回的委托单转工单ID={}", DUBBO, workId);
                boolean proxyInvalid = orderInvalidFacade.proxyInvalid(workId);
                if (proxyInvalid) {
                    return Result.wrapSuccessfulResult("委托单取消成功！");
                } else {
                    log.error("{} 委托单取消成功，但是对应工单取消失败！需要取消的工单ID={}", DUBBO, workId);
                    return Result.wrapErrorResult(LegendErrorCode.CANCEL_PROXY_ORDER_ERROR.getCode(), LegendErrorCode.CANCEL_PROXY_ORDER_ERROR.getErrorMessage());
                }
            } else {
                return Result.wrapErrorResult(resultTemp.getCode(), "无效操作失败！");
            }
        } catch (Exception e) {
            log.error("取消委托单操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.CANCEL_PROXY_ORDER_ERROR.getCode(), LegendErrorCode.CANCEL_PROXY_ORDER_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result<ProxyOrderDetailVo> getProxyOrderDetail(Long shopId, Long proxyOrderId) {
        if (null == proxyOrderId || null == shopId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{}获取委托单详情 开始，委托单ID={}", DUBBO, proxyOrderId);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.getProxyInfoById(proxyOrderId);
            log.info("{}获取委托单详情 结束", DUBBO);
            ProxyDTO proxyDTO = (ProxyDTO) resultTemp.getData();

            if (shopId.equals(proxyDTO.getProxyShopId()) || shopId.equals(proxyDTO.getShopId())) {
                ProxyOrderDetailVo proxyOrderDetailVo = new ProxyOrderDetailVo();
                BeanUtils.copyProperties(proxyDTO, proxyOrderDetailVo);
                List<ProxyServicesDTO> proxyServicesDTOList = proxyDTO.getProxyServicesDTOList();
                List<ProxyServicesVo> proxyServicesVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(proxyServicesDTOList)) {
                    for (ProxyServicesDTO proxyServicesDTO : proxyServicesDTOList) {
                        ProxyServicesVo proxyServicesVo = new ProxyServicesVo();
                        BeanUtils.copyProperties(proxyServicesDTO, proxyServicesVo);
                        proxyServicesVoList.add(proxyServicesVo);
                    }
                }
                proxyOrderDetailVo.setProxyServicesVoList(proxyServicesVoList);
                return Result.wrapSuccessfulResult(proxyOrderDetailVo);

            }
            return Result.wrapErrorResult("", "当前门店无权查看委托单详情！");

        } catch (Exception e) {
            log.error("获取委托单详情操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.GET_PROXY_DETAIL_ERROR.getCode(), LegendErrorCode.GET_PROXY_DETAIL_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result<List<ProxyOrderInfoVo>> getAllProxyOrderByShopId(Long shopId) {
        if (null == shopId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        return null;
    }

    @Override
    public Result<DefaultPage<ProxyOrderInfoVo>> getAuthorizedProxyList(Long shopId, Integer pageNum, Integer pageSize) {
        if (null == shopId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        try {
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.getAuthorizedProxyList(shopId, pageNum, pageSize);
            PageEntityDTO<ProxyDTO> pageEntityDTO = (PageEntityDTO<ProxyDTO>) resultTemp.getData();

            DefaultPage<ProxyOrderInfoVo> proxyOrderInfoVos = null;
            Pageable pageable = new PageRequest(pageNum, pageSize);

            List<ProxyDTO> proxyDTOList = pageEntityDTO.getRecordList();
            List<ProxyOrderInfoVo> proxyOrderInfoVoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(proxyDTOList)) {
                for (ProxyDTO proxyDTO : proxyDTOList) {
                    ProxyOrderInfoVo proxyOrderInfoVo = new ProxyOrderInfoVo();
                    BeanUtils.copyProperties(proxyDTO, proxyOrderInfoVo);
                    proxyOrderInfoVoList.add(proxyOrderInfoVo);
                }
            }
            proxyOrderInfoVos = new DefaultPage<ProxyOrderInfoVo>(proxyOrderInfoVoList, pageable, pageEntityDTO.getTotalNum());

            return Result.wrapSuccessfulResult(proxyOrderInfoVos);


        } catch (Exception e) {
            log.error("分页获取门店作为委托方的相关委托单列表 操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.GET_PROXY_LIST_ERROR.getCode(), LegendErrorCode.GET_PROXY_LIST_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result<DefaultPage<ProxyOrderInfoVo>> getTrusteeProxyList(Long shopId, Integer pageNum, Integer pageSize) {
        if (null == shopId) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        try {
            log.info("{} 调用DUUBO接口获取门店作为受托方的委托单列表，shopId={},pageNum={},pageSize={}", DUBBO, shopId, pageNum, pageSize);
            com.tqmall.core.common.entity.Result resultTemp = rpcProxyService.getTrusteeProxyList(shopId, pageNum, pageSize);
            PageEntityDTO<ProxyDTO> pageEntityDTO = (PageEntityDTO<ProxyDTO>) resultTemp.getData();
            DefaultPage<ProxyOrderInfoVo> proxyOrderInfoVos = null;
            Pageable pageable = new PageRequest(pageNum, pageSize);

            List<ProxyDTO> proxyDTOList = pageEntityDTO.getRecordList();
            List<ProxyOrderInfoVo> proxyOrderInfoVoList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(proxyDTOList)) {
                for (ProxyDTO proxyDTO : proxyDTOList) {
                    ProxyOrderInfoVo proxyOrderInfoVo = new ProxyOrderInfoVo();
                    BeanUtils.copyProperties(proxyDTO, proxyOrderInfoVo);
                    proxyOrderInfoVoList.add(proxyOrderInfoVo);
                }
            }
            proxyOrderInfoVos = new DefaultPage<ProxyOrderInfoVo>(proxyOrderInfoVoList, pageable, pageEntityDTO.getTotalNum());

            return Result.wrapSuccessfulResult(proxyOrderInfoVos);
        } catch (Exception e) {
            log.error("分页获取门店作为受托方的相关委托单列表 操作失败！", e);
            return Result.wrapErrorResult(LegendErrorCode.GET_PROXY_LIST_ERROR.getCode(), LegendErrorCode.GET_PROXY_LIST_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result<DefaultPage<ProxyOrderInfoVo>> getProxyListByParam(ProxyPageParam proxyPageParam) {
        if (null == proxyPageParam) {
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        try {
            log.info("{} 根据参数搜索委托单列表，参数是:{}", DUBBO, JSONUtil.object2Json(proxyPageParam));
            com.tqmall.core.common.entity.Result<PageEntityDTO<ProxyDTO>> result = rpcProxyService.getProxyListByPage(proxyPageParam);
            if (result.isSuccess()) {
                PageEntityDTO<ProxyDTO> pageEntityDTO = (PageEntityDTO<ProxyDTO>) result.getData();
                DefaultPage<ProxyOrderInfoVo> proxyOrderInfoVos = null;
                Pageable pageable = new PageRequest(proxyPageParam.getPageNum(), proxyPageParam.getPageSize());
                List<ProxyDTO> proxyDTOList = pageEntityDTO.getRecordList();
                List<ProxyOrderInfoVo> proxyOrderInfoVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(proxyDTOList)) {
                    for (ProxyDTO proxyDTO : proxyDTOList) {
                        ProxyOrderInfoVo proxyOrderInfoVo = new ProxyOrderInfoVo();
                        BeanUtils.copyProperties(proxyDTO, proxyOrderInfoVo);
                        proxyOrderInfoVoList.add(proxyOrderInfoVo);
                    }
                }
                proxyOrderInfoVos = new DefaultPage<ProxyOrderInfoVo>(proxyOrderInfoVoList, pageable, pageEntityDTO.getTotalNum());
                return Result.wrapSuccessfulResult(proxyOrderInfoVos);
            } else {
                log.info("{}，未获取到对应的委托单信息！", DUBBO);
                return Result.wrapErrorResult(LegendErrorCode.GET_NULL_PROXY_LIST.getCode(), LegendErrorCode.GET_PROXY_LIST_ERROR.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("{}，根据条件获取委托单列表异常，e={}", DUBBO, e);
            return Result.wrapErrorResult(LegendErrorCode.GET_PROXY_LIST_ERROR.getCode(), LegendErrorCode.GET_PROXY_LIST_ERROR.getErrorMessage());
        }
    }

    @Override
    public Result<List<OrderServicesVo>> getNotProxyService(Long orderId, Long shopId) {
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (!orderInfoOptional.isPresent()) {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_NOT_FIND_EX.getCode(), LegendErrorCode.ORDER_NOT_FIND_EX.getErrorMessage());
        }
        //获取工单服务数据
        List<OrderServices> orderServicesList = orderServicesService.queryOrderServiceList(orderId, shopId);
        if (CollectionUtils.isEmpty(orderServicesList)) {
            return Result.wrapErrorResult(LegendErrorCode.ORDER_SERVICE_NULL_EX.getCode(), LegendErrorCode.ORDER_SERVICE_NULL_EX.getErrorMessage());
        }
        //组装服务数据
        List<OrderServicesVo> orderServicesVoList = Lists.newArrayList();//返回的服务对象list
        List<Long> serviceIdsList = Lists.newArrayList();//需要查询的门店服务ids
        List<ProxyServicesDTO> proxyServicesDTODeleteList = Lists.newArrayList();//需要过滤的list
        Map<Long, ProxyServicesVo> proxyServicesVoDeleteMap = Maps.newHashMap();
        try {

            //过滤已委托的服务
            ProxyParam proxyParam = new ProxyParam();
            proxyParam.setSource(Constants.CUST_SOURCE);
            proxyParam.setOrderId(orderId);//委托单工单id
            com.tqmall.core.common.entity.Result<List<ProxyDTO>> proxyResult = rpcProxyService.showProxyList(proxyParam);
            if (proxyResult.isSuccess()) {
                List<ProxyDTO> proxyDTOList = proxyResult.getData();
                for (ProxyDTO proxyDTO : proxyDTOList) {
                    String proxyStatus = proxyDTO.getProxyStatus();
                    if (!ProxyStatusEnum.YQX.getCode().equals(proxyStatus)) {
                        Long id = proxyDTO.getId();
                        com.tqmall.core.common.entity.Result<List<ProxyServicesDTO>> proxyServicesDTOResult = rpcProxyService.getProxyServicesByOrderId(id);
                        if (proxyServicesDTOResult.isSuccess()) {
                            List<ProxyServicesDTO> proxyServicesDTOList = proxyServicesDTOResult.getData();
                            proxyServicesDTODeleteList.addAll(proxyServicesDTOList);
                        }
                    }
                }
                for (ProxyServicesDTO proxyServicesDTO : proxyServicesDTODeleteList) {
                    Long serviceId = proxyServicesDTO.getServiceId();
                    proxyServicesVoDeleteMap.put(serviceId, null);
                }
            }
            for (OrderServices orderServices : orderServicesList) {
                Long serviceId = orderServices.getServiceId();
                //过滤已经委托了的服务
                if (!proxyServicesVoDeleteMap.containsKey(serviceId)) {
                    serviceIdsList.add(serviceId);
                }
            }
        } catch (BeansException e) {
            log.error("【工单转委托单编辑】：工单服务转vo对象copy异常", e);
            return Result.wrapErrorResult(LegendErrorCode.ORDER_COPY_EX.getCode(), "系统繁忙，请稍后再试");
        } catch (Exception e) {
            log.error("【工单转委托单编辑】：出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_PRC_EX.getCode(), "系统繁忙，请稍后再试");
        }
        //组装服务
        if (CollectionUtils.isEmpty(serviceIdsList)) {
            return Result.wrapSuccessfulResult(orderServicesVoList);
        }
        //普通服务也能委托
        Map<String, Object> serviceMap = Maps.newHashMap();
        serviceMap.put("ids", serviceIdsList);
        serviceMap.put("shopId", shopId);
        List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.select(serviceMap);
        Map<Long, ShopServiceInfo> shopServiceInfoMap = Maps.newHashMap();
        for (ShopServiceInfo shopServiceInfo : shopServiceInfoList) {
            shopServiceInfoMap.put(shopServiceInfo.getId(), shopServiceInfo);
        }
        try {
            for (OrderServices orderServices : orderServicesList) {
                Long serviceId = orderServices.getServiceId();
                //添加未委托的服务
                if (!proxyServicesVoDeleteMap.containsKey(serviceId) && shopServiceInfoMap.containsKey(serviceId)) {
                    OrderServicesVo orderServicesVo = new OrderServicesVo();
                    BeanUtils.copyProperties(orderServices, orderServicesVo);

                    ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(serviceId);
                    orderServicesVo.setFlags(shopServiceInfo.getFlags());
                    orderServicesVo.setSharePrice(shopServiceInfo.getSharePrice());
                    orderServicesVo.setSurfaceNum(shopServiceInfo.getSurfaceNum());

                    orderServicesVoList.add(orderServicesVo);
                }
            }
        } catch (BeansException e) {
            log.error("【工单转委托单编辑】：工单服务转vo对象或门店服务转vo对象copy异常", e);
            return Result.wrapErrorResult(LegendErrorCode.ORDER_COPY_EX.getCode(), "系统繁忙，请稍后再试");
        }
        return Result.wrapSuccessfulResult(orderServicesVoList);
    }

    /**
     * 同步委托单状态
     *
     * @param orderId
     * @param shopId
     * @param orderStatus
     */
    @Override
    public void updateProxyOrder(Long orderId, Long shopId, String orderStatus, String proxyStatus) {
        try {
            //查询委托单
            com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = rpcProxyService.getProxyInfoByShopIdAndProxyOrderId(shopId, orderId);
            if (!proxyDTOResult.isSuccess()) {
                return;
            }
            ProxyDTO proxyDTO = proxyDTOResult.getData();
            proxyDTO.setOrderStatus(orderStatus);
            proxyDTO.setProxyStatus(proxyStatus);
            //同步状态
            rpcProxyService.updateProxyOrder(proxyDTO);
        } catch (Exception e) {
            StringBuffer error = new StringBuffer();
            error.append("【工单同步委托单状态】：出现异常，工单id：");
            error.append(orderId);
            error.append("，委托单id：");
            error.append(shopId);
            error.append("，同步状态：");
            error.append(orderStatus);
            log.error(error.toString(), e);
            throw new BizException("工单同步委托单状态出现异常");
        }
    }

}
