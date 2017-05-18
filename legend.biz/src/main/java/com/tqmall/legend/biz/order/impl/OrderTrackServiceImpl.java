package com.tqmall.legend.biz.order.impl;

import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.message.MQPushMessageService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.order.OrderTrackDao;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.entity.order.OrderTrack;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.enums.order.OrderProxyTypeEnum;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.magic.object.param.proxy.ProxyParam;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.service.proxy.RpcProxyService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工单状态扭转
 */
@Service
@Slf4j
public class OrderTrackServiceImpl extends BaseServiceImpl implements OrderTrackService {

    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderTrackDao orderTrackDao;
    @Autowired
    private MQPushMessageService mqPushMessageService;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;
    @Autowired
    private ShopFunFacade shopFunFacade;


    /**
     * TODO 梳理入参,去掉多余orderSn
     * 分配订单
     *
     * @param orderId
     * @param orderSn
     * @param orderStatus
     * @param userInfo
     * @return
     */
    @Override
    public Result tasking(Long orderId, String orderSn, String orderStatus, UserInfo userInfo) {
        OrderInfo orderInfo = super.selectByIdAndShopId(orderInfoDao, orderId, userInfo.getShopId());
        String srcOrderStataus = orderInfo.getOrderStatus();
        if (srcOrderStataus.equals(OrderStatusEnum.DDYFK.getKey()) ||
                srcOrderStataus.equals(OrderStatusEnum.WXDD.getKey())) {
            return Result.wrapErrorResult("", "该工单状态不能进行派工操作,请刷新页面后重试");
        }
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setModifier(userInfo.getUserId());
        Result result = track(orderInfo, userInfo);
        return result;

    }


    /**
     * 工单完工
     *
     * @param orderId
     * @param orderSn
     * @param orderStatus
     * @param userInfo
     * @return
     */
    @Transactional
    public Result finish(Long orderId, String orderSn, String orderStatus,
                         UserInfo userInfo) {
        OrderInfo orderInfo = super.selectByIdAndShopId(orderInfoDao, orderId, userInfo.getShopId());
        String srcOrderStatus = orderInfo.getOrderStatus();
        if (srcOrderStatus.equals(OrderStatusEnum.DDYFK.getKey()) ||
                srcOrderStatus.equals(OrderStatusEnum.WXDD.getKey())) {
            return Result.wrapErrorResult("", "该工单状态不能进行完工操作,请刷新页面后重试");
        }
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setModifier(userInfo.getUserId());
        //设置完工时间
        orderInfo.setFinishTime(new Date());



        Result result = track(orderInfo, userInfo);
        if (result.isSuccess()) {
            //添加完工的工单信息到customer_feedback表
            //customerFeedbackService.addFinishOrder(orderInfo.getId(), userInfo);
            try {
                //push msg to 2C-APP
                mqPushMessageService.pushMsgToApp(Constants.ORDER_FINISH, orderInfo);
            } catch (Exception e) {
                log.error("工单完工,推送消息到2C-APP异常!错误信息:{}", e);
            }


        }


        return result;
    }


    /**
     * 工单报价
     *
     * @param orderId
     * @param orderSn
     * @return
     */
    @Override
    public Result pricing(Long orderId, String orderSn, String orderStatus, UserInfo userInfo) {
        OrderInfo orderInfo = super.selectByIdAndShopId(orderInfoDao, orderId, userInfo.getShopId());
        String srcOrderStatas = orderInfo.getOrderStatus();
        if (srcOrderStatas.equals(OrderStatusEnum.DDYFK.getKey()) ||
                srcOrderStatas.equals(OrderStatusEnum.WXDD.getKey())) {
            return Result.wrapErrorResult("", "该工单状态不能进行报价操作");
        }
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setModifier(userInfo.getUserId());
        Result result = track(orderInfo, userInfo);
        return result;
    }

