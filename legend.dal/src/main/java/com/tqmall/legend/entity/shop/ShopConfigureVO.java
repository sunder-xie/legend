package com.tqmall.legend.entity.shop;

/**
 * Created by litan on 15-5-26.
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopConfigureVO extends ShopConfigure {
    //工单打印
    private String orderComment;
    //结算打印
    private String settleComment;
    //预约提醒
    private String appointNotice;
    //预约提醒失效值
    private Integer appointNoticeInvalid;
    //回访提醒
    private String feedbackNotice;
    //回访提醒失效值
    private Integer feedbackNoticeInvalid;
    //保险提醒
    private String insuranceNotice;
    //保险提醒失效值
    private Integer insuranceNoticeInvalid;
    //年检提醒
    private String carAuditingNotice;
    //年检提醒失效值
    private Integer carAuditingNoticeInvalid;
    //保养到期
    private String keepupNotice;
    //保养到期第二个值
    private Integer keepupNoticeSecond;
    //保养到期失效值
    private Integer keepupNoticeInvalid;
    //生日提醒
    private String birthdayNotice;
    //新流失客户提醒
    private String lostCustomerNotice;
    //新流失客户提醒失效值
    private Integer lostCustomerNoticeInvalid;
}
