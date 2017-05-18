package com.tqmall.legend.facade.marketing.gather.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 车主领券结果
 * Created by wushuai on 16/12/20.
 */
@Getter
@Setter
public class ReceiveCouponResultVo {
    private Integer resultCode;//领取结果:0成功,1没有可以领取的优惠券,2超过当前账户领取上限
    private String mobile;//领券手机号
    private Integer perAccountNum;//单账户领券数量上限
    private Integer surplusNum;//优惠券剩余数量
    private Integer isSourceCustomer;//当前领券车主是否为门店顾问的目标营销客户:0否,1是
}
