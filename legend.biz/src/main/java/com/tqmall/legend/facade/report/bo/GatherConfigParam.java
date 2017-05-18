package com.tqmall.legend.facade.report.bo;

import com.tqmall.cube.shop.result.shop.EmployeePerformanceConfigVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tanghao on 16/12/16.
 */
@Getter
@Setter
public class GatherConfigParam implements Serializable{
    private List<EmployeePerformanceConfigVO> performanceConfigVOs;
    private Integer type = 0;
}
