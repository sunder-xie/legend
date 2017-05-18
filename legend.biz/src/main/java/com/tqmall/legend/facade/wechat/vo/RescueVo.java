package com.tqmall.legend.facade.wechat.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.RescueDTO;

/**
 * Created by pituo on 16/8/18.
 */
public class RescueVo extends RescueDTO {
    private String applyTimeStr;//申请时间

    public String getApplyTimeStr() {
        if(getGmtCreate() == null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getGmtCreate());
    }
}
