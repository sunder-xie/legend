package com.tqmall.legend.facade.insurance.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zwb on 16/8/19.
 */
@Setter
@Getter
public class InsuranceFormVo {
    /**主键ID**/
    private Integer id;

    /**创建时间**/
    private java.util.Date gmtCreate;

    /**创建人ID**/
    private Integer creator;

    /**更新时间**/
    private java.util.Date gmtModified;

    /**更新人ID**/
    private Integer modifier;

    /**是否删除,Y删除,N未删除**/
    private String isDeleted;

    /**保单ID, 关联insurance_basic.id**/
    private Integer insuranceBasicId;

    /**投保单号:安心保费计算时候返回**/
    private String outerInsuranceApplyNo;

    /**保单号:缴费完成返回**/
    private String outerInsuranceFormNo;

    /**保险类别:1表示交强险,2表示商业险**/
    private Integer insuranceType;

    /**配送保单类型 0:监制保单(纸质保单) 1:电子保单**/
    private Integer receiverType;

    /**是否脱保, 0:未脱保； 1:脱保**/
    private Integer hasInsuranced;

    /**产品代码**/
    private String packageProductNo;

    /**起保日期**/
    private java.util.Date packageStartTime;

    /**终保日期**/
    private java.util.Date packageEndTime;

    /**保费比例，和安心结算，以投保所在城市为单位配置,是安心编码**/
    private java.math.BigDecimal insuredRate;

    /**奖励金比例，和云修店结算，以维修店为单位配置**/
    private java.math.BigDecimal rewardRate;

    /**给门店奖励金时扣除的税率**/
    private java.math.BigDecimal insuredTax;

    /**保费,投保人投保的保费**/
    private java.math.BigDecimal insuredFee;

    /**保费分成，保险公司给淘汽**/
    private java.math.BigDecimal insuredRoyalty;

    /**奖励金，淘汽给代理卖保险人**/
    private java.math.BigDecimal rewardFee;

    /**退保状态, 0:未退保； 1:已退保**/
    private Integer quitStatus;

    /**退保时间**/
    private java.util.Date quitTime;

    /**保单状态, 0:暂存（点击保费计算） 1:已提交 2:待缴费 3:已缴费**/
    private Integer insureStatus;

    /**缴费时间**/
    private java.util.Date payTime;

    /**确认打款, 0:未打款 1:已打款**/
    private Integer confirmAgentMoney;

    /**确认打款时间**/
    private java.util.Date confirmAgentMoneyTime;

    /**确认收款, 0:未收款 1:已收款**/
    private Integer confirmInsuranceMoney;

    /**奖励金生效状态, 0:奖励金未生效； 1:奖励金已生效**/
    private Integer rewardFeeEffectived;

    /**确认收款时间**/
    private java.util.Date confirmInsuranceMoneyTime;

    private InsuranceBasicVo basicVo;

    private List<InsuranceItemVo> itemVoList;

    /**业务来源**/
    private String companyName;
    /**服务费率**/
    private BigDecimal serviceRate;
    /**服务费**/
    private BigDecimal serviceFee;

    /**与门店计算奖励金的税率**/
    private BigDecimal taxRate;
    private BigDecimal deductibleAmount;// 优惠券优惠金额
    private Boolean uploadImg;
    private String orderSn;  //淘汽内部保险单号
}
