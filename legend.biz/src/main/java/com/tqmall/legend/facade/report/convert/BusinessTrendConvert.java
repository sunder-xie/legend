package com.tqmall.legend.facade.report.convert;


import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.businessoverview.BusinessTrend;
import com.tqmall.cube.shop.result.businessoverview.DateWithAmountDTO;
import com.tqmall.legend.facade.report.vo.BusinessTrendVo;
import com.tqmall.legend.facade.report.vo.DatePointVo;
import com.tqmall.wheel.lang.Objects;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/6/16.
 */
public class BusinessTrendConvert implements Converter<BusinessTrend, BusinessTrendVo>{

    @Override
    public BusinessTrendVo convert(BusinessTrend source) {
        if (Objects.isNull(source)) {
            return null;
        } else {
            BusinessTrendVo businessTrendVo = new BusinessTrendVo();
            businessTrendVo.setAmount(source.getTotalPaidAmount());
            List<DatePointVo> datePointVos = Lists.newArrayListWithCapacity(Objects.isNull(source.getDateWithAmountDTOList())?0:source.getDateWithAmountDTOList().size());

            if (Objects.isNotNull(source.getDateWithAmountDTOList())) {
                for (DateWithAmountDTO dateWithAmountDTO : source.getDateWithAmountDTOList()) {
                    DatePointVo datePointVo = new DatePointVo();
                    datePointVo.setDate(dateWithAmountDTO.getDate());
                    datePointVo.setAmount(dateWithAmountDTO.getPaidAmount());

                    datePointVos.add(datePointVo);
                }
            }

            businessTrendVo.setDatePointVoList(datePointVos);
            return businessTrendVo;
        }
    }
}
