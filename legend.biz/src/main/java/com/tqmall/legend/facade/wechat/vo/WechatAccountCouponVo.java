package com.tqmall.legend.facade.wechat.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.account.AccountCoupon;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by wushuai on 16/7/9.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WechatAccountCouponVo extends AccountCoupon {

    private String usedTimeStr;// 使用时间(核销时间)格式化字符串
    private String mobile;//优惠券对应客户的手机号
    private Long shareCount;//分享次数
    private Long registerCount;//分享注册用户数

    public String getUsedTimeStr(){
        if(getUsedStatus()!=null&&getUsedStatus()==1){
            if(gmtModified != null){
                return DateUtil.convertDateToYMD(gmtModified);
            }
        }
        return "";
    }
}