    /**
     * 工单状态变更记录
     *
     * @param orderInfo
     * @param userInfo
     * @return
     */
    @Transactional
    public Result track(OrderInfo orderInfo, UserInfo userInfo) {

        // 无效工单
        if (orderInfo == null) {
            return Result.wrapErrorResult("", "工单不存在，记录工单追踪信息失败！");
        }
        // 工单ID
        Long orderId = orderInfo.getId();
        if (orderId == null || orderId < 0) {
            return Result.wrapErrorResult("", "工单不存在，记录工单追踪信息失败！");
        }

        // 工单编号
        String orderSn = orderInfo.getOrderSn();
        // 操作人ID
        Long userId = userInfo.getUserId();
        // 操作人姓名
        String userName = userInfo.getName();
        // 店铺ID
        Long shopId = userInfo.getShopId();
        // 工单状态(默认为：创建订单状态)
        String orderStatus = OrderStatusEnum.CJDD.getKey();
        // 上一个操作人(默认为：0)
        Long preManager = 0l;
        // 上一个状态(默认为："")
        String preOrderStatus = "";

        // 判读是新建的工单、已存在的工单
        // TODO refactor 工单信息重复更新，梳理业务，移除掉
        orderStatus = orderInfo.getOrderStatus();
        orderStatus = (orderStatus == null) ? OrderStatusEnum.CJDD.getKey() : orderStatus;
        try {
            orderInfoDao.updateById(orderInfo);
        } catch (Exception e) {
            log.error("记录工单追踪信息时，更新工单信息异常，异常信息:{}", e);
            throw new RuntimeException("记录工单追踪信息失败!");
        }
        //回写委托单
        writeBackProxy(orderInfo);


        // wrapper order track
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderId(orderId);
        orderTrack.setOrderSn(orderSn);
        orderTrack.setOrderStatus(orderStatus);
        orderTrack.setShopId(shopId);
        orderTrack.setCreator(userId);
        orderTrack.setModifier(userId);
        orderTrack.setOperatorName(userName);
        orderTrack.setPreManager((preManager == null) ? 0l : preManager);
        orderTrack.setPreOrderStatus((preOrderStatus == null) ? "" : preOrderStatus);
        orderTrack.setAttributes(new Gson().toJson(orderInfo));

        // wrapper trac信息
        StringBuffer logTracBuffer = new StringBuffer();
        // 场景：工单号：操作人
        OrderStatusEnum statusEnum = OrderStatusEnum.getOrderStatusEnum(orderStatus);
        logTracBuffer.append(statusEnum.getValue());
        logTracBuffer.append(",工单号为");
        logTracBuffer.append(orderSn);
        logTracBuffer.append(",操作人为");
        logTracBuffer.append(userName);
        orderTrack.setLog(logTracBuffer.toString());
        try {
            orderTrackDao.insert(orderTrack);
            log.info("记录工单追踪信息成功,工单ID:{}", orderId);
            return Result.wrapSuccessfulResult("操作成功");
        } catch (Exception e) {
            log.error("记录工单追踪信息失败，异常信息:{}", e);
            throw new RuntimeException("记录工单追踪信息失败!");
        }
    }

    /**
     * 工单保存
     *
     * @param orderTrack
     * @return
     */
    @Override
    public int save(OrderTrack orderTrack) {
        return orderTrackDao.insert(orderTrack);
    }

    @Override
    public int insert(OrderTrack orderTrack) {
        return orderTrackDao.insert(orderTrack);
    }

    @Override
    public int record(Long orderId, Long shopId, OrderStatusEnum orderStatusEnum, UserInfo userInfo) {
        OrderTrack track = new OrderTrack();
        track.setCreator(userInfo.getUserId());
        track.setModifier(userInfo.getUserId());
        track.setOrderStatus(orderStatusEnum.getKey());
        track.setOrderId(orderId);
        track.setShopId(shopId);
        return this.save(track);
    }



