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
public class GatherCouponConfig extends BaseEntity {

    private Long shopId;//门店id
    private Long userId;//门店员工id
    private String userName;//门店员工姓名
    private Long gatherCustomerNoteId;//集客信息id
    private Date gatherTime;//集客行为时间
    private Long customerId;//客户id
    private Long customerCarId;//客户车辆id
    private Long couponInfoId;//优惠券类型id
    private Integer totalCouponNum;//赠送总数
    private Integer perAccountNum;//单账户最大领取数量
    private Integer gainNum;//已经领取数量
    private Integer accessStatus;//访问状态:0未访问,1已访问
}

