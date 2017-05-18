package com.tqmall.legend.bi.entity.cube;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zsy on 17/3/20.
 */
@Getter
@Setter
public class CustomerInfo extends BaseEntity {

    private Long shopId;//门店id
    private Long customerId;//客户id
    private Long customerCarId;//客户车辆id
    private String carLicense;//车牌
    private String contactName;//联系人
    private String contactMobile;//联系电话
    private Long carBrandId;//车品牌id
    private String carBrand;//车品牌
    private Long carSeriesId;//车系列id
    private String carSeries;//车系列
    private String carLevel;//车辆级别
    private Date lastPayTime;//最近结算时间
    private Long memberLevelId;//会员级别id
    private String memberLevel;//会员级别名
    private Long carModelId;//车型id
    private String carModel;//车型
    private BigDecimal totalAmount;//累计消费金额
    private Long totalNumber;//累计消费次数
    private Integer carLevelTag;//'客户级别打标0
    private Date appointCreateTime;//预约单下单时间
    private Date appointTime;//预约时间
    private String appointContent;//预约内容
    private Long mileage;//行驶里程
    private Date noteKeepupTime;//保养到期时间
    private Date noteInsuranceTime;//保险到期时间
    private Date noteAuditingTime;//年检到期时间
    private Date noteVisitTime;//计划回访时间
    private Date birthday;//客户生日
    private String customerName;//客户姓名
    private String mobile;//手机号
    private Long appointId;//预约单id
    private Long lastOrderId;//最近工单id
    private BigDecimal lastOrderAmount;//最近工单消费金额
    private Long arriveNumbers;//到店消费的累计次数（包括无效订单）
    private BigDecimal signTotalAmount;//挂账总金额
    private Long signTotalOrderNumbers;//挂账工单数量
    private Long userId;//员工id

}

