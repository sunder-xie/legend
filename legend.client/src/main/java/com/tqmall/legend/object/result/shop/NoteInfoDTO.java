package com.tqmall.legend.object.result.shop;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.Date;

/**
 * Created by xin on 16/8/31.
 */
@Data
public class NoteInfoDTO extends BaseEntityDTO {
    private Long shopId;//   门店id
    private Long relId;//   关系id 工单id、预约单id、活动id
    private Long customerId = Long.valueOf("0");//   客户id
    private Long customerCarId = Long.valueOf("0");//   客户车辆id
    private Integer noteWay = Integer.valueOf("0");//   提醒方式0 短信 1 电话 2 优惠劵
    private Integer noteType = Integer.valueOf("0");//   提醒类型 0 预约单提醒 1 保养到期提醒 2 保险到期提醒 3 年检到期提醒 4 回访到期提醒 5 生日提醒 6 流失客户提醒
    private Integer noteFlag = Integer.valueOf("0");//   提醒标记 0 未提醒 1 已提醒 2 失效
    private Date notePastTime;//   提醒过期时间
    private String carLicense;//   车牌
    private String customerName;//   客户姓名
    private String mobile;//   手机号
    private String contactName;//   联系人
    private String contactMobile;//   联系电话
    private String operator;//   操作处理人
    private Date operatorTime;//   操作处理时间
    private Date noteTime;
}
