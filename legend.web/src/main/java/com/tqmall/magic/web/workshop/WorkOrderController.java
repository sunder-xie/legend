package com.tqmall.magic.web.workshop;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderServicesService;
import com.tqmall.legend.biz.order.OrderTrackService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.magic.BoardFacade;
import com.tqmall.legend.facade.magic.WorkOrderFacade;
import com.tqmall.legend.facade.magic.vo.AutoChooseParamVo;
import com.tqmall.legend.facade.magic.vo.ProcessManagerRelVo;
import com.tqmall.legend.facade.magic.vo.ProcessWorkTimeVo;
import com.tqmall.legend.facade.magic.vo.WorkOrderProcessRelVo;
import com.tqmall.legend.facade.order.CommonOrderFacade;
import com.tqmall.legend.facade.magic.vo.WorkOrderVo;
import com.tqmall.legend.facade.order.OrderPrecheckDetailsFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.shopmanager.TeamParam;
import com.tqmall.magic.object.param.workshop.ProcessParam;
import com.tqmall.magic.object.param.workshop.WorkOrderParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.proxy.ProxyDTO;
import com.tqmall.magic.object.result.shopmanager.ShopManagerExtensionDTO;
import com.tqmall.magic.object.result.shopmanager.TeamDTO;
import com.tqmall.magic.object.result.workshop.*;
import com.tqmall.magic.service.proxy.RpcProxyService;
import com.tqmall.magic.service.shopmanger.RpcShopManagerExtService;
import com.tqmall.magic.service.workshop.RpcLineProcessManagerService;
import com.tqmall.magic.service.workshop.RpcPLineProcessRelService;
import com.tqmall.magic.service.workshop.RpcProcessService;
import com.tqmall.magic.service.workshop.RpcProductionLineService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shulin on 16/6/30.
 */
@Controller
@Slf4j
@RequestMapping("workshop/workOrder")
public class WorkOrderController extends BaseController {

    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;
    @Autowired
    private RpcProcessService rpcProcessService;
    @Autowired
    private OrderPrecheckDetailsFacade orderPrecheckDetailsFacade;
    @Autowired
    private RpcPLineProcessRelService rpcPLineProcessRelService;
    @Autowired
    private CommonOrderFacade commonOrderFacade;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private RpcProductionLineService rpcProductionLineService;
    @Autowired
    private OrderServicesService orderServicesService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private RpcShopManagerExtService rpcShopManagerExtService;
    @Autowired
    private CustomerCarService customerCarService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private OrderTrackService orderTrackService;
    @Autowired
    private WorkOrderFacade workOrderFacade;
    @Autowired
    private RpcLineProcessManagerService rpcLineProcessManagerService;
    @Autowired
    private BoardFacade boardFacade;

    /**
     * 跳到施工单创建页面
     *
     * @return
     */
    @RequestMapping("toWorkOrderCreate")
    public String toWorkOrderCreate(Long orderId, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        OrderInfo orderInfo = commonOrderFacade.getOrderInfoById(orderId, shopId);
        if (orderInfo != null) {
            Result<ProxyDTO> result = rpcProxyService.getProxyInfo(orderInfo.getId());
            model.addAttribute("proxyInfo", result.getData());
        }
        model.addAttribute("orderInfo", orderInfo);
        return "yqx/page/magic/workshop/workorder_create";
    }

    /**
     * 跳到施工单详情页面
     *
     * @return
     */
    @RequestMapping("toWorkOrderDetail")
    public String toWorkOrderDetail(Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        Result<WorkOrderDTO> workOrderDTOResult = rpcWorkOrderService.showWorkOrderInfo(id, shopId);
        if (workOrderDTOResult.isSuccess() && workOrderDTOResult.getData() != null) {
            //获取服务项目和物料
            Long orderId = workOrderDTOResult.getData().getOrderId();
            Long lineId = workOrderDTOResult.getData().getLineId();
            OrderInfo orderInfo = commonOrderFacade.getOrderInfoById(orderId, shopId);
            List<OrderServices> orderServiceses = orderServicesService.queryOrderServiceList(orderId, shopId);
            List<OrderGoods> orderGoodses = orderGoodsService.queryOrderGoodList(orderId, shopId);

            List<WorkOrderProcessRelDTO> workOrderProcessRelDTOs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(workOrderDTOResult.getData().getWorkOrderProcessRelDTOList())) {
                for (WorkOrderProcessRelDTO workOrderProcessRelDTO : workOrderDTOResult.getData().getWorkOrderProcessRelDTOList()) {
                    if (workOrderProcessRelDTO.getWorkTime() != null && !workOrderProcessRelDTO.getWorkTime().equals("0")) {
                        workOrderProcessRelDTOs.add(workOrderProcessRelDTO);
                    }
                }
            }

            if (lineId != null && lineId > 0) {
                Result<ProductionLineDTO> productionLineDTOResult = rpcProductionLineService.selectProductionLine(lineId);
                if (productionLineDTOResult.isSuccess() && productionLineDTOResult.getData() != null) {
                    model.addAttribute("type", productionLineDTOResult.getData().getType());
                    if (productionLineDTOResult.getData().getType().equals(2)) {
                        if (!CollectionUtils.isEmpty(workOrderProcessRelDTOs)) {
                            if ("钣金".equals(workOrderProcessRelDTOs.get(0).getProcessName())) {
                                model.addAttribute("flag", workOrderProcessRelDTOs.get(1).getStatus());
                            }else{
                                model.addAttribute("flag", workOrderProcessRelDTOs.get(0).getStatus());
                            }

                        }
                    }
                }

            }
            Result<ProxyDTO> result = rpcProxyService.getProxyInfo(orderInfo.getId());
            if (result.isSuccess() && result.getData() != null) {
                model.addAttribute("proxy", result.getData());
            }

            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("orderServiceses", orderServiceses);
            model.addAttribute("orderGoodses", orderGoodses);
        }
        model.addAttribute("workOrder", workOrderDTOResult.getData());

