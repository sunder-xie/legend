package com.tqmall.legend.facade.magic;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.vo.ProxyOrderDetailVo;
import com.tqmall.legend.facade.magic.vo.ProxyOrderInfoVo;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import com.tqmall.magic.object.param.proxy.ProxyConfirmParam;
import com.tqmall.magic.object.param.proxy.ProxyInfo;
import com.tqmall.magic.object.param.proxy.ProxyPageParam;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;

/**
 * Created by shulin on 16/5/11.
 */
public interface ProxyFacade {

    /**
     * 添加委托单
     *
     * @param proxyInfo
     * @return
     */
    public Result addProxyOrder(ProxyInfo proxyInfo);

    /**
     * 更新委托单服务
     *
     * @param proxyInfo
     * @return
     */
    public Result updateProxyOrder(ProxyInfo proxyInfo);


    /**
     * 接单操作，变更委托单状态为YJD
     *
     * @param proxyOrderId
     * @return
     */
    public com.tqmall.legend.common.Result acceptProxyOrder(Long proxyOrderId, String receiveId, Long receiver, String remark);

    /**
     * 交车操作，变更委托单状态为YJC
     *
     * @param proxyOrderId
     * @return
     */
    public com.tqmall.legend.common.Result backCar(Long proxyOrderId, UserInfo userInfo);

    /**
     * 结清操作，变更委托单状态为YJQ
     *
     * @param proxyOrderId
     * @return
     */
    public com.tqmall.legend.common.Result clearProxyOrder(Long proxyOrderId);

    /**
     * 全部结清
     *
     * @return
     */
    public com.tqmall.legend.common.Result clearAllProxyOrder(Long[] ids);

    /**
     * 根据委托单编号由委托方取消委托单
     *
     * @param proxyOrderId
     * @return
     */
    public com.tqmall.legend.common.Result cancelProxy(Long proxyOrderId);

    /**
     * 根据委托单ID获取委托单详情
     *
     * @param proxyOrderId
     * @return
     */
    public com.tqmall.legend.common.Result<ProxyOrderDetailVo> getProxyOrderDetail(Long shopId, Long proxyOrderId);

    /**
     * 通过门店ID获取该门店所有的委托单信息
     *
     * @param shopId
     * @return
     */
    @Deprecated
    public com.tqmall.legend.common.Result<List<ProxyOrderInfoVo>> getAllProxyOrderByShopId(Long shopId);


    /**
     * 获取门店作为委托方的所有委托单信息
     *
     * @param shopId
     * @return
     */
    public com.tqmall.legend.common.Result<DefaultPage<ProxyOrderInfoVo>> getAuthorizedProxyList(Long shopId, Integer pageNum, Integer pageSize);


    /**
     * 获取门店作为受托方的所有委托单信息
     *
     * @param shopId
     * @return
     */
    @Deprecated
    public com.tqmall.legend.common.Result<DefaultPage<ProxyOrderInfoVo>> getTrusteeProxyList(Long shopId, Integer pageNum, Integer pageSize);

    /**
     * 根据参数搜索委托单信息列表（分页）
     *
     * @param proxyPageParam
     * @return
     */
    public com.tqmall.legend.common.Result<DefaultPage<ProxyOrderInfoVo>> getProxyListByParam(ProxyPageParam proxyPageParam);


    /**
     * 根据工单id获取未委托的服务
     *
     * @return
     */
    public com.tqmall.legend.common.Result<List<OrderServicesVo>> getNotProxyService(Long orderId, Long shopId);

    /**
     * 根据工单id、门店id，更新委托单状态
     *
     * @param orderId
     * @param shopId
     * @param orderStatus
     * @param proxyStatus
     */
    void updateProxyOrder(Long orderId, Long shopId, String orderStatus, String proxyStatus);

}

