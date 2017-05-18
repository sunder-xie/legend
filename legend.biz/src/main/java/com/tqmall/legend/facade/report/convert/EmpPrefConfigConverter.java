package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.shop.EmployeePerformanceConfigVO;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/10/17.
 */
public class EmpPrefConfigConverter implements Converter<EmployeePerformanceConfigVO,EmpPrefConfig> {
    @Override
    public EmpPrefConfig convert(EmployeePerformanceConfigVO resultVO) {
        EmpPrefConfig empPrefConfig = new EmpPrefConfig();
        empPrefConfig.setRelId(resultVO.getRelId());
        empPrefConfig.setPercentageAmount(resultVO.getPercentageAmount());
        empPrefConfig.setPercentageType(resultVO.getPercentageType());
        empPrefConfig.setConfigType(resultVO.getConfigType());
        empPrefConfig.setPercentageRate(resultVO.getPercentageRate());
        empPrefConfig.setShopId(resultVO.getShopId());
        empPrefConfig.setPercentageMethod(resultVO.getPercentageMethod());
        empPrefConfig.setGatherPerfConfigVO(resultVO.getGatherPerfConfigVO());
        return empPrefConfig;
    }
}
