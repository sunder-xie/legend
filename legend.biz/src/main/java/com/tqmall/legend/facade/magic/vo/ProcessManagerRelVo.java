package com.tqmall.legend.facade.magic.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 自动派工中用于返回工序和人以及计划时间的对象
 * Created by shulin on 16/7/11.
 */
@Setter
@Getter
public class ProcessManagerRelVo implements Comparable {
    private Long id;
    private Long shopId;//所属门店id
    private Long lineId;
    private String lineName;
    private Long processId;//工序id
    private String processName;//工序名称
    private Long processSort;//排序号
    private String workTime;//施工时间
    private Date startTime;//计划开始时间
    private Date endTime;//计划结束时间
    private Long teamId;//班组id
    private String teamName;//班组名称
    private Long operatorId;//技师id
    private String operator;//技师名称

    private String startTimeStr;
    private String endTimeStr;

    List<LineProcessManagerVO> lineProcessManagerVOList;    //工序对应可施工人员信息

    @Override
    public int compareTo(Object o) {
        ProcessManagerRelVo processManagerRelVo = (ProcessManagerRelVo) o;
        Long otherProcessSort = processManagerRelVo.getProcessSort();
        return this.processSort.compareTo(otherProcessSort);
    }

    public String getStartTimeStr() {
        return DateUtil.convertDateToYMDHHmm(startTime);
    }

    public String getEndTimeStr() {
        return DateUtil.convertDateToYMDHHmm(endTime);
    }
}
