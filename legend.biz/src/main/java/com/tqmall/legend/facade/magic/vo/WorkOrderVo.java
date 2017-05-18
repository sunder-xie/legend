package com.tqmall.legend.facade.magic.vo;

import java.util.Date;
import java.util.List;

import com.tqmall.legend.facade.order.vo.OrderPrecheckDetailsVo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by shulin on 16/7/9.
 */
@Setter
@Getter
public class WorkOrderVo {
    private Long id;
    protected Date gmtCreate;
    private String gmtCreateStr;//提供给前端的开单时间
    private Long shopId;//所属门店id
    private Long orderId;//工单id
    private String orderSn;//工单编号
    private Long customerCarId;//客户车辆id
    private String carLicense;//车牌号
    private String carInfo;//车型信息
    private String contactName;//联系人名称
    private String contactMobile;//联系人手机电话
    private String serviceSa;//服务顾问
    private Date planStartTime;//计划开始时间
    private Date planEndTime;//计划结束时间
    private String planStartTimeStr;//计划开始时间给前端页面显示
    private String planEndTimeStr;//计划结束时间给前端页面显示
    private Date realStartTime;//实际开始时间
    private Date realEndTime;//实际结束时间
    private Long lineId;//生产线id
    private String lineName;//生产线名称
    private String status;//施工单状态：dsg:待施工，sgz:施工中，sgzd:施工中断，ywg:已完工
    private String processStatus;
    private String operator;//技师名称
    private String workTime;//施工时间
    private String workOrderSn;//施工单编号
    private String carColor;//车身颜色
    private Date expectTime;//期望交车时间
    private String expectTimeStr;//期望交车时间提供给前端
    private String remark;//备注
    private String vin;//vin码
    private String carYearGearBox;//年款排量
    private Integer produceLineType;//生产线type给前端
    private List<WorkOrderProcessRelVo> workOrderProcessRelVoList;//工序列表
    private WorkOrderProcessRelVo workOrderProcessRelVo;//单个工序信息,legend暂时没用到

}
