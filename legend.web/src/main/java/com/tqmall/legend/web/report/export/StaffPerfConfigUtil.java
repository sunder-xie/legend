package com.tqmall.legend.web.report.export;

import java.util.List;

import com.tqmall.cube.shop.result.shop.EmployeePerformanceConfigVO;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;

public class StaffPerfConfigUtil {
    public static final String TURNOVER = "turnover";
    public static final String PROFIT = "profit";

    public static String turnoverOrProfit(List<EmpPrefConfig> configs, Integer configtype) {
        String saletype = TURNOVER;
        for (EmpPrefConfig config : configs) {
            if (config.getConfigType().equals(configtype)
                    && config.getPercentageType().equals(EmployeePerformanceConfigVO.PERCENTAGETYPE_DEFAULT)) {
                saletype = config.getPercentageMethod() == 0 ? TURNOVER : PROFIT;
            }
        }
        return saletype;
    }
}