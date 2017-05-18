package com.tqmall.legend.object.result.activity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dingbao on 16/9/23.
 */
@Data
public class ActivityDTO implements Serializable {
    private String actName;
    private String slogan;
    private String remark;
    private String imgUrl;
    private Date startTime;
    private Date endTime;
    private Integer actStatus;
    private Date withdrawStartTime;
    private Date withdrawEndTime;
    private String actUrl;
}
