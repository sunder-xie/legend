package com.tqmall.legend.object.param.settlement;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 17/3/9.
 */
@Getter
@Setter
public class GuestMobileCheckParam implements Serializable{
    private static final long serialVersionUID = -1258989325303839244L;
    /**
     * 使用其他车主的会员卡、优惠券，需要传手机号和验证码
     */
    private String guestMobile;//其他车主的手机号
    private String code;//验证码
    private Long shopId;//门店id

}