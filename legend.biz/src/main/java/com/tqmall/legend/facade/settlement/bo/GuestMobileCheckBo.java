package com.tqmall.legend.facade.settlement.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/3/9.
 * 使用其他车主的会员卡、优惠券，需要传手机号和验证码
 */
@Getter
@Setter
public class GuestMobileCheckBo {
    private String guestMobile;//其他车主的手机号
    private String code;//验证码
}
