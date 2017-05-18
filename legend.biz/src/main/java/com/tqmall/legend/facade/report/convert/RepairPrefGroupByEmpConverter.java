package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.shop.EmpRepairInfoVO;
import com.tqmall.legend.facade.report.bo.RepairPrefByPersonBO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/10/12.
 */
public class RepairPrefGroupByEmpConverter implements Converter<EmpRepairInfoVO,RepairPrefByPersonBO> {
    @Override
    public RepairPrefByPersonBO convert(EmpRepairInfoVO empRepairInfoVO) {
        RepairPrefByPersonBO repairPrefByPersonBO = new RepairPrefByPersonBO();
        repairPrefByPersonBO.setOrderNum(empRepairInfoVO.getOrderNum());
        repairPrefByPersonBO.setRepairAmount(empRepairInfoVO.getRepairAmount());
        repairPrefByPersonBO.setRepairCarNum(empRepairInfoVO.getRepairCarNum());
        repairPrefByPersonBO.setRepairPercentage(empRepairInfoVO.getRepairPercentage());
        repairPrefByPersonBO.setServiceNum(empRepairInfoVO.getServiceNum());
        repairPrefByPersonBO.setWorkerName(empRepairInfoVO.getEmpName());
        return repairPrefByPersonBO;
    }
}
