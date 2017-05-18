package com.tqmall.legend.entity.shop;

import lombok.Data;

/**
 * 提醒设置——前端参数对象
 */
@Data
public class NoteConfigureVo {

    // 预约提醒时间
    private String appointNoteFirstValue;
    // 预约提醒失效时间
    private String appointNoteInvalidValue;
    // 回访提醒时间
    private String visitNoteFirstValue;
    // 回访提醒失效时间
    private String visitNoteInvalidValue;
    // 保险提醒时间
    private String insuranceNoteFirstValue;
    // 保险提醒失效时间
    private String insuranceNoteInvalidValue;
    // 年检提醒时间
    private String auditNoteFirstValue;
    // 年检提醒失效时间
    private String auditNoteInvalidValue;
    // 保养第一次提醒时间
    private String keepupNoteFirstValue;
    // 保养第二次提醒时间
    private String keepupNoteSecondValue;
    // 保养提醒失效时间
    private String keepupNoteInvalidValue;
    // 客户生日提醒时间
    private String birthdyNoteFirstValue;
    // 流失客户时间
    private String lostCustomerNoteFirstValue;
    // 流失客户失效时间
    private String lostCustomerNoteInvalidValue;
}
