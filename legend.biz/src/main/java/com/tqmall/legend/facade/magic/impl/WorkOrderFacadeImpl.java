package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.enums.magic.WorkOrderStatusEnum;
import com.tqmall.legend.facade.magic.WorkOrderFacade;
import com.tqmall.legend.facade.magic.WorkTimeFacade;
import com.tqmall.legend.facade.magic.vo.AutoChooseParamVo;
import com.tqmall.legend.facade.magic.vo.ProcessManagerRelVo;
import com.tqmall.legend.facade.magic.vo.ProcessWorkTimeVo;
import com.tqmall.legend.facade.magic.vo.WorkOrderProcessRelVo;
import com.tqmall.legend.facade.magic.vo.WorkOrderVo;
import com.tqmall.legend.facade.magic.vo.WorkTimeVo;
import com.tqmall.magic.object.result.workshop.ProcessManagerRelDTO;
import com.tqmall.magic.object.result.workshop.ProcessWorkTimeDTO;
import com.tqmall.magic.object.result.workshop.WorkOrderDTO;
import com.tqmall.magic.object.result.workshop.WorkTimeDTO;
import com.tqmall.magic.service.workshop.RpcAutoAllocateService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 施工单
 * Created by shulin on 16/7/9.
 */
@Service
@Slf4j
public class WorkOrderFacadeImpl implements WorkOrderFacade {
    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;
    @Autowired
    private RpcAutoAllocateService rpcAutoAllocateService;
    @Autowired
    private WorkTimeFacade workTimeFacade;

    @Override
    public Result<WorkOrderVo> getWorkOrderBySn(Long shopId, String workOrderSn) {
        WorkOrderDTO workOrderDTO = null;
        try {
            log.info("[施工单] Start 调用DUBBO接口通过编号获取施工单详情,shopId={},workOrderSn={}", shopId, workOrderSn);
            workOrderDTO = rpcWorkOrderService.getWorkOrderByWorkOrderSn(shopId, workOrderSn).getData();
            log.info("[施工单] End 调用DUBBO接口通过编号获取施工单详情，施工单ID={}", workOrderDTO.getId());
        } catch (Exception e) {
            log.error("[施工单] WorkOrderFacadeImpl-施工单查询失败！workOrderSn={}", workOrderSn);
            return Result.wrapErrorResult("", "施工单查询失败！");
        }
        if (null == workOrderDTO) {
            log.info("[施工单] WorkOrderFacadeImpl-施工单不存在！workOrderSn={}", workOrderSn);
            return Result.wrapErrorResult("", "施工单不存在！");
        }
        String workOrderStatus = workOrderDTO.getStatus();
        if (WorkOrderStatusEnum.DSG.getCode().equals(workOrderStatus) || WorkOrderStatusEnum.SGZ.getCode().equals(workOrderStatus)) {
            WorkOrderVo workOrderVo = BdUtil.bo2do(workOrderDTO, WorkOrderVo.class);
            List<WorkOrderProcessRelVo> workOrderProcessRelVoList = BdUtil.bo2do4List(workOrderDTO.getWorkOrderProcessRelDTOList(), WorkOrderProcessRelVo.class);
            workOrderVo.setWorkOrderProcessRelVoList(workOrderProcessRelVoList);
            return Result.wrapSuccessfulResult(workOrderVo);
        } else {
            return Result.wrapErrorResult("", "施工单扫描失败，施工单处于" + WorkOrderStatusEnum.getNameByCode(workOrderStatus) + "状态！");
        }
    }

    @Override
    public Result<List<ProcessManagerRelVo>> autoChooseAndCalculateTime(Long shopId, AutoChooseParamVo autoChooseParamVo) {
        Long lineId = autoChooseParamVo.getLineId();
        if (lineId == null) {
            return Result.wrapErrorResult("", "参数错误：生产线ID不能为空！");
        }
        List<ProcessWorkTimeVo> realWorkTimeList = autoChooseParamVo.getProcessWorkTimeVoList();
        if (realWorkTimeList == null) {
            return Result.wrapErrorResult("", "参数错误：工序时间队列不能为空！");
        }
        List<ProcessWorkTimeDTO> realWorkTimeDTOList = BdUtil.bo2do4List(realWorkTimeList, ProcessWorkTimeDTO.class);
        WorkTimeVo workTimeVo = workTimeFacade.initWorkTime(shopId);
        WorkTimeDTO workTimeDTO = BdUtil.bo2do(workTimeVo, WorkTimeDTO.class);
        log.info("[自动派工-快修线] Start 调用DUBBO接口自动派工，参数：shopId={},lineId={}", shopId, lineId);
        Result<List<ProcessManagerRelDTO>> processManagerRelDTOListResult = null;
        try {
            processManagerRelDTOListResult = rpcAutoAllocateService.autoChooseManagers(shopId, lineId, realWorkTimeDTOList, workTimeDTO);
            log.info("[自动派工-快修线] End 调用DUBBO接口自动派工,flag={}", processManagerRelDTOListResult.isSuccess());
        } catch (Exception e) {
            log.error("[自动派工-快修线] 调用DUBBO接口自动派工，异常，e={}", e);
            return Result.wrapErrorResult("", "自动派工失败！");
        }
        if (!processManagerRelDTOListResult.isSuccess()) {
            return Result.wrapErrorResult("", processManagerRelDTOListResult.getMessage());
        }
        List<ProcessManagerRelDTO> processManagerRelDTOList = processManagerRelDTOListResult.getData();
        List<ProcessManagerRelVo> processManagerRelVoList = null;
        if (!CollectionUtils.isEmpty(processManagerRelDTOList)) {
            processManagerRelVoList = BdUtil.bo2do4List(processManagerRelDTOList, ProcessManagerRelVo.class);
            return Result.wrapSuccessfulResult(processManagerRelVoList);
        } else {
            return Result.wrapErrorResult("", "自动派工失败！");
        }

    }

