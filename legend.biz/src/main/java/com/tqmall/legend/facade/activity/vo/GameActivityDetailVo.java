package com.tqmall.legend.facade.activity.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.GameDetailsDTO;

/**
 * 微信游戏活动详情(包含优惠券)
 * Created by wushuai on 16/8/25.
 */
@Setter
@Getter
public class GameActivityDetailVo extends GameDetailsDTO {
    private String tplStartTimeStr;//模版活动开始时间格式化字符串
    private String tplEndTimeStr;//模版活动结束时间字符串
    private String startTimeStr;//门店活动开始时间格式化字符串
    private String endTimeStr;//门店活动结束时间字符串
    private Integer autoMenuSuccess;//自动生成菜单状态:0失败,1成功

    public String getStartTimeStr() {
        if(getStartTime()==null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getStartTime());
    }

    public String getEndTimeStr() {
        if(getEndTime()==null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getEndTime());
    }

    public String getTplStartTimeStr() {
        if(getTplStartTime()==null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getTplStartTime());
    }

    public String getTplEndTimeStr() {
        if(getTplEndTime()==null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getTplEndTime());
    }
}
