package com.tqmall.legend.web.insurance.vo;

import lombok.Data;

/**
 * Created by zxg on 17/2/7.
 * 10:13
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class SettleCheckDetailSearchVO {
    Integer agentId;            //门店id，冗余自insurance_basic,精确查找
    Integer cooperationModeId;  //模式，例如模式一：买保险送服务包,精确查找
    Integer settleRuleType;     //对账类型：1:现金 2:奖励金 3:服务包,来自mana字典表,精确查找
    String vehicleSn;           //模糊查找,倒序排序2
    String carOwnerName;        //模糊查找
    Integer settleProjectId;      //结算项目id,精确查找
    String insuranceCompanyName;  //保险公司名称,模糊查找
    String settleConditionTimeStart;     //结算条件值,范围查找,倒序排序1
    String settleConditionTimeEnd;     //结算条件值,范围查找

    Boolean isPackageCheck = false;//是否是服务包对账
}