    @Override
    public Result<List<ProcessManagerRelVo>> accidentAutoCalculateTime(Long shopId, AutoChooseParamVo autoChooseParamVo) {
        List<ProcessManagerRelVo> processManagerRelVoListT = autoChooseParamVo.getProcessManagerRelVoList();
        List<ProcessWorkTimeVo> realProcessWorkTime = autoChooseParamVo.getProcessWorkTimeVoList();
        if (CollectionUtils.isEmpty(processManagerRelVoListT) || CollectionUtils.isEmpty(realProcessWorkTime)) {
            log.info("[自动排工-事故线] 参数不能为空！}");
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        List<ProcessManagerRelDTO> processManagerRelDTOList = new ArrayList<>();
        Long lineId = autoChooseParamVo.getLineId();
        for (ProcessManagerRelVo processManagerRelVo : processManagerRelVoListT){
            ProcessManagerRelDTO tmp = BdUtil.bo2do(processManagerRelVo,ProcessManagerRelDTO.class);
            tmp.setLineId(lineId);
            processManagerRelDTOList.add(tmp);
        }
        List<ProcessWorkTimeDTO> processWorkTimeDTOList = BdUtil.bo2do4List(realProcessWorkTime, ProcessWorkTimeDTO.class);
        WorkTimeVo workTimeVo = workTimeFacade.initWorkTime(shopId);
        WorkTimeDTO workTimeDTO = BdUtil.bo2do(workTimeVo, WorkTimeDTO.class);
        log.info("[自动排工-事故线] Start 调用DUBBO接口获取自动排工");
        List<ProcessManagerRelDTO> processManagerRelDTOs = null;
        try {
            Result<List<ProcessManagerRelDTO>> result = rpcAutoAllocateService.calculateAccidentLinePlanTime(processManagerRelDTOList, processWorkTimeDTOList, workTimeDTO);
            log.info("[自动排工-事故线] End 调用DUBBO接口获取自动排工,flag={}", result.isSuccess());
            processManagerRelDTOs = result.getData();
        } catch (Exception e) {
            log.error("[自动排工-事故线] 调用DUBBO接口获取自动排工结果异常，e={}", e);
            return Result.wrapErrorResult("", "自动派工失败！");
        }
        List<ProcessManagerRelVo> processManagerRelVoList = null;
        if (!CollectionUtils.isEmpty(processManagerRelDTOs)) {
            processManagerRelVoList = BdUtil.bo2do4List(processManagerRelDTOs, ProcessManagerRelVo.class);
        }
        return Result.wrapSuccessfulResult(processManagerRelVoList);
    }

    @Override
    public Result<List<ProcessManagerRelVo>> quickPaintAutoCalculateTime(Long shopId, AutoChooseParamVo autoChooseParamVo) {
        List<ProcessManagerRelVo> processManagerRelVoListT = autoChooseParamVo.getProcessManagerRelVoList();
        List<ProcessWorkTimeVo> realProcessWorkTime = autoChooseParamVo.getProcessWorkTimeVoList();
        if (processManagerRelVoListT == null || realProcessWorkTime == null) {
            log.info("[自动排工-快喷线] 参数不能为空！");
            return Result.wrapErrorResult("", "参数不能为空！");
        }
        List<ProcessManagerRelDTO> processManagerRelDTOList = BdUtil.bo2do4List(processManagerRelVoListT, ProcessManagerRelDTO.class);
        List<ProcessWorkTimeDTO> processWorkTimeDTOList = BdUtil.bo2do4List(realProcessWorkTime, ProcessWorkTimeDTO.class);
        WorkTimeVo workTimeVo = workTimeFacade.initWorkTime(shopId);
        WorkTimeDTO workTimeDTO = BdUtil.bo2do(workTimeVo, WorkTimeDTO.class);
        List<ProcessManagerRelDTO> processManagerRelDTOListTemp = null;
        log.info("[自动排工-快喷线] Start 调用DUBBO接口获取自动排工结果,shopId={},lineId={}",shopId,autoChooseParamVo.getLineId());
        try {
            Result<List<ProcessManagerRelDTO>> result= rpcAutoAllocateService.calculateQuickLinePlanTime(processManagerRelDTOList, processWorkTimeDTOList, workTimeDTO);
            log.info("[自动排工-快喷线] End 调用DUBBO接口获取自动排工结果，flag={}",result.isSuccess());
            processManagerRelDTOListTemp = result.getData();
        } catch (Exception e) {
            log.error("[自动排工-快喷线] 调用DUBBO接口进行快喷线自动派工失败，lineId={},e={}", autoChooseParamVo.getLineId(), e);
            return Result.wrapErrorResult("", "自动派工失败！");
        }
        List<ProcessManagerRelVo> processManagerRelVoList = BdUtil.bo2do4List(processManagerRelDTOListTemp, ProcessManagerRelVo.class);
        return Result.wrapSuccessfulResult(processManagerRelVoList);
    }


}
