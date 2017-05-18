package com.tqmall.legend.facade.magic.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by yanxinyin on 16/7/18.
 */

@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
public class PlateVO {
    private String lineName;
    private Long type;
    private Integer waitNumber;//等待数量
    private Integer workNumber;//工作中的数量
    private Integer rNumberByesterday;       //接车台次前天
    private Integer rNumberYesterday;        //接车台次昨天
    private Integer rNumberToday;           //接车台次今天
    private Integer rNumberMonth;           //接车台次当月
    private Double bNumberByesterday;       //负载台次前天
    private Double bNumberYesterday;        //负载台次昨天
    private Double bNumberToday;           //负载台次今天
    private Double bNumberMonth;           //负载台次当月
    private Date lastTime;           //最后车辆交车时间
    private Date planTime;           //预计交车时间

    private String lastTimeStr;
    private String planTimeStr;
}
