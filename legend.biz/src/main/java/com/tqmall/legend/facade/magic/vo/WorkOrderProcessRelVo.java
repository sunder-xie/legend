package com.tqmall.legend.facade.magic.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shulin on 16/7/9.
 */
@Setter
@Getter
public class WorkOrderProcessRelVo implements Comparable {
    private Long id;
    private Long shopId;//所属门店id
    private Long workOrderId;//施工单id
    private Long processId;//工序id
    private String processName;//工序名称
    private String workTime;//施工时间
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Date breakStartTime;//中断开始时间
    private Date breakEndTime;//中断结束时间
    private String breakReason;//中断原因
    private Long teamId;//班组id
    private String teamName;//班组名称
    private Long operatorId;//技师id
    private String operator;//技师名称
    private Long realOperatorId;//实际技师id
    private String realOperator;//实际技师名称
    private String status;//'工序状态：dsg:待施工，sgz:施工中，sgzd:施工中断，sgwc:施工完成
    private Long processSort;//排序号
    private Date planStartTime;//计划开始时间
    private Date planEndTime;//计划结束时间
    //前端页面显示
    private String startTimeStr;
    private String endTimeStr;
    private String planStartTimeStr;
    private String planEndTimeStr;

    @Override
    public int compareTo(Object o) {
        WorkOrderProcessRelVo workOrderProcessRelVo = (WorkOrderProcessRelVo) o;
        Long otherProcessSort = workOrderProcessRelVo.getProcessSort();
        return this.processSort.compareTo(otherProcessSort);
    }
}
