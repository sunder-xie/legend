package com.tqmall.legend.entity.setting;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lilige on 16/11/2.
 * 派工单打印设置的VO结构
 */
@Getter
@Setter
public class ConfigFieldVO {
    private List<PrintConfigField> customerVO;//车辆信息
    private List<PrintConfigField> carInfoVO;//车辆信息
    private List<PrintConfigField> fixVO;//维修信息
    private List<PrintConfigField> afterFixVO;//其他信息
}

