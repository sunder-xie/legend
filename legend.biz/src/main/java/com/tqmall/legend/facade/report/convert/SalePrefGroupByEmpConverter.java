package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.shop.EmpSaleInfoVO;
import com.tqmall.legend.facade.report.bo.SalePrefByPersonBO;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/10/14.
 */
public class SalePrefGroupByEmpConverter implements Converter<EmpSaleInfoVO,SalePrefByPersonBO> {
    @Override
    public SalePrefByPersonBO convert(EmpSaleInfoVO empSaleInfoVO) {
        SalePrefByPersonBO salePrefByPersonBO = new SalePrefByPersonBO();
        salePrefByPersonBO.setRepairPercentage(empSaleInfoVO.getPercentageAmount());
        salePrefByPersonBO.setGrossBenifitAmount(empSaleInfoVO.getBenefitAmount());
        salePrefByPersonBO.setGrossBenifitAmountRate(empSaleInfoVO.getBenefitRate()+"%");
        salePrefByPersonBO.setGoodsNum(empSaleInfoVO.getGoodsNum());
        salePrefByPersonBO.setSaleAmount(empSaleInfoVO.getSaleAmount());
        salePrefByPersonBO.setWorkerName(empSaleInfoVO.getEmpName());
        return salePrefByPersonBO;
    }
}
