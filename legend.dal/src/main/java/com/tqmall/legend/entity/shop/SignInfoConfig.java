package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;

/**
 * Created by zhoukai on 16/3/14.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class SignInfoConfig {

    private Long shopId;
    private Integer confType;
    private Time signInTime;//上班时间
    private Time signOffTime;//下班时间
    private Time noonBreakStartTime;//午休开始时间
    private Time noonBreakEndTime;//午休结束时间
    private Long userid;
}
