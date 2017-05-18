package com.tqmall.legend.entity.marketing.gather;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2016/12/17.
 */
@Getter
@Setter
public class GatherCustomerNote extends BaseEntity {

    private Long shopId;//店铺id
    private Long userId;//员工id
    private String allotSn;//分配批次号，uuid
    private String userName;//员工姓名
    private String creatorName;//创建人姓名
    private Long customerId;//客户id
    private Long customerCarId;//车辆id
    private String carLicense;//车牌
    private String customerName;//客户姓名
    private String customerMobile;//手机号
    private Integer gatherType;//集客方式：0:盘活客户，1:老客户拉新
    private Integer operateType;//操作方式：0:电话回访，1:短信回访，2:微信优惠券
    private Long relId;//回访或短信的关联id
    private Long accountCouponId;//账户优惠券id

}

