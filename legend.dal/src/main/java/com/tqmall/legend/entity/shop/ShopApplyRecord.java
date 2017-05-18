package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by feilong.li on 16/10/17.
 */
@Setter
@Getter
public class ShopApplyRecord extends BaseEntity {

    private Long shopId;    //门店id
    private Long userGlobalId;  //UC用户id
    private String shopName;    //门店名称
    private String contactName; //联系人
    private String shopTel;     //门店电话
    private String contactMobile;   //联系人手机号码
    private String applyAccount;    //申请帐号
    private Integer applyStatus;    //申请状态 0 申请中 1 对接中 2 中断 3 已开通 4 测试通过
    private Integer applyType;  //申请类型 0 微信 1 支付宝
    private Integer applyWxMode;    //申请微信支付模式 0 非受理模式 1 受理模式
    private String failReason;      //失败原因

}
