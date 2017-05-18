package com.tqmall.legend.facade.wechat.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.CouponDTO;

/**
 * Created by wushuai on 16/7/9.
 */
@Setter
@Getter
public class WechatCfgCouponVo extends CouponDTO {
    private String startTimeStr;//领取开始时间格式化字符串
    private String endTimeStr;//领取结束时间格式化字符串
    private Integer isLongTime;//是否长期开通


    public String getStartTimeStr() {
        if(getStartTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getStartTime());
    }

    public String getEndTimeStr() {
        if(getEndTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getEndTime());
    }

    public Integer getIsLongTime(){
        if(getStartTime()==null){
            return 1;
        }
        return 0;
    }
}
