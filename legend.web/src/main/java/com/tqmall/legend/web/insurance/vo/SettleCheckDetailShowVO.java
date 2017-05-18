package com.tqmall.legend.web.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zxg on 17/2/4.
 * 09:57
 * no bug,以后改代码的哥们，祝你好运~！！
 * 对账分页详情vo
 */
@Setter
@Getter
public class SettleCheckDetailShowVO {
    //服务包返利的project_id
    public static final Integer SETTLE_PACKAGE_PROJECT_ID = 3;

    // 当前分页的总金额
    private BigDecimal pageAmount;

    private Integer id;
    private String bizSn;               //各结算项目的业务唯一主键,基础信息的获得,加settle_project_id
    private String insuranceOrderSn;    //真实保单唯一root，冗余自insurance_basic.insurance_order_sn
    private Integer agentId;            //门店id，冗余自insurance_basic
    private Integer settleRuleType;     //对账类型：1:现金 2:奖励金 3:服务包,来自mana字典表
    private Integer cooperationModeId;  //模式，例如模式一：买保险送服务包
    private String cooperationModeName; //模式名称
    private String carOwnerName;
    private String vehicleSn;          //车牌号,冗余自insurance_virtual_basic.vehicle_sn
    private Integer insuranceCompanyId;   //保险公司id
    private String insuranceCompanyName;  //保险公司名称
    private Integer settleProjectId;      //结算项目id
    private String settleProjectName;       //结算项目名称,例如-商业险返利
    private Integer settleConditionId;      //结算条件id
    private String settleConditionName;     //结算条件名称，例如-起保时间
    private String settleConditionTime;     //结算条件值
    private String settleRate;          //结算比例
    private String settleFee;           //结算金额
    private Integer settleFeeStatus;    //结算状态, 0-未结算 1-已结算
    private String auditPeopleName;     //审核人员
    private String auditTime;           //审核时间
    private String settlePeopleName;    //结算人员
    private String settleTime;          //结算时间

    //服务包名称
    private String settlePackageName;
    //服务包状态, 0--待发货, 1-配送中 2-已签收
    private Integer settlePackageStatus;

    public void setSettlePackageName(String settlePackageName) {
        this.settlePackageName = settlePackageName;
    }

    public void setSettlePackageStatus(Integer settlePackageStatus) {
        this.settlePackageStatus = settlePackageStatus;
    }
}
