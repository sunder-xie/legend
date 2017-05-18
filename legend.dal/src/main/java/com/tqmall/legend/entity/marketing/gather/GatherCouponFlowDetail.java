package com.tqmall.legend.entity.marketing.gather;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wushuai on 16/12/16.
 */
@Getter
@Setter
public class GatherCouponFlowDetail extends BaseEntity {

    private Long shopId;//门店id
    private Long gatherConfigId;//集客优惠券配置id
    private Long gatherCustomerNoteId;//集客信息id
    private Date gatherTime;//集客行为时间
    private Long referUserId;//源门店员工id
    private Long referCustomerId;//源客户id
    private Long referCustomerCarId;//源客户车辆id
    private String referCustomerName;//源客户名称
    private Long customerId;//领券客户id
    private String customerMobile;//领券客户手机号
    private Long accountCouponId;//优惠券实体id
    private Integer isNew;//是否新客户:0否1是

}
