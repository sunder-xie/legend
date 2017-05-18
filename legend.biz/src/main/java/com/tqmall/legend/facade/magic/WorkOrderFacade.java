package com.tqmall.legend.facade.magic;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.magic.vo.*;
import com.tqmall.magic.object.result.workshop.LineProcessManagerDTO;
import com.tqmall.magic.object.result.workshop.PLineProcessRelDTO;

import java.util.List;

/**
 * 施工单与工序
 * Created by shulin on 16/7/9.
 */
public interface WorkOrderFacade {

    /**
     * 根据施工单编号获取施工单详情
     *
     * @param workOrderSn 施工单编号
     * @return
     */
    Result<WorkOrderVo> getWorkOrderBySn(Long shopId, String workOrderSn);


    /**
     * 快修线派工并计算生产计划
     *
     * @param shopId
     * @param autoChooseParamVo
     * @return
     */
    Result<List<ProcessManagerRelVo>> autoChooseAndCalculateTime(Long shopId, AutoChooseParamVo autoChooseParamVo);

    /**
     * 事故线-计算生产计划
     *
     * @param autoChooseParamVo
     * @return
     */
    Result<List<ProcessManagerRelVo>> accidentAutoCalculateTime(Long shopId,AutoChooseParamVo autoChooseParamVo);

    /**
     * 快喷线-计算生产计划
     *
     * @param autoChooseParamVo
     * @return
     */
    Result<List<ProcessManagerRelVo>> quickPaintAutoCalculateTime(Long shopId,AutoChooseParamVo autoChooseParamVo);




}