    /**
     * 同步委托单状态
     * order_status     proxy_status
     * FPDD 分配订单 ==> FPDD：已派工
     * DDWC 订单完成 ==> DDWC：已完工
     */
    private Result writeBackProxy(OrderInfo orderInfo) {
        if (orderInfo.getProxyType() != null) {
            String orderStatus = orderInfo.getOrderStatus();
            Long orderId = orderInfo.getId();
            Long shopId = orderInfo.getShopId();
            Integer proxyType = orderInfo.getProxyType();
            //如果门店使用车间，则判断施工单是否完工
            if (OrderStatusEnum.DDWC.getKey().equals(orderStatus) && shopFunFacade.isUseWorkshop(shopId)) {
                //如果是工单完成，则需要校验施工单有没有完成，如果完成才能完工工单
                log.info("【工单完工,查询施工单是否完工】：，门店id:{},工单id:{}", shopId, orderId);
                com.tqmall.core.common.entity.Result<Boolean> booleanResult = null;
                try {
                    booleanResult = rpcWorkOrderService.getWorkOrderInfoByOrderId(shopId, orderId);
                } catch (Exception e) {
                    log.error("工单完工,查询施工单是否完工】,出现异常", e);
                    throw new BizException("工单完工异常，查询对应施工单失败");
                }
                if (booleanResult != null && booleanResult.isSuccess()) {
                    Boolean bool = booleanResult.getData();
                    if (!bool) {
                        throw new BizException("有施工单未完工，请先完工施工单");
                    }
                } else {
                    throw new BizException("工单完工异常，查询对应施工单失败");
                }
            }
            if (OrderProxyTypeEnum.ST.getCode().equals(proxyType)) {
                //受托工单，需要回填委托单信息
                if (OrderStatusEnum.FPDD.getKey().equals(orderStatus) || OrderStatusEnum.DDWC.getKey().equals(orderStatus)) {
                    log.info("【委托单转工单，工单状态变更同步委托单】：查询委托单信息，门店id:{},受托的工单id:{}", shopId, orderId);
                    com.tqmall.core.common.entity.Result<ProxyDTO> proxyDTOResult = null;
                    try {
                        proxyDTOResult = rpcProxyService.getProxyInfoByShopIdAndProxyOrderId(shopId, orderId);
                    } catch (Exception e) {
                        log.error("【委托单转工单】：查询委托单信息,出现异常", e);
                        throw new BizException("同步委托单失败");
                    }
                    if (proxyDTOResult != null && proxyDTOResult.isSuccess()) {
                        ProxyDTO proxyDTO = proxyDTOResult.getData();
                        proxyDTO.setOrderStatus(orderStatus);
                        proxyDTO.setProxyStatus(orderStatus);
                        log.info("【委托单转工单，工单状态变更同步委托单】：更新委托单，委托单id：{}，orderStatus：{}，proxyStatus：{}", proxyDTO.getId(), orderStatus, orderStatus);
                        com.tqmall.core.common.entity.Result updateResult = rpcProxyService.updateProxyOrder(proxyDTO);
                        if (!updateResult.isSuccess()) {
                            throw new BizException("同步委托单失败");
                        }
                    } else {
                        throw new BizException("同步委托单失败");
                    }
                }
            } else if (OrderProxyTypeEnum.WT.getCode().equals(proxyType) && OrderStatusEnum.DDWC.getKey().equals(orderStatus)) {
                //委托工单，如果委托单都是已交车，则可以完工
                ProxyParam proxyParam = new ProxyParam();
                proxyParam.setSource(Constants.CUST_SOURCE);
                proxyParam.setOrderId(orderId);//委托单工单id
                log.info("【委托工单完工】：查询所有委托工单，工单id：{}", orderId);
                com.tqmall.core.common.entity.Result<List<ProxyDTO>> proxyResult = null;
                try {
                    proxyResult = rpcProxyService.showProxyList(proxyParam);
                } catch (Exception e) {
                    log.error("【委托工单完工】：查询所有委托工单,出现异常", e);
                    throw new BizException("同步委托单失败");
                }
                if (proxyResult != null && proxyResult.isSuccess()) {
                    List<ProxyDTO> proxyDTOList = proxyResult.getData();
                    for (ProxyDTO proxyDTO : proxyDTOList) {
                        String proxyStatus = proxyDTO.getProxyStatus();
                        if (!ProxyStatusEnum.YQX.getCode().equals(proxyStatus) && !ProxyStatusEnum.YJC.getCode().equals(proxyStatus) && !ProxyStatusEnum.YJQ.getCode().equals(proxyStatus)) {
                            throw new BizException("有委托单未交车，无法完工");
                        }
                    }
                } else {
                    throw new BizException("同步委托单失败");
                }
            }
        }
        return Result.wrapSuccessfulResult(true);
    }
}
