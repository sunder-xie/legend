package com.tqmall.legend.entity.lottery;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class Activity extends BaseEntity {

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

