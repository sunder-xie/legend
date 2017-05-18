package com.tqmall.legend.facade.activity.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import lombok.Data;

/**
 * Created by wushuai on 16/8/2.
 */
@Data
public class ActivitytemplateVo extends ActivityTemplate {
    private String timeLimitStr;//活动时间范围

    public String getTimeLimitStr() {
        if(getStartTime()!=null &&getEndTime()!=null) {
            return DateUtil.convertDateToYMD(getStartTime()) +"至" +DateUtil.convertDateToYMD(getEndTime());
        }
        return "";
    }
}
