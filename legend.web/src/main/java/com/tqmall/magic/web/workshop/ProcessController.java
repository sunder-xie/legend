package com.tqmall.magic.web.workshop;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.facade.magic.BoardFacade;
import com.tqmall.legend.facade.magic.WorkOrderFacade;
import com.tqmall.legend.facade.magic.vo.*;
import com.tqmall.magic.object.param.workshop.ProcessParam;
import com.tqmall.magic.object.result.workshop.*;
import com.tqmall.magic.service.workshop.RpcProcessService;
import com.tqmall.magic.service.workshop.RpcTaskScanService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 业务场景：施工扫描 与 派工/排工接口
 * Created by shulin on 16/6/30.
 */
@Controller
@Slf4j
@RequestMapping("workshop/process")
public class ProcessController {


    @Autowired
    private WorkOrderFacade workOrderFacade;

    @Autowired
    private RpcProcessService rpcProcessService;

    @Autowired
    private RpcTaskScanService rpcTaskScanService;
    @Autowired
    private BoardFacade boardFacade;

    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;

    @RequestMapping("scanpage")
    public String toProcessScanPage() {
        return "yqx/page/magic/workshop/process_scan";
    }


    @RequestMapping(value = "/autoChoose", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ProcessManagerRelVo>> autoChooseAndCalculateTime(@RequestBody AutoChooseParamVo autoChooseParamVo, HttpServletRequest request) {
        if (autoChooseParamVo == null) {
            log.info("[自动派工-快修线] 参数不能为空！");
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[自动派工-快修线] Start shopId={},lineId={}", shopId, autoChooseParamVo.getLineId());
        Result<List<ProcessManagerRelVo>> result = workOrderFacade.autoChooseAndCalculateTime(shopId, autoChooseParamVo);
        log.info("[自动派工-快修线] End");
        return result;
    }

    @RequestMapping(value = "/accident/calculate", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ProcessManagerRelVo>> accidentAutoCalculateTime(@RequestBody AutoChooseParamVo autoChooseParamVo, HttpServletRequest request) {
        if (autoChooseParamVo == null) {
            log.info("[自动派工-事故线] 参数不能为空！autoChooseParamVo={}", JSONUtil.object2Json(autoChooseParamVo));
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[自动派工-事故线] Start 参数={}", JSONUtil.object2Json(autoChooseParamVo));
        Result<List<ProcessManagerRelVo>> result = workOrderFacade.accidentAutoCalculateTime(shopId, autoChooseParamVo);
        log.info("[自动派工-事故线] End");
        return result;
    }


    @RequestMapping(value = "/quick-paint/calculate", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ProcessManagerRelVo>> quickPaintAutoCalculateTime(@RequestBody AutoChooseParamVo autoChooseParamVo, HttpServletRequest request) {
        if (autoChooseParamVo == null) {
            log.info("[自动派工-快喷线] 参数不能为空！");
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[自动派工-快喷线] Start 参数={}", JSONUtil.object2Json(autoChooseParamVo));
        Result<List<ProcessManagerRelVo>> result = workOrderFacade.quickPaintAutoCalculateTime(shopId, autoChooseParamVo);
        log.info("[自动派工-快喷线] End");
        return result;
    }


    /**
     * 扫描施工单编号接口
     *
     * @param workOrderSn
     * @param request
     * @return 施工单ID
     */
    @RequestMapping("workorder/scan")
    @ResponseBody
    public Result<Integer> scanWorkOrderSn(String workOrderSn, HttpServletRequest request) {
        if (null == workOrderSn) {
            return Result.wrapErrorResult("", "施工单号不能为空！");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[施工扫描] Start 施工单扫描，shopId={},workOrderSn={}", shopId, workOrderSn);
        Result<Integer> result = null;
        try {
            result = rpcTaskScanService.scanWorkOrderSn(shopId, workOrderSn);
        } catch (Exception e) {
            log.error("施工单扫描失败", e);
            return Result.wrapErrorResult("", "施工单扫描失败");
        }
        log.info("[施工扫描] End 返回结果={}", result.isSuccess());
        if (result.isSuccess() && result.getData().compareTo(0) == 0) {
            boardFacade.sendMessage(shopId, workOrderSn, null);
        }
        return result;
    }


    /**
     * 扫描工牌号码接口
     *
     * @param cardNum
     * @return 员工扩展信息ID
     */
    @RequestMapping("manager/scan")
    @ResponseBody
    public Result<Long> scanCardNum(String cardNum, HttpServletRequest request) {
        if (null == cardNum) {
            return Result.wrapErrorResult("", "工牌号不能为空！");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        log.info("[施工扫描] Start 工牌扫描，shopId={},cardNum={}", shopId, cardNum);
        Result<Long> result = null;
        try {
            result = rpcTaskScanService.scanCardNum(shopId, cardNum);
        } catch (Exception e) {
            log.error("工牌扫描失败", e);
            return Result.wrapErrorResult("", "工牌扫描失败");
        }
        log.info("[施工扫描] End");
        return result;
    }

    /**
     * 扫描工序条码接口
     *
     * @param workOrderSn 施工单编号
     * @param cardNum     员工编号
     * @param barCode     工序编号
     * @return
     */
    @RequestMapping("process/scan")
    @ResponseBody
    public Result<String> scanProcessCode(String workOrderSn, String cardNum, String barCode, HttpServletRequest request) {
        if (null == workOrderSn) {
            return Result.wrapErrorResult("", "施工单号不能为空！");
        }
        if (null == cardNum) {
            return Result.wrapErrorResult("", "工牌号不能为空！");
        }
        if (null == barCode) {
            return Result.wrapErrorResult("", "工序编号不能为空！");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        log.info("[施工扫描] Start 工序开始扫描，shopId={},workOrderSn={},cardNum={},barCode={}", shopId, workOrderSn, cardNum, barCode);
        Result<String> result = null;
        try {
            result = rpcTaskScanService.scanProcessCode(shopId, workOrderSn, cardNum, barCode);
        } catch (Exception e) {
            log.error("工序开始扫描失败", e);
            return Result.wrapErrorResult("", "工序开始扫描失败");
        }
        log.info("[施工扫描] End");
        if (result.isSuccess()) {
            boardFacade.sendMessage(shopId, workOrderSn, null);
        }
        return result;
    }


    /**
     * 三期新增，扫描工牌号返回员工待修车辆列表信息
     *
     * @param cardNum
     * @param request
     * @return
     */
    @RequestMapping("card/scan")
    @ResponseBody
    public Result<WaitingCarsDTO> scanCardNumOnly(String cardNum, HttpServletRequest request) {
        if (null == cardNum) {
            return Result.wrapErrorResult("", "工牌号不能为空！");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        log.info("[施工扫描] Start 扫描工牌获取员工待修车辆列表，shopId={},cardNum={}", shopId, cardNum);
        Result<WaitingCarsDTO> result = null;
        try {
            result = rpcWorkOrderService.getWaitingCarList(shopId, cardNum);
        } catch (Exception e) {
            log.error("工牌扫描失败", e);
            return Result.wrapErrorResult("", "工牌扫描失败");
        }
        log.info("[施工扫描] End 扫描工牌获取员工待修车辆列表");
        return result;
    }


    //TODO 废弃
    @RequestMapping(value = "/selectlist", method = RequestMethod.GET)
    @ResponseBody
    public Result selectListProcess(HttpServletRequest request) {
        try {
            ProcessParam processParam = new ProcessParam();
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            processParam.setShopId(shopId);
            List<ProcessVO> ProcessVOList = Lists.newArrayList();
            log.info("[共享中心] DUBBO接口rpcProcessService.selectProcessList查询工序,参数:{}", processParam);
            Result<List<ProcessDTO>> ProcessDTOList = null;
            try {
                ProcessDTOList = rpcProcessService.selectProcessList(processParam);
            } catch (Exception e) {
                log.error("查询工序失败", e);
                return Result.wrapErrorResult("", "查询工序失败");
            }
            log.info("[共享中心] DUBBO接口rpcProcessService.selectProcessList查询工序,返回:{}");
            if (ProcessDTOList.isSuccess()) {
                List<ProcessDTO> paintLevelDTOs = ProcessDTOList.getData();
                for (ProcessDTO ProcessDTO : paintLevelDTOs) {
                    ProcessVO ProcessVO = new ProcessVO();
                    BeanUtils.copyProperties(ProcessDTO, ProcessVO);
                    ProcessVOList.add(ProcessVO);
                }
            }
            return Result.wrapSuccessfulResult(ProcessVOList);
        } catch (Exception e) {
            log.error("[查询工序信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PROCESS_LIST_ERROR.getCode(), LegendErrorCode.PROCESS_LIST_ERROR.getErrorMessage());
        }
    }
}
