package com.tqmall.legend.facade.magic.vo;

/**
 * Created by yanxinyin on 16/7/18.
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class CarVO {
    private  String license;
    private  Long  customerCarId;
    private  Date planStartTime;
    private  Long processId;//工序id
    private  Long managerId;//人员id
    private  Date planEndTime;
}
