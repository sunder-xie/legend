package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.shop.EmpRepairInfoVO;
import com.tqmall.legend.facade.report.bo.RepairPrefByServiceBO;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by tanghao on 16/10/14.
 */
public class RepairPrefGroupByServiceConverter implements Converter<EmpRepairInfoVO,RepairPrefByServiceBO> {
    @Override
    public RepairPrefByServiceBO convert(EmpRepairInfoVO empRepairInfoVO) {
        RepairPrefByServiceBO repairPrefByServiceBO = new RepairPrefByServiceBO();
        repairPrefByServiceBO.setServiceName(empRepairInfoVO.getServiceName());
        repairPrefByServiceBO.setRepairAmount(empRepairInfoVO.getRepairAmount());
        repairPrefByServiceBO.setServiceCount(new BigDecimal(empRepairInfoVO.getServiceNum()));
        repairPrefByServiceBO.setPercentageRule(empRepairInfoVO.getPercentageRule()+empRepairInfoVO.getPercentageNum());
        repairPrefByServiceBO.setRepairPercentage(empRepairInfoVO.getRepairPercentage());
        repairPrefByServiceBO.setEmpName(empRepairInfoVO.getEmpName());
        return repairPrefByServiceBO;
    }
}
