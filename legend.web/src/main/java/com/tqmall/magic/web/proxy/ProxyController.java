package com.tqmall.magic.web.proxy;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.magic.ProxyStatusEnum;
import com.tqmall.legend.facade.magic.ProxyFacade;
import com.tqmall.legend.facade.magic.vo.ProxyOrderDetailVo;
import com.tqmall.legend.facade.magic.vo.ProxyOrderInfoVo;
import com.tqmall.legend.facade.magic.vo.ProxyServicesVo;
import com.tqmall.legend.facade.magic.vo.ProxyStatusVO;
import com.tqmall.legend.facade.order.CommonOrderFacade;
import com.tqmall.legend.facade.order.OrderPrecheckDetailsFacade;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import com.tqmall.legend.facade.order.vo.OrderToProxyVo;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.partner.ShopPartnerSimParam;
import com.tqmall.magic.object.param.proxy.ProxyInfo;
import com.tqmall.magic.object.param.proxy.ProxyPageParam;
import com.tqmall.magic.object.param.proxy.ProxyServicesInfo;
import com.tqmall.magic.object.result.partner.ShopPartnerDTO;
import com.tqmall.magic.service.partner.RpcShopPartnerService;
import com.tqmall.magic.service.proxy.RpcProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingbao on 16/5/11.
 */
@Controller
@Slf4j
@RequestMapping("/proxy")
public class ProxyController extends BaseController {

    private static final String TAG = "【ProxyController】";

    @Autowired
    private ProxyFacade proxyFacade;

    @Autowired
    private CommonOrderFacade commonOrderFacade;

    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;

    @Autowired
    private ShopService shopService;

    @Autowired
    private RpcShopPartnerService rpcShopPartnerService;
    @Autowired
    private OrderPrecheckDetailsFacade orderPrecheckDetailsFacade;


    @RequestMapping("/selectProxy")
    public String toSelectProxy() {
        return "yqx/page/magic/proxy/selectproxy";
    }

    /**
     * 跳转到委托单详情
     *
     * @return
     */
    @RequestMapping("/detail")
    public String toProxyDetail(Long proxyOrderId, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        //表示当前登录的店铺是委托方
        String currentRole = "authorize";
        Result<ProxyOrderDetailVo> proxyOrderDetail = proxyFacade.getProxyOrderDetail(shopId, proxyOrderId);
        if (shopId.equals(proxyOrderDetail.getData().getProxyShopId())) {
            currentRole = "trustee";
        }
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("result", proxyOrderDetail);
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        orderPrecheckDetailsFacade.setOrderPrecheckDetailsModelByOrderId(model, proxyOrderDetail.getData().getOrderId());
        log.info("跳转委托单详情proxyOrderId={}，currentRole={}", proxyOrderId, currentRole);
        return "yqx/page/magic/proxy/proxydetail";
    }

    /**
     * 导出委托单Excel表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportProxyList/{type}")
    @ResponseBody
    public Object exportProxyList(@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
        long sTime = System.currentTimeMillis();
        ModelAndView view = new ModelAndView("yqx/page/order/proxy-order-list-export");
        if ("authorize".equals(type) || "trustee".equals(type)){
            ProxyPageParam proxyPageParam = this.getProxyPageParam(type, request);
            proxyPageParam.setPageNum(1);
            proxyPageParam.setPageSize(5000);
            Result<DefaultPage<ProxyOrderInfoVo>> pageResult = proxyFacade.getProxyListByParam(proxyPageParam);
            DefaultPage page = pageResult.getData();
            view.addObject("page", page);
        }
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "order_info";
        try {
            filename = URLEncoder.encode("委托单信息", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("委托单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        log.info("【委托单导出】,耗时{}毫秒", System.currentTimeMillis() - sTime);
        return view;
    }

    /**
     * 受托方 接单操作
     *
     * @param proxyOrderId
     * @return
     */
    @RequestMapping("/acceptProxy")
    @ResponseBody
    public Result acceptProxyOrder(Long proxyOrderId, String receiveName, Long receive, String remark) {

        Result result = proxyFacade.acceptProxyOrder(proxyOrderId, receiveName, receive, remark);
        return result;
    }

