package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.shop.EmpRepairInfoVO;
import com.tqmall.legend.facade.report.bo.RepairPrefByOrderBO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/10/14.
 */
public class RepairPrefGroupByOrderConverter implements Converter<EmpRepairInfoVO,RepairPrefByOrderBO> {
    @Override
    public RepairPrefByOrderBO convert(EmpRepairInfoVO empRepairInfoVO) {
        RepairPrefByOrderBO repairPrefByOrderBO = new RepairPrefByOrderBO();
        repairPrefByOrderBO.setDateStr(empRepairInfoVO.getDateStr());
        repairPrefByOrderBO.setRepairPercentage(empRepairInfoVO.getRepairPercentage());
        repairPrefByOrderBO.setPercentageRule(empRepairInfoVO.getPercentageRule()+empRepairInfoVO.getPercentageNum());
        repairPrefByOrderBO.setHours(empRepairInfoVO.getHours());
        repairPrefByOrderBO.setLicense(empRepairInfoVO.getLicense());
        repairPrefByOrderBO.setOrderSn(empRepairInfoVO.getOrderSn());
        repairPrefByOrderBO.setServiceName(empRepairInfoVO.getServiceName());
        repairPrefByOrderBO.setServicePrice(empRepairInfoVO.getServicePrice());
        repairPrefByOrderBO.setWorkerName(empRepairInfoVO.getEmpName());
        return repairPrefByOrderBO;
    }
}
