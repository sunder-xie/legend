package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2016/12/16.
 */
@Getter
@Setter
public class CustomerTypeNum {
    private int hasMobileNum; //有车主电话客户
    private int hasMemberNum; //会员客户
    private int noneMemberNum; //非会员客户
    private int sleepNum; //休眠客户数
    private int lostNum; //流失客户数
    private int activeNum; //活跃客户数

    private int auditingNoteNum; //年检提醒数
    private int maintainNoteNum; //保养提醒数
    private int insuranceNoteNum; //保险提醒数
    private int birthdayNoteNum; //生日提醒数
    private int lostNoteNum; // 流失客户提醒数
    private int visitNoteNum;// 回访提醒数
}