    /**
     * 受托方 交车操作
     *
     * @param proxyOrderId
     * @return
     */
    @RequestMapping("/backCar")
    @ResponseBody
    public Result backCar(Long proxyOrderId) {
        if (proxyOrderId == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        return proxyFacade.backCar(proxyOrderId, userInfo);
    }

    /**
     * 受托方 结清操作
     *
     * @param proxyOrderId
     * @return
     */
    @RequestMapping("/clearProxy")
    @ResponseBody
    public Result clearProxyOrder(Long proxyOrderId) {
        if (proxyOrderId == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        return proxyFacade.clearProxyOrder(proxyOrderId);
    }

    /**
     * 受托方 取消操作
     *
     * @param proxyOrderId
     * @return
     */
    @RequestMapping("/cancelProxy")
    @ResponseBody
    public Result cancelProxyOrder(Long proxyOrderId) {
        if (proxyOrderId == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        return proxyFacade.cancelProxy(proxyOrderId);
    }


    /**
     * 工单委托/更新委托单
     *
     * @return
     */
    @RequestMapping("/toTranslateProxy")
    public String toTranslateProxy(Long orderId, @RequestParam(value = "proxyId", required = false) Long proxyId, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        Result result = getOrderInfo(orderId, shopId);
        Result<ProxyOrderDetailVo> proxyDtlResult = null;
        Result<List<OrderServicesVo>> servicesResult = null;
        if (proxyId != null && proxyId > 0) {
            proxyDtlResult = proxyFacade.getProxyOrderDetail(shopId, proxyId);
            if (proxyDtlResult.isSuccess() && proxyDtlResult.getData() != null) {
                Long proxyShopId = proxyDtlResult.getData().getProxyShopId();
                servicesResult = getOrderServiceInfoForUpdate(orderId, shopId, proxyShopId, proxyId);
            }

            if (!proxyDtlResult.isSuccess()) {
                log.error(proxyDtlResult.getErrorMsg());
            }
            if (!servicesResult.isSuccess()) {
                log.error(servicesResult.getErrorMsg());
            }
            model.addAttribute("proxyDtlResult", proxyDtlResult);
            model.addAttribute("orderServicesVos", servicesResult.getData());

        }
        orderPrecheckDetailsFacade.setOrderPrecheckDetailsModelByOrderId(model, orderId);
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop", shop);
        model.addAttribute("result", result);
        return "yqx/page/magic/proxy/translateproxy";
    }


    /**
     * 跳转到委托单列表
     *
     * @return
     */
    @RequestMapping("/proxyList")
    public String proxylist(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/magic/proxy/proxylist";
    }


    @RequestMapping("/searchProxyList/{type}")
    @ResponseBody
    public Result<DefaultPage<ProxyOrderInfoVo>> searchProxyList(@PathVariable("type") String type, HttpServletRequest request, @PageableDefault(page = 1, value = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        try {
            ProxyPageParam proxyPageParam = getProxyPageParam(type, request);
            log.info("调用DUBBO接口搜索委托单列表开始，type={},params={}", type, proxyPageParam);

            proxyPageParam.setPageSize(pageable.getPageSize());
            proxyPageParam.setPageNum(pageable.getPageNumber());

            Result<DefaultPage<ProxyOrderInfoVo>> result = proxyFacade.getProxyListByParam(proxyPageParam);
            log.info("调用DUBBO接口搜索委托单列表结束，flag={}", result.isSuccess());
            return result;
        } catch (Exception e) {
            log.error("调用DUBBO接口搜索委托单列表 异常，e={}", e);
            return Result.wrapErrorResult("", "获取数据异常！");
        }
    }


    /**
     * 获取查询参数
     *
     * @param request
     * @return
     */
    private ProxyPageParam getProxyPageParam(String type, HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String shopName = request.getParameter("shopName");
        String proxyShopName = request.getParameter("proxyShopName");
        String carLicense = request.getParameter("carLicense");
        String proxyStatus = request.getParameter("proxyStatus");
        String proxyStartTime = request.getParameter("proxyStartTime");
        String proxyEndTime = request.getParameter("proxyEndTime");
        String proxySn = request.getParameter("proxySn");
        ProxyPageParam proxyPageParam = new ProxyPageParam();
        if (shopName != null) {
            proxyPageParam.setShopName(shopName);
        }
        if (proxyShopName != null) {
            proxyPageParam.setProxyShopName(proxyShopName);
        }
        if (carLicense != null) {
            proxyPageParam.setCarLicense(carLicense);
        }
        if (proxyStatus != null) {
            String proxyStatusTemp = ProxyStatusEnum.getCodeByName(proxyStatus);
            proxyPageParam.setProxyStatus(proxyStatusTemp);
        }
        if (proxyStartTime != null) {
            proxyPageParam.setProxyStartTime(proxyStartTime);
        }
        if (proxyEndTime != null) {
            proxyPageParam.setProxyEndTime(proxyEndTime);
        }
        if (proxySn != null) {
            proxyPageParam.setProxySn(proxySn);
        }

        if ("authorize".equals(type)) {
            proxyPageParam.setShopId(shopId);
        }
        if ("trustee".equals(type)) {
            proxyPageParam.setProxyShopId(shopId);
        }
        return proxyPageParam;
    }


    @RequestMapping("/getProxyStatus")
    @ResponseBody
    public Object getProxyStatusList() {
        List<ProxyStatusVO> proxyStatusVOs = new ArrayList<>();
        for (ProxyStatusEnum e : ProxyStatusEnum.values()) {
            ProxyStatusVO proxyStatusVO = new ProxyStatusVO();
            proxyStatusVO.setCode(e.getCode());
            proxyStatusVO.setName(e.getName());
            proxyStatusVOs.add(proxyStatusVO);
        }
        return Result.wrapSuccessfulResult(proxyStatusVOs);
    }


    /**
     * 添加委托单
     *
     * @param proxyInfo
     * @return
     */
    @RequestMapping(value = "addProxy", method = RequestMethod.POST)
    @ResponseBody
    public Result addProxy(@RequestBody ProxyInfo proxyInfo) {
        log.info("[添加委托单]Begin addProxy.proxyInfo={}", proxyInfo);
        if (proxyInfo == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        if (!StringUtil.isStringEmpty(proxyInfo.getExpectTimeStr())) {
            proxyInfo.setExpectTime(DateUtil.convertStringToDate1(proxyInfo.getExpectTimeStr()));
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo.getUserId() != null) {
            proxyInfo.setCreator(userInfo.getUserId());
            proxyInfo.setOperatorName(userInfo.getName());
            if (!CollectionUtils.isEmpty(proxyInfo.getProxyServicesInfoList())) {
                for (ProxyServicesInfo proxyServicesInfo : proxyInfo.getProxyServicesInfoList()) {
                    proxyServicesInfo.setCreator(userInfo.getUserId());
                }
            }
        }
        com.tqmall.zenith.errorcode.support.Result result = proxyFacade.addProxyOrder(proxyInfo);
        if (result.isSuccess()) {
            commonOrderFacade.updateOrderInfoProxyType(proxyInfo.getOrderId(), 1);
            log.info("【添加委托单】End addProxy.data={}", result.getData());
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
    }

    /**
     * 更新委托单服务
     *
     * @param proxyInfo
     * @return
     */
    @RequestMapping(value = "updateProxy", method = RequestMethod.POST)
    @ResponseBody
    public Result updateProxy(@RequestBody ProxyInfo proxyInfo) {
        log.info("【更新委托单】Begin updateProxy.proxyInfo={}", proxyInfo);
        if (proxyInfo == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo.getUserId() != null) {
            proxyInfo.setModifier(userInfo.getUserId());
            proxyInfo.setOperatorName(userInfo.getName());
            if (!CollectionUtils.isEmpty(proxyInfo.getProxyServicesInfoList())) {
                for (ProxyServicesInfo proxyServicesInfo : proxyInfo.getProxyServicesInfoList()) {
                    proxyServicesInfo.setModifier(userInfo.getUserId());
                }
            }
        }
        com.tqmall.zenith.errorcode.support.Result result = proxyFacade.updateProxyOrder(proxyInfo);
        if (result.isSuccess()) {
            log.info("【更新委托单】End updateProxy.data={}", result.getData());
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
    }


    /**
     * 查询多个服务价格（例如喷漆，钣金）
     *
     * @param serviceId
     * @return
     */
    @RequestMapping("selectHourPrice")
    @ResponseBody
    public Result selectHourPrice(Long serviceId) {
        log.info("【查询服务价格】Begin selectHourPrice.serviceId={}", serviceId);
        if (serviceId == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        if (serviceId < 0 || shopId < 0) {
            log.error("参数错误。serviceId={},shopId={}", serviceId, shopId);
            return Result.wrapErrorResult(LegendErrorCode.APP_PARAM_ERROR.getCode(), "参数出错");
        }
        Result<List<ShopServiceInfo>> result = shopServiceInfoFacade.getSameServiceInfo(serviceId, shopId);
        log.info("【查询服务价格】End selectHourPrice.result={}", result);
        return result;
    }

    /**
     * 查询工单信息
     *
     * @param orderId
     * @param shopId
     * @return
     */
    private Result getOrderInfo(Long orderId, Long shopId) {
        log.info("Begin getOrderInfo.orderId={},shopId={}", orderId, shopId);
        if (orderId == null || shopId == null) {
            log.error("参数错误。orderId={},shopId={}", orderId, shopId);
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        Result<OrderToProxyVo> result = commonOrderFacade.getProxyOrderInfo(orderId, shopId);
        if (!result.isSuccess()) {
            log.error(result.getErrorMsg());
        }
        log.info("End getOrderInfo.result={}", result);
        return result;
    }


    /**
     * 编辑委托单时组合服务列表
     *
     * @param orderId
     * @param shopId
     * @param proxyId
     * @return
     */
    private Result getOrderServiceInfoForUpdate(Long orderId, Long shopId, Long proxyShopId, Long proxyId) {
        if (orderId == null || shopId == null || proxyShopId == null || proxyId == null) {
            return Result.wrapErrorResult(LegendErrorCode.PROXY_PARAM_NULL_ERROR.getCode(), LegendErrorCode.PROXY_PARAM_NULL_ERROR.getErrorMessage());
        }
        Result<ProxyOrderDetailVo> proxyDtlResult = proxyFacade.getProxyOrderDetail(shopId, proxyId);
        Result<List<OrderServicesVo>> listResult = proxyFacade.getNotProxyService(orderId, shopId);
        BigDecimal rate = getPartnerRate(proxyShopId);
        if (proxyDtlResult.isSuccess() && listResult.isSuccess()) {

            if (!CollectionUtils.isEmpty(listResult.getData())) {
                for (OrderServicesVo orderServicesVo : listResult.getData()) {
                    List<ShopServiceInfo> shopServiceInfos = shopServiceInfoFacade.getSameServiceInfo(orderServicesVo.getServiceId(), shopId).getData();
                    BigDecimal proxyAmount = BigDecimal.ZERO;
                    if (orderServicesVo.getServiceHour() != null && orderServicesVo.getSharePrice() != null) {
                        proxyAmount = orderServicesVo.getServiceHour().multiply(orderServicesVo.getSharePrice()).multiply(rate);
                    }
                    orderServicesVo.setProxyAmount(proxyAmount);
                    orderServicesVo.setShopServiceInfoList(shopServiceInfos);
                    orderServicesVo.setId(null);
                }
            }

            if (proxyDtlResult.getData() != null) {
                if (!CollectionUtils.isEmpty(proxyDtlResult.getData().getProxyServicesVoList())) {
                    List<ProxyServicesVo> list = proxyDtlResult.getData().getProxyServicesVoList();
                    for (ProxyServicesVo proxyServicesVo : list) {
                        List<ShopServiceInfo> shopServiceInfos = shopServiceInfoFacade.getSameServiceInfo(proxyServicesVo.getServiceId(), shopId).getData();
                        OrderServicesVo orderServicesVo = new OrderServicesVo();
                        orderServicesVo.setId(proxyServicesVo.getId());
                        orderServicesVo.setServiceId(proxyServicesVo.getServiceId());
                        orderServicesVo.setServiceCatName(proxyServicesVo.getServiceType());
                        orderServicesVo.setServiceName(proxyServicesVo.getServiceName());
                        orderServicesVo.setServiceHour(proxyServicesVo.getServiceHour());
                        orderServicesVo.setServiceNote(proxyServicesVo.getServiceNote());
                        orderServicesVo.setServicePrice(proxyServicesVo.getServiceAmount());
                        orderServicesVo.setServiceSn(proxyServicesVo.getServiceSn());
                        orderServicesVo.setSharePrice(proxyServicesVo.getSharePrice());
                        orderServicesVo.setProxyAmount(proxyServicesVo.getProxyAmount());
                        orderServicesVo.setChooseStatus(true);
                        orderServicesVo.setShopServiceInfoList(shopServiceInfos);
                        listResult.getData().add(orderServicesVo);

                    }
                }
            }
        }
        return listResult;
    }

    /**
     * 获取股东或者非股东的比率
     *
     * @param id
     * @return
     */
    private BigDecimal getPartnerRate(Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopPartnerSimParam shopPartnerSimParam = new ShopPartnerSimParam();
        shopPartnerSimParam.setShopId(id);
        shopPartnerSimParam.setPartnerId(shopId);
        com.tqmall.core.common.entity.Result<List<ShopPartnerDTO>> result = rpcShopPartnerService.getAllPartnerList(shopPartnerSimParam);
        if (result.isSuccess() && result.getData().size() > 0) {
            return result.getData().get(0).getRate();
        }
        Shop shop = shopService.selectById(id);
        return shop.getRate();
    }


    @RequestMapping("toReceiveOrder")
    public String toReceiveOrder(Long proxyId, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<ProxyOrderDetailVo> proxyDtlResult = proxyFacade.getProxyOrderDetail(shopId, proxyId);
        if (proxyDtlResult.isSuccess()) {
            model.addAttribute("proxyOrderDetailVo", proxyDtlResult.getData());
        }
        return "yqx/page/magic/proxy/selectproxy";
    }


}