        if (workOrderDTOResult.getData() != null){
            List<WorkOrderProcessRelDTO> workOrderProcessRelDTOs = workOrderDTOResult.getData().getWorkOrderProcessRelDTOList();
            if (!CollectionUtils.isEmpty(workOrderProcessRelDTOs)){
                if (!"钣金".equals(workOrderProcessRelDTOs.get(0).getProcessName())){
                    model.addAttribute("flag1","1");
                }else if("钣金".equals(workOrderProcessRelDTOs.get(0).getProcessName()) && "0".equals(workOrderProcessRelDTOs.get(0).getWorkTime())){
                    model.addAttribute("flag1","1");
                }else {
                    model.addAttribute("flag1",workOrderProcessRelDTOs.get(0).getStatus());
                }
            }
        }


        log.info("[施工单详情页面]跳到施工单详情页面");
        return "yqx/page/magic/workshop/workorder_detail";
    }

    /**
     * 事故线排工页面
     *
     * @return
     */
    @RequestMapping("toUpdateWorkOrder")
    public String toUpdateWorkOrder(Long id, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<WorkOrderDTO> workOrderDTOResult = rpcWorkOrderService.showWorkOrderInfo(id, shopId);
        if (workOrderDTOResult.isSuccess() && workOrderDTOResult.getData() != null) {
            Long lineId = workOrderDTOResult.getData().getLineId();
            Long orderId = workOrderDTOResult.getData().getOrderId();
            OrderInfo orderInfo = commonOrderFacade.getOrderInfoById(orderId, shopId);
            Result<ProxyDTO> result = rpcProxyService.getProxyInfo(orderInfo.getId());
            if (result.isSuccess() && result.getData() != null) {
                model.addAttribute("proxy", result.getData());
            }
            if (lineId != null && lineId > 0) {
                Result<ProductionLineDTO> lineDTOResult = rpcProductionLineService.selectProductionLine(lineId);
                if (lineDTOResult.isSuccess()) {
                    model.addAttribute("productionLine", lineDTOResult.getData());
                }
            }

            AutoChooseParamVo autoChooseParamVo = new AutoChooseParamVo();
            List<ProcessWorkTimeVo> processWorkTimeVos = BdUtil.bo2do4List(workOrderDTOResult.getData().getWorkOrderProcessRelDTOList(), ProcessWorkTimeVo.class);
            List<ProcessManagerRelVo> processManagerRelVos = BdUtil.bo2do4List(workOrderDTOResult.getData().getWorkOrderProcessRelDTOList(), ProcessManagerRelVo.class);
            autoChooseParamVo.setLineId(lineId);
            autoChooseParamVo.setProcessWorkTimeVoList(processWorkTimeVos);
            autoChooseParamVo.setProcessManagerRelVoList(processManagerRelVos);
            List<ProcessManagerRelVo> processManagerRelVoList1 = workOrderFacade.accidentAutoCalculateTime(shopId, autoChooseParamVo).getData();
            model.addAttribute("processManagerRelVoList", processManagerRelVoList1);
            model.addAttribute("workOrder", workOrderDTOResult.getData());
            model.addAttribute("orderInfo", orderInfo);
        }
        return "yqx/page/magic/workshop/workorder-update";
    }






    /**
     * 跳到施工单列表页面
     *
     * @return
     */
    @RequestMapping("toWorkOrderList")
    public String toWorkOrderList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/magic/workshop/workorder_list";
    }


    /**
     * 施工单导出
     *
     * @param pageable
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("exportWorkOrderList")
    @ResponseBody
    public Object exportWorkOrderList(@PageableDefault(page = 1, value = 5000, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
        WorkOrderParam workOrderParam = checkWorkOrderParam(request);
        workOrderParam.setPageSize(5000);
        workOrderParam.setPageNum(1);
        Result<PageEntityDTO<WorkOrderDTO>> result = rpcWorkOrderService.getWorkOrderListByPage(workOrderParam);
        DefaultPage<WorkOrderDTO> page = null;
        if (result.isSuccess()) {
            PageEntityDTO<WorkOrderDTO> pageEntityDTO = result.getData();
            List<WorkOrderDTO> workOrderDTOs = pageEntityDTO.getRecordList();
            PageRequest pageRequest =
                    new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                            pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
            page = new DefaultPage<WorkOrderDTO>(workOrderDTOs, pageRequest, pageEntityDTO.getTotalNum());
        }
        ModelAndView view = new ModelAndView("yqx/page/magic/workshop/workorder_list_export");
        view.addObject("page", page);
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "order_info";
        try {
            filename = URLEncoder.encode("施工单信息", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("施工单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        return view;
    }

    /**
     * 跳到施工单中断列表页面
     *
     * @return
     */
    @RequestMapping("toWorkOrderBreakList")
    public String toWorkOrderBreakList() {
        return "yqx/page/magic/workshop/workorder_break_list";
    }

    /**
     * 跳转到施工单中断列表页面
     * @return
     */
    @RequestMapping("toWorkOrderBreakListDtl")
    public String toWorkOrderBreakListDtl(Long id,Model model){
        log.info("【施工单中断详情】Begin selectBreakWorkOrderProcessDtl.id={}",id);
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<WorkOrderDTO> workOrderDTOResult = null;
        try {
            workOrderDTOResult = rpcWorkOrderService.showBreakWorkOrderInfo(id, shopId);
        } catch (Exception e) {
            log.error("施工单中断查询详情失败", e);
        }
        model.addAttribute("workOrder",workOrderDTOResult.getData());
        log.info("【施工单中断详情】End selectBreakWorkOrderProcessDtl.isSuccess",workOrderDTOResult.isSuccess());
        return "yqx/page/magic/workshop/workOrderBreakListDtl";
    }

    /**
     * 跳到看板页面
     *
     * @return
     */
    @RequestMapping("toWorkOrderWorkShopBoard")
    public String toWorkOrderWorkShopBoard() {
        return "yqx/page/magic/workshop/workshop_board";
    }


    /**
     * 获取门店的生产线对应的工序
     *
     * @param shopId
     * @param lineId
     * @return
     */
    @RequestMapping("getPLineProcessRel")
    @ResponseBody
    public Result getPLineProcessRel(Long shopId, Long lineId) {
        Result<List<PLineProcessRelDTO>> result = null;
        try {
            result = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
        } catch (Exception e) {
            log.error("调用远程接口获取门店的生产线对应的工序失败", e);
            return Result.wrapErrorResult("", "调用远程接口获取门店的生产线对应的工序失败");
        }
        if (!result.isSuccess()) {
            log.error(result.getMessage());
            return Result.wrapErrorResult("", "查询门店生产线对应的工序失败");
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    /**
     * 分页查找施工单列表
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping("selectWorkOrderByPage")
    @ResponseBody
    public Result selectWorkOrderByPage(HttpServletRequest request, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("[施工单列表] Begin selectWorkOrderByPage.");
        WorkOrderParam workOrderParam = checkWorkOrderParam(request);
        workOrderParam.setPageSize(pageable.getPageSize());
        workOrderParam.setPageNum(pageable.getPageNumber());
        Result<PageEntityDTO<WorkOrderDTO>> result = null;
        try {
            result = rpcWorkOrderService.getWorkOrderListByPage(workOrderParam);
        } catch (Exception e) {
            log.error("调用远程接口分页查找施工单失败", e);
            return Result.wrapErrorResult("", "调用远程接口分页查找施工单失败");
        }
        DefaultPage<WorkOrderDTO> page = null;
        if (result.isSuccess()) {
            PageEntityDTO<WorkOrderDTO> pageEntityDTO = result.getData();
            List<WorkOrderDTO> workOrderDTOs = pageEntityDTO.getRecordList();
            PageRequest pageRequest =
                    new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                            pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
            page = new DefaultPage<WorkOrderDTO>(workOrderDTOs, pageRequest, pageEntityDTO.getTotalNum());
        }


        log.info("[施工单列表] End selectWorkOrderByPage.");
        return Result.wrapSuccessfulResult(page);

    }

    /**
     * 施工单参数
     *
     * @param request
     * @return
     */
    private WorkOrderParam checkWorkOrderParam(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String carLicense = request.getParameter("carLicense");
        String contactMobile = request.getParameter("contactMobile");
        String contactName = request.getParameter("contactName");
        String workOrderSn = request.getParameter("workOrderSn");
        String orderSn = request.getParameter("orderSn");
        String realOperator = request.getParameter("realOperator");
        String processName = request.getParameter("processName");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String workOrderStatus = request.getParameter("workOrderStatus");
        String accessoriesPrepare = request.getParameter("accessoriesPrepare");
        String paintPrepare = request.getParameter("paintPrepare");
        String startExpectTime = request.getParameter("startExpectTime");
        String endExpectTime = request.getParameter("endExpectTime");
        WorkOrderParam workOrderParam = new WorkOrderParam();
        workOrderParam.setShopId(shopId);
        workOrderParam.setCarLicense(carLicense);
        workOrderParam.setContactMobile(contactMobile);
        workOrderParam.setContactName(contactName);
        workOrderParam.setWorkOrderSn(workOrderSn);
        workOrderParam.setOrderSn(orderSn);
        workOrderParam.setRealOperator(realOperator);
        workOrderParam.setProcessName(processName);
        workOrderParam.setStartTime(startTime);
        workOrderParam.setEndTime(endTime);
        workOrderParam.setWorkOrderStatus(workOrderStatus);
        workOrderParam.setAccessoriesPrepare(accessoriesPrepare);
        workOrderParam.setPaintPrepare(paintPrepare);
        workOrderParam.setStartExpectTime(startExpectTime);
        workOrderParam.setEndExpectTime(endExpectTime);
        return workOrderParam;
    }

    /**
     * 查询施工单详情
     *
     * @param id
     * @return
     */
    @RequestMapping("showWorkOrderInfo")
    @ResponseBody
    public Result showWorkOrderInfo(Long id) {
        log.info("[查询施工单详情]Begin showWorkOrderInfo.id={}", id);
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<WorkOrderDTO> workOrderDTOResult = null;
        try {
            workOrderDTOResult = rpcWorkOrderService.showWorkOrderInfo(id, shopId);
        } catch (Exception e) {
            log.error("查询施工单详情失败", e);
            return Result.wrapErrorResult("", "查询施工单详情失败");
        }
        log.info("[施工单详情] End showWorkOrderInfo.");
        return workOrderDTOResult;
    }

    /**
     * 添加或更新施工单
     *
     * @param workOrderDTO
     * @return
     */
    @RequestMapping(value = "addOrUpdateWorkOrder", method = RequestMethod.POST)
    @ResponseBody
    public Result addOrUpdateWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) {
        log.info("[添加施工单] Begin addOrUpdateWorkOrder.");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (workOrderDTO.getId() != null && workOrderDTO.getId() > 0) {
            workOrderDTO.setModifier(userInfo.getUserId());
        } else {
            workOrderDTO.setCreator(userInfo.getUserId());
            workOrderDTO.setModifier(userInfo.getUserId());
        }

        if (!CollectionUtils.isEmpty(workOrderDTO.getWorkOrderProcessRelDTOList())) {
            for (WorkOrderProcessRelDTO workOrderProcessRelDTO : workOrderDTO.getWorkOrderProcessRelDTOList()) {
                if (workOrderDTO.getId() != null && workOrderDTO.getId() > 0) {
                    workOrderProcessRelDTO.setModifier(userInfo.getUserId());
                } else {
                    workOrderProcessRelDTO.setCreator(userInfo.getUserId());
                    workOrderProcessRelDTO.setModifier(userInfo.getUserId());
                }
            }
        }

        Result result = null;
        try {
            result = rpcWorkOrderService.addOrUpdateWorkOrder(workOrderDTO);
        } catch (Exception e) {
            log.error("添加or更新施工单失败", e);
            return Result.wrapErrorResult("", "添加or更新施工单失败");
        }
        if (result.isSuccess()) {
            if (workOrderDTO.getId() == null) {
                //更新施工单状态为已派工
                orderTrackService.tasking(workOrderDTO.getOrderId(), "", "FPDD", userInfo);
            }
            boardFacade.sendMessage(userInfo.getShopId(), null, Long.valueOf(result.getData().toString()));
        }
        log.info("[添加施工单] End addOrUpdateWorkOrder.");
        return result;
    }

    /**
     * 施工单中断查询
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping("selectBreakWorkOrderProcess")
    @ResponseBody
    public Result selectBreakWorkOrderProcess(HttpServletRequest request, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        WorkOrderParam workOrderParam = checkWorkOrderParam(request);
        workOrderParam.setPageSize(pageable.getPageSize());
        workOrderParam.setPageNum(pageable.getPageNumber());
        Result<PageEntityDTO<WorkOrderDTO>> result = null;
        try {
            result = rpcWorkOrderService.selectBreakWorkOrderProcess(workOrderParam);
        } catch (Exception e) {
            log.error("施工单中断查询失败", e);
            return Result.wrapErrorResult("", "施工单中断查询失败");
        }

        DefaultPage<WorkOrderDTO> page = null;
        if (result.isSuccess()) {
            PageEntityDTO<WorkOrderDTO> pageEntityDTO = result.getData();
            List<WorkOrderDTO> workOrderDTOs = pageEntityDTO.getRecordList();
            PageRequest pageRequest =
                    new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                            pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
            page = new DefaultPage<WorkOrderDTO>(workOrderDTOs, pageRequest, pageEntityDTO.getTotalNum());
        }

        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 根据施工单id主键查询即将工序信息
     *
     * @param workOrderId
     * @return
     */
    @RequestMapping("getWorkOrderProcessRel")
    @ResponseBody
    public Result getWorkOrderProcessRel(Integer workOrderId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<WorkOrderProcessRelDTO> result = null;
        try {
            result = rpcWorkOrderService.getWorkOrderProcessRel(shopId.intValue(), workOrderId);
        } catch (Exception e) {
            log.error("查询即将工序信息失败", e);
            return Result.wrapErrorResult("", "查询即将工序信息失败");
        }
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return Result.wrapErrorResult("", "第一道工序状态异常,是DSG状态,不可中断");
            }
        }
        return result;
    }

    /**
     * 查询所有工序列表---下拉框
     *
     * @return
     */
    @RequestMapping("getProcessNameList")
    @ResponseBody
    public Result getProcessNameList() {
        ProcessParam processParam = new ProcessParam();
        processParam.setShopId(null);
        Result<List<ProcessDTO>> result = null;
        try {
            result = rpcProcessService.selectProcessList(processParam);
        } catch (Exception e) {
            log.error("查询所有工序列表失败", e);
            return Result.wrapErrorResult("", "查询所有工序列表失败");
        }
        List list = new ArrayList();
        if (result.isSuccess()) {
            List<ProcessDTO> processDTOList = result.getData();
            Map<String, String> map = null;
            for (ProcessDTO processDTO : processDTOList) {
                map = Maps.newHashMap();
                map.put("code", processDTO.getId().toString());
                map.put("name", processDTO.getName());
                list.add(map);
            }

        }
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 查询施工人--下拉框
     *
     * @return
     */
    @RequestMapping("getRealOperatorList")
    @ResponseBody
    public Result getRealOperatorList(HttpServletRequest request) {
        WorkOrderParam workOrderParam = checkWorkOrderParam(request);
        Result<List<WorkOrderDTO>> result = null;
        try {
            result = rpcWorkOrderService.selectRealOperatorList(workOrderParam);
        } catch (Exception e) {
            log.error("查询施工人员失败", e);
            return Result.wrapErrorResult("", "查询施工人员失败");
        }
        List list = new ArrayList();
        if (result.isSuccess()) {
            List<WorkOrderDTO> workOrderDTOList = result.getData();
            Map<String, String> map = Maps.newHashMap();
            Map<String, String> map1 = null;
            for (WorkOrderDTO workOrderDTO : workOrderDTOList) {
                WorkOrderProcessRelVo workOrderProcessRelVo = BdUtil.do2bo(workOrderDTO.getWorkOrderProcessRelDTO(), WorkOrderProcessRelVo.class);
                if (workOrderProcessRelVo != null)
                    map.put(workOrderProcessRelVo.getRealOperator().toString(), workOrderProcessRelVo.getRealOperatorId().toString());
            }
            for (Map.Entry<String, String> entryMap : map.entrySet()) {
                map1 = Maps.newHashMap();
                map1.put("name", entryMap.getKey().toString());
                map1.put("code", entryMap.getValue().toString());
                list.add(map1);
            }
        }
        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 中断操作
     *
     * @param workOrderId
     * @param processId
     * @return
     */
    @RequestMapping(value = "interruptedOperator", method = RequestMethod.POST)
    @ResponseBody
    public Result interruptedOperator(@RequestParam(value = "workOrderId", required = false) Integer workOrderId,
                                      @RequestParam(value = "processId", required = false) Integer processId,
                                      @RequestParam(value = "breakReason", required = false) String breakReason,
                                      @RequestParam(value = "breakRemark", required = false) String breakRemark) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long operatorId = userInfo.getUserId();
        Result result = null;
        try {
            result = rpcWorkOrderService.interruptedOperator(shopId.intValue(), operatorId, workOrderId, processId, breakReason, breakRemark);
        } catch (Exception e) {
            log.error("中断操作失败", e);
            return Result.wrapErrorResult("", "中断操作失败");
        }
        boardFacade.sendMessage(shopId, null, workOrderId.longValue());
        return result;
    }

    /**
     * 中断恢复
     *
     * @param workOrderId
     * @param processId
     * @return
     */
    @RequestMapping(value = "interruptRecovery", method = RequestMethod.POST)
    @ResponseBody
    public Result interruptRecovery(@RequestParam(value = "workOrderId", required = false) Integer workOrderId,
                                    @RequestParam(value = "processId", required = false) Integer processId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long operatorId = userInfo.getUserId();
        Result result = null;
        try {
            result = rpcWorkOrderService.interruptRecovery(shopId.intValue(), workOrderId, processId, operatorId);
        } catch (Exception e) {
            log.error("中断恢复失败", e);
            return Result.wrapErrorResult("", "中断恢复失败");
        }

        boardFacade.sendMessage(shopId, null, workOrderId.longValue());
        return result;
    }

    /**
     * 恢复即完工
     *
     * @param workOrderId
     * @param processId
     * @return
     */
    @RequestMapping(value = "interruptedCompletion", method = RequestMethod.POST)
    @ResponseBody
    public Result interruptedCompletion(@RequestParam(value = "workOrderId", required = false) Integer workOrderId,
                                        @RequestParam(value = "processId", required = false) Integer processId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long operatorId = userInfo.getUserId();
        Result result = null;
        try {
            result = rpcWorkOrderService.interruptedCompletion(shopId, workOrderId, processId, operatorId);
        } catch (Exception e) {
            log.error("恢复完工失败", e);
            return Result.wrapErrorResult("", "恢复完工失败");
        }
        boardFacade.sendMessage(shopId, null, workOrderId.longValue());
        return result;
    }

    /**
     * 施工单打印
     *
     * @param id 施工单的id
     * @return
     */
    @RequestMapping(value = "workOrderPrint", method = RequestMethod.GET)
    public String workOrderPrint(@RequestParam(value = "id", required = false) Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<WorkOrderDTO> workOrderDTOResult = rpcWorkOrderService.showWorkOrderInfo(id, shopId);
        WorkOrderVo workOrderVo = new WorkOrderVo();
        List<WorkOrderProcessRelVo> workOrderProcessRelVos = Lists.newArrayList();
        List<WorkOrderProcessRelVo> workOrderProcessRelVoList = Lists.newArrayList();
        if (workOrderDTOResult.isSuccess() && workOrderDTOResult.getData() != null) {
            BeanUtils.copyProperties(workOrderDTOResult.getData(), workOrderVo);
            Result<ProductionLineDTO> productionLineDTOResult = null;
            try {
                productionLineDTOResult = rpcProductionLineService.selectProductionLine(workOrderVo.getLineId());
            } catch (Exception e) {
                log.error("施工单打印失败", e);
            }
            //获取生产线type
            if (productionLineDTOResult.isSuccess() && productionLineDTOResult.getData() != null) {
                Integer lineType = productionLineDTOResult.getData().getType();
                workOrderVo.setProduceLineType(lineType);
            }
            workOrderVo.setGmtCreateStr(DateUtil.convertDateToYMDHHmm(workOrderVo.getGmtCreate()));//开单时间
            workOrderVo.setExpectTimeStr(DateUtil.convertDateToYMDHHmm(workOrderVo.getExpectTime()));//期望交车时间
            workOrderVo.setPlanEndTimeStr(DateUtil.convertDateToYMDHHmm(workOrderVo.getPlanEndTime()));//计划完工时间
            workOrderProcessRelVos = BdUtil.do2bo4List(workOrderDTOResult.getData().getWorkOrderProcessRelDTOList(), WorkOrderProcessRelVo.class);
            for (WorkOrderProcessRelVo workOrderProcessRelVo : workOrderProcessRelVos) {
                workOrderProcessRelVo.setPlanStartTimeStr(DateUtil.convertDateToYMDHHmm(workOrderProcessRelVo.getPlanStartTime()));
                workOrderProcessRelVo.setPlanEndTimeStr(DateUtil.convertDateToYMDHHmm(workOrderProcessRelVo.getPlanEndTime()));
                workOrderProcessRelVoList.add(workOrderProcessRelVo);
            }
        }
        String vin = "";
        String yearPower = "";
        if (workOrderVo != null) {
            Long carId = workOrderVo.getCustomerCarId();
            //获得customer_car_信息
            CustomerCar customerCar = customerCarService.selectById(carId);
            if (customerCar != null) {
                vin = customerCar.getVin();
                yearPower = customerCar.getCarYear() + " " + customerCar.getCarGearBox();
            }
        }
        workOrderVo.setVin(vin);
        workOrderVo.setCarYearGearBox(yearPower);
        //施工单信息封装
        model.addAttribute("workOrderVo", workOrderVo);
        //工序信息封装
        model.addAttribute("workOrderProcessRelVoList", workOrderProcessRelVoList);
        Long orderId = 0L;
        if (workOrderVo.getId() != null)
            orderId = workOrderVo.getOrderId();
        //调用本地接口获取车辆外观信息
        orderPrecheckDetailsFacade.setOrderPrecheckDetailsModelByOrderId(model, orderId);
        List<OrderServices> orderServiceses = orderServicesService.queryOrderServiceList(orderId, shopId);
        List<OrderGoods> orderGoodses = orderGoodsService.queryOrderGoodList(orderId, shopId);
        model.addAttribute("orderServiceses", orderServiceses);
        model.addAttribute("orderGoodses", orderGoodses);
        Optional<OrderInfo> orderInfoOptional = orderService.getOrder(orderId, shopId);
        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            model.addAttribute("orderInfo", orderInfo);
        }
        return "yqx/page/magic/workshop/workorder_print";
    }

    /**
     * 获取门店的生产线
     *
     * @param request
     * @return
     */
    @RequestMapping("getProductLineByShopId")
    @ResponseBody
    public Result getProductLineByShopId(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<ProductionLineDTO>> result = null;
        try {
            result = rpcProductionLineService.selectLineByShopId(shopId);
            log.info("[获取门店的生产线] 获取门店生产线列表，shopId={}，result={}", shopId, result.isSuccess());
        } catch (Exception e) {
            log.error("获取门店的生产线失败", e);
            return Result.wrapErrorResult("", "获取门店的生产线失败");
        }
        return result;
    }

    /**
     * 面漆准备和备件准备
     *
     * @param id
     * @param paintPrepare
     * @return
     */
    @RequestMapping("updatePaintPrepareOrAccessoriesPrepare")
    @ResponseBody
    public Result updatePaintPrepareOrAccessoriesPrepare(@RequestParam(value = "id", required = true) Long
                                                                 id, @RequestParam(value = "paintPrepare", required = false) Integer
                                                                 paintPrepare, @RequestParam(value = "accessoriesPrepare", required = false) Integer accessoriesPrepare) {
        log.info("[面漆准备和备件准备] Begin updatePaintPrepareOrAccessoriesPrepare。id={},paintPrepare={},accessoriesPrepare={}", id, paintPrepare, accessoriesPrepare);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        WorkOrderDTO workOrderDTO = new WorkOrderDTO();
        workOrderDTO.setId(id);
        workOrderDTO.setPaintPrepare(paintPrepare);
        workOrderDTO.setAccessoriesPrepare(accessoriesPrepare);
        Result result = null;
        try {
            result = rpcWorkOrderService.updatePaintPrepareOrAccessoriesPrepare(workOrderDTO);
        } catch (Exception e) {
            log.error("更新面漆准备和备件准备失败", e);
            return Result.wrapErrorResult("", "更新面漆准备和备件准备失败");
        }
        if (!result.isSuccess()) {
            return Result.wrapErrorResult("", "操作失败");
        }
        log.info("[面漆准备和备件准备] End updatePaintPrepareOrAccessoriesPrepare.");
        if (paintPrepare != null) {
            //发送工序看板websocket消息
            boardFacade.sendMessage(userInfo.getShopId(), id);
        }
        return Result.wrapSuccessfulResult(result);
    }

    /**
     * 获取门店的班组信息
     *
     * @param shopId
     * @return
     */
    @RequestMapping("getTeamListByShopId")
    @ResponseBody
    public Result<List<TeamDTO>> getTeamListByShopId(Long shopId) {
        log.info("[获取班组信息] Begin getTeamListByShopId.shopId={}", shopId);
        TeamParam teamParam = new TeamParam();
        teamParam.setShopId(shopId);
        Result<List<TeamDTO>> result = null;
        try {
            result = rpcShopManagerExtService.getTeamList(teamParam);
        } catch (Exception e) {
            log.error("获取门店的班组信息失败", e);
            return Result.wrapErrorResult("", "获取门店的班组信息失败");
        }
        log.info("[获取班组信息] End getTeamListByShopId.");
        return result;
    }

    /**
     * 获取每日排班的班组
     *
     * @param shopId
     * @param lineId
     * @return
     */
    @RequestMapping("getTeamListByShopIdAndLineId")
    @ResponseBody
    public Result<List<TeamDTO>> getTeamListByShopIdAndLineId(Long shopId, Long lineId) {
        log.info("[获取每日排班班组信息] Begin getTeamListByShopIdAndLineId.shopId={}，lineId={}", shopId, lineId);

        Result<List<LineProcessManagerDTO>> result = null;
        try {
            result = rpcLineProcessManagerService.selectTeamByLineId(shopId, lineId);
        } catch (Exception e) {
            log.error("获取每日排班的班组失败", e);
            return Result.wrapErrorResult("", "获取每日排班的班组失败");
        }

        List<TeamDTO> teamDTOs = new ArrayList<>();
        Map<String, Object> teamMap = new HashMap<>();

        if (result.isSuccess() && result.getData() != null) {
            List<LineProcessManagerDTO> lineProcessManagerDTOs = result.getData();
            if (!CollectionUtils.isEmpty(lineProcessManagerDTOs)) {
                for (LineProcessManagerDTO lineProcessManagerDTO : lineProcessManagerDTOs) {
                    TeamDTO teamDTO = new TeamDTO();
                    teamDTO.setId(lineProcessManagerDTO.getTeamId());
                    teamDTO.setName(lineProcessManagerDTO.getTeamName());
                    teamMap.put(teamDTO.getId().toString(), teamDTO);
                }
            }
        }

        if (teamMap.size() > 0) {
            for (Object teamDTO : teamMap.values()) {
                teamDTOs.add((TeamDTO) teamDTO);
            }
        }
        /*if (CollectionUtils.isEmpty(teamDTOs)){
            log.warn("获取每日排班班组信息为空");
            return Result.wrapErrorResult("生产线没有排班","");
        }*/

        log.info("[获取班组信息] End getTeamListByShopId.size={}",teamDTOs.size());
        return Result.wrapSuccessfulResult(teamDTOs);
    }

    /**
     * 根据班组获取该组的技师
     *
     * @param teamId
     * @return
     */
    @RequestMapping("getShopManagerByTeamId")
    @ResponseBody
    public Result getShopManagerByTeamId(Long teamId,Long lineProcessId) {
        log.info("[根据班组获取该组的技师]Begin getShopManagerByTeamId.teamId={}", teamId);
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<LineProcessManagerDTO>> result = null;
        try {
            result = rpcLineProcessManagerService.selectManagerByTeamId(teamId,lineProcessId);
        } catch (Exception e) {
            log.error("根据班组获取该组的技师失败", e);
            return Result.wrapErrorResult("", "根据班组获取该组的技师失败");
        }
        log.info("[根据班组获取该组的技师] End getShopManagerByTeamId.");
        return result;
    }


    /**
     * 自动派工获取生产线工序(快修线和快喷线)
     *
     * @param lineId
     * @return
     */
    @RequestMapping("setAutoChooseParam")
    @ResponseBody
    public Result<AutoChooseParamVo> setAutoChooseParam(Long lineId) {
        log.info("[自动派工获取生产线] Begin setAutoChooseParam.lineId={}", lineId);
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<PLineProcessRelDTO>> relDTOResult = null;
        try {
            relDTOResult = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
        } catch (Exception e) {
            log.error("获取生产线工序关联失败", e);
            return Result.wrapErrorResult("", "获取生产线工序关联失败");
        }
        AutoChooseParamVo autoChooseParamVo = new AutoChooseParamVo();
        autoChooseParamVo.setLineId(lineId);
        if (relDTOResult.isSuccess() && relDTOResult.getData() != null) {
            List<ProcessWorkTimeVo> processWorkTimeVos = BdUtil.bo2do4List(relDTOResult.getData(), ProcessWorkTimeVo.class);
            List<ProcessManagerRelVo> processManagerRelVos = BdUtil.bo2do4List(relDTOResult.getData(), ProcessManagerRelVo.class);
            autoChooseParamVo.setProcessWorkTimeVoList(processWorkTimeVos);
            autoChooseParamVo.setProcessManagerRelVoList(processManagerRelVos);
        }
        log.info("[自动派工获取生产线]End setAutoChooseParam.");
        return Result.wrapSuccessfulResult(autoChooseParamVo);
    }





    /**
     * 事故线获取生产线工序
     *
     * @param lineId
     * @return
     */
    @RequestMapping("getPLineProcessRelList")
    @ResponseBody
    public Result<List<PLineProcessRelDTO>> getPLineProcessRelList(Long lineId) {
        log.info("【事故线获取生产线】Begin getPLineProcessRelList.lineId={}", lineId);
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<PLineProcessRelDTO>> relDTOResult = null;
        try {
            relDTOResult = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
        } catch (Exception e) {
            log.error("获取生产线工序关联失败", e);
            return Result.wrapErrorResult("", "获取生产线工序关联失败");
        }
        log.info("【事故线获取生产线】End getPLineProcessRelList.");
        return relDTOResult;
    }

}


