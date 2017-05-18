package com.tqmall.legend.facade.report.convert;

import com.tqmall.cube.shop.result.businessoverview.BusinessOverviewDTO;
import com.tqmall.legend.facade.report.vo.BusinessOverviewVo;
import com.tqmall.wheel.lang.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class BusinessOverviewConvert implements Converter<BusinessOverviewDTO,BusinessOverviewVo> {


    @Override
    public BusinessOverviewVo convert(BusinessOverviewDTO source) {
        if (Objects.isNotNull(source)) {
            BusinessOverviewVo businessOverviewVo = new BusinessOverviewVo();
            BeanUtils.copyProperties(source, businessOverviewVo);
            return businessOverviewVo;
        }
        return null;
    }
}
